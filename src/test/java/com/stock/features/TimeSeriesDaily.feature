@alphavantage @timeseriesdaily
Feature: Alpha Vantage Time Series Daily API

  @negative
  Scenario: TC_TSD_01 Cannot retrieve time series daily using POST method
    When I request time series daily for symbol "IBM" using "POST" method
    Then the time series daily request should fail with HTTP status 405
    And the response should indicate method not allowed

  @negative
  Scenario: TC_TSD_02 Cannot retrieve time series daily with invalid API key
    When I request time series daily for symbol "IBM" with API key ""
    Then the time series daily request should fail with error "the parameter apikey is invalid or missing"

  @negative
  Scenario: TC_TSD_03 Cannot retrieve time series daily with missing symbol parameter
    When I request time series daily for symbol ""
    Then the time series daily request should fail with error "Invalid API call"

  @positive
  Scenario: TC_TSD_04 Successfully retrieve time series daily data with complete validation
    When I request time series daily for symbol "IBM"
    Then the time series daily request should be successful
    And the response should contain valid time series daily data for "IBM"
    And the response should have meta data section
    And the response should have time series data section
    And the time series data should contain valid date entries

  @positive
  Scenario: TC_TSD_05 Successfully retrieve time series daily for different symbol
    When I request time series daily for symbol "AAPL"
    Then the time series daily request should be successful
    And the response should contain valid time series daily data for "AAPL"
    And the symbol in meta data should match "AAPL"

  @positive
  Scenario: TC_TSD_06 Successfully retrieve time series daily data in CSV format
    When I request time series daily for symbol "GOOGL" with "datatype" "csv"
    Then the time series daily request should be successful
    And the response should be in CSV format
    And the CSV response should contain headers "timestamp,open,high,low,close,volume"

  @negative
  Scenario: TC_TSD_07 Handle invalid symbol gracefully
    When I request time series daily for symbol "INVALID_SYMBOL_XYZ"
    Then the time series daily request should fail with error "Invalid API call"

  @positive
  Scenario: TC_TSD_08 Successfully retrieve compact time series daily data
    When I request time series daily for symbol "MSFT" with "outputsize" "compact"
    Then the time series daily request should be successful
    And the response should contain valid time series daily data for "MSFT"
    And the response should contain limited number of data points for compact size