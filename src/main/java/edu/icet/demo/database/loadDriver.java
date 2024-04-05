package edu.icet.demo.database;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class loadDriver {
    private static loadDriver instance;
    private final Connection connection;

    private loadDriver() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/burger_shop?autoReconnect=true&useSSL=false", "root", "200284");
    }

    public static loadDriver getInstance() throws SQLException {
        if(instance==null){
            instance=new loadDriver();
        }
        return instance;
    }
}
