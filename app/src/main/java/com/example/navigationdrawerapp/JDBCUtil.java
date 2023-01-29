package com.example.navigationdrawerapp;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
    private static Connection connection = null;

    static {
        try {
            connection = buildConnection();
        } catch (SQLException e) {
            Log.e("JDBCUtil", e.getMessage());
            e.printStackTrace();
        }
    }

    private static Connection buildConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://dpg-cf6k25da499d72vco220-a.oregon-postgres.render.com/maternity",
                "maternity_user",
                "WkG0jbD171u9JsvVar23ngMOI0S5n0Ha"
        );
    }

    public static Connection getConnection() {
        return connection;
    }
}
