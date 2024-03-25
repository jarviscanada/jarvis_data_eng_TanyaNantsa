package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.jdbc.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.doa.Position;
import ca.jrvs.apps.stockquote.doa.PositionDao;
import ca.jrvs.apps.stockquote.doa.Quote;
import ca.jrvs.apps.stockquote.doa.QuoteDao;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class PositionService_IntTest {

    private static PositionService positionService;
    private static PositionDao positionDao;

    @BeforeAll
    public static void setUp() {
        try {
            DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                    "stock_quote", "postgres", "password");
            Connection connection = dcm.getConnection();
            positionDao = new PositionDao(connection);
            positionService = new PositionService(positionDao);
            QuoteDao quoteDao = new QuoteDao(connection);

            quoteDao.deleteAll();
            Date latestTradingDate = new Date(System.currentTimeMillis());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Quote quote1 = new Quote("AAPL", 150.00, 155.00, 149.50, 152.00, 100000, latestTradingDate, 149.75, 2.25, "+1.5%", timestamp);
            quoteDao.save(quote1);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }

    @BeforeEach
    public void cleanUp() {
        positionDao.deleteAll();
    }

    @Test
    public void testBuy() {
        String ticker = "AAPL";
        int numberOfShares = 10;
        double price = 150.0;

        Position position = positionService.buy(ticker, numberOfShares, price);
        assertEquals(ticker, position.getTicker());
        assertEquals(numberOfShares, position.getNumOfShares());
        assertEquals(price, position.getValuePaid());

        // Verify position is saved in the database
        assertTrue(positionDao.findById(ticker).isPresent());
    }

    @Test
    public void testSell() {
        String ticker = "AAPL";
        int numberOfShares = 10;
        double price = 150.0;

        positionService.buy(ticker, numberOfShares, price);
        positionService.sell(ticker);

        // Verify position is deleted from the database
        assertFalse(positionDao.findById(ticker).isPresent());
    }
}

