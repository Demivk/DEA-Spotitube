package nl.han.dea.demi.DataAccessLayer.Databases;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDatabaseConnection {
    void connect() throws SQLException;
    PreparedStatement preparedStatement(String query);
    ResultSet getResult(PreparedStatement ps);
}
