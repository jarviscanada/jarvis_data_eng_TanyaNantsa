package ca.jrvs.apps.stockquote.doa;

import ca.jrvs.apps.jdbc.DatabaseConnectionManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PositionDao_Test {
    private static PositionDao positionDao;

    @BeforeAll
    public static void setUp() {
        try {
            DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                    "stock_quote", "postgres", "password");
            Connection connection = dcm.getConnection();
            positionDao = new PositionDao(connection);

            QuoteDao quoteDao = new QuoteDao(connection);
            Date latestTradingDate = new Date(System.currentTimeMillis());
            quoteDao.deleteAll();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Quote quote1 = new Quote("AAPL", 150.00, 155.00, 149.50, 152.00, 100000, latestTradingDate, 149.75, 2.25, "+1.5%", timestamp);
            Quote quote2 = new Quote("GOOG", 2500.00, 2550.00, 2499.50, 2525.00, 50000, latestTradingDate, 2499.75, 25.25, "+1.0%", timestamp);
            quoteDao.save(quote1);
            quoteDao.save(quote2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void beforeEach() {
        // Clean up the database before each test
        positionDao.deleteAll();
    }

    @Test
    public void testSave() {
        Position position = new Position("AAPL", 10, 1711.20);
        Position savedPosition = positionDao.save(position);
        assertNotNull(savedPosition);
        assertEquals("AAPL", savedPosition.getTicker());
    }

    @Test
    public void testFindById() {
        Position position = new Position("AAPL", 10, 1711.20);
        positionDao.save(position);
        Optional<Position> foundPosition = positionDao.findById("AAPL");
        assertTrue(foundPosition.isPresent());
        assertEquals("AAPL", foundPosition.get().getTicker());
    }

    @Test
    public void testFindAll() {
        Position position1 = new Position("AAPL", 10, 1711.20);
        Position position2 = new Position("GOOG", 10, 1484.40);
        positionDao.save(position1);
        positionDao.save(position2);
        List<Position> positionList = (List<Position>) positionDao.findAll();
        assertEquals(positionList.size(), 2);
        assertEquals(position1.getTicker(), positionList.get(0).getTicker());
        assertEquals(position2.getTicker(),positionList.get(1).getTicker());
    }

    @Test
    public void testDeleteById() {
        Position position = new Position("AAPL", 10, 1711.20);
        positionDao.save(position);
        positionDao.deleteById("AAPL");
        Optional<Position> deletedPosition = positionDao.findById("AAPL");
        assertFalse(deletedPosition.isPresent());
    }

    @Test
    public void testDeleteAll() {
        Position position1 = new Position("AAPL", 10, 1711.20);
        Position position2 = new Position("GOOG", 10, 1484.40);
        positionDao.save(position1);
        positionDao.save(position2);
        positionDao.deleteAll();
        Iterable<Position> positions = positionDao.findAll();
        assertFalse(positions.iterator().hasNext());
    }

}
