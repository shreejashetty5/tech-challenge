package tech_challenge;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.*;

public class StockOutlierDetector {

    // Function to sample 30 consecutive rows from a random starting point
    public static List<String[]> getRandomSample(String filePath) throws Exception {
        List<String[]> data = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            data = reader.readAll();
        } 
        
        catch (IOException e) {
            throw new Exception("Error reading file: " + filePath, e);
        }

        if (data.size() <= 30) {
            throw new Exception("Insufficient rows for sampling in file: " + filePath);
        }

        // Exclude last 29 rows
        int maxStartIndex = data.size() - 30;
        Random random = new Random();
        int startIndex = random.nextInt(maxStartIndex - 1) + 1; // Skip row 0
        return data.subList(startIndex, startIndex + 30);
    }

    // Function to detect outliers
    public static List<String[]> detectOutliers(List<String[]> sampledData) {
        List<String[]> outliers = new ArrayList<>();

        // Extract stock prices as a double array
        double[] prices = sampledData.stream().mapToDouble(row -> Double.parseDouble(row[2])).toArray();

        //calculate mean, standard deviation, threshold
        double mean = mean(prices);
        double stdDev = standardDeviation(prices, mean);
        double threshold = 2 * stdDev;

        for (String[] row : sampledData) {
            double price = Double.parseDouble(row[2]);
            double deviation = price - mean;
            if (Math.abs(deviation) > threshold) {
                double percentDeviation = (deviation / mean) * 100;

                outliers.add(new String[]{
                        row[0], // Stock-ID
                        row[1], // Timestamp
                        row[2], // Actual Price
                        String.format("%.2f", mean), // Mean
                        String.format("%.2f", deviation), // Deviation
                        String.format("%.2f", percentDeviation) // % Deviation
                });
            }
        }
        return outliers;
    }

    private static double mean(double[] prices) {
        return Arrays.stream(prices).average().orElse(0.0);
    }

    private static double standardDeviation(double[] prices, double mean) {
        double variance = Arrays.stream(prices).map(price -> Math.pow(price - mean, 2)).average().orElse(0.0);
        return Math.sqrt(variance);
    }

    // Write outliers to a new CSV file
    public static void writeOutliersToFile(String originalFileName, List<String[]> outliers) {
        String outputFileName = "outliers_" + originalFileName;

        try (CSVWriter writer = new CSVWriter(new FileWriter(outputFileName))) {
            writer.writeNext(new String[]{"Stock-ID", "Timestamp", "Price", "Mean", "Deviation", "% Deviation"});
            writer.writeAll(outliers);
            System.out.println("Outliers written to: " + outputFileName);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + outputFileName);
        }
    }

    // Main processing function
    public static void processFiles(String inputDir, int numFilesToProcess) {
        File directory = new File(inputDir);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".csv"));

        if (files == null || files.length == 0) {
            System.out.println("No CSV files found in the directory.");
            return;
        }

        int processedCount = 0;
        for (File file : files) {
            if (processedCount >= numFilesToProcess) 
            	break;

            try {
                List<String[]> sampledData = getRandomSample(file.getAbsolutePath());
                List<String[]> outliers = detectOutliers(sampledData);

                if (!outliers.isEmpty()) {
                    writeOutliersToFile(file.getName(), outliers);
                }

                processedCount++;
            } catch (Exception e) {
                System.err.println("Error processing file " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        String inputData = "/tech_challenge/data"; //location of data having all files
        int numFiles = 5; // Number of files to process
        processFiles(inputData, numFiles);
    }
}
