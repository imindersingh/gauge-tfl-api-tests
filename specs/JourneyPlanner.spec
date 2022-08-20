Journey Planner Tests
=====================

User can perform journey search within London with valid parameters and retrieve journey options successfully
--------------------------------------------------------------------------------------------------------------
tags: iminder
* User makes a GET request to plan a journey from "Piccadilly Circus Underground Station" to "London Paddington Rail Station" with parameters:
     |parameter         |value                  |
     |------------------|-----------------------|
     |mode              |walking                |
     |timeis            |departing              |
     |journeypreference |leasttime              |
     |time              |1300                   |
* Assert the http response code is "200"
* Assert the http response text is "OK"
* Then at least one journey is returned in the results
* And the response contains values that match the search parameters


User can perform journey search travelling outside of London, with valid parameters and retrieve journey options successfully
------------------------------------------------------------------------------------------------------------------------------
tags: iminder
* User makes a GET request to plan a journey from "Kings Cross Station, London, UK" to "Manchester Piccadilly Rail Station" with parameters:
     |parameter         |value                  |
     |------------------|-----------------------|
     |mode              |tube,bus,national-rail |
     |timeis            |departing              |
     |journeypreference |leasttime              |
     |time              |1300                   |
     |nationalsearch    |true                   |
* Assert the http response code is "200"
* Assert the http response text is "OK"
* Then at least one journey is returned in the results
* And the response contains values that match the search parameters

//no journey found for your inputs 404, travel outside of london just walking

//SCHEMA MATCH

//* Assert the http response body contains the posted value

///startDateTime":"2022-08-19T13:00:00",
//

//response code
//body contains sections
// schema validation?

//Can perform journey from London to Leeds
//----------------------------------------

