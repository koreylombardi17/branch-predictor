import java.util.Arrays;
import java.util.List;

public class Simulation {

    private int m;              // m = The number of PC bits used to index the gshare table
    private int n;              // n = The number of global history register bits
    private int misses;
    private int totalRequests;
    private int twoToPowerOfM;
    private char[] ghr;         // ghr = global history register
    private int[] table;        // table = gshare table

    public Simulation(int m, int n) {
        this.m = m;
        this.n = n;
        this.misses = 0;
        this.totalRequests = 0;
        this.twoToPowerOfM = (int)Math.pow(2, m);
        intializeGHR();
        initializeTable();
    }

    // Used to execute the commands stored in ArrayList
    public void execute(List<String> commands) {
        for (String command : commands) {
            executeSingleCommand(command);
        }
        printResults();
    }

    // Function used to execute a single command
    public void executeSingleCommand(String command) {
        // Split the command:
        // first part is the address, second part is a char 't'(taken) or 'n'(not taken)
        String[] splitCommand = command.split(" ");
        String addressStr = splitCommand[0];
        char action = splitCommand[1].charAt(0);

        // Convert address from String to int
        int address = Integer.parseInt(addressStr, 16);

        // Remove offset
        address /= 4;

        // Mask address to m bits
        address %= twoToPowerOfM;

        // Add zeros to end of GHR
        int ghrWithZeros = addZerosToGHR();

        // xor address with ghrWithzeros to get the index to the gshare table
        int index = address^ghrWithZeros;

        // Get block from gshare table, make prediction, update
        checkTableAndUpdate(index, action);
        totalRequests++;
    }

    // Global history register in initialized to 0000
    public void intializeGHR() {
        ghr = new char[n];
        for(int i = 0; i < n; i++) {
            ghr[i] = '0';
        }
    }

    // All blocks in the gshare table are initialized to 2(weakly taken)
    public void initializeTable() {
        table = new int[twoToPowerOfM];
        for (int i = 0; i < twoToPowerOfM; i++) {
            table[i] = 2;
        }
    }

    // Make a copy of the global history register, add (m-n) zeros to the right side, returns int
    public int addZerosToGHR() {
        char[] ghrWithZeros = Arrays.copyOf(ghr, m);
        for(int i = n; i < m; i++) {
            ghrWithZeros[i] = '0';
        }
        String ghrZerosString = String.valueOf(ghrWithZeros);
        return Integer.valueOf(ghrZerosString, 2);
    }

    // Shifts a 1 or a 0 into the global history register's leftmost bit, shifts out the rightmost bit
    public void shiftGHR(char oneOrZero) {
        for(int i = (n-2); i >= 0; i--) {
            ghr[i+1] = ghr[i];
        }
        ghr[0] = oneOrZero;
    }

    // Gets the block from gshare table, makes prediction, stores metadata, updates block, updates ghr
    public void checkTableAndUpdate(int index, char action) {
        // Case 1: Strongly not taken
        if(table[index] == 0) {
            if (action == 'n'){
                shiftGHR('1');
            } else {
                misses++;
                table[index]++;
                shiftGHR('0');
            }
        }
        // Case 2: Weakly not taken
        else if(table[index] == 1) {
            if (action == 'n'){
                table[index]--;
                shiftGHR('1');
            } else {
                misses++;
                table[index]++;
                shiftGHR('0');
            }
        }
        // Case 3: Weakly taken
        else if(table[index] == 2) {
            if (action == 't'){
                table[index]++;
                shiftGHR('1');
            } else {
                misses++;
                table[index]--;
                shiftGHR('0');
            }
        }
        // Case 4: Strognly taken
        else{
            if (action == 't'){
                shiftGHR('1');
            } else {
                misses++;
                table[index]--;
                shiftGHR('0');
            }
        }
    }

    // Print data using float data type
    public void printResults() {
        System.out.println("Miss rate =\t\t" + String.format("%.6f", calculateMissRatio()));
    }

    // Formula to calculate miss-prediction ratio
    public float calculateMissRatio() {
        return (float)misses/totalRequests;
    }
}
