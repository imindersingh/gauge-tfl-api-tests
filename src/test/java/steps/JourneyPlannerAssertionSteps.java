package steps;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.SpecDataStore;
import kong.unirest.HttpResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class JourneyPlannerAssertionSteps {

    @Step("Assert the http response code is <httpResponseCode>")
    public void assertHttpResponseCode(final String httpResponseCode) {
        final HttpResponse<?> response = (HttpResponse<?>) SpecDataStore.get("response");
        assertThat(response.getStatus()).isEqualTo(Integer.parseInt(httpResponseCode));
    }

    @Step("Assert the http response text is <httpResponseText>")
    public void assertHttpResponseText(final String httpResponseText) {
        final HttpResponse<?> response = (HttpResponse<?>) SpecDataStore.get("response");
        assertThat(response.getStatusText()).isEqualTo(httpResponseText);
    }

    @Step("Then at least one journey is returned in the results")
    public void assertJourneyIsNotEmpty() {
        final HttpResponse<?> response = (HttpResponse<?>) SpecDataStore.get("response");
        final int journeys = JsonPath.read(response.getBody().toString(), "$.journeys.length()");
        assertThat(journeys).isNotZero();
    }

    @Step("And the response contains values that match the search parameters")
    public void assertHttpResponseBodyContainsValuesThatMatchSearchParameters() {
        assertResponseDateTimeEqualsSearchParameterDateTime();
        assertResponseDestinationEqualsSearchDestination();
        assertResponseDepartureEqualsSearchDeparture();
        assertResponseContainsAtLeastOneJourneyModeFromSearchCriteria();
    }

    @Step("Response contains dateTime that matches the search parameter")
    public void assertResponseDateTimeEqualsSearchParameterDateTime() {
        final HttpResponse<?> response = (HttpResponse<?>) SpecDataStore.get("response");
        final String actualDateTime = JsonPath.read(response.getBody().toString(), "$.searchCriteria.dateTime");
        final String expectedDateTime = (String) SpecDataStore.get("dateTime");
        assertThat(actualDateTime).isEqualTo(expectedDateTime);
    }

    @Step("Response contains destination that matches the search parameter")
    public void assertResponseDestinationEqualsSearchDestination() {
        final HttpResponse<?> response = (HttpResponse<?>) SpecDataStore.get("response");
        final String actualDestination = JsonPath.read(response.getBody().toString(), "$.journeyVector.to");
        final String expectedDestination = (String) SpecDataStore.get("destination");
        assertThat(actualDestination).isEqualTo(expectedDestination);
    }

    @Step("Response contains departure that matches the search parameter")
    public void assertResponseDepartureEqualsSearchDeparture() {
        final HttpResponse<?> response = (HttpResponse<?>) SpecDataStore.get("response");
        final String actualDeparture = JsonPath.read(response.getBody().toString(), "$.journeyVector.from");
        final String expectedDeparture = (String) SpecDataStore.get("departure");
        assertThat(actualDeparture).isEqualTo(expectedDeparture);
    }

    @Step("Response contains at least one journey mode that was specified in the search parameters")
    public void assertResponseContainsAtLeastOneJourneyModeFromSearchCriteria() {
        final HttpResponse<?> response = (HttpResponse<?>) SpecDataStore.get("response");
        final Object jsonResponse = Configuration.defaultConfiguration().jsonProvider().parse(response.getBody().toString());
        final Map<String, Object> queryParameters = (Map<String, Object>) SpecDataStore.get("searchParameters");

        final List<String> modesList = JsonPath.read(jsonResponse, "$.journeys[*].legs[*].mode.name");
        final List<String> actualDistinctModesList = modesList.stream().distinct().collect(Collectors.toList());
        final List<String> expectedModeList = List.of(queryParameters.get("mode").toString().split(","));
        assertThat(actualDistinctModesList).containsAnyElementsOf(expectedModeList);
    }
}
