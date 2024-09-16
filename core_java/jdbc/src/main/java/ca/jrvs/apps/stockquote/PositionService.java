package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.doa.*;
import java.util.List;

public class PositionService {
    private PositionDao dao;
    private QuoteHttpHelper httpHelper;

    public PositionService(PositionDao dao) {
        this.dao = dao;
    }

    public PositionService(PositionDao dao, QuoteHttpHelper httpHelper) {
        this.dao = dao;
        this.httpHelper = httpHelper;
    }

    /**
     * Processes a buy order and updates the database accordingly
     * @param ticker
     * @param numberOfShares
     * @param price
     * @return The position in our database after processing the buy
     */
    public Position buy(String ticker, int numberOfShares, double price) {
        Position position =  new Position(ticker, numberOfShares, price);
        return dao.save(position);
    }

    /**
     * Sells all shares of the given ticker symbol
     * @param ticker
     */
    public void sell(String ticker) {
        dao.deleteById(ticker);
    }

    /**
     * Print out all shares that have been bought in table format
     */
    public void displayAll() {
        // Print table header
        System.out.printf("%-15s%-15s%-15s%-15s%-15s\n", "Symbol", "Num of Shares", "Book Cost",
                "Current Value", "Gain/Loss");
        List<Position> positions = (List<Position>) dao.findAll();

        if (positions.isEmpty()) {
            System.out.println("No stocks bought");
        } else {
            Quote quote;
            for (Position position : positions) {
                quote = httpHelper.fetchQuoteInfo(position.getTicker());
                double curValue = quote.getPrice() * position.getNumOfShares();
                double gainLoss = curValue - position.getValuePaid();
                System.out.printf("%-15s%-15s%-15s%-15.2f%-15.2f\n", position.getTicker(),
                        position.getNumOfShares(), position.getValuePaid(), curValue, gainLoss);
            }
        }
    }

    /**
     * Return whether given symbol is in position table
     * @param symbol
     * @return true or false
     */
    public boolean symbolInPosition(String symbol) {
        return dao.findById(symbol).isPresent();
    }
}
