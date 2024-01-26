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
        return Optional.ofNullable(HttpHelper.FetchQuoteInfo(ticker));
    }
}
