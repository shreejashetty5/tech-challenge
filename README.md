# tech-challenge
Repository contains java solution file


README
-------
Overview
This program processes stock price data from .csv files, detects outliers, and saves them to new .csv files. It is designed to:

Extract 30 consecutive data points starting from a random timestamp (excluding the last 29 rows).
Detect outliers using the definition of values greater than 2 standard deviations from the mean.
Output outliers to a new .csv file with details such as Stock-ID, Timestamp, Price, Mean, Deviation, and % Deviation.

How to Set Up and Run the Application
1) Setup:

Install Java 8 or above.
Add the OpenCSV library to  project dependencies:


<dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
    <version>5.7.1</version>
</dependency>


2)Prepare Input Files:

Place all input .csv files in a directory named data.
Ensure each file contains the following columns: Stock-ID, Timestamp, and Price.


3)Run the Program:

Compile the Java code:
javac StockOutlierDetector.java

Run the program:
java StockOutlierDetector


4)Output:

Output .csv files will be created in the project directory. Each file will be named outliers_<given_filename>.csv.
The output file contains the following columns:
Stock-ID: Ticker symbol of the stock.
Timestamp: Date of the outlier.
Price: Actual stock price at the timestamp.
Mean: Mean of the 30 sampled data points.
Deviation: Difference between actual price and mean.
% Deviation: Percent deviation of the actual price above the threshold.

Error Handling
The application includes exception handling for:

Missing or empty .csv files.
Insufficient rows in a file (fewer than 30 rows).
Invalid file format or incorrect column structure.



References
The following resources were referred to during the development of this program:

OpenCSV Documentation - For reading and writing .csv files.
Oracle Java Documentation - For Java standard library usage.
Baeldung - For guidance on handling exceptions and file processing in Java.
GeeksforGeeks - For concepts on statistical calculations and random sampling.
Stack Overflow - For resolving common Java errors and debugging issues.
