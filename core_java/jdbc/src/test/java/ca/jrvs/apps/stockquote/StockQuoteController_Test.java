package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.doa.PositionDao;
import ca.jrvs.apps.stockquote.doa.Quote;
import ca.jrvs.apps.stockquote.doa.QuoteDao;
import ca.jrvs.apps.stockquote.doa.QuoteHttpHelper;
import org.junit.jupiter.api.*;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.sql.Date;
import java.sql.Timestamp;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StockQuoteController_Test {

    private static Connection connection;
    private QuoteHttpHelper httppHelperMock;
    private StockQuoteController con;
    private Quote quoteAAPL;
    private Quote quoteGOOG;
    private Quote quoteMSFT;

    @BeforeAll
    public static void setUpClass() {
        // Initialize the connection
        Map<String, String> properties = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/properties.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(":");
                properties.put(tokens[0], tokens[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Class.forName(properties.get("db-class"));
            String url = "jdbc:postgresql://" + properties.get("server") + ":" + properties.get("port") + "/" + properties.get("database");
            connection = DriverManager.getConnection(url, properties.get("username"), properties.get("password"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDownClass() {
        // Close the connection after all tests have completed
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void setUp() {
        httppHelperMock = mock(QuoteHttpHelper.class);
        quoteAAPL = new Quote("AAPL", 150.00, 155.00, 149.50, 152.00, 100000,
                new Date(System.currentTimeMillis()), 149.75, 2.25, "+1.5%", new Timestamp(System.currentTimeMillis()));

        quoteGOOG = new Quote("GOOG", 1800.00, 1850.00, 1795.00, 1820.00, 200000,
                new Date(System.currentTimeMillis()), 1790.50, 30.50, "+1.7%", new Timestamp(System.currentTimeMillis()));

        quoteMSFT = new Quote("MSFT", 200.00, 205.00, 198.50, 203.00, 150000,
                new Date(System.currentTimeMillis()), 199.50, 3.50, "+1.8%", new Timestamp(System.currentTimeMillis()));

        QuoteDao qRepo = new QuoteDao(connection);
        PositionDao pRepo = new PositionDao(connection);
        when(httppHelperMock.fetchQuoteInfo("AAPL")).thenReturn(quoteAAPL);
        when(httppHelperMock.fetchQuoteInfo("GOOG")).thenReturn(quoteGOOG);
        when(httppHelperMock.fetchQuoteInfo("MSFT")).thenReturn(quoteMSFT);

        QuoteService sQuote = new QuoteService(qRepo, httppHelperMock);
        PositionService sPos = new PositionService(pRepo, httppHelperMock);
        con = new StockQuoteController(sQuote, sPos);
    }

    @Test
    public void testInitClientSell() {
        // Create input string to simulate user input
        String input = "2\nMSFT\n2\nAMZN\n2\ninvalid\n4\n";

        // Create a ByteArrayInputStream with the input string
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        con.initClient();
    }

    @Test
    public void testInitClientBuy() {
        // Create input string to simulate user input
        String input = "1\nMSFT\n10\n1\ninvalid\n14\n4\n";

        // Create a ByteArrayInputStream with the input string
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        con.initClient();
    }

    @Test
    public void testInitClientSearch() {
        // Create input string to simulate user input
        String input = "3\nGOOG\n3\nINVALID\n4\n";

        // Create a ByteArrayInputStream with the input string
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        con.initClient();
    }

    @Test
    public void testInitClientOptions() {
        // Create input string to simulate user input
        String input = "-1\n5\ninvalid\n1\nMSFT\n-1\n4\n";

        // Create a ByteArrayInputStream with the input string
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        con.initClient();
    }
}

