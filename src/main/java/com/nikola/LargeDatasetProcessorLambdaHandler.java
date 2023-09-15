package com.nikola;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.time.LocalDate;

public class LargeDatasetProcessorLambdaHandler {

    public String handleRequest() {
        // Generate the dates and process each one
        List<String> dates = generateDates();

        for (String date : dates) {
            // Simulate invoking the Lambda function for each date
            String payload = "{\"date\": \"" + date + "\"}";
            String response = simulateLambdaInvocation(payload);

            // Handle the response (modify as needed)
            System.out.println("Lambda function response for date " + date + ": " + response);
        }

        return "Dates processed successfully for testing.";
    }

    private List<String> generateDates() {
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < 500000; i++) {
            String randomDate = createRandomDate(1900, 2000);
            dates.add(randomDate);
        }
        return dates;
    }

    private String createRandomDate(int startYear, int endYear) {
        Random rand = new Random();
        int day = rand.nextInt(28) + 1;
        int month = rand.nextInt(12) + 1;
        int year = rand.nextInt(endYear - startYear + 1) + startYear;
        LocalDate date = LocalDate.of(year, month, day);
        return date.toString();
    }

    private String simulateLambdaInvocation(String payload) {
        // Simulate the Lambda function invocation for testing
        // In a real AWS Lambda environment, this would be replaced with actual invocation logic
        return "Simulated Lambda function response: " + payload;
    }

    public static void main(String[] args) {
        LargeDatasetProcessorLambdaHandler lambdaHandler = new LargeDatasetProcessorLambdaHandler();
        String result = lambdaHandler.handleRequest();
        System.out.println("Lambda function result: " + result);
    }
}
