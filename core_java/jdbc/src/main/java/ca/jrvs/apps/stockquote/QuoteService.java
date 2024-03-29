package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.doa.Quote;
import ca.jrvs.apps.stockquote.doa.QuoteDao;
import ca.jrvs.apps.stockquote.doa.QuoteHttpHelper;
import java.util.Optional;

public class QuoteService {

    private QuoteDao dao;
    private final QuoteHttpHelper httpHelper;

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

        //save data to quote table
        if (dao.findById(quote.getTicker()).isPresent()) {
            dao.deleteById(quote.getTicker());
        }
        dao.save(quote);

        return Optional.ofNullable(quote);
    }

    /**
     * Return whether given symbol is valid and has valid data
     * @param symbol
     * @return true or false
     */
    public boolean isValidSymbol(String symbol) {
        Quote quote = httpHelper.fetchQuoteInfo(symbol);
        return quote != null && !quote.getTicker().isEmpty();
    }

    /**
     * Prints stock quote data info for given symbol
     */
    public void displayOne(String symbol) {
        Quote quote = httpHelper.fetchQuoteInfo(symbol);

        System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s\n",
                "Ticker", "Open", "High", "Low", "Price", "Volume", "Previous Close",
                "Change", "Change Percent");
        System.out.printf("%-15s%-15.2f%-15.2f%-15.2f%-15.2f%-15d%-15.2f%-15.2f%-15s\n",
                quote.getTicker(), quote.getOpen(), quote.getHigh(), quote.getLow(),
                quote.getPrice(), quote.getVolume(), quote.getPreviousClose(), quote.getChange(), quote.getChangePercent());
    }
}
