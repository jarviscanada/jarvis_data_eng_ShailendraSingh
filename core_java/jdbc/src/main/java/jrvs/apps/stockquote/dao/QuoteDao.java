package jrvs.apps.stockquote.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String>
{
    private Connection ConnectionObject;

    public QuoteDao(Connection newConnection)
    {
        this.ConnectionObject = newConnection;
    }

    @Override
    public Quote save(Quote entity) throws IllegalArgumentException
    {
        if(entity == null)
        {
            throw new IllegalArgumentException();
        }

        //Check if the entity is already in the database
        boolean entityExists = findById(entity.getSymbol()).isPresent();

        //If the entity already exists, then simply update the entry
        if(entityExists)
        {
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
                throw new RuntimeException(e);
            }

        }

        //If the entity does not exist, it is new and must be inserted
        else
        {
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
                throw new RuntimeException(e);
            }
        }

        return entity;
    }

    @Override
    public Optional<Quote> findById(String s) throws IllegalArgumentException
    {
        if(s == null)
        {
            throw new IllegalArgumentException();
        }

        //Get every row with the symbol s. symbol is a primary key and thus only one row should exist
        String sqlStatement =
                "SELECT * " +
                "FROM quote " +
                "WHERE symbol = ?;";
        try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
        {
            statement.setString(1, s);

            ResultSet outputSet = statement.executeQuery();

            //Get the output row, convert it to a quote object and return it
            if(outputSet.next())
            {
                return Optional.of(new Quote(outputSet));
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        //If it gets here, we could not find any entries with symbol s
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
            throw new RuntimeException(e);
        }

        //Return final list of quotes
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
        try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
        {
            statement.setString(1, s);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll()
    {
        String sqlStatement = "DELETE FROM quote;";
        try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
        {
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}