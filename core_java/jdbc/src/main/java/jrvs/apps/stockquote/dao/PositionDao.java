package jrvs.apps.stockquote.dao;

import java.sql.Connection;
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

        return null;
    }

    @Override
    public Optional<Position> findById(String s) throws IllegalArgumentException
    {
        return Optional.empty();
    }

    @Override
    public Iterable<Position> findAll()
    {
        return null;
    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException
    {

    }

    @Override
    public void deleteAll()
    {

    }
}