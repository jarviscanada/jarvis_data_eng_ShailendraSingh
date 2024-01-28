package jrvs.apps.stockquote;

public class StockQuoteProperties
{
    private String DatabaseClass;
    private String Server;
    private String Database;
    private int Port;
    private String Username;
    private String Password;
    private String ApiKey;

    public String getDatabaseClass()
    {
        return DatabaseClass;
    }

    public String getServer()
    {
        return Server;
    }

    public String getDatabase()
    {
        return Database;
    }

    public int getPort()
    {
        return Port;
    }

    public String getUsername()
    {
        return Username;
    }

    public String getPassword()
    {
        return Password;
    }

    public String getApiKey()
    {
        return ApiKey;
    }
}
