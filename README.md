# Transport For London Tests

BDD tests created to test Transport for London APIs

## Project Dependencies
- Java 11
- Maven
- Gauge - see [instructions](https://docs.gauge.org/getting_started/installing-gauge.html) for installation
- Gauge IDE Plugin - available for [vs code](https://marketplace.visualstudio.com/items?itemName=getgauge.gauge) and [intelliJ](https://plugins.jetbrains.com/plugin/7535-gauge)

## Approach
- I started off by looking at the documentation available for TfL APIs which are well documented and easy to follow. There was a postman collection available to download which I used as the basis for my exploration.
- After understanding the functionality of the Journey Planner API I created an outline of the scenarios required as per the task
- Once the scenarios were written I decided on the framework to create executable specs

### Exploring and findings
- I focused and explored the following areas during testing
- What is the functionality of the journey planner api?
  - The functionality overall is quite simple in that you can plan for a journey from point A to point B. What makes it complex are the search parameters and the varied results returned on the data available.
- Am I able to plan a journey as specified in the task and get back valid results?
  - Once I was able to understand the input parameters, I tried performing journey searches from within London and 
    outside, but I was getting 300 Multiple Choices returned. These were redirects to potential matches to the 
    journey departure and destination points. To get actual journeys in the response I needed specific location data.
  - I searched for specific location data in the formats accepted by the API to perform more specific searches and 
    get the results required.
- Depending on the search parameters, what are the responses returned?
  - I had learned that if the input parameters were not specific enough, a redirect response was returned
  - I tested the validation on the key input parameters I was using. I didn't explore all the parameters, but the 
    key ones required to fulfill the task such as time, date, journey preference and mode. 
  - As I tested my assumptions and documented the responses and error messages. Not entirely BDD, as the 
    functionality is already there. I suppose you can say it was reverse engineering BDD!
- What are the dependencies?
  - Based on the documentation, I signed up to create a token for the `app_key` parameter, however I found later 
    that this was only necessary to allow up to 500 requests per minute.
- Assumptions
  - Given the nature of the API and data returned, I have made the assumption that the data is likely to change even 
    with fixed input parameters. For example the journey data could return more, less, or different results if the 
    data for point A to point B is ever updated.
  - Based on the above assumption, I decided to create checks that were not too specific that would cause the 
    automated tests to be brittle and break, but enough to cover the functionality and desired behaviour
assumption is that the journeys could change
- not specified date in tests as today's date is taken to avoid the tests being brittle and broken

and negative test - date in the past

- The results could vary consistently so assert against exact values at low level would make the tests brittle. Better to assert against schema and that the results returned are not empty based on the journey modes


## Automation
- Gauge is a lightweight 

### Framework

### Design Approach
- See structure below:

### Additional implementation

### Running tests

### Results

## Improvements
- Test scenario with redirect