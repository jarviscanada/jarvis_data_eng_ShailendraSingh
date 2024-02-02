package jrvs.apps.stockquote.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionDao implements CrudDao<Position, String>
{
    private static final Logger logger = LoggerFactory.getLogger(PositionDao.class);
    private Connection ConnectionObject;

    public PositionDao(Connection newConnection)
    {
        //Initialize logger
        BasicConfigurator.configure();
        PositionDao.logger.debug("Started PositionDao");

        this.ConnectionObject = newConnection;
    }

    @Override
    public Position save(Position entity) throws IllegalArgumentException
    {
        if(entity == null)
        {
            PositionDao.logger.debug("Invalid entity input");
            throw new IllegalArgumentException();
        }

        //Check if the entity is already in the database
        boolean entityExists = findById(entity.getSymbol()).isPresent();

        //If the entity already exists, then simply update the entry
        if(entityExists)
        {
            PositionDao.logger.debug(String.format("Trying to update Position (%s)", entity.getSymbol()));

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
                PositionDao.logger.debug(String.format("Database error trying to update Position (%s)", entity.getSymbol()));
                throw new RuntimeException(e);
            }

            PositionDao.logger.debug(String.format("Finished update (%s)", entity.getSymbol()));
        }

        //If the entity does not exist, it is new and must be inserted
        else
        {
            PositionDao.logger.debug(String.format("Trying to save Position (%s)", entity.getSymbol()));

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
                PositionDao.logger.debug(String.format("Database error trying to update position (%s)", entity.getSymbol()));
                throw new RuntimeException(e);
            }

            PositionDao.logger.debug(String.format("Finished save (%s)", entity.getSymbol()));
        }

        return entity;
    }

    @Override
    public Optional<Position> findById(String s) throws IllegalArgumentException
    {
        if(s == null)
        {
            PositionDao.logger.debug(String.format("Invalid ID input, (%s)", s));
            throw new IllegalArgumentException();
        }

        //Get every row with the symbol s. symbol is a primary key and thus only one row should exist
        String sqlStatement =
                "SELECT * " +
                "FROM position " +
                "WHERE symbol = ?;";
        PositionDao.logger.debug(String.format("Attempting to retrieve Position object (%s)", s));
        try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
        {
            statement.setString(1, s);

            ResultSet outputSet = statement.executeQuery();

            //Get the output row, convert it to a Position object and return it
            if(outputSet.next())
            {
                PositionDao.logger.debug(String.format("Retrieved Position object (%s)", s));
                return Optional.of(new Position(outputSet));
            }
        }
        catch (SQLException e)
        {
            PositionDao.logger.debug(String.format("Database error trying to retrieve Position (%s)", s));
            throw new RuntimeException(e);
        }

        //If it gets here, we could not find any entries with symbol s
        PositionDao.logger.debug(String.format("Could not find Position object (%s)", s));
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
        PositionDao.logger.debug("Attempting to find all Positions in database");
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
            PositionDao.logger.debug("Database error while searching for positions");
            throw new RuntimeException(e);
        }

        //Return final list of Positions
        PositionDao.logger.debug(String.format("Found %d positions"), allPositions.size());
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
        PositionDao.logger.debug(String.format("Trying to delete Position by ID (%s)", s));
        try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
        {
            statement.setString(1, s);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            PositionDao.logger.debug("Database error while deleting position");
            throw new RuntimeException(e);
        }

        PositionDao.logger.debug(String.format("Deleted position (%s)", s));

    }

    @Override
    public void deleteAll()
    {
        String sqlStatement = "DELETE FROM position;";
        PositionDao.logger.debug("Trying to delete all positions");
        try(PreparedStatement statement = ConnectionObject.prepareStatement(sqlStatement))
        {
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            PositionDao.logger.debug("Database error while trying to delete all positions");
            throw new RuntimeException(e);
        }

        PositionDao.logger.debug("Deleted all positions");
    }
}