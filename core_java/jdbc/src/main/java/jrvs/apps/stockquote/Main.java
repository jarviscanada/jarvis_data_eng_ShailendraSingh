package jrvs.apps.stockquote;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main
{
    private static final Path PROPERTIES_PATH = Path.of("jdbc/src/main/resources/properties.json");

    public static void main(String[] args) throws IOException
    {
        //Read out whole properties json into string
        String propertiesString = Files.readString(PROPERTIES_PATH);

        //Initialize Properties
        Gson gsonObject = new Gson();
        StockQuoteProperties properties = gsonObject.fromJson(propertiesString, StockQuoteProperties.class);

        System.out.println(properties.getPort());
    }
}
