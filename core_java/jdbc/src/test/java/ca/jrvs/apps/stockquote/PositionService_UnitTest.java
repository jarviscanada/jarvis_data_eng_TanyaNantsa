package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.doa.Position;
import ca.jrvs.apps.stockquote.doa.PositionDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PositionService_UnitTest {

    private PositionService positionService;
    private PositionDao positionDaoMock;

    @BeforeEach
    public void setUp() {
        positionDaoMock = mock(PositionDao.class);
        positionService = new PositionService(positionDaoMock);
    }

    @Test
    public void testBuy() {
        String ticker = "AAPL";
        int numberOfShares = 10;
        double price = 150.0*numberOfShares;

        Position position = new Position(ticker, numberOfShares, price);
        when(positionDaoMock.save(anyObject())).thenReturn(position);
        Position actualPosition = positionService.buy(ticker, numberOfShares, price);

        assertEquals(position, actualPosition);
    }

    @Test
    public void testSell() {
        String ticker = "AAPL";
        positionService.sell(ticker);

        verify(positionDaoMock, times(1)).deleteById(ticker);
    }
}
