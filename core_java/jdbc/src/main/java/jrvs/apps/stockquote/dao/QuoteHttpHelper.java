package jrvs.apps.stockquote.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;

public class QuoteHttpHelper
{
    private static final Logger logger = LoggerFactory.getLogger(QuoteHttpHelper.class);
    private String APIKey;
    private HttpClient Client;

    public QuoteHttpHelper(String apiKey)
    {
        QuoteHttpHelper.logger.debug("starting QuoteHttpHelper");
        this.APIKey = apiKey;
        this.Client = HttpClient.newHttpClient();
    }

    public Quote FetchQuoteInfo(String symbol) throws IllegalArgumentException
    {
        String apiUrl = String.format("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=%s&datatype=json", symbol);

        //Create request for quote
        QuoteHttpHelper.logger.debug(String.format("Crafting API request for (%s)", symbol));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("X-RapidAPI-Key", APIKey)
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        //Send request and get JSON response string
        String jsonResponse;
        QuoteHttpHelper.logger.debug(String.format("Sending API request for (%s)", symbol));
        try
        {
            HttpResponse<String> response = Client.send(request, HttpResponse.BodyHandlers.ofString());
            jsonResponse = response.body();
            QuoteHttpHelper.logger.debug(String.format("Sent API request for (%s) and received response", symbol));
        }
        catch (IOException | InterruptedException e)
        {
            String errorMessage = String.format("Error while processing API request for (%s)", symbol);
            QuoteHttpHelper.logger.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }

        //Convert JSON to Quote object
        QuoteHttpHelper.logger.debug(String.format("Attempting to convert JSON response to Quote (%s)", symbol));
        Gson gsonObject = new GsonBuilder().setDateFormat("yyyy-MM-dd").create(); //Ensure the LatestTradingDay date is parsed properly
        Quote quoteObject = gsonObject.fromJson(jsonResponse, Quote_Root.class).GlobalQuote;
        if(quoteObject == null)
        {
            String errorMessage = String.format("Error while converting to JSON (%s)", symbol);
            QuoteHttpHelper.logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        //If the quote's symbol is null, then clearly the API search failed and  the symbol doesn't exist
        if(quoteObject.getSymbol() == null)
        {
            QuoteHttpHelper.logger.debug(String.format("Symbol (%s) could not be retrieved from the database", symbol));
            return null;
        }

        QuoteHttpHelper.logger.debug(String.format("Symbol (%s) found and has been converted to Quote object", symbol));
        return quoteObject;
    }
}
