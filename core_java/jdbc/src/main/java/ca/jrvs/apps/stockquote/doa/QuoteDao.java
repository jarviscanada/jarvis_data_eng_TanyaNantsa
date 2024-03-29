package ca.jrvs.apps.stockquote.doa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String> {

    private Connection c;
    private static final String INSERT = "INSERT INTO quote (symbol, open, high, low, price, " +
            "volume, latest_trading_day, previous_close, change, change_percent, timestamp) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_ONE = "SELECT symbol, open, high, low, price, volume, " +
            "latest_trading_day, previous_close, change, change_percent, timestamp FROM " +
            "quote WHERE symbol = ?";
    private static final String GET_ALL =  "SELECT symbol, open, high, low, price, volume, " +
            "latest_trading_day, previous_close, change, change_percent, timestamp FROM " +
            "quote";
    private static final String DELETE_ONE = "DELETE FROM quote WHERE symbol = ?";
    private static final String DELETE_ALL = "DELETE FROM quote";

    public QuoteDao(Connection c) {
        this.c = c;
    }

    @Override
    public Quote save(Quote entity) throws IllegalArgumentException {
        try(PreparedStatement statement = this.c.prepareStatement(INSERT);) {
            statement.setString(1, entity.getTicker());
            statement.setDouble(2, entity.getOpen());
            statement.setDouble(3, entity.getHigh());
            statement.setDouble(4, entity.getLow());
            statement.setDouble(5, entity.getPrice());
            statement.setInt(6, entity.getVolume());
            statement.setDate(7, entity.getLatestTradingDay());
            statement.setDouble(8, entity.getPreviousClose());
            statement.setDouble(9, entity.getChange());
            statement.setString(10, entity.getChangePercent());
            statement.setTimestamp(11, entity.getTimestamp());
            statement.execute();

            String id = entity.getTicker();
            return this.findById(id).get();
        } catch(SQLException e){
            throw new RuntimeException("Error saving data to quote table",e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error - invalid input", e);
        }
    }

    @Override
    public Optional<Quote> findById(String s) throws IllegalArgumentException {
        Quote quote = null;
        try (PreparedStatement preparedStatement = this.c.prepareStatement(GET_ONE);) {
            preparedStatement.setString(1, s);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                quote = new Quote();
                quote.setTicker(rs.getString("symbol"));
                quote.setOpen(rs.getDouble("open"));
                quote.setHigh(rs.getDouble("high"));
                quote.setLow(rs.getDouble("low"));
                quote.setPrice(rs.getDouble("price"));
                quote.setVolume(rs.getInt("volume"));
                quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
                quote.setPreviousClose(rs.getDouble("previous_close"));
                quote.setChange(rs.getDouble("change"));
                quote.setChangePercent(rs.getString("change_percent"));
                quote.setTimestamp(rs.getTimestamp("timestamp"));
            }
            // Return an Optional with quote, or empty if quote is null
            return Optional.ofNullable(quote);
        } catch (SQLException e){
            throw new RuntimeException("Error finding data in quote table by ID",e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error - invalid symbol", e);
        }
    }

    @Override
    public Iterable<Quote> findAll() {
        try (PreparedStatement statement = this.c.prepareStatement(GET_ALL);) {
            ResultSet rs = statement.executeQuery();
            List<Quote> listQuotes = new ArrayList<Quote>();
            Quote quote;
            while(rs.next()) {
                quote = new Quote();
                quote.setTicker(rs.getString("symbol"));
                quote.setOpen(rs.getDouble("open"));
                quote.setHigh(rs.getDouble("high"));
                quote.setLow(rs.getDouble("low"));
                quote.setPrice(rs.getDouble("price"));
                quote.setVolume(rs.getInt("volume"));
                quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
                quote.setPreviousClose(rs.getDouble("previous_close"));
                quote.setChange(rs.getDouble("change"));
                quote.setChangePercent(rs.getString("change_percent"));
                quote.setTimestamp(rs.getTimestamp("timestamp"));
                listQuotes.add(quote);
            }
            return listQuotes;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all data in quote table", e);
        }
    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException {
        try (PreparedStatement statement = this.c.prepareStatement(DELETE_ONE);) {
            statement.setString(1, s);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting single data from quote table", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error - Invalid symbol", e);
        }
    }

    @Override
    public void deleteAll() {
        try (PreparedStatement statement = this.c.prepareStatement(DELETE_ALL);) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all data from quote table", e);
        }
    }

}
