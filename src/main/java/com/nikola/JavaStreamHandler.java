package com.nikola;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.time.LocalDate;

public class JavaStreamHandler implements RequestStreamHandler {
    private static final String LAMBDA_FUNCTION_NAME = "no-db-app-1-MySpringBootFunction-sYtKLvgeRe3s";

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        // Generate the dates and process each one
        List<String> dates = generateDates();

        // Initialize the Lambda client
        AWSLambda lambda = AWSLambdaClientBuilder.defaultClient();

        for (String date : dates) {
            // Invoke the LargeDatasetProcessorLambdaHandler for each date
            String payload = "{\"date\": \"" + date + "\"}";
            String response = invokeLambdaFunction(lambda, LAMBDA_FUNCTION_NAME, payload);

            // Handle the Lambda function's response (modify as needed)
            outputStream.write(response.getBytes());
        }
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

    private String invokeLambdaFunction(AWSLambda lambda, String functionName, String payload) {
        try {
            InvokeRequest invokeRequest = new InvokeRequest()
                    .withFunctionName(functionName)
                    .withInvocationType("RequestResponse")
                    .withPayload(payload);

            InvokeResult invokeResult = lambda.invoke(invokeRequest);
            return new String(invokeResult.getPayload().array(), "UTF-8");
        } catch (IOException e) {
            return "Error invoking Lambda function: " + e.getMessage();
        }
    }
}
