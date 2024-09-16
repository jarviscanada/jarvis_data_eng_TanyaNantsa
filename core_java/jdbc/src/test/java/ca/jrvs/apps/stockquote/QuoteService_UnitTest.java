package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.doa.Quote;
import ca.jrvs.apps.stockquote.doa.QuoteDao;
import ca.jrvs.apps.stockquote.doa.QuoteHttpHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuoteService_UnitTest {

    private QuoteService quoteService;
    private QuoteHttpHelper httpHelperMock;

    @BeforeEach
    public void setUp() {
        httpHelperMock = mock(QuoteHttpHelper.class);
        QuoteDao quoteDaoMock = mock(QuoteDao.class);
        quoteService = new QuoteService(quoteDaoMock, httpHelperMock);
    }

    @Test
    public void testFetchQuoteDataFromAPI() {
        Date latestTradingDate = new Date(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Quote expectedQuote = new Quote("AAPL", 150.00, 155.00, 149.50, 152.00, 100000, latestTradingDate, 149.75, 2.25, "+1.5%", timestamp);
        when(httpHelperMock.fetchQuoteInfo("AAPL")).thenReturn(expectedQuote);

        Optional<Quote> actualQuoteOptional = quoteService.fetchQuoteDataFromAPI("AAPL");
        assertEquals(Optional.of(expectedQuote), actualQuoteOptional);
    }
}
