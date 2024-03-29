package ca.jrvs.apps.stockquote.doa;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class QuoteHttpHelper {

    private String apiKey;
    private OkHttpClient client;

    public QuoteHttpHelper(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
    }
    /**
     * Fetch latest quote data from Alpha Vantage endpoint
     * @param symbol
     * @return Quote with latest data
     * @throws IllegalArgumentException - if no data was found for the given symbol
     */
    public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;

        Request request = new Request.Builder()
                .url(url)
                .build();

        Quote quote;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseData = response.body().string();

            // Check if response contains valid data
            if (responseData.contains("\"Global Quote\": {}")) {
                throw new IllegalArgumentException("No data found for symbol: " + symbol);
            }

            // Parse the response and create Quote object
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseData);

            JsonNode globalQuote = jsonNode.get("Global Quote");
            String ticker = globalQuote.get("01. symbol").asText();
            double open = globalQuote.get("02. open").asDouble();
            double high = globalQuote.get("03. high").asDouble();
            double low = globalQuote.get("04. low").asDouble();
            double price = globalQuote.get("05. price").asDouble();
            int volume = globalQuote.get("06. volume").asInt();
            String dateString = globalQuote.get("07. latest trading day").asText();
            dateString = dateString.replaceAll("\"", "");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date latestTradingDate = new java.sql.Date(dateFormat.parse(dateString).getTime());
            double previousClose = globalQuote.get("08. previous close").asDouble();
            double change = globalQuote.get("09. change").asDouble();
            String changePercent = globalQuote.get("10. change percent").asText();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            quote = new Quote(ticker, open, high, low, price, volume, latestTradingDate, previousClose, change, changePercent, timestamp);
            return quote;
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Error fetching quote data", e);
        }
    }
}
