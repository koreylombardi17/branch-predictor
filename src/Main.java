import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        // Get arguments from command line
        int m = Integer.parseInt(args[0]);  // m = The number of PC bits used to index the gshare table
        int n = Integer.parseInt(args[1]);  // n = The number of global history register bits
        String file = args[2];

        // ArrayList used to store each command
        List<String> commands = new ArrayList<>();

        // BufferedReader used to read the input file
        BufferedReader br = null;
        try {
            // Get command from the user at command line
            br = new BufferedReader(new FileReader(file));
            String text = br.readLine();

            // Loop until BufferedReader reaches null, populate list with read and write commands
            while (text != null) {
                commands.add(text);
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

        // Create simulation object
        Simulation simulation = new Simulation(m, n);
        simulation.execute(commands);

        // Used to get the execution time of program
        System.out.println("Execution time in ms =\t" + (System.currentTimeMillis() - startTime));
    }
}
