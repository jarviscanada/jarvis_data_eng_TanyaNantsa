package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.doa.Quote;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockQuoteController {

    private QuoteService quoteService;
    private PositionService positionService;
    private static final Logger flowLogger = LoggerFactory.getLogger("FlowLogger");
    private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
    private static final String FLOW_LOG_FILE = "flow.log";
    private static final String ERROR_LOG_FILE = "error.log";

    public StockQuoteController(QuoteService quoteService, PositionService positionService) {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }

    private void configureLogger(String logFile) {
        try {
            File file = new File(logFile);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error configuring logger: " + e.getMessage());
        }
    }

    /**
     * User interface for our application
     */
    public void initClient() {
        configureLogger("flow.log");
        configureLogger("error.log");
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
                        flowLogger.info("Option 1 - Buy selected");
                        System.out.flush();
                        System.out.println("You selected Option 1 - Buy");
                        System.out.print("Enter ticket symbol you wish to buy: ");
                        symbol = scanner.next();

                        if (positionService.symbolInPosition(symbol)) {
                            System.out.println("Symbol already in portfolio - Try again");
                            errorLogger.warn("Symbol already in portfolio - Try again");
                            break;
                        }
                        if (!quoteService.isValidSymbol(symbol)) {
                            System.out.println("Invalid ticket symbol - Try again");
                            errorLogger.warn("Invalid ticket symbol - Try again");
                            break;
                        }
                        System.out.print("Enter number amount you wish to buy: ");
                        int num = scanner.nextInt();
                        if (!(num >= 0)) {
                            System.out.println("Number of shares must be a positive integer.");
                            errorLogger.warn("Number of shares must be a positive integer.");
                            break;
                        }
                        Optional<Quote> optionalQuote = quoteService.fetchQuoteDataFromAPI(symbol);
                        if (optionalQuote.isPresent()) {
                            double price = optionalQuote.get().getPrice();
                            positionService.buy(symbol, num, price*num);
                            flowLogger.info("Bought " + num + " shares of " + symbol + " at price " + price);
                        } else {
                            System.out.println("Invalid symbol - Try again");
                            errorLogger.warn("Invalid symbol - Try again");
                        }
                        break;
                    case 2:
                        flowLogger.info("Option 2 - Sell selected");
                        System.out.flush();
                        System.out.println("You selected Option 2 - Sell");
                        System.out.print("Enter ticket symbol you wish to sell: ");
                        symbol = scanner.nextLine();
                        if (!positionService.symbolInPosition(symbol)) {
                            System.out.println("Invalid ticket symbol - Try again");
                            errorLogger.warn("Invalid ticket symbol - Try again");
                            break;
                        }
                        positionService.sell(symbol);
                        flowLogger.info("Sold all shares of " + symbol);
                        break;
                    case 3:
                        flowLogger.info("Option 3 - Search Symbol selected");
                        System.out.flush();
                        System.out.println("You selected Option 3 - Search Symbol");
                        System.out.print("Enter ticket symbol you wish to look up: ");
                        symbol = scanner.nextLine();
                        if (!quoteService.isValidSymbol(symbol)) {
                            System.out.println("Invalid ticket symbol - Try again");
                            errorLogger.warn("Invalid ticket symbol - Try again");
                            break;
                        }
                        quoteService.displayOne(symbol);
                        break;
                    case 4:
                        flowLogger.info("Exiting...");
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a number from 1-4");
                        errorLogger.warn("Invalid choice. Please enter a number from 1-4");
                }
            } catch (InputMismatchException e) {
                errorLogger.warn("Invalid input. Please enter a valid number.");
                System.out.flush();
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
        }
    }

}
