package jrvs.apps.stockquote;

import com.google.gson.Gson;
import jrvs.apps.stockquote.dao.PositionDao;
import jrvs.apps.stockquote.dao.Quote;
import jrvs.apps.stockquote.dao.QuoteDao;
import jrvs.apps.stockquote.dao.QuoteHttpHelper;
import jrvs.apps.stockquote.services.PositionService;
import jrvs.apps.stockquote.services.QuoteService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
1. Go to core_java directory
2. Run java -cp jdbc/target/stockquote-1.0-SNAPSHOT.jar jrvs.apps.stockquote.Main
 */

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

        //Load the driver
        try
        {
            Class.forName(properties.getDatabaseClass());
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }

        //Establish connection with database
        String databaseUrl = String.format("jdbc:postgresql://%s:%d/%s", properties.getServer(), properties.getPort(), properties.getDatabase());
        try(Connection connection = DriverManager.getConnection(databaseUrl, properties.getUsername(), properties.getPassword()))
        {
            //Setup DAOs
            QuoteDao quoteDao = new QuoteDao(connection);
            PositionDao positionDao = new PositionDao(connection);

            //Setup the HTTP client
            QuoteHttpHelper httpHelper = new QuoteHttpHelper(properties.getApiKey());

            //Setup the services for quotes and positions
            QuoteService quoteService = new QuoteService(quoteDao, httpHelper);
            PositionService positionService = new PositionService(positionDao, quoteService);

            //Create the StockQuoteController and initialize it to start the program
            StockQuoteController controller = new StockQuoteController(quoteService, positionService);
            while(true) //If an exception isn't caught, print out its info and try again
            {
                try
                {
                    controller.initClient();
                    break;
                }
                catch(Exception e)
                {
                    System.out.println("Unknown Error");
                    System.out.println("Exception Name: " + e.toString());
                    System.out.println("Exception Cause: " + e.getCause());
                    System.out.println("Exception Message: " + e.getMessage());
                    System.out.println();
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        System.out.println("Goodbye!");
    }
}
