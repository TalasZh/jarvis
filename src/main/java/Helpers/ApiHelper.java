package Helpers;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by won on 4/29/15.
 */
public class ApiHelper {
    public static String postContent(String contentBody) throws IOException {
        return postJson(Session.RestApiUrl.contentUrl(), contentBody);
        //return "";
    }

    public static String querySpaces() throws IOException {
        return getJson(Session.RestApiUrl.spaceUrl());
    }

    public static String queryContent() throws IOException {
        return getJson(Session.RestApiUrl.contentUrl()+"?expand=space,body.storage,version,container");
    }

    public static String queryByCustomUrl(String url) throws IOException {
        return getJson(url);
    }


    private static String getJson(String urlString) throws IOException {
        String jsonString = "";
        try {
            HttpURLConnection connection = ConnectionHelper.buildGetRequest(urlString, Session.credentials);
            jsonString = ConnectionHelper.getResponse(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private static String postJson(String urlString, String contentBody) throws IOException {
        String jsonString = "";
        try {
            HttpURLConnection connection = ConnectionHelper.buildPostRequest(urlString, Session.credentials, contentBody);
            jsonString = ConnectionHelper.getResponse(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}


