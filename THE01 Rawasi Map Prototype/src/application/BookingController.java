package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.controlsfx.control.Notifications;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sun.util.calendar.LocalGregorianCalendar;

public class BookingController implements Initializable{
	@FXML public AnchorPane panBooking;
	@FXML public TextField txtBooID;
	@FXML public TextField txtBooREF;
	@FXML public TextField txtBooCurrencyValue;
	@FXML public TextField txtBooMPrice;
	@FXML public TextField txtBooTotalP;
	@FXML public TextField txtBooNote;
	@FXML public TextField txtBooArea;
	@FXML public ComboBox<String> comboxBooCustomer;
	@FXML public ComboBox<Integer> comboxBooBlock;
	@FXML public ComboBox<Integer> comboxBooLandNo;
	@FXML public ComboBox<String> comboxBooStatus;
	@FXML public ComboBox<String> comboxBooCurrency;
	@FXML public Button btnBooInsert;
	@FXML public Button btnBooInsertAddNew;
	@FXML public Button btnBooCancel;
	@FXML public Button btnBooNewCustomer;
	@FXML public DatePicker picBooStartDate;
	@FXML public DatePicker picBooLastDate;
	public Pane NewCustomerPane,pane;
	public Stage staBooClose, NewCustomerStage;
	private int block=0;
	private Connection connection;
	private String Query,qurCurrency, qurStatus, qurBookLand, qurContact,qurLandBlock;
	private PreparedStatement prS = null, prSCurrency, prSStatus, prSBookLand, prSContact,prSLandBlock; 
	private ResultSet reS = null, reSCurrency, reSStatus, reSBookLand, reSContact;
	
