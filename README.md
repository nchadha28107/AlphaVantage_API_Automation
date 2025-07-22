# Alpha Vantage API Test Automation Framework

## Overview

This framework is designed to test the **Alpha Vantage Financial APIs** using **Cucumber**, **JUnit**, and **RestAssured**. It validates various operations related to stock market data retrieval, specifically focusing on the **Global Quote API** and **Time Series Daily API**. The tests are written using Cucumber's Gherkin syntax and the framework is organized to facilitate running API tests efficiently against real Alpha Vantage endpoints.

**Note:** Due to Alpha Vantage's rate limiting restrictions (25 API calls per day for free tier), only a limited set of tests are automated to stay within the quota.

## Features

- **Global Quote API**:
   - Retrieve real-time stock quote data for any stock symbol
   - Support for different output formats (JSON/CSV)
   - Validation of all quote fields (price, volume, change, etc.)
   - Error handling for invalid symbols and missing API keys
   - HTTP method validation testing

- **Time Series Daily API**:
   - Retrieve daily historical stock data
   - Support for compact and full output sizes
   - Meta data validation and time series data verification
   - CSV format support with header validation
   - Date format validation and data integrity checks

- **Comprehensive Error Handling**:
   - Invalid API key validation
   - Missing parameter detection
   - HTTP method restriction testing
   - Rate limiting awareness

