package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;

import javafx.scene.control.ComboBox;

import javafx.scene.control.TableView;

import javafx.scene.control.ChoiceBox;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;

public class ReportsController implements Initializable{
	
	@FXML private TableView<Holder> tblRepLands;
	@FXML private TableColumn<Holder,Integer> coltblRepLandsNo;
	@FXML private TableColumn<Holder,String> coltblRepLandsArea;
	@FXML private TableColumn<Holder,String> coltblRepLandsType;
	@FXML private TableColumn<Holder,String> coltblRepLandsStatus;
	@FXML private TableColumn<Holder,Double> coltblRepLandsMPrice;
	@FXML private TableColumn<Holder,Double> coltblRepLandsTotal;
	@FXML private TableColumn<Holder,String> coltblRepLandsCustomer;
	
	@FXML private TextField txtRepSearch;
	@FXML private Button btnRepClose, btnRepPrint;
	
	@FXML private ComboBox<String> cmxRepStatus;
	@FXML private ComboBox<Integer> cmxRepLand;
	@FXML private ComboBox<String> cmxRepType;
	@FXML private ComboBox<String> cmxRepName;
	@FXML private ComboBox<Integer> cmxRepBlock;
	
	ObservableList<Holder> obstblRepLands = FXCollections.observableArrayList();
	FilteredList<Holder> fliRep = new FilteredList<>(obstblRepLands, b-> true);
	
	ObservableList<Integer> obscmxRepBlock = FXCollections.observableArrayList(1,2);
	ObservableList<Integer> obscoxRepLands = FXCollections.observableArrayList();
	ObservableList<Integer> obscmxRepType = FXCollections.observableArrayList();
	ObservableList<Integer> obscmxRepStatus = FXCollections.observableArrayList();
	ObservableList<Integer> obscmxRepName = FXCollections.observableArrayList();
	
	private Connection connection;
	private PreparedStatement prS, prSStatus,prSType,prSContact;
	private ResultSet reS, reSStatus, reSType,reSContact;
	private String Query, qurStatus, qurType,qurContact;
	private Integer block;
	Holder holder;
	
