package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.SpecDataStore;
import kong.unirest.HttpResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Step("And response contains message <message>")
    public void assertResponseContainsMessage(final String message) {
        final HttpResponse<?> response = (HttpResponse<?>) SpecDataStore.get("response");
        final String actualMessage = JsonPath.read(response.getBody().toString(), "$.message");
        assertThat(actualMessage).isEqualTo(message);
    }

    @Step("And response contains message <message> with mode")
        public void assertResponseContainsMessageForInvalidMode(final String message) {
        final Map<String, Object> queryParameters = (Map<String, Object>) SpecDataStore.get("searchParameters");
        final String mode = queryParameters.get("mode").toString();
        assertResponseContainsMessage(String.format("%s%s", message, mode));
    }

    @Step("The response is valid against the schema")
    public void assertResponseIsValidAgainstSchema() {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema jsonSchema = factory.getSchema(getClass().getClassLoader().getResourceAsStream("schema.json"));
        final HttpResponse<?> response = (HttpResponse<?>) SpecDataStore.get("response");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse;
        try {
            jsonResponse = mapper.readTree(response.getBody().toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Set<ValidationMessage> errors = jsonSchema.validate(jsonResponse);
        assertThat(errors).isEmpty();

    }
}
