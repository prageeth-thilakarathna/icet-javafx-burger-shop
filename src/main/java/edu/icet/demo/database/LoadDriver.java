package edu.icet.demo.database;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class LoadDriver {
    private static LoadDriver instance;
    private final Connection connection;

    private LoadDriver() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/burger_shop?autoReconnect=true&useSSL=false", "root", "200284");
    }

    public static LoadDriver getInstance() throws SQLException {
        if(instance==null){
            instance=new LoadDriver();
        }
        return instance;
    }
}
