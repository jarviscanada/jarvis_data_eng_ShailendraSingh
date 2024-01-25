package jrvs.apps.stockquote.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Position
{
    private String Symbol; //id
    private int NumOfShares;
    private double ValuePaid; //total amount paid for shares

    public Position(String symbol, int numOfShares, double valuePaid)
    {
        this.Symbol = symbol;
        this.NumOfShares = numOfShares;
        this.ValuePaid = valuePaid;
    }

    public Position(ResultSet resultSet) throws SQLException
    {
        this.Symbol = resultSet.getString("symbol");
        this.NumOfShares = resultSet.getInt("number_of_shares");
        this.ValuePaid = resultSet.getDouble("value_paid");
    }

    public String getSymbol()
    {
        return Symbol;
    }

    public int getNumOfShares()
    {
        return NumOfShares;
    }

    public double getValuePaid()
    {
        return ValuePaid;
    }
}