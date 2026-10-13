package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController implements Initializable {
	public Login login = new Login();
	@FXML private StackPane panLogMain;
	@FXML private AnchorPane panLogMainSignIn, panLogParentRight, panLogParentLeft;
	
	@FXML private AnchorPane panLogOneRight, panLogOneLeft;
	@FXML private Label lblLogOneStatus, lblLogOneTitle, lblLogOneGuide, lblLogOneTHE01, lblLogOneAbu, lblLogOneVersion, lblLogOneYear, lblLogOneAnswer;
	@FXML private TextField txtLogOneEmail, txtLogOnePassword;
	@FXML private Button btnLogOneLogin, btnLogOneExit, btnLogOneForget;
	
	@FXML private AnchorPane panLogTwoRight, panLogTwoLeft;
	@FXML private Label lblLogTowTitle, lblLogTowQuestion, lblLogTowTHE01, lblLogTowAbu, lblLogTowVersion, lblLogTowYear;
	@FXML private TextField txtLogTowName, txtLogTowAnswer, txtLogTowNewPassword;
	@FXML private Button btnLogTowGet, btnLogTowReset, btnLogTowExit, btnLogTowSign;
	
	private Connection connection;
	private Stage staLogClose;
	private String Query, qurInsert;
	private PreparedStatement prS, prSInsert;
	private ResultSet reS;
	private boolean isLogQuestionShown = false;

	public LoginController() {
		
		connection = DatabaseConnection.connection;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		panLogOneRight.setVisible(true);
		panLogOneLeft.setVisible(true);
		
		lblLogOneStatus.setVisible(true);
		lblLogOneTitle.setVisible(true);
		lblLogOneGuide.setVisible(true);
		lblLogOneTHE01.setVisible(true);
		lblLogOneAbu.setVisible(true);
		lblLogOneVersion.setVisible(true);
		lblLogOneYear.setVisible(true);
		txtLogOneEmail.setVisible(true);
		txtLogOnePassword.setVisible(true);
		btnLogOneLogin.setVisible(true);
		btnLogOneExit.setVisible(true);
		lblLogOneAnswer.setVisible(true);
		btnLogOneForget.setVisible(true);
		
		panLogTwoRight.setVisible(false);
		panLogTwoLeft.setVisible(false);
		
		lblLogTowTitle.setVisible(false);
		lblLogTowQuestion.setVisible(false);
		lblLogTowTHE01.setVisible(false);
		lblLogTowAbu.setVisible(false);
		lblLogTowVersion.setVisible(false);
		lblLogTowYear.setVisible(false);
		txtLogTowName.setVisible(false);
		txtLogTowAnswer.setVisible(false);
		txtLogTowNewPassword.setVisible(false);
		btnLogTowGet.setVisible(false);
		btnLogTowReset.setVisible(false);
		btnLogTowExit.setVisible(false);
		btnLogTowSign.setVisible(false);
		
		txtLogTowAnswer.setDisable(true);
		txtLogTowNewPassword.setDisable(true);
		btnLogTowReset.setDisable(true);
		
		txtLogTowAnswer.textProperty().addListener((observable, oldValue, newValue) ->{
			if(newValue == null || newValue == "" || newValue.isEmpty()) {	
				txtLogTowNewPassword.setDisable(true);
			}else if(!newValue.isEmpty()) {
				txtLogTowNewPassword.setDisable(false);
				btnLogTowReset.setDisable(false);
			}
		});
		
		txtLogTowNewPassword.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.ENTER) {
				btnLogTowReset.fire();
			}
		});
	}
	
	public void mthbtnLogOneLogin(ActionEvent event) throws IOException {
		try {
			if(login.mthLogIsLogin(txtLogOneEmail.getText(), txtLogOnePassword.getText())) {
				lblLogOneStatus.setText("User Name And Password is Correct");
				Holder.setuser_Name(txtLogOneEmail.getText(), true);
				btnLogOneExit.fire();
				Notifications.create()
			       .title("Sign-In Process Notification")
			       .text("Sign-In Successfull!")
			       .graphic(null)
			       .hideAfter(Duration.seconds(5))
			       .position(Pos.BOTTOM_RIGHT)
			       .show();
			}else {
				lblLogOneStatus.setText("User Name And Password is Not Correct");				
				Notifications.create()
				.title("Sign-In Process Notification")
				.text("UserName And Password is Not Correct")
				.graphic(null)
				.hideAfter(Duration.seconds(5))
				.position(Pos.BOTTOM_RIGHT)
				.show();
			}
		} catch (SQLException e) {
			lblLogOneStatus.setText("User Name And Password is Not Correct");	
			e.printStackTrace();
			Notifications.create()
			.title("Sign-In Process Notification")
			.text("Somthing Wrong")
			.graphic(null)
			.hideAfter(Duration.seconds(5))
			.position(Pos.BOTTOM_RIGHT)
			.show();
		}
	}
	
	public void mthbtnLogOneExit(ActionEvent event) {
		staLogClose = (Stage) btnLogOneExit.getScene().getWindow();
		staLogClose.close();
	}
	
	public void mthbtnLogOneForget(ActionEvent event) {
		TranslateTransition trT = new TranslateTransition();
		trT.setDuration(Duration.seconds(0.7));
		trT.setNode(panLogParentLeft);
		trT.setToX(400);
		trT.play();
		panLogParentRight.setTranslateX(-300);
		
		panLogOneRight.setVisible(false);
		panLogOneLeft.setVisible(false);
		
		lblLogOneStatus.setVisible(false);
		lblLogOneTitle.setVisible(false);
		lblLogOneGuide.setVisible(false);
		lblLogOneTHE01.setVisible(false);
		lblLogOneAbu.setVisible(false);
		lblLogOneVersion.setVisible(false);
		lblLogOneYear.setVisible(false);
		txtLogOneEmail.setVisible(false);
		txtLogOnePassword.setVisible(false);
		btnLogOneLogin.setVisible(false);
		btnLogOneExit.setVisible(false);
		lblLogOneAnswer.setVisible(false);
		btnLogOneForget.setVisible(false);
		
		panLogTwoRight.setVisible(true);
		panLogTwoLeft.setVisible(true);
		
		lblLogTowTitle.setVisible(true);
		lblLogTowQuestion.setVisible(true);
		lblLogTowTHE01.setVisible(true);
		lblLogTowAbu.setVisible(true);
		lblLogTowVersion.setVisible(true);
		lblLogTowYear.setVisible(true);
		txtLogTowName.setVisible(true);
		txtLogTowAnswer.setVisible(true);
		txtLogTowNewPassword.setVisible(true);
		btnLogTowGet.setVisible(true);
		btnLogTowReset.setVisible(true);
		btnLogTowExit.setVisible(true);
		btnLogTowSign.setVisible(true);
		
		trT.setOnFinished(e->{
			
		});
	}
	
	public void mthbtnLogTowGet(ActionEvent event) throws SQLException {
		
		Query = "select * from users where user_username ='"+txtLogTowName.getText()+"' or user_name='"+txtLogTowName.getText()+"'";
		
		prS = connection.prepareStatement(Query);
		reS = prS.executeQuery();
		
		if(reS.getString("user_question")!=null) {
			lblLogTowQuestion.setText(reS.getString("user_question"));
			btnLogTowGet.setDisable(true);
			txtLogTowName.setDisable(true);
			isLogQuestionShown = true;
			txtLogTowAnswer.setDisable(false);
		}else {
			btnLogTowGet.setDisable(false);
			txtLogTowName.setDisable(false);
			isLogQuestionShown = false;
		}
		
		prS.close();
		reS.close();
	}
	
	public void mthbtnLogTowReset(ActionEvent event) throws SQLException {
		
		if(isLogQuestionShown && lblLogTowQuestion.getText()!="") {
			
			Query = "select * from users where user_username= '"+txtLogTowName.getText()+"' or user_name='"+txtLogTowName.getText()+"'"
					+ " and user_question ='"+lblLogTowQuestion.getText()+"'";
			
			prS = connection.prepareStatement(Query);
			reS = prS.executeQuery();
			
			if(reS.getString("user_answer").matches(txtLogTowAnswer.getText())) {
			
				qurInsert = "update users set user_password='"+txtLogTowNewPassword.getText()+"' where user_username ='"+txtLogTowName.getText()+"'"
						+ " or user_name='"+txtLogTowName.getText()+"' and user_question='"+lblLogTowQuestion.getText()+"'";
				
				prSInsert = connection.prepareStatement(qurInsert);
				prSInsert.executeUpdate();
				prSInsert.close();
				
				btnLogTowSign.fire();
				lblLogTowQuestion.setText("");
				txtLogTowName.setText("");
				txtLogTowAnswer.setText("");
				txtLogTowNewPassword.setText("");
				txtLogTowAnswer.setDisable(true);
				txtLogTowNewPassword.setDisable(true);
				txtLogTowName.setDisable(false);
				btnLogTowGet.setDisable(false);
				
				Notifications.create()
				.title("Reset Password Process Notification")
				.text("Password Reset Successfull! ")
				.graphic(null)
				.hideAfter(Duration.seconds(5))
				.position(Pos.BOTTOM_RIGHT)
				.show();
			}else {
				Notifications.create()
				.title("Reset Password Process Notification")
				.text("The Answer Is Not Correct ")
				.graphic(null)
				.hideAfter(Duration.seconds(5))
				.position(Pos.BOTTOM_RIGHT)
				.show();
			}
			
			prS.close();
			reS.close();
		}
		
	}
	
	public void mthbtnLogTowExit(ActionEvent event) { 
		staLogClose = (Stage) btnLogTowExit.getScene().getWindow();
		staLogClose.close();
	}
	
	public void mthbtnLogTowSign(ActionEvent event) {
		 
		TranslateTransition trT = new TranslateTransition();
		trT.setDuration(Duration.seconds(0.7));
		trT.setNode(panLogParentLeft);
		trT.setToX(0);
		trT.play();
		panLogParentRight.setTranslateX(0);
		
		panLogOneRight.setVisible(true);
		panLogOneLeft.setVisible(true);
		
		lblLogOneStatus.setVisible(true);
		lblLogOneTitle.setVisible(true);
		lblLogOneGuide.setVisible(true);
		lblLogOneTHE01.setVisible(true);
		lblLogOneAbu.setVisible(true);
		lblLogOneVersion.setVisible(true);
		lblLogOneYear.setVisible(true);
		txtLogOneEmail.setVisible(true);
		txtLogOnePassword.setVisible(true);
		btnLogOneLogin.setVisible(true);
		btnLogOneExit.setVisible(true);
		lblLogOneAnswer.setVisible(true);
		btnLogOneForget.setVisible(true);
		
		panLogTwoRight.setVisible(false);
		panLogTwoLeft.setVisible(false);
		
		lblLogTowTitle.setVisible(false);
		lblLogTowQuestion.setVisible(false);
		lblLogTowTHE01.setVisible(false);
		lblLogTowAbu.setVisible(false);
		lblLogTowVersion.setVisible(false);
		lblLogTowYear.setVisible(false);
		txtLogTowName.setVisible(false);
		txtLogTowAnswer.setVisible(false);
		txtLogTowNewPassword.setVisible(false);
		btnLogTowGet.setVisible(false);
		btnLogTowReset.setVisible(false);
		btnLogTowExit.setVisible(false);
		btnLogTowSign.setVisible(false);
		
		trT.setOnFinished(e->{
			
		});
	}
}