	ObservableList<Integer> olistcomboxBooBlock = FXCollections.observableArrayList(1,2);
	
	
	public BookingController() {
		
		connection = DatabaseConnection.connection;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		comboxBooBlock.setItems(olistcomboxBooBlock);
		
		qurCurrency = "select cur_name from currency";
		
		qurStatus = "select las_value from land_status where las_id in (3,4,7)";
		
		qurBookLand = "select max(bok_id) as id, max(bok_ref) as ref from booking_lands ";
		
		try {	
			prSBookLand = connection.prepareStatement(qurBookLand);
			reSBookLand = prSBookLand.executeQuery();
			
			prSCurrency = connection.prepareStatement(qurCurrency);
			reSCurrency = prSCurrency.executeQuery();
			
			prSStatus = connection.prepareStatement(qurStatus);
			reSStatus = prSStatus.executeQuery();
			
			if(reSBookLand.getString("id")==null) {
			
				txtBooID.setText("1");
				txtBooREF.setText("1");
			}else {
				
				txtBooID.setText(Integer.toString(Integer.parseInt(reSBookLand.getString("id"))+1));
				txtBooREF.setText(Integer.toString(Integer.parseInt(reSBookLand.getString("ref"))+1));
			}
			
			comboxBooCurrency.getItems().clear();
			
			while(reSCurrency.next()) {
				comboxBooCurrency.getItems().add(reSCurrency.getString("cur_name"));
			}
			comboxBooCurrency.getSelectionModel().select(0);
			txtBooCurrencyValue.setText("1.0");
			comboxBooStatus.getItems().clear();
			while(reSStatus.next()) {
				comboxBooStatus.getItems().add(reSStatus.getString("las_value"));
			}
			
			mthfillcomboxBooCustomer();
			
			comboxBooBlock.getSelectionModel().select(Holder.Block_Selected - 1);
			mthcomboxBooBlock(new ActionEvent());
			
			comboxBooLandNo.getSelectionModel().select(Holder.land_Selected - 1);
			mthcomboxBooLandNo(new ActionEvent());
			
			picBooStartDate.setValue(LocalDate.now());
			picBooLastDate.setFocusTraversable(true);
			
			txtBooArea.textProperty().addListener((observable, oldValue, newValue) ->{
				if(newValue == null || newValue == "" || newValue.isEmpty()) {	
				
				}else if(!newValue.matches("(.*)[a-zA-Z]+(.*)") && !newValue.isEmpty()) {
					if(txtBooMPrice.getText().isEmpty()) {
						txtBooMPrice.setText("0.0");
					}
				
					txtBooTotalP.setText(Double.toString(Double.parseDouble(newValue) * Double.parseDouble(txtBooMPrice.getText())));
				
				}else {
					txtBooMPrice.setText(oldValue);
				}
			});
			
			txtBooMPrice.textProperty().addListener((observable, oldValue, newValue) ->{
				if(newValue == null || newValue == "" || newValue.isEmpty()) {	
				}else if(!newValue.matches("(.*)[a-zA-Z]+(.*)") && !newValue.isEmpty()) {
					txtBooTotalP.setText(Double.toString(Double.parseDouble(newValue) * Double.parseDouble(txtBooArea.getText())));
				}else {
					txtBooMPrice.setText(oldValue);
				}
			});
			
			txtBooID.setDisable(true);
			txtBooREF.setDisable(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try { prSCurrency.close(); reSCurrency.close(); prSStatus.close(); reSStatus.close(); prSBookLand.close(); reSBookLand.close(); } 
			catch (SQLException e) { e.printStackTrace(); }
		}
	}
	
	public void mthbtnBooInsert(ActionEvent event) throws SQLException {
		Holder.isBooking_insert = false;
		int statusId = 0; 
		
		Query = "insert into booking_lands (bok_id, bok_ref, bok_date, contacts_con_id, bok_land_block, bok_land_no, land_status_las_id, bok_currency, "
				+ " bok_currency_value, bok_metre_price, bok_total_price, bok_end_date, bok_note) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
		if(comboxBooCustomer.getSelectionModel().getSelectedIndex()!=-1 && comboxBooStatus.getSelectionModel().getSelectedIndex()!=-1
					&& comboxBooStatus.getSelectionModel().getSelectedIndex()!=2 && picBooLastDate.getValue()!=null) {
		
			qurContact = "select con_id from Contacts where con_name='"+comboxBooCustomer.getValue().toString()+"'";
			
			qurStatus = "select las_id from land_status where lav_value='"+comboxBooStatus.getValue().toString()+"'";
			
			qurCurrency = "select cur_id from Currency where cur_name='"+comboxBooCurrency.getValue().toString()+"'";
			
			try {
				prSStatus = connection.prepareStatement(qurStatus);
				reSStatus = prSStatus.executeQuery();
				
				prSContact = connection.prepareStatement(qurContact);
				reSContact = prSContact.executeQuery();
				
				prSCurrency = connection.prepareStatement(qurCurrency);
				reSCurrency = prSCurrency.executeQuery();
				
				statusId = reSStatus.getInt(1);
		
				prS = connection.prepareStatement(Query);
				
				prS.setString(1, txtBooID.getText());
				prS.setString(2, txtBooREF.getText());
				prS.setString(3, picBooStartDate.getValue().toString());
				prS.setInt(4, reSContact.getInt(1));
				prS.setString(5, comboxBooBlock.getValue().toString());
				prS.setString(6, comboxBooLandNo.getValue().toString());
				prS.setInt(7, statusId );
				prS.setInt(8, reSCurrency.getInt(1));
				prS.setString(9, txtBooCurrencyValue.getText());
				prS.setString(10, txtBooMPrice.getText());
				prS.setString(11, txtBooTotalP.getText());
				prS.setString(12, picBooLastDate.getValue().toString());
				prS.setString(13, txtBooNote.getText());
				prS.executeUpdate();
				
				if(comboxBooBlock.getValue().intValue()==1) {
					
					qurLandBlock=" update land_block_1 set land_status_las_id="+statusId+", lb1_metre_price="+txtBooMPrice.getText()+", "
							+ " lb1_total_price="+txtBooTotalP.getText()	+ ", booking_lands_bok_ref="+txtBooREF.getText()
							+ " where lb1_no ="+comboxBooLandNo.getValue().toString();
				
				}else if(comboxBooBlock.getValue().intValue()==2){
					
					qurLandBlock=" update land_block_2 set land_status_las_id="+statusId+", lb2_metre_price="+txtBooMPrice.getText()+", "
							+ " lb2_total_price="+txtBooTotalP.getText() +", booking_lands_bok_ref="+txtBooREF.getText()
							+ " where lb2_no ="+comboxBooLandNo.getValue().toString();
					
				}
				
				prSLandBlock = connection.prepareStatement(qurLandBlock);
				prSLandBlock.executeUpdate();
				
				Holder.Block_Selected= Integer.parseInt(comboxBooBlock.getValue().toString());
				Holder.land_Selected= Integer.parseInt(comboxBooLandNo.getValue().toString());
				Holder.Status_Selected= statusId;
				Holder.isBooking_insert = true;
				Holder.isSales_insert = false;
				
				if(!Holder.isBooking_InsertAddNew) {
					btnBooCancel.fire();
				}
				
				Image im = new Image("Images/correct.png");
				Notifications.create()
			       .title("Insertion Process Notification")
			       .text("Successfully Saved")
			       .graphic(new ImageView(im))
			       .hideAfter(Duration.seconds(5))
			       .position(Pos.BOTTOM_RIGHT)
			       .show();
			
			} catch (Exception e) {
				e.printStackTrace();
				Notifications.create()
				.title("Insertion Process Notification")
				.text("Something Wrong")
				.graphic(null)
				.hideAfter(Duration.seconds(5))
				.position(Pos.BOTTOM_RIGHT)
				.show();
			} finally {
				prS.close();
				prSContact.close();
				reSContact.close();
				prSCurrency.close();
				reSCurrency.close();
				prSStatus.close();
				reSStatus.close();
				prSLandBlock.close();
			}
		}else {
			Notifications.create()
			.title("Insertion Process Notification")
			.text("Please Review Your Entered Data, Something Wrong.")
			.graphic(null)
			.hideAfter(Duration.seconds(5))
			.position(Pos.BOTTOM_RIGHT)
			.show();
		}
	}
	
	public void mthbtnBooInsertAddNew(ActionEvent event) {
		Holder.isBooking_InsertAddNew = true;
		btnBooInsert.fire();
		txtBooID.setText(Integer.toString(Integer.parseInt(txtBooID.getText())+1));
		comboxBooBlock.getSelectionModel().select(-1);
		comboxBooLandNo.getSelectionModel().select(-1);
		comboxBooStatus.getSelectionModel().select(-1);
		comboxBooLandNo.setDisable(true);
		txtBooArea.setText("");
		txtBooTotalP.setText("0.0");
		txtBooNote.setText("");
		Holder.isBooking_InsertAddNew = false;
		
	}
	
	public void mthbtnBooCancel(ActionEvent event) {
		staBooClose = (Stage) btnBooCancel.getScene().getWindow();
		staBooClose.close();
	}
	
	public void mthbtnBooOpenNewCustomer(ActionEvent event) {
		
	}
	
	public void mthcomboxBooCustomer(ActionEvent event) {}
	
	public void mthcomboxBooBlock(ActionEvent event) throws SQLException {
		
		if(comboxBooBlock.getSelectionModel().getSelectedIndex() >= 0 ) {
			block = comboxBooBlock.getValue();
			comboxBooLandNo.setDisable(false);
			
			Query = "select * from land_block_"+block;
		
			try {	
				prS = connection.prepareStatement(Query); 
				reS = prS.executeQuery();
				comboxBooLandNo.getItems().clear();
			
				while(reS.next()) {
					comboxBooLandNo.getItems().add(reS.getInt("lb"+block+"_no"));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				prS.close();
				reS.close();
			}
		}else {
			comboxBooLandNo.setDisable(true);
		}
	}
	
	public void mthcomboxBooLandNo(ActionEvent event) throws SQLException {
		
		if(comboxBooBlock.getSelectionModel().getSelectedIndex() >= 0 && comboxBooLandNo.getSelectionModel().getSelectedIndex() >=0 ) {
			block = comboxBooBlock.getValue();
		
			Query = "select * from land_block_"+block+" l, land_status s where l.land_status_las_id = s.las_id and "
					+ " lb"+block+"_No ="+comboxBooLandNo.getValue().toString();
			
			try {
			
				prS = connection.prepareStatement(Query); 
				reS = prS.executeQuery();
				comboxBooStatus.getSelectionModel().select(reS.getString("las_value"));
				txtBooArea.setText(reS.getString("lb"+block+"_area"));
			
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				prS.close();
				reS.close();
			}
		}
	}
	
	public void mthcomboxBooStatus(ActionEvent event) {
		if(comboxBooStatus.getSelectionModel().getSelectedIndex() >=0) {
			
		}
	}
	
	public void mthcomboxBooCurrency(ActionEvent event) throws SQLException {
		
		Query = "select * from currency where cur_name="+comboxBooCurrency.getValue().toString();
		
		try {
			prS = connection.prepareStatement(Query); 
			reS = prS.executeQuery();
			txtBooCurrencyValue.setText(reS.getString("cur_value"));
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			prS.close();
			reS.close();
		}
	}
	
	public void mthtxtBooNote(ActionEvent event) {}
	
	public void mthfillcomboxBooCustomer() throws SQLException {
		
		qurContact = "select contacts.con_name from contacts";
		
		prSContact = connection.prepareStatement(qurContact);
		reSContact = prSContact.executeQuery();
		comboxBooCustomer.getItems().clear();
		
		while(reSContact.next()) {
			comboxBooCustomer.getItems().add(reSContact.getString("con_name"));
		}
		
		prSContact.close();
		reSContact.close();
	}
}
