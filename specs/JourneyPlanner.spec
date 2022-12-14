Journey Planner Tests
=====================

    METHOD: GET
    ENDPOINT: journey/journeyresults/{from}/to/{destination}
    DESCRIPTION: Perform a Journey Planner search from the parameters specified in simple types

    |departure                              |destination                    |mode                       |
    |---------------------------------------|-------------------------------|---------------------------|
    |Piccadilly Circus Underground Station  |London Paddington Rail Station |walking                    |
    |1000013                                |1000266                        |tube,overground            |
    |SE172DN                                |EC4M8AE                        |bus,national-rail          |

User can perform journey search within London with valid parameters and retrieve journey options successfully
--------------------------------------------------------------------------------------------------------------
* User makes a GET request to plan a journey from <departure> to <destination> with parameters:
     |parameter         |value                  |
     |------------------|-----------------------|
     |mode              |<mode>                 |
     |timeis            |arriving               |
     |journeypreference |leasttime              |
     |time              |1320                   |
* Assert the http response code is "200"
* Assert the http response text is "OK"
* Then at least one journey is returned in the results
* And the response contains values that match the search parameters


User can perform journey search travelling outside of London with nationalSearch set to true and retrieve journey options successfully
-------------------------------------------------------------------------------------------------------------------------------------
* User makes a GET request to plan a journey from "Kings Cross Station, London, UK" to "Manchester Piccadilly Rail Station" with parameters:
     |parameter         |value                  |
     |------------------|-----------------------|
     |mode              |tube,bus,national-rail |
     |timeis            |departing              |
     |journeypreference |leasttime              |
     |time              |1315                   |
     |nationalsearch    |true                   |
* Assert the http response code is "200"
* Assert the http response text is "OK"
* Then at least one journey is returned in the results
* And the response contains values that match the search parameters


Journey search with invalid mode returns 400 Bad Request
---------------------------------------------------------
* User makes a GET request to plan a journey from "Kings Cross Station, London, UK" to "Manchester Piccadilly Rail Station" with parameters:
     |parameter         |value                  |
     |------------------|-----------------------|
     |mode              |invalid                |
* Assert the http response code is "400"
* Assert the http response text is "Bad Request"
* And response contains message "The following journey planner mode is not recognised: " with mode


Journey search with invalid date returns 400 Bad Request
---------------------------------------------------------
* User makes a GET request to plan a journey from "Kings Cross Station, London, UK" to "Manchester Piccadilly Rail Station" with parameters:
     |parameter         |value                  |
     |------------------|-----------------------|
     |date              |invalid                |
* Assert the http response code is "400"
* Assert the http response text is "Bad Request"
* And response contains message "Date is not in a valid format.  It must be in the format: yyyyMMdd"


Journey search with invalid time returns 400 Bad Request
---------------------------------------------------------
* User makes a GET request to plan a journey from "Kings Cross Station, London, UK" to "Manchester Piccadilly Rail Station" with parameters:
     |parameter         |value                  |
     |------------------|-----------------------|
     |time              |invalid                |
* Assert the http response code is "400"
* Assert the http response text is "Bad Request"
* And response contains message "Time is not in a valid format.  It must be in the format: HHmm"


Journey search with invalid nationalsearch returns 400 Bad Request
------------------------------------------------------------------
* User makes a GET request to plan a journey from "Kings Cross Station, London, UK" to "Manchester Piccadilly Rail Station" with parameters:
     |parameter         |value                  |
     |------------------|-----------------------|
     |nationalsearch    |invalid                |
* Assert the http response code is "400"
* Assert the http response text is "Bad Request"
* And response contains message "The value 'invalid' is not valid for Boolean. "


Journey search with ambigious destination returns 300 Multiple Choices
-----------------------------------------------------------------------
* User makes a GET request to plan a journey from "London" to "Manchester Piccadilly Rail Station" with parameters:
     |parameter         |value                  |
     |------------------|-----------------------|
     |mode              |walking                |
* Assert the http response code is "300"
* Assert the http response text is "Multiple Choices"


GET to journey/journeyresults returns response valid against schema
--------------------------------------------------------------------
tags:wip
* User makes a GET request to plan a journey from "Kings Cross Station, London, UK" to "Manchester Piccadilly Rail Station" with parameters:
     |parameter         |value                  |
     |------------------|-----------------------|
     |mode              |tube,bus,national-rail |
     |timeis            |departing              |
     |journeypreference |leasttime              |
     |time              |1315                   |
     |nationalsearch    |true                   |
* Assert the http response code is "200"
* Assert the http response text is "OK"
* The response is valid against the schema
