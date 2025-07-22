package com.stock.steps;

import com.stock.api.TimeSeriesDailyApi;
import com.stock.enums.Context;
import com.stock.model.TimeSeriesDailyResponse;
import com.stock.utils.ConfigReader;
import com.stock.utils.ScenarioContext;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class TimeSeriesDailySteps {

    private static final String ApiKey = ConfigReader.getEndpoint("api", "key");
    private Response response;

    private Response getResponse() {
        return ScenarioContext.getContext(Context.RESPONSE);
    }

    private TimeSeriesDailyResponse parseTimeSeriesDailyResponse(Response response) {
        try {
            return response.as(TimeSeriesDailyResponse.class);
        } catch (Exception e) {
            // Handle parsing errors gracefully
            return null;
        }
    }

    // Basic Time Series Daily Request Steps
    @When("I request time series daily for symbol {string}")
    public void i_request_time_series_daily_for_symbol(String symbol) {
        response = TimeSeriesDailyApi.getTimeSeriesDaily(symbol, ApiKey);
        ScenarioContext.setContext(Context.RESPONSE, response);
    }

    @When("I request time series daily for symbol {string} with API key {string}")
    public void i_request_time_series_daily_for_symbol_with_api_key(String symbol, String apiKey) {
        response = TimeSeriesDailyApi.getTimeSeriesDaily(symbol, apiKey);
        ScenarioContext.setContext(Context.RESPONSE, response);
    }

    @When("I request time series daily for symbol {string} with {string} {string}")
    public void i_request_time_series_daily_for_symbol_with_param(String symbol, String param, String paramValue) {
        Map<String, String> params = new HashMap<>();
        params.put(param, paramValue);

        response = TimeSeriesDailyApi.getTimeSeriesDaily(symbol, ApiKey, params);
        ScenarioContext.setContext(Context.RESPONSE, response);
    }

    @When("I request time series daily without API key for symbol {string}")
    public void i_request_time_series_daily_without_api_key(String symbol) {
        response = TimeSeriesDailyApi.getTimeSeriesDaily(symbol, null);
        ScenarioContext.setContext(Context.RESPONSE, response);
    }

    // HTTP Method Testing Steps
    @When("I request time series daily for symbol {string} using {string} method")
    public void i_request_time_series_daily_using_method(String symbol, String method) {
        response = TimeSeriesDailyApi.getTimeSeriesDaily(symbol, ApiKey, method);
        ScenarioContext.setContext(Context.RESPONSE, response);
    }

    // Success Response Validation Steps
    @Then("the time series daily request should be successful")
    public void the_time_series_daily_request_should_be_successful() {
        response = getResponse();
        assertEquals(200, response.statusCode(), "Time series daily request should return HTTP 200");
    }

    @Then("the response should contain valid time series daily data for {string}")
    public void the_response_should_contain_valid_time_series_daily_data_for_symbol(String expectedSymbol) {
        response = getResponse();
        TimeSeriesDailyResponse timeSeriesResponse = parseTimeSeriesDailyResponse(response);

        assertNotNull(timeSeriesResponse, "Time Series Daily response should not be null");

        TimeSeriesDailyResponse.MetaData metaData = timeSeriesResponse.getMetaData();
        if (metaData != null) {
            assertTrue(metaData.getSymbol().contains(expectedSymbol), "Symbol should match in metadata");
        }
    }

    @Then("the response should have meta data section")
    public void the_response_should_have_meta_data_section() {
        response = getResponse();
        TimeSeriesDailyResponse timeSeriesResponse = parseTimeSeriesDailyResponse(response);

        assertNotNull(timeSeriesResponse, "Time Series Daily response should not be null");

        TimeSeriesDailyResponse.MetaData metaData = timeSeriesResponse.getMetaData();
        assertNotNull(metaData, "Meta Data should not be null");
        assertNotNull(metaData.getInformation(), "Information field should not be null");
        assertNotNull(metaData.getSymbol(), "Symbol field should not be null");
        assertNotNull(metaData.getLastRefreshed(), "Last refreshed field should not be null");
        assertNotNull(metaData.getOutputSize(), "Output size field should not be null");
        assertNotNull(metaData.getTimeZone(), "Time zone field should not be null");
    }

    @Then("the response should have time series data section")
    public void the_response_should_have_time_series_data_section() {
        response = getResponse();
        TimeSeriesDailyResponse timeSeriesResponse = parseTimeSeriesDailyResponse(response);

        assertNotNull(timeSeriesResponse, "Time Series Daily response should not be null");

        Map<String, TimeSeriesDailyResponse.DailyData> timeSeries = timeSeriesResponse.getTimeSeries();
        assertNotNull(timeSeries, "Time Series Daily data should not be null");
        assertFalse(timeSeries.isEmpty(), "Time Series Daily data should not be empty");
    }

    @Then("the time series data should contain valid date entries")
    public void the_time_series_data_should_contain_valid_date_entries() {
        response = getResponse();
        TimeSeriesDailyResponse timeSeriesResponse = parseTimeSeriesDailyResponse(response);

        assertNotNull(timeSeriesResponse, "Time Series Daily response should not be null");

        Map<String, TimeSeriesDailyResponse.DailyData> timeSeries = timeSeriesResponse.getTimeSeries();
        assertNotNull(timeSeries, "Time Series Daily data should not be null");

        // Check that at least one date entry exists and follows YYYY-MM-DD format
        for (String dateKey : timeSeries.keySet()) {
            assertTrue(dateKey.matches("\\d{4}-\\d{2}-\\d{2}"),
                    "Date key should be in YYYY-MM-DD format: " + dateKey);
            break; // Check at least one entry
        }

        // Check first entry for required fields
        for (TimeSeriesDailyResponse.DailyData data : timeSeries.values()) {
            assertNotNull(data.getOpen(), "Open field should not be null");
            assertNotNull(data.getHigh(), "High field should not be null");
            assertNotNull(data.getLow(), "Low field should not be null");
            assertNotNull(data.getClose(), "Close field should not be null");
            assertNotNull(data.getVolume(), "Volume field should not be null");
            break; // Check first entry
        }
    }

    @Then("the symbol in meta data should match {string}")
    public void the_symbol_in_meta_data_should_match(String expectedSymbol) {
        response = getResponse();
        TimeSeriesDailyResponse timeSeriesResponse = parseTimeSeriesDailyResponse(response);

        assertNotNull(timeSeriesResponse, "Time Series Daily response should not be null");

        TimeSeriesDailyResponse.MetaData metaData = timeSeriesResponse.getMetaData();
        assertNotNull(metaData, "Meta Data should not be null");

        assertTrue(metaData.getSymbol().contains(expectedSymbol),
                "Response symbol should match requested symbol");
    }

    @Then("the response should contain limited number of data points for compact size")
    public void the_response_should_contain_limited_number_of_data_points_for_compact_size() {
        response = getResponse();
        TimeSeriesDailyResponse timeSeriesResponse = parseTimeSeriesDailyResponse(response);

        assertNotNull(timeSeriesResponse, "Time Series Daily response should not be null");

        Map<String, TimeSeriesDailyResponse.DailyData> timeSeries = timeSeriesResponse.getTimeSeries();
        assertNotNull(timeSeries, "Time Series Daily data should not be null");

        // Compact should return approximately 100 data points
        assertTrue(timeSeries.size() <= 100,
                "Compact output should contain limited data points, got: " + timeSeries.size());
    }

    @Then("the CSV response should contain headers {string}")
    public void the_csv_response_should_contain_headers(String expectedHeaders) {
        response = getResponse();
        String csvResponse = response.getBody().asString();

        String[] headers = expectedHeaders.split(",");
        for (String header : headers) {
            assertTrue(csvResponse.toLowerCase().contains(header.toLowerCase()),
                    "CSV response should contain header: " + header);
        }
    }

    // Error Response Validation Steps
    @Then("the time series daily request should fail with error {string}")
    public void the_time_series_daily_request_should_fail_with_error(String expectedErrorMessage) {
        response = getResponse();
        // Alpha Vantage typically returns 200 even for errors, so check error message
        assertEquals(200, response.statusCode(), "Should return HTTP 200");

        String actualError = response.jsonPath().getString("'Error Message'");
        if (actualError == null) {
            actualError = response.jsonPath().getString("'Information'");
        }

        assertTrue(actualError != null && actualError.toLowerCase().contains(expectedErrorMessage.toLowerCase()),
                "Response should contain expected error message: " + expectedErrorMessage);
    }

    // HTTP Status Code Validation Steps
    @Then("the time series daily request should fail with HTTP status {int}")
    public void the_time_series_daily_request_should_fail_with_http_status(int expectedStatusCode) {
        response = getResponse();
        assertEquals(expectedStatusCode, response.statusCode(),
                "HTTP method validation should prevent request");
    }
}