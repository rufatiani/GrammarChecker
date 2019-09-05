/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.checker;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Ru'fatiani
 */
public class ConnectionManager {
    
    private static String dbURL = "jdbc:mysql://localhost:3306/grammar_checker";
    private static String username = "root";
    private static String password = "";
    private static java.sql.Connection conn;

    public ConnectionManager() {
        try {
            conn = DriverManager.getConnection(dbURL, username, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getDbURL() {
        return dbURL;
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static java.sql.Connection getConn() {
        return conn;
    }

    public void setConn(java.sql.Connection conn) {
        this.conn = conn;
    }
}
