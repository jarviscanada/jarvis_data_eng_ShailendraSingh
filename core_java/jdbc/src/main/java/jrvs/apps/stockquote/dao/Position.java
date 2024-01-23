package jrvs.apps.stockquote.dao;

public class Position
{
    private String ticker; //id
    private int numOfShares;
    private double valuePaid; //total amount paid for shares


    public String getTicker()
    {
        return ticker;
    }

    public int getNumOfShares()
    {
        return numOfShares;
    }

    public double getValuePaid()
    {
        return valuePaid;
    }
}
