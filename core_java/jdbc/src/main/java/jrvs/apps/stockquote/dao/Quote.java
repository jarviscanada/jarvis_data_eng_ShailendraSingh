package jrvs.apps.stockquote.dao;

import com.google.gson.annotations.SerializedName;

public class Quote
{
    @SerializedName("01. symbol")
    String Symbol;
    @SerializedName("02. open")
    String Open;
    @SerializedName("03. high")
    String High;
    @SerializedName("04. low")
    String Low;
    @SerializedName("05. price")
    String Price;
    @SerializedName("06. volume")
    String Volume;
    @SerializedName("07. latest trading day")
    String LatestTradingDay;
    @SerializedName("08. previous close")
    String PreviousClose;
    @SerializedName("09. change")
    String Change;
    @SerializedName("10. change percent")
    String ChangePercent;
}