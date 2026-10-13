package application;

import java.net.URISyntaxException;

import org.controlsfx.control.Notifications;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Holder {
	public static String user_Name = " ";
	public static boolean isUser_Logged = false;
	public static String UserName_Login = "";
	public static int land_Selected = 0;
	public static int Block_Selected = 0;
	public static int Status_Selected = 0;
	public static boolean isBooking_insert = false;
	public static boolean isSales_insert = false;
	public static boolean isBooking_InsertAddNew = false;
	public static boolean isSale_InsertAddNew = false;
	public static Image success_image ;
	public static boolean isMySQLConnecter_Local = true;
	public static ReportsController injShowLand;
	public static BookingController injBooking;
	public static LoginController injLogin;
	
	private int vcoltblShoLandsNo;
	private String vcoltblShoLandsArea;
	private String vcoltblShoLandsType;
	private String vcoltblShoLandsStatus;
	private Double vcoltblShoLandsMPrice;
	private Double vcoltblShoLandsTotal;
	private String vcoltblShoLandsCustomer;
	
    public Holder(int vcoltblShoLandsNo, String vcoltblShoLandsArea, String vcoltblShoLandsType,
			String vcoltblShoLandsStatus, Double vcoltblShoLandsMPrice, Double vcoltblShoLandsTotal,
			String vcoltblShoLandsCustomer) {
		super();
		this.vcoltblShoLandsNo = vcoltblShoLandsNo;
		this.vcoltblShoLandsArea = vcoltblShoLandsArea;
		this.vcoltblShoLandsType = vcoltblShoLandsType;
		this.vcoltblShoLandsStatus = vcoltblShoLandsStatus;
		this.vcoltblShoLandsMPrice = vcoltblShoLandsMPrice;
		this.vcoltblShoLandsTotal = vcoltblShoLandsTotal;
		this.vcoltblShoLandsCustomer = vcoltblShoLandsCustomer;
	}
	Holder() throws URISyntaxException{
 		success_image = new Image(getClass().getResource("/Images/correct.png").toURI().toString());
	}
	public static void setuser_Name(String us,boolean s) {
		user_Name=us;
		isUser_Logged = s;
	}
	public static String getuser_Name() {
		return user_Name;
	}
	public static boolean getisUser_Logged() {
		return isUser_Logged;
	}
	
	public int getVcoltblShoLandsNo() {
		return vcoltblShoLandsNo;
	}
	public static void setVcoltblShoLandsNo(int vcoltblShoLandsNo) {
		vcoltblShoLandsNo = vcoltblShoLandsNo;
	}
	public String getVcoltblShoLandsArea() {
		return vcoltblShoLandsArea;
	}
	public static void setVcoltblShoLandsArea(String vcoltblShoLandsArea) {
		vcoltblShoLandsArea = vcoltblShoLandsArea;
	}
	public String getVcoltblShoLandsType() {
		return vcoltblShoLandsType;
	}
	public static void setVcoltblShoLandsType(String vcoltblShoLandsType) {
		vcoltblShoLandsType = vcoltblShoLandsType;
	}
	public String getVcoltblShoLandsStatus() {
		return vcoltblShoLandsStatus;
	}
	public static void setVcoltblShoLandsStatus(String vcoltblShoLandsStatus) {
		vcoltblShoLandsStatus = vcoltblShoLandsStatus;
	}
	public Double getVcoltblShoLandsMPrice() {
		return vcoltblShoLandsMPrice;
	}
	public static void setVcoltblShoLandsMPrice(Double vcoltblShoLandsMPrice) {
		vcoltblShoLandsMPrice = vcoltblShoLandsMPrice;
	}
	public Double getVcoltblShoLandsTotal() {
		return vcoltblShoLandsTotal;
	}
	public static void setVcoltblShoLandsTotal(Double vcoltblShoLandsTotal) {
		vcoltblShoLandsTotal = vcoltblShoLandsTotal;
	}
	public String getVcoltblShoLandsCustomer() {
		return vcoltblShoLandsCustomer;
	}
	public static void setVcoltblShoLandsCustomer(String vcoltblShoLandsCustomer) {
		vcoltblShoLandsCustomer = vcoltblShoLandsCustomer;
	}
	
	// ####################### Notification 
    public static void mthCreateNotifications(String title, String text,ImageView image) {    	
    	Notifications.create()
    	.title(title)
    	.text(text)
    	.graphic(image)
    	.hideAfter(Duration.seconds(5))
    	.position(Pos.TOP_RIGHT)
//    	.show();
    	.showInformation();
    }
}
