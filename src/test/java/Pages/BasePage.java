package Pages;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static Data.UserInfo.getApiKey;
import static Data.UserInfo.getApiToken;

public abstract class BasePage {
    protected String createURL;
    protected String updateURL;
    protected String deleteURL;
    protected String getURL;

    protected JSONObject requestBody;
    protected String name;
    protected String apiKey = getApiKey();
    protected String apiToken = getApiToken();
    protected HashMap<String, String> params;


    public BasePage(String name) {
        this.name = name;
        params = new HashMap<>();
        apiToken = getApiToken();
        apiKey = getApiKey();
        requestBody = new JSONObject();
    }

    public BasePage() {
        params = new HashMap<>();
        apiToken = getApiToken();
        apiKey = getApiKey();
        requestBody = new JSONObject();
    }

    public void setRequestBody(String key, String value) {
        this.requestBody = requestBody.put(key, value);
    }

    public void deleteRequestBodyKey(String key) {
        this.requestBody = (JSONObject) requestBody.remove(key);
    }

    public String getRequestBody() {
        return requestBody.toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParamsUsingKeys(ArrayList<String> keys) {
        for (String key : keys) {
            switch (key) {
                case "name" -> params.put("name", name);
                case "apiKey" -> params.put("key", apiKey);
                case "apiToken" -> params.put("token", apiToken);
            }
        }
    }

    public void setParamsDefault() {
        // default parameters
        if (name != null) {params.put("name", name);}
        params.put("key", apiKey);
        params.put("token", apiToken);
    }

    public void addParameter(String key, String value) {
        params.put(key, value);
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public String getCreateURL() {
        return createURL;
    }

    public void setCreateURL(String createURL) {
        this.createURL = createURL;
    }

    public String getUpdateURL() {
        return updateURL;
    }

    public void setUpdateURL(String updateURL) {
        this.updateURL = updateURL;
    }

    public String getDeleteURL() {
        return deleteURL;
    }

    public void setDeleteURL(String deleteURL) {
        this.deleteURL = deleteURL;
    }

    public String getGetURL() {
        return getURL;
    }

    public void setGetURL(String getURL) {
        this.getURL = getURL;
    }
}