	public ReportsController() {
		
		connection = DatabaseConnection.connection;
		//connection = SQLiteConnection.connector();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cmxRepBlock.setItems(obscmxRepBlock);
		
		qurStatus = "select las_value from land_status";
		qurType = "select lat_value from land_type";
		qurContact = "select contacts.con_name from contacts";
		
		try {
			prSStatus = connection.prepareStatement(qurStatus);
			reSStatus = prSStatus.executeQuery();
			cmxRepStatus.getItems().clear();
			while(reSStatus.next()) {
				cmxRepStatus.getItems().add(reSStatus.getString("las_value"));
			}
			prSType = connection.prepareStatement(qurType);
			reSType = prSType.executeQuery();
			cmxRepType.getItems().clear();
			while(reSType.next()) {
				cmxRepType.getItems().add(reSType.getString("lat_value"));
			}
			prSContact = connection.prepareStatement(qurContact);
			reSContact = prSContact.executeQuery();
			cmxRepName.getItems().clear();
			while(reSContact.next()) {
				cmxRepName.getItems().add(reSContact.getString("con_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
			 prSContact.close();  reSStatus.close(); reSType.close(); 
			 reSContact.close();  reSStatus.close(); reSType.close(); 
			}catch(Exception e) {e.printStackTrace();}
		}
		
		txtRepSearch.textProperty().addListener((observable,oldValue,newValue)->{
			fliRep.setPredicate(holder ->{				
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if(String.valueOf(holder.getVcoltblShoLandsNo()).indexOf(lowerCaseFilter) != -1) {
					return true;
				}else if (String.valueOf(holder.getVcoltblShoLandsArea()).indexOf(lowerCaseFilter) != -1) {
					return true;
				}else if (String.valueOf(holder.getVcoltblShoLandsMPrice()).indexOf(lowerCaseFilter) != -1) {
					return true;
				}else if (String.valueOf(holder.getVcoltblShoLandsTotal()).indexOf(lowerCaseFilter) != -1) {
					return true;
				}else if(holder.getVcoltblShoLandsStatus().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}else if(holder.getVcoltblShoLandsType().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}else {
					if (holder.getVcoltblShoLandsCustomer() != null) {						
						if (holder.getVcoltblShoLandsCustomer().toLowerCase().indexOf(lowerCaseFilter) != -1) {
							return true;
						}else {
							return false;
						}
					}else {
						return false;
					}
				}
				
			});
		});
		SortedList<Holder> sliRep = new SortedList<>(fliRep);
		sliRep.comparatorProperty().bind(tblRepLands.comparatorProperty());
		tblRepLands.setItems(sliRep);
	}
	
	// Event Listener on ComboBox[#cmxRepBlock].onAction
	@FXML
	public void mthcmxRepBlock(ActionEvent event) throws SQLException {
		if(!Holder.getisUser_Logged()) {
			coltblRepLandsCustomer.setVisible(false);
			coltblRepLandsMPrice.setVisible(false);
			coltblRepLandsTotal.setVisible(false);
		}else {
			coltblRepLandsCustomer.setVisible(true);
			coltblRepLandsMPrice.setVisible(true);
			coltblRepLandsTotal.setVisible(true);
		}
		
		block = cmxRepBlock.getValue();
		
		Query = "select * from land_block_"+block;
		prS = connection.prepareStatement(Query); 
		reS = prS.executeQuery();
		cmxRepLand.getItems().clear();
		
		while(reS.next()) {
			cmxRepLand.getItems().add(reS.getInt("lb"+block+"_no"));
		}
		prS.close();
		reS.close();
		
		tblRepLands.getItems().clear();
		
		prS = connection.prepareStatement(Query);
		reS = prS.executeQuery();
		
		while(reS.next()) {
			obstblRepLands.add(new Holder(reS.getInt(1), reS.getString(2),
					reS.getString(13),reS.getString(16),reS.getDouble(6),reS.getDouble(7),reS.getString(22)));
		}
		
		coltblRepLandsNo.setCellValueFactory(new PropertyValueFactory<>("vcoltblShoLandsNo"));
		coltblRepLandsArea.setCellValueFactory(new PropertyValueFactory<>("vcoltblShoLandsArea"));
		coltblRepLandsType.setCellValueFactory(new PropertyValueFactory<>("vcoltblShoLandsType"));
		coltblRepLandsStatus.setCellValueFactory(new PropertyValueFactory<>("vcoltblShoLandsStatus"));
		coltblRepLandsMPrice.setCellValueFactory(new PropertyValueFactory<>("vcoltblShoLandsMPrice"));
		coltblRepLandsTotal.setCellValueFactory(new PropertyValueFactory<>("vcoltblShoLandsTotal"));
		coltblRepLandsCustomer.setCellValueFactory(new PropertyValueFactory<>("vcoltblShoLandsCustomer"));
		tblRepLands.setItems(obstblRepLands);
		prS.close();
		reS.close();
	}
	
	@FXML
	public void mthcmxRepLand(ActionEvent event) throws SQLException {
		mthRepFilter(cmxRepLand.getValue().toString(),"No");
	}
	
	// Event Listener on ComboBox[#cmxRepStatus].onAction
	@FXML
	public void mthcmxRepStatus(ActionEvent event) {
		mthRepFilter(cmxRepStatus.getValue(),"Status");
	}
	
	// Event Listener on ComboBox[#cmxRepType].onAction
	@FXML
	public void mthcmxRepType(ActionEvent event) {
		mthRepFilter(cmxRepType.getValue(),"Type");
	}
	
	// Event Listener on ComboBox[#cmxRepName].onAction
	@FXML
	public void mthcmxRepName(ActionEvent event) {
		mthRepFilter(cmxRepName.getValue(),"Name");
	}
	
	@FXML
	public void mthbtnRepClose(ActionEvent event) {
		btnRepClose.getScene().getWindow().hide();
	}
	
	@FXML
	public void mthbtnRepPrint(ActionEvent event) {
		
	}
	
	public void mthRepFilter(String value,String column) {
		fliRep.setPredicate(holder ->{				
			if(value == null || value.isEmpty()) {
				return true;
			}
			String lowerCaseFilter = value.toLowerCase();
			if(String.valueOf(holder.getVcoltblShoLandsNo()).equals(lowerCaseFilter) && column=="No") {
				return true;
			}else if (String.valueOf(holder.getVcoltblShoLandsArea()).indexOf(lowerCaseFilter) != -1 && column=="Area") {
				return true;
			}else if (String.valueOf(holder.getVcoltblShoLandsMPrice()).indexOf(lowerCaseFilter) != -1 && column=="MPrice") {
				return true;
			}else if (String.valueOf(holder.getVcoltblShoLandsTotal()).indexOf(lowerCaseFilter) != -1 && column=="Total") {
				return true;
			}else if(holder.getVcoltblShoLandsStatus().toLowerCase().indexOf(lowerCaseFilter) != -1 && column=="Status") {
				return true;
			}else if(holder.getVcoltblShoLandsType().toLowerCase().indexOf(lowerCaseFilter) != -1 && column=="Type") {
				return true;
			}else {
				if (holder.getVcoltblShoLandsCustomer() != null ) {						
					if (holder.getVcoltblShoLandsCustomer().toLowerCase().indexOf(lowerCaseFilter) != -1 && column=="Name") {
						return true;
					}else {
						return false;
					}
				}else {
					return false;
				}
			}
		});
		SortedList<Holder> sliRep = new SortedList<>(fliRep);
		sliRep.comparatorProperty().bind(tblRepLands.comparatorProperty());
		tblRepLands.setItems(sliRep);
	}
}
