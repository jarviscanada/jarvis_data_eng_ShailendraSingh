package jrvs.apps.stockquote.services;

import jrvs.apps.stockquote.dao.Quote;
import jrvs.apps.stockquote.dao.QuoteDao;
import jrvs.apps.stockquote.dao.QuoteHttpHelper;

import java.util.Optional;

public class QuoteService
{
    private QuoteDao Dao;
    private QuoteHttpHelper HttpHelper;

    public QuoteService(QuoteDao dao, QuoteHttpHelper httpHelper)
    {
        this.Dao = dao;
        this.HttpHelper = httpHelper;
    }

    /**
     * Fetches latest quote data from endpoint
     * @param ticker
     * @return Latest quote information or empty optional if ticker symbol not found
     */
    public Optional<Quote> FetchQuoteDataFromAPI(String ticker)
    {
        //Get the new quote from the API
        Optional<Quote> quote = Optional.ofNullable(HttpHelper.FetchQuoteInfo(ticker));
        if(quote.isEmpty())
        {
            return quote;
        }

        //If a quote actually arrives from the API, save the new copy to the database and return it
        return Optional.of(Dao.save(quote.get()));
    }
}
