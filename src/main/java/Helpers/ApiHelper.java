package Helpers;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by won on 4/29/15.
 */
public class ApiHelper {
    public static String postContent(String contentBody) throws IOException {
        return postJson(Session.RestApiUrl.contentUrl(), contentBody);
    }

    public static String postContent(int ID, String contentBody) throws IOException {
        return postJson(Session.RestApiUrl.contentUrl() + '/' + ID, contentBody);
    }

    public static String putContent(int ID, String contentBody) throws IOException {
        return putJson(Session.RestApiUrl.contentUrl() + '/' + ID, contentBody);
    }

    public static String createPage(String title, String spaceKey, String content) throws IOException {
        return ApiHelper.postContent(TemplatesHelper.pageHelper(title, spaceKey, content));
    }

    public static String createSubPage(String title, String spaceKey,String content, String ancestorID) throws IOException {
        return ApiHelper.postContent(TemplatesHelper.subPageHelper(title, spaceKey, content, ancestorID));
    }

    public static String updatePage(int pageID, String pageTitle, String spaceKey, int version, boolean isMinor, String content) throws IOException {
        return ApiHelper.putContent(pageID, TemplatesHelper.updatePageHelper(pageID, pageTitle, spaceKey, version, isMinor, content));
    }

    public static String updateSubPage(int pageID, String pageTitle, String spaceKey, int version, boolean isMinor, String content, int ancestorID) throws IOException {
        return ApiHelper.putContent(pageID, TemplatesHelper.updateSubPageHelper(pageID, pageTitle, spaceKey, version, isMinor, content, ancestorID));
    }

    public static String addCommentToPage(int pageID, String commentBody) throws IOException {
        return ApiHelper.postContent(TemplatesHelper.addCommentHelper(pageID, commentBody));
    }

    public static String querySpaces() throws IOException {
        return getJson(Session.RestApiUrl.spaceUrl());
    }

    public static String createSpace(String contentBody) throws IOException {
        return postJson(Session.RestApiUrl.spaceUrl(), contentBody);
    }

    public static String queryContent() throws IOException {
        return getJson(Session.RestApiUrl.contentUrl() + "?expand=space,body.storage,version,container");
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

    private static String putJson(String urlString, String contentBody) throws IOException {
        String jsonString = "";
        try {
            HttpURLConnection connection = ConnectionHelper.buildPutRequest(urlString, Session.credentials, contentBody);
            jsonString = ConnectionHelper.getResponse(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }


}


