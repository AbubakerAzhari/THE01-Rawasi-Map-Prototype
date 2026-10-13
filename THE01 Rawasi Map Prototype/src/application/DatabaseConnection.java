package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import org.controlsfx.control.Notifications;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class DatabaseConnection {
	
	public static Connection connection;
	
	public static boolean isMySQL_SQLite = true;
	
	static {
		if (isMySQL_SQLite) {
			connection = MySQLConnection.connection;
		}else {
			
		}
	}
	
	
	public static boolean IsDBConnected() {
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
