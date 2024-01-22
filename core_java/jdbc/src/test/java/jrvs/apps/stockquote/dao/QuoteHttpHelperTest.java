package jrvs.apps.stockquote.dao;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QuoteHttpHelperTest
{
    QuoteHttpHelper QuoteGetter;
    @Before
    public void setUp() throws Exception
    {
        QuoteGetter = new QuoteHttpHelper("c84683554dmsh2433fc8bb0f3fc4p1316a6jsn6c71701641e1");
    }

    @Test
    public void fetchQuoteInfo() throws ParseException
    {
        //Get the quote once
        Quote testQuote = QuoteGetter.FetchQuoteInfo("MSFT");

        double expectedOpen = 395.7600;
        double actualOpen = testQuote.getOpen();

        assertEquals(expectedOpen, actualOpen, 0);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date expectedLatestTrading= formatter.parse("2024-01-19");
        Date actualLatestTrading = testQuote.getLatestTradingDay();

        assertEquals(formatter.format(expectedLatestTrading), formatter.format(actualLatestTrading));
    }
}