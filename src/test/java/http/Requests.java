package http;

import com.thoughtworks.gauge.Gauge;

import org.apache.commons.codec.binary.Base64;

import java.util.Map;

import kong.unirest.GetRequest;
import kong.unirest.HttpRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.HttpResponse;
import kong.unirest.MultipartBody;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

public abstract class Requests {

    private enum Type {
        JSON,
        STRING
    }

    private String host;

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Requests(String host) {
        this.host = host;
    }

//    public HttpResponse<?> getRequest(String path, Map<String, String> headers) {
//        return this.getRequest(path, headers, Type.JSON);
//    }

    public HttpResponse<?> getRequest(String path, Map<String, String> headers, Map<String, Object> queryParams) {
        return this.getRequest(path, headers, queryParams, Type.JSON);
    }

//    public HttpResponse<?> getRequestAsString(String path, Map<String, String> headers) {
//        return this.getRequest(path, headers, Type.STRING);
//    }

//    public HttpResponse<?> getRequest(String path, Map<String, String> headers, Type type) {
//        HttpRequest<?> request = Unirest.get(this.host + path).headers(headers);
//        return this.send(request, type);
//    }

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

    private HttpResponse<?> bodyRequest(RequestType requestType, String path, Map<String, String> headers, String body, Type type) {
        HttpRequest<?> request = ((HttpRequestWithBody)requestType.method(this.host + path).headers(headers)).body(body);
        return this.send(request, type);
    }

    private HttpResponse<?> fieldsRequest(RequestType requestType, String path, Map<String, String> headers, Map<String, Object> fields, Type type) {
        MultipartBody request = ((HttpRequestWithBody)requestType.method(this.host + path).headers(headers)).fields(fields);
        return this.send(request, type);
    }

    private HttpResponse<?> optionsRequest(String path, Map<String, String> headers, Type type) {
        GetRequest request = (GetRequest)Unirest.options(this.host + path).headers(headers);
        return this.send(request, type);
    }

    private HttpResponse<?> noBodyRequest(RequestType requestType, String path, Map<String, String> headers, Type type) {
        HttpRequest<?> request = requestType.method(this.host + path).headers(headers);
        return this.send(request, type);
    }

    public HttpResponse<?> deleteRequest(String path, Map<String, String> headers, String body, Type type) {
        return this.bodyRequest(Unirest::delete, path, headers, body, type);
    }

    public HttpResponse<?> deleteRequest(String path, Map<String, String> headers, Map<String, Object> fields, Type type) {
        return this.fieldsRequest(Unirest::delete, path, headers, fields, type);
    }

    public HttpResponse<?> deleteRequest(String path, Map<String, String> headers, String body) {
        return this.deleteRequest(path, headers, body, Type.JSON);
    }

    public HttpResponse<?> deleteRequest(String path, Map<String, String> headers, Map<String, Object> fields) {
        return this.deleteRequest(path, headers, fields, Type.JSON);
    }

    protected HttpResponse<?> deleteRequest(String path, Map<String, String> headers) {
        return this.noBodyRequest(Unirest::delete, path, headers, Type.JSON);
    }

    protected HttpResponse<?> postRequest(String path, Map<String, String> headers) {
        return this.noBodyRequest(Unirest::post, path, headers, Type.JSON);
    }

    protected HttpResponse<?> postRequest(String path, Map<String, String> headers, Type type) {
        return this.noBodyRequest(Unirest::post, path, headers, type);
    }

    public HttpResponse<?> postRequest(String path, Map<String, String> headers, String body, Type type) {
        return this.bodyRequest(Unirest::post, path, headers, body, type);
    }

    public HttpResponse<?> postRequest(String path, Map<String, String> headers, Map<String, Object> fields, Type type) {
        return this.fieldsRequest(Unirest::post, path, headers, fields, type);
    }

    public HttpResponse<?> postRequest(String path, Map<String, String> headers, String body) {
        return this.postRequest(path, headers, body, Type.JSON);
    }

    public HttpResponse<?> postRequest(String path, Map<String, String> headers, Map<String, Object> fields) {
        return this.postRequest(path, headers, fields, Type.JSON);
    }

    public HttpResponse<?> putRequest(String path, Map<String, String> headers) {
        return this.noBodyRequest(Unirest::put, path, headers, Type.JSON);
    }

    public HttpResponse<?> putRequest(String path, Map<String, String> headers, String body, Type type) {
        return this.bodyRequest(Unirest::put, path, headers, body, type);
    }

    public HttpResponse<?> putRequest(String path, Map<String, String> headers, Map<String, Object> fields, Type type) {
        return this.fieldsRequest(Unirest::put, path, headers, fields, type);
    }

    public HttpResponse<?> putRequest(String path, Map<String, String> headers, String body) {
        return this.putRequest(path, headers, body, Type.JSON);
    }

    public HttpResponse<?> putRequest(String path, Map<String, String> headers, Map<String, Object> fields) {
        return this.putRequest(path, headers, fields, Type.JSON);
    }

    protected HttpResponse<?> optionsRequest(String path, Map<String, String> headers) {
        return this.optionsRequest(path, headers, Type.JSON);
    }

    public String getBasicAuthFromCreds(String auth) {
        byte[] encodedBytes = Base64.encodeBase64(auth.getBytes());
        String var10000 = new String(encodedBytes);
        return "Basic " + var10000;
    }

    private interface RequestType {
        HttpRequestWithBody method(String var1);
    }
}
