package au.com.securepay.test.automation.db;

import java.sql.*;

public class DBConnection {

    private Connection con = null;

    public DBConnection(String dbUrl, String dbUser, String dbPwd) {
        try {
            con = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return con;
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = con.createStatement();
        return statement.executeQuery(sql);
    }

    public void executeUpdate(String sql) throws SQLException {
        Statement statement = con.createStatement();
        statement.executeUpdate(sql);
    }

    public void close() throws SQLException{
        con.close();
    }
}