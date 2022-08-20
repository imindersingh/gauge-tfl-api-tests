package steps;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.thoughtworks.gauge.Gauge;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.datastore.DataStore;
import http.JourneyPlannerApiRequests;
import kong.unirest.HttpResponse;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public class JourneyPlannerApiSteps {

    private final DataStore dataStore = new DataStore();
    private final JourneyPlannerApiRequests request = new JourneyPlannerApiRequests();

    @Step("User makes a GET request to plan a journey from <departure> to <destination> with parameters: <parametersTable>")
    public void getRequest(final String departure, final String destination, final Table parametersTable) {
        final String path = String.format("journey/journeyresults/%s/to/%s", departure, destination);
        dataStore.put("departure", departure);
        dataStore.put("destination", destination);
        final Map<String, Object> queryParams = new HashMap<>();
        parametersTable.getTableRows().forEach(
            (param) -> {
                String parameter = param.getCell("parameter");
                if ("time".equalsIgnoreCase(parameter)) {
                    //convert value to timedrtgtxc
                    int hour = Integer.parseInt(param.getCell("value").substring(0,2));
                    int minutes = Integer.parseInt(param.getCell("value").substring(2,4));

                    LocalDateTime timeStamp = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minutes, 00));
                    String tfutf = timeStamp.toString();

//                    SimpleTimeZone simpleDateFormat = new SimpleTimeZone(."HH:mm:ss");
//                    simpleDateFormat.format("time");
//                    time = simpleDateFormat.toString();

                    //
                }
                String value = param.getCell("value");
                queryParams.put(parameter, value);
            }
        );
        dataStore.put("searchParameters", queryParams);

        HttpResponse<?> response = request.get(path, queryParams);
        Gauge.writeMessage(String.format("RESPONSE STATUS CODE: %s", response.getStatus()));
        Gauge.writeMessage(String.format("RESPONSE BODY: %s", response.getBody()));
        Gauge.writeMessage(String.format("RESPONSE HEADERS: %s", response.getHeaders()));
        dataStore.put("response", response);
    }

    @Step("Assert the http response code is <httpResponseCode>")
    public void assertHttpResponseCode(final String httpResponseCode) {
        HttpResponse<?> response = (HttpResponse<?>) dataStore.get("response");
        assertThat(response.getStatus()).isEqualTo(Integer.parseInt(httpResponseCode));
    }

    @Step("Assert the http response text is <httpResponseText>")
    public void assertHttpResponseText(final String httpResponseText) {
        HttpResponse<?> response = (HttpResponse<?>) dataStore.get("response");
        assertThat(response.getStatusText()).isEqualTo(httpResponseText);
    }

    @Step("Then at least one journey is returned in the results")
    public void assertJourneyIsNotEmpty() {
        HttpResponse<?> response = (HttpResponse<?>) dataStore.get("response");
        JSONObject jsonObject = new JSONObject(response.getBody().toString());
        int journeys = jsonObject.getJSONArray("journeys").length();
        assertThat(journeys).isNotZero();
    }

    @Step("And the response contains values that match the search parameters")
    public void assertHttpResponseBody() {
        HttpResponse<?> response = (HttpResponse<?>) dataStore.get("response");
        Object jsonResponse = Configuration.defaultConfiguration().jsonProvider().parse(response.getBody().toString());
        Map<String, Object> queryParameters = (Map<String, Object>) dataStore.get("searchParameters");

        List<String> modesList = JsonPath.read(jsonResponse, "$.journeys[*].legs[*].mode.name");
        List<String> actualDistinctModesList = modesList.stream().distinct().collect(Collectors.toList());
        List<String> expectedModeList = List.of(queryParameters.get("mode").toString().split(","));
        assertThat(actualDistinctModesList).containsAnyElementsOf(expectedModeList);

        String actualDeparture = JsonPath.read(jsonResponse, "$.journeyVector.from");
        String expectedDeparture = (String) dataStore.get("departure");
        assertThat(actualDeparture).isEqualTo(expectedDeparture);

        String actualDestination = JsonPath.read(jsonResponse, "$.journeyVector.to");
        String expectedDestination = (String) dataStore.get("destination");
        assertThat(actualDestination).isEqualTo(expectedDestination);

        String actualTimeIs = JsonPath.read(jsonResponse, "$.searchCriteria.dateTimeType");
        String expectedTimeIs = queryParameters.get("timeis").toString();
        assertThat(actualTimeIs).isEqualToIgnoringCase(expectedTimeIs);


        String actualStartDateTime = JsonPath.read(jsonResponse, "$.searchCriteria.dateTime");




//        String time = queryParameters.get("time").toString();


        //todays date, plus key time, value converted to 13:00:00

//        Gson gson = new Gson();
//        User user = gson.fromJson(json_string, User.class);

        // mode - DONE
        // startDateTime
        //journey vector from
        // journey vector to
        // search criteria dateTimeType

    }
}
