package application;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.annotation.Resource;
import javax.management.Notification;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.controlsfx.control.Notifications;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor.TextPredictor;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jdk.management.cmm.SystemResourcePressureMXBean;
import javafx.scene.input.ScrollEvent;

public class MainController implements Initializable {
	@FXML Pane root;
	@FXML Pane panMain;
	@FXML Pane panManageZoom;
	@FXML ScrollPane scrMain;
	@FXML Group groMain;
	@FXML Group groFixedMain;
	@FXML Button btnMainZoomIn;
	@FXML Button btnMainZoomOut;
	@FXML Button btnMainSignIn;
	@FXML Button btnMainLogout;
	@FXML Button btnMainConnectMySQL;
	@FXML Button btnMainExit;
	@FXML Button btnMainReport;
	@FXML Button btnMainCustomer;
	@FXML Label lblMainUsername;
	@FXML Rectangle B1L525;
	
	private Pane panDet,panLogin, panReport;
	private Connection connection;
	private Stage staMaiClose, stageDetails, stageSignIn, stageReport;
	private Scene sceneSignIn, sceneDetails, sceneReport;
	
	
	public static boolean isPaneDetailShown = false;
	private boolean isObjectClickedFound = false, isFxidRec = false;
	
	private String fxid;
	private Rectangle2D screenBound;
	private Point2D clickDetailsPoint = null;
	private String Query;
	private PreparedStatement prS;
	private ResultSet reS;
	private Rectangle recFxid;
	private Polygon polFxid;
	
	public MainController() {		
		
		connection = DatabaseConnection.connection;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		mthLoadUser();
		
		Platform.runLater(()->{
		});
		
	}
	
	public void mthFillAllLandsColor() {
		
		if(Holder.isBooking_insert || Holder.isSales_insert) {
			int j = Holder.Status_Selected;
			try {
				isFxidRec = false;
				recFxid = (Rectangle) panMain.lookup("#B" + Holder.Block_Selected + "L" + Holder.land_Selected);
				
				if(j==1)      { recFxid.getStyleClass().add("claLandSold");  }
				else if(j==2) { recFxid.getStyleClass().add("claLandSold");  }
				else if(j==3) { recFxid.getStyleClass().add("claLandFinalBooking");  }
				else if(j==4) { recFxid.getStyleClass().add("claLandInitialBooking");  }
				else if(j==5) { recFxid.getStyleClass().add("claLandIsra");  }
				isFxidRec = true;
				
			} catch (Exception e) {
				if(!isFxidRec) {	
					polFxid = (Polygon) panMain.lookup("#B"+Holder.Block_Selected+"L"+Holder.land_Selected);
					if(j==1)      { polFxid.getStyleClass().add("claLandSold");  }
					else if(j==2) { polFxid.getStyleClass().add("claLandSold");  }
					else if(j==3) { polFxid.getStyleClass().add("claLandFinalBooking");  }
					else if(j==4) { polFxid.getStyleClass().add("claLandInitialBooking");  }
					else if(j==5) { polFxid.getStyleClass().add("claLandIsra");  }
				}
			}
			
			Holder.isBooking_insert = false;
			Holder.isSales_insert = false;
			Holder.Block_Selected=0;
			Holder.land_Selected=0;
			
		}
	}
	
	@FXML
	public void mthbtnMainCustomer (ActionEvent event) throws IOException {
		
	}
	
	@FXML
	public void mthBtnMainZoomIn(ActionEvent event) {
		mthLoadUser();
		  
		groMain.setScaleX(groMain.getScaleX()*1.05);
		groMain.setScaleY(groMain.getScaleY()*1.05);
		
	}
	
	@FXML
	public void mthBtnMainZoomOut(ActionEvent event) {
		mthLoadUser();
		
		groMain.setScaleX(groMain.getScaleX()/1.05);
		groMain.setScaleY(groMain.getScaleY()/1.05);
	}
	
