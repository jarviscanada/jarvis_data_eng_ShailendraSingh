package jrvs.apps.stockquote.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuoteDao implements CrudDao<Quote, String>
{
    private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);
    private Connection ConnectionObject;

    public QuoteDao(Connection newConnection)
    {
        //Initialize logger
        BasicConfigurator.configure();
        QuoteDao.logger.debug("Started QuoteDao");
        this.ConnectionObject = newConnection;
    }

    @Override
    public Quote save(Quote entity) throws IllegalArgumentException
    {
        if(entity == null)
        {
            QuoteDao.logger.debug("Invalid entity input");
            throw new IllegalArgumentException();
        }

        //Check if the entity is already in the database
        boolean entityExists = findById(entity.getSymbol()).isPresent();

        //If the entity already exists, then simply update the entry
        if(entityExists)
        {
            QuoteDao.logger.debug(String.format("Trying to update Quote (%s)", entity.getSymbol()));

            String sqlStatement =
                    "UPDATE quote " +
                    "SET symbol = ?, open = ?, high = ?, low = ?, price = ?, volume = ?, latest_trading_day = ?, previous_close = ?, change = ?, change_percent = ?, timestamp = ? " +
                    "WHERE symbol = ?;";

            try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
            {
                statement.setString(1, entity.getSymbol());
                statement.setDouble(2, entity.getOpen());
                statement.setDouble(3, entity.getHigh());
                statement.setDouble(4, entity.getLow());
                statement.setDouble(5, entity.getPrice());
                statement.setInt(6, entity.getVolume());
                statement.setDate(7, entity.getLatestTradingDay());
                statement.setDouble(8, entity.getPreviousClose());
                statement.setDouble(9, entity.getChange());
                statement.setString(10, entity.getChangePercent());
                statement.setTimestamp(11, entity.getRetrievalTimestamp());

                statement.setString(12, entity.getSymbol());

                statement.executeUpdate();
            }

            catch (SQLException e)
            {
                QuoteDao.logger.debug(String.format("Database error trying to update Quote (%s)", entity.getSymbol()));
                throw new RuntimeException(e);
            }

            QuoteDao.logger.debug(String.format("Finished update (%s)", entity.getSymbol()));
        }

        //If the entity does not exist, it is new and must be inserted
        else
        {
            QuoteDao.logger.debug(String.format("Trying to save Quote (%s)", entity.getSymbol()));

            String sqlStatement =
                    "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
            {
                statement.setString(1, entity.getSymbol());
                statement.setDouble(2, entity.getOpen());
                statement.setDouble(3, entity.getHigh());
                statement.setDouble(4, entity.getLow());
                statement.setDouble(5, entity.getPrice());
                statement.setInt(6, entity.getVolume());
                statement.setDate(7, entity.getLatestTradingDay());
                statement.setDouble(8, entity.getPreviousClose());
                statement.setDouble(9, entity.getChange());
                statement.setString(10, entity.getChangePercent());
                statement.setTimestamp(11, entity.getRetrievalTimestamp());

                statement.executeUpdate();
            }

            catch (SQLException e)
            {
                QuoteDao.logger.debug(String.format("Database error trying to save Quote (%s)", entity.getSymbol()));
                throw new RuntimeException(e);
            }

            QuoteDao.logger.debug(String.format("Finished save (%s)", entity.getSymbol()));
        }

        return entity;
    }

    @Override
    public Optional<Quote> findById(String s) throws IllegalArgumentException
    {
        if(s == null)
        {
            QuoteDao.logger.debug(String.format("Invalid ID input, (%s)", s));
            throw new IllegalArgumentException();
        }

        //Get every row with the symbol s. symbol is a primary key and thus only one row should exist
        String sqlStatement =
                "SELECT * " +
                "FROM quote " +
                "WHERE symbol = ?;";
        QuoteDao.logger.debug(String.format("Attempting to retrieve Quote object (%s)", s));
        try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
        {
            statement.setString(1, s);

            ResultSet outputSet = statement.executeQuery();

            //Get the output row, convert it to a quote object and return it
            if(outputSet.next())
            {
                QuoteDao.logger.debug(String.format("Retrieved Quote object (%s)", s));
                return Optional.of(new Quote(outputSet));
            }
        }
        catch (SQLException e)
        {
            QuoteDao.logger.debug(String.format("Database error trying to retrieve Quote (%s)", s));
            throw new RuntimeException(e);
        }

        //If it gets here, we could not find any entries with symbol s
        QuoteDao.logger.debug(String.format("Could not find Quote object (%s)", s));
        return Optional.empty();
    }

    @Override
    public Iterable<Quote> findAll()
    {
        ArrayList<Quote> allQuotes = new ArrayList<Quote>();

        //Get all rows in quote table
        String sqlStatement =
                "SELECT * " +
                "FROM quote;";
        QuoteDao.logger.debug("Attempting to find all Quotes in database");
        try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
        {
            ResultSet outputSet = statement.executeQuery();

            //Get every object and insert into the list
            while(outputSet.next())
            {
                allQuotes.add(new Quote(outputSet));
            }
        }
        catch (SQLException e)
        {
            QuoteDao.logger.debug("Database error while searching for quotes");
            throw new RuntimeException(e);
        }

        //Return final list of quotes
        QuoteDao.logger.debug(String.format("Found %d quotes"), allQuotes.size());
        return allQuotes;
    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException
    {
        if(s == null)
        {
            throw new IllegalArgumentException();
        }

        String sqlStatement =
                "DELETE FROM quote " +
                "WHERE symbol = ?;";
        QuoteDao.logger.debug(String.format("Trying to delete Quote by ID (%s)", s));
        try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
        {
            statement.setString(1, s);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            QuoteDao.logger.debug("Database error while deleting quote");
            throw new RuntimeException(e);
        }
        QuoteDao.logger.debug(String.format("Deleted quote (%s)", s));
    }

    @Override
    public void deleteAll()
    {
        String sqlStatement = "DELETE FROM quote;";
        QuoteDao.logger.debug("Trying to delete all quotes");
        try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
        {
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            QuoteDao.logger.debug("Database error while trying to delete all quotes");
            throw new RuntimeException(e);
        }

        QuoteDao.logger.debug("Deleted all quotes");
    }
}