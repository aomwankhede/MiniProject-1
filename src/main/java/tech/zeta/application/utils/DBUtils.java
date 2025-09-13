package tech.zeta.application.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class DBUtils {
    public static Connection getConnection() {
        Properties config=  new Properties();
        Connection connection = null;
        try{
            FileInputStream inputStream = new FileInputStream("src/main/resources/application.properties");
            config.load(inputStream);
            String db_class_name = config.getProperty("db_class_name");
            String db_database_url = config.getProperty("db_database_url");
            String db_username=config.getProperty("db_username");
            String db_password=config.getProperty("db_password");
            String db_database_name= config.getProperty("db_database_name");

            Class.forName(db_class_name);
            connection = DriverManager.getConnection(db_database_url + "/" + db_database_name,db_username,db_password);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            log.error("Database Connection Error : {}" , e.getMessage());
            return null;
        }
        return connection;
    }
}
