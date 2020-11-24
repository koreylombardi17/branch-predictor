import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Used to keep track of execution time
        long startTime = System.currentTimeMillis();

        // Get arguments from command line
        int m = Integer.parseInt(args[0]);  // m = The number of PC bits used to index the gshare table
        int n = Integer.parseInt(args[1]);  // n = The number of global history register bits
        String file = args[2];

        // Number of entries in table
        int twoToPowerOfM = (int) Math.pow(2, m);
        String text = null;
        String[] splitCommand;
        String addressStr;
        boolean action;

        // Global history table
        int[] table = new int[twoToPowerOfM];
        for (int i = 0; i < twoToPowerOfM; i++) {
            table[i] = 2;
        }

        // BufferedReader used to read the input file
        BufferedReader br = null;

        // Used to keep track of miss prediction ratio
        int misses = 0;
        int hits = 0;

        try {
            // Get command from the user at command line
            br = new BufferedReader(new FileReader(file));
            text = br.readLine();

            // Loop until BufferedReader reaches null, populate list with read and write commands
            // ghr = global history register
            int ghr = 0;
            while (text != null) {
                // Split the command:
                // First part is the address, second part is a char 't'(taken) or 'n'(not taken)
                splitCommand = text.split(" ");
                addressStr = splitCommand[0];

                // Convert char into boolean value
                if (splitCommand[1].charAt(0) == 'n'){
                    action= false;
                }else {
                    action = true;
                }

                // Convert address from String to int
                int address = Integer.parseInt(addressStr, 16);

                // Remove offset
                address = address / 4;

                // Mask address to m bits
                address = address % twoToPowerOfM;

                // Add zeros to end of GHR
                int ghrWithZeros = ghr << (m - n);

                // xor address with ghrWithzeros to get the index to the gshare table
                int index = address ^ ghrWithZeros;

                // Get value of prediction
                int prediction = table[index];

                // Prediction value from the table
                boolean predict;

                // Check prediction
                if (prediction >= 2) {
                    predict = true;
                }
                else {
                    predict = false;
                }
                // Increment prediction value unless its already strongly takem
                if (action == true && prediction != 3) {
                    prediction++;
                }
                // Decrement the prediction value unless it's already strongly not taken
                else if (action == false && prediction != 0){
                    prediction--;
                }
                table[index] = prediction;

                // Update metadata
                if (predict == action) {
                    hits++;
                }
                else {
                    misses++;
                }

                // Shift right one bit
                ghr = ghr >> 1;
                if (action == true)
                {
                    // Add a 1 to leftmost bit
                    ghr += (int) Math.pow(2, (double) (n - 1));
                }
                // Read next line
                text = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            // Close down the BufferedReader
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ioe) {
                // Error message
                System.out.println("BufferedReader failed to close");
            }
        }
        // Display the metadata
        System.out.println(m + " " + n +  " " + String.format("%.2f", 100 * ((float)misses/(hits+misses))));

        // Used to get the execution time of program
        System.out.println("Execution time in ms =\t" + (System.currentTimeMillis() - startTime));
    }
}
