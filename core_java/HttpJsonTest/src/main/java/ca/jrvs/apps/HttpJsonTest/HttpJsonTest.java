package ca.jrvs.apps.HttpJsonTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpJsonTest
{
    public static void main(String[] args)
    {
        String symbol = "AAPL";
        String apiToken = "c84683554dmsh2433fc8bb0f3fc4p1316a6jsn6c71701641e1";
        String apiUrl = String.format("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=%s&datatype=json", symbol);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("X-RapidAPI-Key", apiToken)
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        try
        {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}