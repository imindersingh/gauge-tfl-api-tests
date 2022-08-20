package http;

import com.github.sitture.env.config.EnvConfig;

import java.util.HashMap;
import java.util.Map;

import kong.unirest.HttpResponse;

public class JourneyPlannerApiRequests extends Requests {

    private final String ACCEPT = "Accept";
    private final String CONTENT_TYPE  = "Content-Type";
    private final String ACCEPT_DEFAULT = "*/*";
    private final String APPLICATION_JSON = "application/json";
    private final String APP_KEY = "app_key";
    private final String APP_KEY_VALUE = EnvConfig.get("APP_KEY");

    public JourneyPlannerApiRequests() {
        super(EnvConfig.get("HOST"));
    }

//    public HttpResponse get(String path) {
//        HashMap<String, String> headers = new HashMap<>();
//        headers.put(ACCEPT, "*/*");
//        return getRequest(path, headers);
//    }

//    public HttpResponse get(String path, Map<String, String> headers) {
//        return getRequest(path, headers);
//    }

    public HttpResponse get(final String path, Map<String, Object> queryParams) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(ACCEPT, ACCEPT_DEFAULT);
        headers.put(CONTENT_TYPE, APPLICATION_JSON);
        headers.put(APP_KEY, APP_KEY_VALUE);
        headers.put("Cache-Control", "no-cache");
        return getRequest(path, headers, queryParams);
    }

//    public HttpResponse post(String path, String body) {
//        HashMap<String, String> headers = new HashMap<>();
//        headers.put(ACCEPT, ACCEPT_DEFAULT);
//        headers.put(CONTENT_TYPE, APPLICATION_JSON);
//        return postRequest(path, headers, body);
//    }
//
//    public HttpResponse post(String path, Map<String, String> headers, String body) {
//        return postRequest(path, headers, body);
//    }

}
