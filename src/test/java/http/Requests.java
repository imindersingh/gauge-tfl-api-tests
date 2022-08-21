package http;

import com.thoughtworks.gauge.Gauge;
import kong.unirest.HttpResponse;
import kong.unirest.HttpRequest;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

import java.util.Map;

public abstract class Requests {

    private enum Type {
        JSON,
        STRING
    }

    private final String host;
    public Requests(final String host) {
        this.host = host;
    }

    public HttpResponse<?> getRequest(String path, Map<String, String> headers, Map<String, Object> queryParams) {
        return this.getRequest(path, headers, queryParams, Type.JSON);
    }

    public HttpResponse<?> getRequest(String path, Map<String, String> headers, Map<String, Object> queryString, Type type) {
        HttpRequest<?> request = Unirest.get(this.host + path).queryString(queryString).headers(headers);
        Gauge.writeMessage(String.format("REQUEST URL: %s", request.getUrl()));
        Gauge.writeMessage(String.format("REQUEST HEADERS: %s", request.getHeaders()));
        return this.send(request, type);
    }

    private HttpResponse<?> send(HttpRequest<?> request, Type type) {
        HttpResponse<?> response = null;
        try {
            if (type == Type.JSON) {
                response = request.asJson();
            }
            if (type == Type.STRING) {
                response = request.asString();
            }
            return response;
        } catch (UnirestException e) {
            throw new RuntimeException(String.format("Error making the request. \n%s", e.getMessage()));
        }
    }
}
