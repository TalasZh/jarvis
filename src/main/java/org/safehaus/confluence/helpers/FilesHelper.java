package org.safehaus.confluence.helpers;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


/**
 * Created by root on 5/8/15.
 */
public class FilesHelper
{
    public String ReadFile( String filePath ) throws IOException
    {
        //        Path path = Paths.get(filePath);
        //        Scanner sc = new Scanner(path, "UTF-8");
        //        String content="";
        //        while(sc.hasNextLine())
        //            content+=sc.nextLine();
        //        return content;


        InputStream is = getClass().getClassLoader().getResourceAsStream( filePath );

        BufferedReader reader = new BufferedReader( new InputStreamReader( is ) );
        StringBuilder out = new StringBuilder();
        String line;
        while ( ( line = reader.readLine() ) != null )
        {
            out.append( line );
        }
        reader.close();

        return out.toString();
    }
}
