package http;

import com.github.sitture.env.config.EnvConfig;
import kong.unirest.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class JourneyPlannerRequests extends Requests {

    public JourneyPlannerRequests() {
        super(EnvConfig.get("HOST"));
    }

    public HttpResponse<?> get(final String path, final Map<String, Object> queryParams) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "*/*");
        headers.put("Content-Type", "application/json");
        headers.put("Cache-Control", "no-cache");
        queryParams.put("app_key", EnvConfig.get("APP_KEY"));
        return getRequest(path, headers, queryParams);
    }
}
