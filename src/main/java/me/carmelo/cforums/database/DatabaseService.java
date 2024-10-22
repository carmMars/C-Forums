package me.carmelo.cforums.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DatabaseService {

    private final DataSource dataSource;

    @Autowired
    public DatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String checkDatabaseConnection() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            return "Successfully connected to the database: " + connection.getCatalog();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}
