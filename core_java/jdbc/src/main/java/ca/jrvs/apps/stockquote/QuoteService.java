package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.jdbc.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.doa.Quote;
import ca.jrvs.apps.stockquote.doa.QuoteDao;
import ca.jrvs.apps.stockquote.doa.QuoteHttpHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class QuoteService {

    private QuoteDao dao;
    private final QuoteHttpHelper httpHelper;

    public QuoteService() {
        Connection connection;
        try {
            DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                    "stock_quote", "postgres", "password");
            connection = dcm.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        httpHelper = new QuoteHttpHelper("54706e6343msh96825dc87a0b044p188036jsn4bab408b03a2");
        dao = new QuoteDao(connection);
    }

    public QuoteService(QuoteDao dao, QuoteHttpHelper httpHelper) {
        this.dao = dao;
        this.httpHelper = httpHelper;
    }

    /**
     * Fetches latest quote data from endpoint
     * @param ticker
     * @return Latest quote information or empty optional if ticker symbol not found
     */
    public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
        Quote quote = httpHelper.fetchQuoteInfo(ticker);
        return Optional.ofNullable(quote);
    }

}