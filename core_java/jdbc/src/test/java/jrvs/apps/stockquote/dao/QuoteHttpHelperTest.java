package jrvs.apps.stockquote.dao;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuoteHttpHelperTest
{
    QuoteHttpHelper QuoteGetter;
    @Before
    public void setUp() throws Exception
    {
        QuoteGetter = new QuoteHttpHelper("c84683554dmsh2433fc8bb0f3fc4p1316a6jsn6c71701641e1");
    }

    @Test
    public void fetchQuoteInfo()
    {
        String expected = "395.5000";
        String actual = QuoteGetter.FetchQuoteInfo("MSFT").Open;

        assertEquals(expected, actual);
    }
}