package Helpers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by root on 5/8/15.
 */
public class FilesHelper {
    public static String ReadFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Scanner sc = new Scanner(path, "UTF-8");
        String content="";
        while(sc.hasNextLine())
            content+=sc.nextLine();
        return content;
    }
}