	@FXML
	public void mthMainGetDetails(MouseEvent event) throws SQLException {
		mthLoadUser();
		String b="";
		mthFillAllLandsColor();
		
		try {
			isObjectClickedFound = false;
			Rectangle rec = (Rectangle) event.getSource();
			fxid = rec.getId();
			isObjectClickedFound = true;
		} catch (Exception e) {
			if(!isObjectClickedFound) {				
				Polygon pol = (Polygon) event.getSource();
				fxid = pol.getId(); 
				isObjectClickedFound = true;
			}
		}
		
		mthCalculateDetPanePosition(event.getScreenX(),event.getScreenY());
		
		String[] arrFxId = fxid.split("L", 2);
		if(isPaneDetailShown) {
			stageDetails.hide();
		}
		
		try {
			
			if(arrFxId[0].equals("B1")) {				
				
				Query = "select * from land_block_1 l, land_rank r, land_type t, land_status s where l.lb1_No = "+arrFxId[1] +
						" and l.land_rank_lar_id = r.lar_id and l.land_status_las_id = s.las_id and l.land_type_lat_id = t.lat_id ";
				
				b = "1";
			}else {		
				
				Query = "select * from land_block_2 l, land_rank r, land_type t, land_status s where l.lb2_No = "+arrFxId[1] +
						" and l.land_rank_lar_id = r.lar_id and l.land_status_las_id = s.las_id and l.land_type_lat_id = t.lat_id ";
				
				b = "2";
			}
			prS = connection.prepareStatement(Query);
			reS = prS.executeQuery();
			
			FXMLLoader loader = new FXMLLoader();
			panDet = loader.load(getClass().getResource("Details.fxml").openStream());
			
			Holder.Status_Selected = reS.getInt("las_id");
			int v = Integer.parseInt(reS.getString("las_id")); 
			
			if(Holder.isUser_Logged) {
				
				if(v > 0 && v < 5 ) {
					
				}
			}
			
			if(v != 7) {
				
			} 
			
			if(v != 7 && v != 2 && v != 4 && v != 3) {
			}
			lblMainUsername.setText(Holder.getuser_Name());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			prS.close();
			reS.close();
		}
		
		sceneDetails = new Scene(panDet);
		sceneDetails.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stageDetails = new Stage();
		stageDetails.setX(clickDetailsPoint.getX());
		stageDetails.setY(clickDetailsPoint.getY());
		stageDetails.initStyle(StageStyle.DECORATED.UNDECORATED);
		stageDetails.initOwner(((Node)event.getSource()).getScene().getWindow());
		stageDetails.setScene(sceneDetails);
		stageDetails.show();
		isPaneDetailShown = true;
	}
	
	@FXML
	public void mthBtnMainSignIn(ActionEvent event) {
		
		try {
			
			/*
			stageSignIn = new Stage();
			FXMLLoader loader = new FXMLLoader();
			
			panLogin = loader.load(getClass().getResource("Login.fxml").openStream());
			
			sceneSignIn = new Scene(panLogin);
			sceneSignIn.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			stageSignIn.initStyle(StageStyle.DECORATED.UNDECORATED);
			stageSignIn.initModality(Modality.WINDOW_MODAL);
			stageSignIn.initOwner(((Node)event.getSource()).getScene().getWindow());
			stageSignIn.setScene(sceneSignIn);
			stageSignIn.showAndWait();
			
			lblMainUsername.setText(Holder.getuser_Name());
			
			*/
		} catch (Exception e) {
			System.out.println(" Error : " + e.getMessage());
		}
		
	}
	
	@FXML
	public void mthBtnMainLogout(ActionEvent event) {
		
		if (Holder.isUser_Logged) {
			Holder.setuser_Name("", false);
			btnMainSignIn.setDisable(false);
			mthLoadUser();
			Notifications.create()
		       .title("Logout Process Notification")
		       .text("Logged out Successfully")
		       .graphic(new ImageView(Holder.success_image))
		       .hideAfter(Duration.seconds(5))
		       .position(Pos.BOTTOM_RIGHT)
		       .show();
		} else {
			Holder.mthCreateNotifications("Login Notification", "Please Sign In first", null);
		}
	}
	
	public void mthLoadUser( ) {
		lblMainUsername.setText(Holder.getuser_Name());
	}
	
	public void mthCalculateDetPanePosition(double clickX, double clickY) {
		screenBound = Screen.getPrimary().getBounds();
		
		if(screenBound.getWidth()-clickX >= 420 && screenBound.getHeight()-clickY >= 580 ) {
			clickDetailsPoint = new Point2D(clickX, clickY);
		}else if(screenBound.getWidth()-clickX >= 420 && screenBound.getHeight()-clickY < 580 ) {		
			clickDetailsPoint = new Point2D(clickX, (clickY-500));
		}else if(screenBound.getWidth()-clickX < 420 && screenBound.getHeight()-clickY >= 580 ) {		
			clickDetailsPoint = new Point2D((clickX - 400), clickY);
		}else if(screenBound.getWidth()-clickX < 420 && screenBound.getHeight()-clickY < 580 ) {		
			clickDetailsPoint = new Point2D((clickX - 400), (clickY-500));
		}
		
	}
	
	@FXML
	public void mthbtnMainReport(ActionEvent event) throws Exception{
	
		if (Holder.isUser_Logged) {
			FXMLLoader loader = new FXMLLoader();
			panReport = loader.load(getClass().getResource("Reports.fxml").openStream());
			Holder.injShowLand = (ReportsController) loader.getController(); 
			sceneReport = new Scene(panReport);
			stageReport = new Stage();
			stageReport.initStyle(StageStyle.DECORATED.UNDECORATED);
			stageReport.initOwner(((Node)event.getSource()).getScene().getWindow());
			stageReport.setScene(sceneReport);
			stageReport.show();
		} else {
			Holder.mthCreateNotifications("Login Notification", "Please Sign In first", null);
		}
		
	}
	
	@FXML
	public void mthbtnMainExit(ActionEvent event) {
		staMaiClose = (Stage) btnMainExit.getScene().getWindow();
		staMaiClose.close();
	}
	
	java.awt.Rectangle rec = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();	
}