package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.jdbc.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.doa.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class PositionService {
    private PositionDao dao;

    public PositionService() {
        try {
            DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                    "stock_quote", "postgres", "password");
            Connection connection = null;
            connection = dcm.getConnection();
            dao = new PositionDao(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PositionService(PositionDao dao) {
        this.dao = dao;
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
}
