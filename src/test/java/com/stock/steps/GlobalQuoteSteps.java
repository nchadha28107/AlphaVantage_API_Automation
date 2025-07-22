package com.stock.steps;

import com.stock.api.GlobalQuoteApi;
import com.stock.enums.Context;
import com.stock.model.GlobalQuoteResponse;
import com.stock.utils.ConfigReader;
import com.stock.utils.ScenarioContext;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class GlobalQuoteSteps {

    private static final String ApiKey = ConfigReader.getEndpoint("api", "key");
    private Response response;

    private Response getResponse() {
        return ScenarioContext.getContext(Context.RESPONSE);
    }

    private GlobalQuoteResponse parseGlobalQuoteResponse(Response response) {
        try {
            return response.as(GlobalQuoteResponse.class);
        } catch (Exception e) {
            // Handle parsing errors gracefully
            return null;
        }
    }

    // Basic Global Quote Request Steps
    @When("I request global quote for symbol {string}")
    public void i_request_global_quote_for_symbol(String symbol) {
        response = GlobalQuoteApi.getGlobalQuote(symbol, ApiKey);
        ScenarioContext.setContext(Context.RESPONSE, response);
    }

    @When("I request global quote for symbol {string} with API key {string}")
    public void i_request_global_quote_for_symbol(String symbol, String apiKey) {
        response = GlobalQuoteApi.getGlobalQuote(symbol, apiKey);
        ScenarioContext.setContext(Context.RESPONSE, response);
    }

    @When("I request global quote for symbol {string} with datatype {string}")
    public void i_request_global_quote_for_symbol_with_datatype(String symbol, String datatype) {
        Map<String, String> params = new HashMap<>();
        params.put("datatype", datatype);

        response = GlobalQuoteApi.getGlobalQuote(symbol, ApiKey, params);
        ScenarioContext.setContext(Context.RESPONSE, response);
    }

    @When("I request global quote for symbol {string} without API key")
    public void i_request_global_quote_without_api_key(String symbol) {
        response = GlobalQuoteApi.getGlobalQuote(symbol, null);
        ScenarioContext.setContext(Context.RESPONSE, response);
    }

    @When("I request global quote without symbol parameter")
    public void i_request_global_quote_without_symbol() {
        response = GlobalQuoteApi.getGlobalQuote(null, ApiKey);
        ScenarioContext.setContext(Context.RESPONSE, response);
    }

    // HTTP Method Testing Steps
    @When("I request global quote for symbol {string} using {string} method")
    public void i_request_global_quote_using_post_method(String symbol, String method) {
        response = GlobalQuoteApi.getGlobalQuote(symbol, ApiKey, method);
        ScenarioContext.setContext(Context.RESPONSE, response);
    }

    // Success Response Validation Steps
    @Then("the global quote request should be successful")
    public void the_global_quote_request_should_be_successful() {
        response = getResponse();
        assertEquals(200, response.statusCode(), "Global quote request should return HTTP 200");
    }

    @Then("the response should contain valid global quote data for {string}")
    public void the_response_should_contain_valid_global_quote_data_for_symbol(String expectedSymbol) {
        response = getResponse();
        GlobalQuoteResponse globalQuoteResponse = parseGlobalQuoteResponse(response);

        assertNotNull(globalQuoteResponse, "Global Quote response should not be null");

        GlobalQuoteResponse.GlobalQuote globalQuote = globalQuoteResponse.getGlobalQuote();
        if (globalQuote != null) {
            assertEquals(expectedSymbol, globalQuote.getSymbol(), "Symbol should match");
        }
    }

    @Then("the response should have all required fields")
    public void the_response_should_have_all_required_fields() {
        response = getResponse();
        GlobalQuoteResponse globalQuoteResponse = parseGlobalQuoteResponse(response);

        assertNotNull(globalQuoteResponse,"Global Quote response should not be null");

        GlobalQuoteResponse.GlobalQuote globalQuote = globalQuoteResponse.getGlobalQuote();
        assertNotNull(globalQuote, "Global Quote should not be null");

        assertNotNull(globalQuote.getSymbol(), "Symbol field should not be null");
        assertNotNull(globalQuote.getOpen(), "Open field should not be null");
        assertNotNull(globalQuote.getHigh(), "High field should not be null");
        assertNotNull(globalQuote.getLow(), "Low field should not be null");
        assertNotNull(globalQuote.getPrice(), "Price field should not be null");
        assertNotNull(globalQuote.getVolume(), "Volume field should not be null");
        assertNotNull(globalQuote.getLatestTradingDay(), "Latest trading day field should not be null");
        assertNotNull(globalQuote.getPreviousClose(), "Previous close field should not be null");
        assertNotNull(globalQuote.getChange(), "Change field should not be null");
        assertNotNull(globalQuote.getChangePercent(), "Change percent field should not be null");
    }

    @Then("the symbol in response should match {string}")
    public void the_symbol_in_response_should_match(String expectedSymbol) {
        response = getResponse();
        GlobalQuoteResponse globalQuoteResponse = parseGlobalQuoteResponse(response);

        assertNotNull(globalQuoteResponse, "Global Quote response should not be null");

        GlobalQuoteResponse.GlobalQuote globalQuote = globalQuoteResponse.getGlobalQuote();
        assertNotNull(globalQuote, "Global Quote should not be null");

        assertEquals(expectedSymbol, globalQuote.getSymbol(), "Response symbol should match requested symbol");
    }

    // Format Validation Steps
    @Then("the response should be in JSON format")
    public void the_response_should_be_in_json_format() {
        response = getResponse();
        String contentType = response.getContentType();
        assertTrue(contentType != null && contentType.contains("application/json"), "Response should be in JSON format");
    }

    @Then("the response should be in CSV format")
    public void the_response_should_be_in_csv_format() {
        response = getResponse();
        String contentType = response.getContentType();
        assertTrue(contentType != null && contentType.contains("application/x-download"), "Response should be in CSV format");
    }

    @Then("the CSV response should contain valid headers")
    public void the_csv_response_should_contain_valid_headers() {
        response = getResponse();
        String csvResponse = response.getBody().asString();
        assertTrue(csvResponse.contains("symbol") || csvResponse.contains("open") || csvResponse.contains("high"), "CSV response should contain headers");
    }

    // Error Response Validation Steps
    @Then("the global quote request should fail with error {string}")
    public void the_global_quote_request_should_fail_with_error(String expectedErrorMessage) {
        response = getResponse();
        // Alpha Vantage typically returns 200 even for errors, so check error message
        assertEquals(200, response.statusCode(), "Should return HTTP 200");
        String actualError = response.jsonPath().getString("'Error Message'");
        if (actualError == null) {
            actualError = response.jsonPath().getString("'Information'");
        }

        assertTrue(actualError != null && actualError.toLowerCase().contains(expectedErrorMessage.toLowerCase()), "Response should contain expected error message");
    }

    @Then("the global quote request should return empty global quote object")
    public void the_global_quote_request_should_return_empty_global_quote_object() {
        response = getResponse();
        assertEquals(200, response.statusCode(), "Should return HTTP 200");
        GlobalQuoteResponse globalQuoteResponse = parseGlobalQuoteResponse(response);
        assertTrue(globalQuoteResponse.getGlobalQuote().isEmpty() || globalQuoteResponse.getGlobalQuote() == null, "Global Quote should be null or empty");
    }

    // HTTP Status Code Validation Steps
    @Then("the global quote request should fail with HTTP status {int}")
    public void the_global_quote_request_should_fail_with_http_status(int expectedStatusCode) {
        response = getResponse();
        assertEquals(expectedStatusCode, response.statusCode(), "HTTP method validation should prevent request");
    }

    @Then("the response should indicate method not allowed")
    public void the_response_should_indicate_method_not_allowed() {
        response = getResponse();
        String responseBody = response.getBody().asString();
        assertTrue(responseBody.contains("Method Not Allowed") ||
                responseBody.contains("method not allowed") ||
                response.statusCode() == 405, "Response should indicate method not allowed");
    }
}