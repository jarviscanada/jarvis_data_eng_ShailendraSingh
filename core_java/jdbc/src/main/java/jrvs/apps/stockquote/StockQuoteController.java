package jrvs.apps.stockquote;

import jrvs.apps.stockquote.dao.Position;
import jrvs.apps.stockquote.dao.Quote;
import jrvs.apps.stockquote.services.QuoteService;
import jrvs.apps.stockquote.services.PositionService;

import java.util.Optional;
import java.util.Scanner;

public class StockQuoteController
{
    private final QuoteService QuoteServiceObj;
    private final PositionService PositionServiceObj;

    public StockQuoteController(QuoteService quoteService, PositionService positionService)
    {
        this.QuoteServiceObj = quoteService;
        this.PositionServiceObj = positionService;
    }

    /**
     * User interface for our application
     */
    public void initClient()
    {
        //User input initialization
        Scanner input = new Scanner(System.in);

        while(true)
        {
            //Get the input symbol for a stock
            System.out.print("Please enter a stock ticker (Press 'enter' with no other input to exit): ");
            String symbolInput = input.nextLine();
            if(symbolInput.isEmpty())
            {
                break;
            }

            //If the symbol is invalid, notify the user and go back to the beginning of the prompt
            Optional<Quote> quoteObject = QuoteServiceObj.FetchQuoteDataFromAPI(symbolInput);
            if(quoteObject.isEmpty())
            {
                System.out.println("INVALID TICKER\n\n");
                continue;
            }

            //If the symbol is valid, print out the whole quote
            System.out.printf("\n%s\n\n", quoteObject.get());

            //Get the current Position for this quote and if it exists in the database, print out its status
            Optional<Position> currentPosition = PositionServiceObj.GetPositionFromDatabase(symbolInput);
            if(currentPosition.isEmpty())
            {
                System.out.println("You have not purchased any shares with this stock yet!\n");
            }

            else
            {
                System.out.println("Position Status");
                System.out.println("_______________");
                System.out.printf("%s\n\n", currentPosition.get().toString());
            }

            //Ask the user if they want to sell all of their shares
            int optionNumber;
            while(true)
            {
                optionNumber = 0;
                System.out.println("Do you wish to...");
                System.out.println("(1) Sell All Shares");
                System.out.println("(2) Buy Some Shares");
                try
                {
                    optionNumber = Integer.parseInt(input.nextLine());
                }
                catch(NumberFormatException e)
                {
                    System.out.println("NOT AN INTEGER\n");
                    continue;
                }

                if(optionNumber == 1 || optionNumber == 2)
                {
                    break;
                }
            }

            //Sell All Shares
            if(optionNumber == 1)
            {
                PositionServiceObj.Sell(symbolInput);
                System.out.println("All Shares Sold\n");
            }

            //Buy Some Shares
            else if(optionNumber == 2)
            {
                while(true)
                {
                    int numberOfRequestedShares = -1;
                    System.out.print("Please enter the number of shares you wish to sell: ");
                    try
                    {
                        numberOfRequestedShares = Integer.parseInt(input.nextLine());
                    }
                    catch(NumberFormatException e)
                    {
                        System.out.println("NOT AN INTEGER\n");
                        continue;
                    }

                    //Try to buy the shares. If it returns a null, then the number of requested shares is probably invalid (it could also be price or the symbol but, those are already guaranteed as valid)
                    Position newPosition = PositionServiceObj.Buy(symbolInput, numberOfRequestedShares, quoteObject.get().getPrice());
                    if(newPosition == null)
                    {
                        System.out.println("Number of shares is out of range. Please try again!");
                        continue;
                    }

                    //If it makes it to this point, the purchase went through. So give the user the updated position
                    System.out.println("\nPurchase Successful! Your new position is below!");
                    System.out.println("Position Status");
                    System.out.println("_______________");
                    System.out.printf("%s\n\n", newPosition.toString());
                    break;
                }
            }

            //Option number should only be 1 or 2, if it is anything else, there is a critical error and the program should go back to the main menu
            else
            {
                System.out.println("CRITICAL ERROR! GOING BACK TO MAIN MENU!\n");
            }
        }
    }

}