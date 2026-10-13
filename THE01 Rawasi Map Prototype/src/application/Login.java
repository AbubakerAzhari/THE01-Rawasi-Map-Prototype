package application;
import java.sql.*;
import javax.swing.JOptionPane;

import javafx.scene.control.Alert;

public class Login {
	
	Connection SQLConnection, MSQConnection, connection;
		
	public Login() {
		
		connection = DatabaseConnection.connection;
		
		if(connection == null) {
			
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setContentText("Connection Not Successful");
	        alert.show();
		}
		
	}

	
	public boolean mthLogIsLogin(String user,String pass) throws SQLException {
		PreparedStatement ps = null;
		ResultSet re = null;
		
		String query = "select * from users where user_username = ? and user_password =? ";
		
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, user);
			ps.setString(2, pass);
			re = ps.executeQuery();
			if(re.next()) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}finally {
			ps.close();
			re.close();
		}
	}
}
