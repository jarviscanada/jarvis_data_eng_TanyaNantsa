package ca.jrvs.apps.stockquote.doa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuoteHttpHelper_Test {

    Quote quote;
    @Test
    public void testFetchQuoteInfo() {
        String apiKey = "54706e6343msh96825dc87a0b044p188036jsn4bab408b03a2";
        QuoteHttpHelper qhh = new QuoteHttpHelper(apiKey);

        // Test symbol
        String symbol = "MSFT";

        // Fetch quote info

        try {
            quote = qhh.fetchQuoteInfo(symbol);
        } catch (RuntimeException e) {
            fail("Failed to fetch quote info: " + e.getMessage());
        }

        assertNotNull(quote, "Quote Object is null");
        assertEquals("MSFT", quote.getTicker());
        assertTrue(quote.getPrice() > 0);
    }
}
