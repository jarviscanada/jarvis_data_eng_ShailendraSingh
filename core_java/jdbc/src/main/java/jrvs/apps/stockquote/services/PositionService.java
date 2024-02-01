package jrvs.apps.stockquote.services;

import jrvs.apps.stockquote.dao.Position;
import jrvs.apps.stockquote.dao.PositionDao;
import jrvs.apps.stockquote.dao.Quote;

import java.util.Optional;

public class PositionService
{
    private PositionDao Dao;
    private QuoteService QuoteServiceInstance;

    public PositionService(PositionDao dao, QuoteService quoteServiceInstance)
    {
        this.Dao = dao;
        this.QuoteServiceInstance = quoteServiceInstance;
    }

    /**
     * Processes a buy order and updates the database accordingly
     * @param ticker
     * @param numberOfShares
     * @param price
     * @return The position in our database after processing the buy (null if the inputs are invalid)
     */
    public Position Buy(String ticker, int numberOfShares, double price)
    {
        ticker = ticker.toUpperCase();
        //You can't sell negative shares or have a negative price
        if(numberOfShares < 0 || price < 0)
        {
            return null;
        }

        //Get quote for ticker. If it doesn't exist, then the purchase should fail (return null)
        Optional<Quote> quote = QuoteServiceInstance.FetchQuoteDataFromAPI(ticker);
        if(quote.isEmpty())
        {
            return null;
        }

        //Get the volume of the quote, to ensure bought shares are no more than the volume
        int volume = quote.get().getVolume();

        //Check if the Position already exists
        Optional<Position> position = Dao.findById(ticker);

        //If position already exists, it must simply be updated
        if(position.isPresent())
        {
            //Update the position object, with the new share purchase
            position.get().BuyShares(numberOfShares, price);

            //If the new share purchase, will result in a numberOfShares higher than the volume, it must fail
            if(position.get().getNumOfShares() > volume)
            {
                return null;
            }

            //Update database
            Dao.save(position.get());
        }

        //If position does not exist, create new object and insert
        else
        {
            //Since the position doesn't exist in the database, all we have to check is if the numberOfShares purchase is higher than volume
            if(numberOfShares > volume)
            {
                return null;
            }

            //Update database and make new position object
            position = Optional.of(Dao.save(new Position(ticker, numberOfShares, numberOfShares*price)));
        }

        //By this point, the position object shouldn't be null and thus a purchase was successful
        return position.get();
    }

    /**
     * Sells all shares of the given ticker symbol
     * @param ticker
     */
    public void Sell(String ticker)
    {
        ticker = ticker.toUpperCase();
        Dao.deleteById(ticker);
    }

    public Optional<Position> GetPositionFromDatabase(String symbol)
    {
        symbol = symbol.toUpperCase();
        return Dao.findById(symbol);
    }

}
