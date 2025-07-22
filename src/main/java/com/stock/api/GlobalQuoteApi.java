package com.stock.api;

import com.stock.utils.ConfigReader;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class GlobalQuoteApi {

    private static final String baseUri = ConfigReader.getBaseUrl(System.getProperty("env", "qa"));
    private static final String quoteEndpoint = ConfigReader.getEndpoint("time_series", "quote");

    /**
     *
     * @param symbol The stock symbol (e.g., "IBM")
     * @param apiKey The API key
     * @return Response object
     */
    public static Response getGlobalQuote(String symbol, String apiKey) {
        String endpoint = quoteEndpoint
                .replace("{symbol}", symbol != null ? symbol : "")
                .replace("{apikey}", apiKey != null ? apiKey : "");

        return given()
                .baseUri(baseUri) // base URI
                .when()
                .get(endpoint);
    }

    public static Response getGlobalQuote(String symbol, String apiKey, String method) {
        String endpoint = quoteEndpoint
                .replace("{symbol}", symbol != null ? symbol : "")
                .replace("{apikey}", apiKey != null ? apiKey : "");

        String httpMethod = (method != null && !method.isEmpty()) ? method.toUpperCase() : "GET";

        RequestSpecification requestSpec = given().baseUri(baseUri);

        switch (httpMethod) {
            case "POST":
                return requestSpec.when().post(endpoint);
            case "PUT":
                return requestSpec.when().put(endpoint);
            case "DELETE":
                return requestSpec.when().delete(endpoint);
            case "PATCH":
                return requestSpec.when().patch(endpoint);
            default:
                // Default to GET if an unknown method is passed
                return requestSpec.when().get(endpoint);
        }
    }

    /**
     *
     * @param symbol The stock symbol (e.g., "IBM")
     * @param apiKey The API key
     * @param optionalParams Optional parameters such as "datatype"
     * @return Response object
     */
    public static Response getGlobalQuote(String symbol, String apiKey, Map<String, String> optionalParams) {
        String endpoint = quoteEndpoint
                .replace("{symbol}", symbol)
                .replace("{apikey}", apiKey);

        // Build full URL
        StringBuilder fullUrlBuilder = new StringBuilder(endpoint);

        // Append query parameters if any
        if (optionalParams != null && !optionalParams.isEmpty()) {
            fullUrlBuilder.append("&");
            String queryString = optionalParams.entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"));
            fullUrlBuilder.append(queryString);
        }

        // Build query parameters
        RequestSpecification requestSpec = given().baseUri(baseUri);

        return given()
                .baseUri(baseUri) // base URI
                .when()
                .get(String.valueOf(fullUrlBuilder));
    }
}