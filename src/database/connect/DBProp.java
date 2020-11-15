/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database.connect;

import java.io.FileInputStream;
import java.util.Properties;

public class DBProp {

    private static String dbUrl;
    private static String driver;
    private static String login;
    private static String paswoord;

    private DBProp() {
        Properties appProperties = new Properties();
        try (FileInputStream in = new FileInputStream("DB.properties")) {
            appProperties.load(in);

            dbUrl = appProperties.getProperty("dbUrl");
            driver = appProperties.getProperty("driver");
            login = appProperties.getProperty("login");
            paswoord = appProperties.getProperty("paswoord");

        } catch (java.io.IOException ex) {
            System.out.println("Properties file niet gevonden");
        }
    }
    
    /**
     * @return the dbUrl
     */
    public static String getDbUrl() {
        if (dbUrl == null) {
            DBProp db = new DBProp();
        }
        return dbUrl;
    }

    /**
     * @return the driver
     */
    public static String getDriver() {
        if (driver == null) {
            DBProp db = new DBProp();
        }
        return driver;
    }

    /**
     * @return the login
     */
    public static String getLogin() {
        if (login == null) {
            DBProp db = new DBProp();
        }
        return login;
    }

    /**
     * @return the paswoord
     */
    public static String getPaswoord() {
        if (paswoord == null) {
            DBProp db = new DBProp();
        }
        return paswoord;
    }
}
