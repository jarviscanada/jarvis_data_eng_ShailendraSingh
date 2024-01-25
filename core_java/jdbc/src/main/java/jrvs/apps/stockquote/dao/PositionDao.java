package jrvs.apps.stockquote.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String>
{

    private Connection ConnectionObject;

    public PositionDao(Connection newConnection)
    {
        this.ConnectionObject = newConnection;
    }

    @Override
    public Position save(Position entity) throws IllegalArgumentException
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
                    "UPDATE position " +
                    "SET symbol = ?, number_of_shares = ?, value_paid = ? " +
                    "WHERE symbol = ?;";

            try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
            {
                statement.setString(1, entity.getSymbol());
                statement.setInt(2, entity.getNumOfShares());
                statement.setDouble(3, entity.getValuePaid());

                statement.setString(4, entity.getSymbol());

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
                    "INSERT INTO position (symbol, number_of_shares, value_paid) " +
                    "VALUES (?, ?, ?);";

            try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
            {
                statement.setString(1, entity.getSymbol());
                statement.setInt(2, entity.getNumOfShares());
                statement.setDouble(3, entity.getValuePaid());

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
    public Optional<Position> findById(String s) throws IllegalArgumentException
    {
        if(s == null)
        {
            throw new IllegalArgumentException();
        }

        //Get every row with the symbol s. symbol is a primary key and thus only one row should exist
        String sqlStatement =
                "SELECT * " +
                "FROM position " +
                "WHERE symbol = ?;";
        try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
        {
            statement.setString(1, s);

            ResultSet outputSet = statement.executeQuery();

            //Get the output row, convert it to a Position object and return it
            if(outputSet.next())
            {
                return Optional.of(new Position(outputSet));
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
    public Iterable<Position> findAll()
    {
        ArrayList<Position> allPositions = new ArrayList<Position>();

        //Get all rows in position table
        String sqlStatement =
                "SELECT * " +
                "FROM position;";
        try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
        {
            ResultSet outputSet = statement.executeQuery();

            //Get every object and insert into the list
            while(outputSet.next())
            {
                allPositions.add(new Position(outputSet));
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        //Return final list of Positions
        return allPositions;
    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException
    {
        if(s == null)
        {
            throw new IllegalArgumentException();
        }

        String sqlStatement =
                "DELETE FROM position " +
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
        String sqlStatement = "DELETE FROM position;";
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