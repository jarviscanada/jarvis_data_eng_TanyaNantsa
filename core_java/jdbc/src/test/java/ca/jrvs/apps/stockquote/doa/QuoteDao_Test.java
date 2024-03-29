package ca.jrvs.apps.stockquote.doa;

import ca.jrvs.apps.jdbc.DatabaseConnectionManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class QuoteDao_Test {

    private static QuoteDao quoteDao;

    @BeforeClass
    public static void setUp() {
        try {
            DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                    "stock_quote", "postgres", "password");
            Connection connection = dcm.getConnection();
            quoteDao = new QuoteDao(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void beforeEach() {
        // Clean up the database before each test
        quoteDao.deleteAll();
    }

    @Test
    public void testSave() {
        quoteDao.deleteAll();
        Date latestTradingDate = new Date(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Quote quote = new Quote("AAPL", 150.00, 155.00, 149.50, 152.00, 100000, latestTradingDate, 149.75, 2.25, "+1.5%", timestamp);
        Quote savedQuote = quoteDao.save(quote);
        assertNotNull(savedQuote);
        assertEquals("AAPL", savedQuote.getTicker());
    }

    @Test
    public void testFindById() {
        Date latestTradingDate = new Date(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Quote quote = new Quote("AAPL", 150.00, 155.00, 149.50, 152.00, 100000, latestTradingDate, 149.75, 2.25, "+1.5%", timestamp);
        quoteDao.save(quote);
        Optional<Quote> foundQuote = quoteDao.findById("AAPL");
        assertTrue(foundQuote.isPresent());
        assertEquals("AAPL", foundQuote.get().getTicker());
    }

    @Test
    public void testFindAll() {
        Date latestTradingDate = new Date(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Quote quote1 = new Quote("AAPL", 150.00, 155.00, 149.50, 152.00, 100000, latestTradingDate, 149.75, 2.25, "+1.5%", timestamp);
        Quote quote2 = new Quote("GOOG", 2500.00, 2550.00, 2499.50, 2525.00, 50000, latestTradingDate, 2499.75, 25.25, "+1.0%", timestamp);
        quoteDao.save(quote1);
        quoteDao.save(quote2);
        List<Quote> quoteList = (List<Quote>) quoteDao.findAll();
        assertEquals(quoteList.size(), 2);
//        assertTrue(quoteList.contains(quote1)); //doesn't work case is the same object in memory even tho values are the same
//        assertTrue(quoteList.contains(quote2));
        assertEquals(quote1.getTicker(), quoteList.get(0).getTicker());
        assertEquals(quote2.getTicker(), quoteList.get(1).getTicker());
    }

    @Test
    public void testDeleteById() {
        Date latestTradingDate = new Date(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Quote quote = new Quote("AAPL", 150.00, 155.00, 149.50, 152.00, 100000, latestTradingDate, 149.75, 2.25, "+1.5%", timestamp);
        quoteDao.save(quote);
        quoteDao.deleteById("AAPL");
        Optional<Quote> deletedQuote = quoteDao.findById("AAPL");
        assertFalse(deletedQuote.isPresent());
    }

    @Test
    public void testDeleteAll() {
        Date latestTradingDate = new Date(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Quote quote1 = new Quote("AAPL", 150.00, 155.00, 149.50, 152.00, 100000, latestTradingDate, 149.75, 2.25, "+1.5%", timestamp);
        Quote quote2 = new Quote("GOOG", 2500.00, 2550.00, 2499.50, 2525.00, 50000, latestTradingDate, 2499.75, 25.25, "+1.0%", timestamp);
        quoteDao.save(quote1);
        quoteDao.save(quote2);
        quoteDao.deleteAll();
        Iterable<Quote> quotes = quoteDao.findAll();
        assertFalse(quotes.iterator().hasNext());
    }
}
