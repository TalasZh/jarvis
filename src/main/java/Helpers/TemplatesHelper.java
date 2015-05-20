package Helpers;

import com.sun.istack.internal.Nullable;

import java.io.IOException;

/**
 * Created by root on 5/8/15.
 */
public class TemplatesHelper {
    public static String pageHelper(String title, String spaceKey, String content) throws IOException {
        String templateContent = FilesHelper.ReadFile(String.format("%s/src/main/java/PostTemplates/contentTemplate", Session.currentDir));
        String result = templateContent
                .replace("[TITLE]", title)
                .replace("[SPACE_KEY]", spaceKey)
                .replace("[BODY_STORAGE_VALUE]", content);
        return result;
    }

    public static String subPageHelper(String title, String spaceKey, String content, String ancestorID) throws IOException {
        String templateContent = FilesHelper.ReadFile(String.format("%s/src/main/java/PostTemplates/subPageTemplate", Session.currentDir));
        String result = templateContent
                .replace("[TITLE]", title)
                .replace("[SPACE_KEY]", spaceKey)
                .replace("[BODY_STORAGE_VALUE]", content)
                .replace("[ANCESTOR_ID]", ancestorID);
        return result;
    }

    public static String updatePageHelper(int pageID, String pageTitle, String spaceKey, int version, boolean isMinor, String content) throws IOException {
        String templateContent = FilesHelper.ReadFile(String.format("%s/src/main/java/PostTemplates/updatePageTemplate", Session.currentDir));
        String result = templateContent
                .replace("[PAGE_ID]", Integer.toString(pageID))
                .replace("[PAGE_TITLE]", pageTitle)
                .replace("[SPACE_KEY]", spaceKey)
                .replace("[VERSION]", Integer.toString(version))
                .replace("[IS_MINOR]", Boolean.toString(isMinor))
                .replace("[BODY_STORAGE_VALUE]", content);
        return result;
    }

    public static String updateSubPageHelper(int pageID, String pageTitle, String spaceKey, int version, boolean isMinor, String content, int ancestorID) throws IOException {
        String templateContent = FilesHelper.ReadFile(String.format("%s/src/main/java/PostTemplates/updateSubPageTemplate", Session.currentDir));
        String result = templateContent
                .replace("[PAGE_ID]", Integer.toString(pageID))
                .replace("[PAGE_TITLE]", pageTitle)
                .replace("[SPACE_KEY]", spaceKey)
                .replace("[VERSION]", Integer.toString(version))
                .replace("[IS_MINOR]", Boolean.toString(isMinor))
                .replace("[BODY_STORAGE_VALUE]", content)
                .replace("[ANCESTOR_ID]", Integer.toString(ancestorID));
        return result;
    }

    public static String addCommentHelper(int pageID, String commentBody) throws IOException {
        String templateContent = FilesHelper.ReadFile(String.format("%s/src/main/java/PostTemplates/commentTemplate", Session.currentDir));
        String result = templateContent
                .replace("[PARENT_ID]", Integer.toString(pageID))
                .replace("[COMMENT_BODY]", commentBody);
        return result;
    }
}
