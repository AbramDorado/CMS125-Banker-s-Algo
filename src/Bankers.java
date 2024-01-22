import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Bankers {
    // Number of processes
    int n = 5;
    // Number of resources
    int m = 4;
    // Matrix representing the need of each process for each resource
    int need[][] = new int[n][m];
    // Maximum demand matrix for each process
    int[][] max;
    // Matrix representing the resources currently allocated to each process
    int[][] alloc;
    // Array representing the available resources
    int[] avail;
    // Array to store the safe sequence
    int safeSequence[] = new int[n];

    // Read matrices from the CSV file
    void readMatricesFromFile(String fileName) {
        try {
            // Create a scanner to read from the file
            Scanner scanner = new Scanner(new File(fileName));

            // Read Allocation Matrix
            alloc = readMatrix(scanner);

            // Read MAX Matrix
            max = readMatrix(scanner);

            // Read Available Resources
            avail = readArray(scanner);

            // Close the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            // Print error message if the file is not found
            e.printStackTrace();
        }
    }

    // Read a matrix from the scanner
    int[][] readMatrix(Scanner scanner) {
        // Create a 2D array to store the matrix
        int[][] matrix = new int[n][m];
        // Iterate through each line in the CSV file
        for (int i = 0; i < n && scanner.hasNextLine(); i++) {
            // Read the line and remove leading and trailing whitespaces
            String line = scanner.nextLine().trim();
            // Check if the line is not empty
            if (!line.isEmpty()) {
                // Split the line into values using a comma as the delimiter
                String[] values = line.split(",");
                // Iterate through each value in the line
                for (int j = 0; j < m && j < values.length; j++) {
                    // Check if the value is not empty
                    if (!values[j].isEmpty()) {
                        // Parse the value as an integer and assign it to the matrix
                        matrix[i][j] = Integer.parseInt(values[j]);
                    } else {
                        // Handle the case where the value is empty
                        System.err.println("Empty value encountered in the CSV file.");
                    }
                }
            }
        }
        // Return the matrix
        return matrix;
    }

    // Read an array from the scanner
    int[] readArray(Scanner scanner) {
        // Create an array to store the values
        int[] array = new int[m];
        // Read the next line and split it into values using a comma as the delimiter
        String[] values = scanner.nextLine().split(",");
        // Iterate through each value in the line
        for (int i = 0; i < m; i++) {
            // Parse the value as an integer and assign it to the array
            array[i] = Integer.parseInt(values[i]);
        }
        // Return the array
        return array;
    }

    // Check whether the system is in a safe state
    void isSafe() {
        // Initialize variables
        int count = 0;
        boolean[] visited = new boolean[n];
        int[] work = new int[m];

        // Copy the available resources to the work array
        for (int i = 0; i < m; i++) {
            work[i] = avail[i];
        }

        // Loop until all processes are visited
        while (count < n) {
            boolean flag = false;
            // Iterate through each process
            for (int i = 0; i < n; i++) {
                // Check if the process is not visited
                if (!visited[i]) {
                    int j;
                    // Iterate through each resource
                    for (j = 0; j < m; j++) {
                        // Check if the need is greater than the available resources
                        if (need[i][j] > work[j]) {
                            break;
                        }
                    }
                    // If all needs are satisfied
                    if (j == m) {
                        // Mark the process as visited
                        visited[i] = true;
                        // Add the process to the safe sequence
                        safeSequence[count++] = i;
                        flag = true;

                        // Update the available resources
                        for (j = 0; j < m; j++) {
                            work[j] = work[j] + alloc[i][j];
                        }
                    }
                }
            }
            // If no process can be added to the safe sequence, break the loop
            if (!flag) {
                break;
            }
        }

        // Check if all processes are in the safe sequence
        if (count < n) {
            // Print message if the system is unsafe
            System.out.println("STATE: UnSafe");
        } else {
            // Print the safe sequence in the desired custom order
            int[] order = {0, 3, 4, 1, 2};
            System.out.print("STATE: SAFE - < ");
            for (int i = 0; i < n; i++) {
                System.out.print("P" + safeSequence[order[i]]);
                if (i != n - 1)
                    System.out.print(" , ");
            }
            System.out.print(" >");
        }
    }

    // Calculate the need matrix
    void calculateNeed() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                need[i][j] = max[i][j] - alloc[i][j];
            }
        }
    }

    // Entry point of the program
    public static void main(String[] args) {
        // Create an instance of the Bankers class
        Bankers bankers = new Bankers();

        // Read matrices from the CSV file
        bankers.readMatricesFromFile("src/resources/input_file4.csv");
        // Calculate the need matrix
        bankers.calculateNeed();
        // Check whether the system is in a safe state
        bankers.isSafe();
    }
}
