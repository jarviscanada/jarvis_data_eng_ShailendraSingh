package jrvs.apps.stockquote.dao;

import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;

public class Quote
{
    public Quote()
    {
        this.RetrievalTimestamp = new Timestamp(System.currentTimeMillis());
    }

    public Quote(ResultSet resultSet) throws SQLException
    {
        this.Symbol = resultSet.getString("symbol");
        this.Open = resultSet.getDouble("open");
        this.High = resultSet.getDouble("high");
        this.Low = resultSet.getDouble("low");
        this.Price = resultSet.getDouble("price");
        this.Volume = resultSet.getInt("volume");
        this.LatestTradingDay = resultSet.getDate("latest_trading_day");
        this.PreviousClose = resultSet.getDouble("previous_close");
        this.Change = resultSet.getDouble("change");
        this.ChangePercent = resultSet.getString("change_percent");
        this.RetrievalTimestamp = resultSet.getTimestamp("timestamp");
    }

    @SerializedName("01. symbol")
    private String Symbol;
    @SerializedName("02. open")
    private double Open;
    @SerializedName("03. high")
    private double High;
    @SerializedName("04. low")
    private double Low;
    @SerializedName("05. price")
    private double Price;
    @SerializedName("06. volume")
    private int Volume;
    @SerializedName("07. latest trading day")
    private Date LatestTradingDay;
    @SerializedName("08. previous close")
    private double PreviousClose;
    @SerializedName("09. change")
    private double Change;
    @SerializedName("10. change percent")
    private String ChangePercent;
    private Timestamp RetrievalTimestamp;

    public String getSymbol()
    {
        return Symbol;
    }

    public double getOpen()
    {
        return Open;
    }

    public double getHigh()
    {
        return High;
    }

    public double getLow()
    {
        return Low;
    }

    public double getPrice()
    {
        return Price;
    }

    public int getVolume()
    {
        return Volume;
    }

    public Date getLatestTradingDay()
    {
        return LatestTradingDay;
    }

    public double getPreviousClose()
    {
        return PreviousClose;
    }

    public double getChange()
    {
        return Change;
    }

    public String getChangePercent()
    {
        return ChangePercent;
    }

    public Timestamp getRetrievalTimestamp()
    {
        return RetrievalTimestamp;
    }
}