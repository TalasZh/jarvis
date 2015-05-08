package Helpers;

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
}
