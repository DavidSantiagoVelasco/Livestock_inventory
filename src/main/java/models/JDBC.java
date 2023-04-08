package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class JDBC {
    public static Connection connection(){
        try {
            return DriverManager.getConnection(Config.URL_DB, Config.USERNAME_DB, Config.PASSWORD_DB);
        }catch (SQLException e){
            e.printStackTrace();
            return  null;
        }
    }
}
