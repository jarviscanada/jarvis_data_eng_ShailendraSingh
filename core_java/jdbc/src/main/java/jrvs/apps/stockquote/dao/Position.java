package jrvs.apps.stockquote.dao;

import com.sun.jdi.Value;

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

    public void BuyShares(int incomingNewNumSharesBought, double priceOfEachShare)
    {
        this.NumOfShares += incomingNewNumSharesBought;
        this.ValuePaid += priceOfEachShare*incomingNewNumSharesBought;
    }

    public String toString()
    {
        String positionString =
                String.format
                        (
                                "Symbol: %s\n" +
                                "Number Of Owned: %d\n" +
                                "Value Paid: %f\n",

                                Symbol,
                                NumOfShares,
                                ValuePaid
                        );

        return positionString;
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