- **Test Categories**: Tests are tagged with specific categories like `@positive`, `@negative`, `@alphavantage`, `@globalquote`, and `@timeseriesdaily` for organization and future selective execution

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Setup](#setup)
3. [Directory Structure](#directory-structure)
4. [API Configuration](#api-configuration)
5. [Running the Tests](#running-the-tests)
6. [Test Structure](#test-structure)
7. [Rate Limiting Considerations](#rate-limiting-considerations)
8. [Reporting](#reporting)
9. [Test Results Screenshots](#test-results-screenshots)
10. [Tagging Convention](#tagging-convention)
11. [Model Classes](#model-classes)
12. [Error Handling](#error-handling)
13. [Environment Support](#environment-support)
14. [Troubleshooting](#troubleshooting)
15. [License](#license)

## Prerequisites

Before running the tests, ensure that the following dependencies are installed:

- **Java 11+**
- **Maven** (to manage dependencies and build the project)
- **Cucumber** (for behavior-driven testing)
- **JUnit** (for running tests)
- **RestAssured** (for making API requests and validating responses)
- **Alpha Vantage API Key** (sign up at [Alpha Vantage](https://www.alphavantage.co/support/#api-key))
- **IDE** (e.g., IntelliJ IDEA, Eclipse) for development and test execution

## Setup

1. **Clone the repository:**
    ```bash
    git clone https://github.com/nchadha28107/AlphaVantage_API_Automation.git
    cd AlphaVantage_API_Automation
    ```

2. **Install Maven dependencies:**
    ```bash
    mvn clean install
    ```

3. **Configure API Key:**
   - Update the API key in `src/main/resources/endpoints.yml`

4. **Import project in IDE:**
   - Import the project as a Maven project in your preferred IDE

## Directory Structure

```
.
├── pom.xml
├── src
│   ├── main
│   │   └── java
│   │       └── com
│   │           └── stock
│   │               ├── api
│   │               │   ├── GlobalQuoteApi.java
│   │               │   └── TimeSeriesDailyApi.java
│   │               ├── enums
│   │               │   └── Context.java
│   │               ├── model
│   │               │   ├── GlobalQuoteResponse.java
│   │               │   └── TimeSeriesDailyResponse.java
│   │               └── utils
│   │                   ├── ConfigReader.java
│   │                   └── ScenarioContext.java
│   │   └── resources
│   │       └── endpoints.yml
│   └── test
│       └── java
│           └── com
│               └── stock
│                   ├── features
│                   │   ├── GlobalQuote.feature
│                   │   └── TimeSeriesDaily.feature
│                   ├── steps
│                   │   ├── GlobalQuoteSteps.java
│                   │   └── TimeSeriesDailySteps.java
│                   └── runner
│                       └── TestRunner.java
│       └── resources
│           └── extent.properties
```

## API Configuration

The `endpoints.yml` file contains environment-specific configurations:

```yaml
environments:
  local:
    base_url: "http://localhost:9090"
  mock:
    base_url: "https://mockapi.example.com"
  qa:
    base_url: "https://www.alphavantage.co"

api:
  key: "YOUR_API_KEY_HERE"  # Replace with your actual API key

time_series:
  daily: "/query?function=TIME_SERIES_DAILY&symbol={symbol}&apikey={apikey}"
  quote: "/query?function=GLOBAL_QUOTE&symbol={symbol}&apikey={apikey}"
```

## Running the Tests

### Using Maven

**Run all tests:**
```bash
mvn clean test
```

**Run tests for specific environment:**
```bash
mvn clean test -Denv=qa
```

### Using IDE

You can also run tests directly from your IDE by:
1. Right-clicking on `TestRunner.java` and selecting "Run"
2. Running individual feature files from the features directory
3. Running specific scenarios by using the IDE's Cucumber plugin

## Test Structure

### Example Test Scenarios

**Global Quote API Test:**
```gherkin
@alphavantage @globalquote @positive
Scenario: TC_GQ_04 Successfully retrieve global quote data with complete validation
  When I request global quote for symbol "IBM"
  Then the global quote request should be successful
  And the response should contain valid global quote data for "IBM"
  And the response should have all required fields
```

**Time Series Daily API Test:**
```gherkin
@alphavantage @timeseriesdaily @positive
Scenario: TC_TSD_04 Successfully retrieve time series daily data with complete validation
  When I request time series daily for symbol "IBM"
  Then the time series daily request should be successful
  And the response should contain valid time series daily data for "IBM"
  And the response should have meta data section
  And the response should have time series data section
```

### Test Coverage

- **Global Quote API**: 7 test scenarios covering positive and negative cases
- **Time Series Daily API**: 8 test scenarios covering various data formats and validations
- **Total**: 15 comprehensive test scenarios

## Rate Limiting Considerations

⚠️ **Important:** Alpha Vantage free tier allows only **25 API calls per day**. To manage this limitation:

1. **Limited Test Execution**: Only essential tests are included to stay within quota
2. **Environment Strategy**: Consider using mock servers for frequent testing
3. **API Key Management**: Monitor your daily usage at Alpha Vantage dashboard

**Recommended Testing Strategy:**
- Run complete test suite sparingly to avoid quota exhaustion
- Focus on individual feature files during development
- Use negative test scenarios (that may not consume quota) for frequent validation

## Reporting

The framework generates multiple types of reports:

### Extent Reports
- **HTML Report**: `target/extentReports/SparkReport/Reports/Spark.html`
- **PDF Report**: `target/extentReports/SparkReport/PdfReport/ExtentPdf.pdf`

### Cucumber Reports
- **HTML Report**: `target/cucumber-reports/cucumber.html`
- **JSON Report**: `target/cucumber-reports/cucumber.json`

### Viewing Reports
After test execution, open the HTML reports in your browser to view:
- Test execution summary
- Pass/fail status for each scenario
- Detailed step-by-step execution logs
- Response data validation results

## Test Results Screenshots

### Cucumber HTML Reports

The following screenshots demonstrate the Cucumber HTML report outputs:

#### Report Overview

<img width="1920" height="4573" alt="Cucumber Report Overview" src="https://github.com/user-attachments/assets/cb6d1750-54ad-4226-8414-b185616d24d0" />

### Extent Reports

The following screenshots demonstrate the Extent HTML report outputs:

#### Dashboard Summary

<img width="1920" height="1429" alt="Extent Report Dashboard" src="https://github.com/user-attachments/assets/499527fa-cc4b-4da0-a5e2-2aee910e359c" />

#### Test Results by Category

<img width="1920" height="1132" alt="Extent Test Categories" src="https://github.com/user-attachments/assets/85155240-752f-4873-81a1-b3603a19e9c2" />

#### Detailed Test Execution

<img width="1920" height="2925" alt="Extent Test Details 1" src="https://github.com/user-attachments/assets/3d979651-790b-4ad2-808e-dff95d18d829" />
<img width="1920" height="3394" alt="Extent Test Details 2" src="https://github.com/user-attachments/assets/d216b798-cbd4-4006-9944-e83b7dba98f4" />

## Tagging Convention

Tests are organized using comprehensive tagging:

### API-Specific Tags
- **@alphavantage**: All Alpha Vantage API tests
- **@globalquote**: Global Quote API specific tests
- **@timeseriesdaily**: Time Series Daily API specific tests

### Test Type Tags
- **@positive**: Tests expecting successful API responses
- **@negative**: Tests expecting API errors or failures

## Model Classes

The framework includes well-structured model classes for API responses:

- **GlobalQuoteResponse**: Maps all Global Quote API response fields
- **TimeSeriesDailyResponse**: Maps Time Series Daily API with metadata and time series data

## Error Handling

Comprehensive error scenarios are tested:

- Invalid API keys
- Missing required parameters
- Invalid stock symbols
- Unsupported HTTP methods
- Rate limit exceeded scenarios

## Environment Support

- **Local**: For local mock server testing
- **Mock**: For external mock API testing
- **QA**: For Alpha Vantage production API testing

Switch environments using: `-Denv=<environment>`

## Troubleshooting

### Common Issues

1. **API Key Issues**: Ensure your API key is valid and not expired
2. **Rate Limiting**: Check your daily quota usage
3. **Network Issues**: Verify internet connectivity to Alpha Vantage servers
4. **Test Failures**: Check if stock symbols are valid and markets are open

### Support

- Alpha Vantage API Documentation: https://www.alphavantage.co/documentation/
- Check API status: https://www.alphavantage.co/support/#status

## License

This project is licensed under the MIT License.
