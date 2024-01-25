package jrvs.apps.stockquote.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;

public class QuoteHttpHelper
{
    private String APIKey;
    private HttpClient Client;

    public QuoteHttpHelper(String apiKey)
    {
        this.APIKey = apiKey;
        this.Client = HttpClient.newHttpClient();
    }

    public Quote FetchQuoteInfo(String symbol) throws IllegalArgumentException
    {
        String apiUrl = String.format("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=%s&datatype=json", symbol);

        //Create request for quote
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("X-RapidAPI-Key", APIKey)
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        //Send request and get JSON response string
        String jsonResponse;
        try
        {
            HttpResponse<String> response = Client.send(request, HttpResponse.BodyHandlers.ofString());
            jsonResponse = response.body();
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        //Convert JSON to Quote object
        Gson gsonObject = new GsonBuilder().setDateFormat("yyyy-MM-dd").create(); //Ensure the LatestTradingDay date is parsed properly
        Quote quoteObject = gsonObject.fromJson(jsonResponse, Quote_Root.class).GlobalQuote;
        if(quoteObject == null)
        {
            throw new IllegalArgumentException();
        }

        return quoteObject;
    }
}
