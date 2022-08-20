package steps;

import com.thoughtworks.gauge.Gauge;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import com.thoughtworks.gauge.datastore.SpecDataStore;
import http.JourneyPlannerRequests;
import kong.unirest.HttpResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class JourneyPlannerSteps {

    private final JourneyPlannerRequests request = new JourneyPlannerRequests();

    @Step("User makes a GET request to plan a journey from <departure> to <destination> with parameters: <parametersTable>")
    public void getRequest(final String departure, final String destination, final Table parametersTable) {
        final String path = String.format("journey/journeyresults/%s/to/%s", departure, destination);
        SpecDataStore.put("departure", departure);
        SpecDataStore.put("destination", destination);
        final Map<String, Object> queryParams = new HashMap<>();
        parametersTable.getTableRows().forEach(
            (param) -> {
                String parameter = param.getCell("parameter");
                if ("time".equalsIgnoreCase(parameter)) {
                    createDateTimeStamp(param);
                }
                String value = param.getCell("value");
                queryParams.put(parameter, value);
            }
        );
        SpecDataStore.put("searchParameters", queryParams);

        final HttpResponse<?> response = request.get(path, queryParams);
        SpecDataStore.put("response", response);

        Gauge.writeMessage(String.format("RESPONSE STATUS CODE: %s", response.getStatus()));
        Gauge.writeMessage(String.format("RESPONSE BODY: %s", response.getBody()));
        Gauge.writeMessage(String.format("RESPONSE HEADERS: %s", response.getHeaders()));
    }

    private void createDateTimeStamp(final TableRow param) {
        final int hour = Integer.parseInt(param.getCell("value").substring(0,2));
        final int minutes = Integer.parseInt(param.getCell("value").substring(2,4));
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        final String dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minutes, 0)).format(formatter);
        SpecDataStore.put("dateTime", dateTime);
    }
}