@alphavantage @globalquote
Feature: Alpha Vantage Global Quote API

  @negative
  Scenario: TC_GQ_01 Cannot retrieve global quote using POST method
    When I request global quote for symbol "IBM" using "POST" method
    Then the global quote request should fail with HTTP status 405
    And the response should indicate method not allowed

  @negative
  Scenario: TC_GQ_02 Cannot retrieve global quote with invalid API key
    When I request global quote for symbol "IBM" with API key ""
    Then the global quote request should fail with error "the parameter apikey is invalid or missing"

  @negative
  Scenario: TC_GQ_03 Cannot retrieve global quote with empty symbol
    When I request global quote for symbol ""
    Then the global quote request should fail with error "Invalid API call"

  @positive
  Scenario: TC_GQ_04 Successfully retrieve global quote data with complete validation
    When I request global quote for symbol "IBM"
    Then the global quote request should be successful
    And the response should contain valid global quote data for "IBM"
    And the response should have all required fields

  @positive
  Scenario: TC_GQ_05 Successfully retrieve global quote for different symbol
    When I request global quote for symbol "AAPL"
    Then the global quote request should be successful
    And the response should contain valid global quote data for "AAPL"
    And the symbol in response should match "AAPL"

  @positive
  Scenario: TC_GQ_06 Successfully retrieve global quote data with CSV format
    When I request global quote for symbol "GOOGL" with datatype "csv"
    Then the global quote request should be successful
    And the response should be in CSV format
    And the CSV response should contain valid headers

  @negative
  Scenario: TC_GQ_07 Cannot retrieve global quote for invalid symbol
    When I request global quote for symbol "INVALID_SYMBOL_123"
    Then the global quote request should return empty global quote object