package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.doa.Quote;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class StockQuoteController {

    private QuoteService quoteService;
    private PositionService positionService;

    public StockQuoteController(QuoteService quoteService, PositionService positionService) {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }

    /**
     * User interface for our application
     */
    public void initClient() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Display menu options
            System.out.println("HOLDINGS");
            positionService.displayAll();
            System.out.println();
            System.out.println("1. Buy");
            System.out.println("2. Sell");
            System.out.println("3. Search Symbol");
            System.out.println("4. Exit");
            System.out.print("Enter your number choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                String symbol;

                switch (choice) {
                    case 1:
                        System.out.flush();
                        System.out.println("You selected Option 1 - Buy");
                        System.out.print("Enter ticket symbol you wish to buy: ");
                        symbol = scanner.next();

                        if (positionService.symbolInPosition(symbol)) {
                            System.out.println("Symbol already in portfolio - Try again");
                            break;
                        }
                        if (!quoteService.isValidSymbol(symbol)) {
                            System.out.println("Invalid ticket symbol - Try again");
                            break;
                        }
                        System.out.print("Enter number amount you wish to buy: ");
                        int num = scanner.nextInt();
                        if (!(num >= 0)) {
                            System.out.println("Number of shares must be a positive integer.");
                            break;
                        }
                        Optional<Quote> optionalQuote = quoteService.fetchQuoteDataFromAPI(symbol);
                        if (optionalQuote.isPresent()) {
                            double price = optionalQuote.get().getPrice();
                            positionService.buy(symbol, num, price*num);
                        } else {
                            System.out.println("Invalid symbol - Try again");
                        }
                        break;
                    case 2:
                        System.out.flush();
                        System.out.println("You selected Option 2 - Sell");
                        System.out.print("Enter ticket symbol you wish to sell: ");
                        symbol = scanner.nextLine();
                        if (!positionService.symbolInPosition(symbol)) {
                            System.out.println("Invalid ticket symbol - Try again");
                            break;
                        }
                        positionService.sell(symbol);
                        break;
                    case 3:
                        System.out.flush();
                        System.out.println("You selected Option 3 - Search Symbol");
                        System.out.print("Enter ticket symbol you wish to look up: ");
                        symbol = scanner.nextLine();
                        if (!quoteService.isValidSymbol(symbol)) {
                            System.out.println("Invalid ticket symbol - Try again");
                            break;
                        }
                        quoteService.displayOne(symbol);
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a number from 1-4");
                }
            } catch (InputMismatchException e) {
                System.out.flush();
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }
    }

}
