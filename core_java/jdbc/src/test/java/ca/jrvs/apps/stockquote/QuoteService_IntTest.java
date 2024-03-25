package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.jdbc.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.doa.Quote;
import ca.jrvs.apps.stockquote.doa.QuoteDao;
import ca.jrvs.apps.stockquote.doa.QuoteHttpHelper;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class QuoteService_IntTest {

    private static QuoteService quoteService;

    @BeforeAll
    public static void setUp() {
        try {
            DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                    "stock_quote", "postgres", "password");
            Connection connection = dcm.getConnection();
            QuoteDao quoteDao = new QuoteDao(connection);
            quoteService = new QuoteService(quoteDao, new QuoteHttpHelper("your_api_key"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFetchQuoteDataFromAPI() {
        Optional<Quote> actualQuoteOptional = quoteService.fetchQuoteDataFromAPI("AAPL");

        assertTrue(actualQuoteOptional.isPresent());
        assertEquals("AAPL", actualQuoteOptional.get().getTicker());
        assertNotNull(actualQuoteOptional.get());
        assertNotNull(actualQuoteOptional.get().getTimestamp());
    }
}
