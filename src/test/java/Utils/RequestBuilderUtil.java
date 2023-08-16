package Utils;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.Map;

import static Data.UserInfo.getApiKey;
import static Data.UserInfo.getApiToken;
import static io.restassured.RestAssured.given;

public class RequestBuilderUtil {

    private String endpoint;
    private String method;
    private String body;
    private Map<String, String> params;

    public RequestBuilderUtil(String endpoint, String method, String body, Map<String, String> params) {
        this.endpoint = endpoint;
        this.method = method;
        this.body = body;
        this.params = params;
    }

    public Response sendRequest() {
        Response response;

        RequestSpecification requestPayload = given()
                .queryParams(params)
                .body(body);
        response = switch (method.toUpperCase()) {
            case "POST" -> requestPayload.when().post(endpoint);
            case "PUT" -> requestPayload.when().put(endpoint);
            case "GET" -> requestPayload.when().get(endpoint);
            case "DELETE" -> requestPayload.when().delete(endpoint);
            default ->
                    throw new IllegalStateException("\n Method try to use doesnt in the list: " + method + "!! methods are: POST- PUT - DELETE - GET");
        };
        return response;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
