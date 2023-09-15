/*
 * It reads the sortingPageUrl from the input event, which allows you to dynamically specify the URL of the /sortingPage endpoint when you invoke the Lambda function.

It generates 500,000 random dates using the generateDates method, similar to your previous code.

It triggers the /sortingPage endpoint using an HTTP POST request, passing the generated dates as the request body. The response from the /sortingPage endpoint is captured and returned.

It includes a createRandomDate method to generate random dates, as before.
 */




package com.nikola;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.Random;

public class LargeDatasetProcessorLambdaHandler implements RequestHandler<Map<String, String>, String> {

    @Override
    public String handleRequest(Map<String, String> input, Context context) {
        // Read the URL of the /sortingPage endpoint from the input event
        String sortingPageUrl = input.get("sortingPageUrl");

        if (sortingPageUrl == null) {
            throw new RuntimeException("sortingPageUrl not provided in input.");
        }

        // Generate 500,000 random dates
        List<String> dates = generateDates(500000);

        // Trigger the /sortingPage endpoint to sort the dates
        String response = triggerSortingPage(sortingPageUrl, dates);

        return "Dates generated and sorted successfully!";
    }

    private List<String> generateDates(int numberOfDates) {
        // Generate random dates as before
        // ...
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < numberOfDates; i++) {
            String randomDate = createRandomDate(1900, 2000);
            dates.add(randomDate);
        }
        return dates;
    }

    private String triggerSortingPage(String endpoint, List<String> dates) {
        // Send an HTTP POST request to your /sortingPage endpoint
        RestTemplate restTemplate = new RestTemplate();
        // You can set any additional form parameters if needed
        // For example, you might serialize the 'dates' list and include it in the request body

        // Perform the HTTP POST request and capture the response
        String response = restTemplate.postForObject(endpoint, dates, String.class);

        return response;
    }
    private String createRandomDate(int startYear, int endYear) {
        Random rand = new Random();
        int day = rand.nextInt(28) + 1;
        int month = rand.nextInt(12) + 1;
        int year = rand.nextInt(endYear - startYear + 1) + startYear;
        LocalDate date = LocalDate.of(year, month, day);
        return date.toString();
    }
}
