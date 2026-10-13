package application;

import java.sql.Connection;
import java.sql.DriverManager;

import org.controlsfx.control.Notifications;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class MySQLConnection {
	
	public static Connection connection;
	
	private static final String USERNAME = "";
	private static final String PASSWORD = "";
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 3307;
	private static final String DBNAME = "database_scheme";
	private static final String SERVERTIMEZONE = "serverTimezone=UTC";
	
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://"+HOST+":"+PORT+"/"+DBNAME+"?"+SERVERTIMEZONE, USERNAME, PASSWORD);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
