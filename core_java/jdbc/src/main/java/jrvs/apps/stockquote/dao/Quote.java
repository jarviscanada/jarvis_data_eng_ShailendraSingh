package jrvs.apps.stockquote.dao;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.Date;

public class Quote
{
    public Quote()
    {
        this.RetrievalTimestamp = new Timestamp(System.currentTimeMillis());
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