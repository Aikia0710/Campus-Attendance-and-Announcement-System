//package Controller;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//import Server.Protocal;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.ListView;
//import javafx.scene.text.Text;
//import javafx.stage.Stage;
//
//public class LecturerAttendanceController {
//	public String courseNameCid="";
//	public ArrayList<String> attList=new ArrayList<>();
//
//	public LecturerAttendanceController(ArrayList<String> list,String courseNameCid) {
//		this.attList=list;
//		this.courseNameCid=courseNameCid;
//	}
//	
//	
//	@FXML
//	private ListView<String> attendanceList;
//	@FXML
//	private Button attendanceAddButton;
//	@FXML
//	private Button attendanceDeleteButton;
//	@FXML
//	private Text usernameTexOfAttendance;
//	@FXML
//	private Text courseNameForAttendance;
//	
//	public void initialize() {
//		// here will be connected with the database
//		ObservableList<String> list = FXCollections.observableArrayList(attList);
//		attendanceList.setItems(list);
//		courseNameForAttendance.setText(courseNameCid);
//	}
//	
//	/**Define methods for controllers in lecturer_attendancePage.fxml*/
//	@FXML
//	public void attendanceAddButton(ActionEvent event) throws IOException{
//		/**TO DO:
//		 * turn to lecturer_createAttendance.fxml
//		 * and set the Text usernameForCreateAttendance=client.username, Text courserDetail=courseNameForAttendance.getText()*/
//	
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/lecturer_createAttendance.fxml"));
//
//		LecturerCreateAttendanceController controller = new LecturerCreateAttendanceController(courseNameForAttendance.getText());
//		loader.setController(controller);
//
//		Parent mainPageParent = loader.load();
//
//		//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
//		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//		Scene mainPageScene = new Scene(mainPageParent);
//		mainStage.setScene(mainPageScene);
//		mainStage.show();
//	}
//	
//	@FXML
//	public void attendanceDeleteButton(ActionEvent event) throws IOException{
//		
//		String attendancePeriod=getChosenAttendance().split(":")[0];
//		/*this real message is"beginTime-endTime&coursename cid"*/
//		LoginPageController.client.out.write(Protocal.LECTURER_DELETE_AN_ATTENDENCE+attendancePeriod+"&"+courseNameForAttendance.getText()+Protocal.LECTURER_DELETE_AN_ATTENDENCE);
//		LoginPageController.client.out.flush();
//		/**要不要直接更新attendanceList的内容呢，要的话再运行userProfilePage里的attendanceButton方法应该就行*/
//		
//	}
//	
//	/**this method get the chosen String in ListView: lectureList*/
//	public String getChosenAttendance() {
//		String attendanceDetails=attendanceList.getSelectionModel().getSelectedItem();
//		
//		return attendanceDetails;
//	}
//}





package Controller;

import java.io.IOException;
import java.util.ArrayList;

import Server.Protocal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LecturerAttendanceController {
	public String courseNameCid="";
	public ArrayList<String> attList=new ArrayList<>();

	public LecturerAttendanceController(ArrayList<String> list,String courseNameCid) {
		this.attList=list;
		this.courseNameCid=courseNameCid;
	}
	
	
	@FXML
	private ListView<String> attendanceList;
	@FXML
	private Button attendanceAddButton;
	@FXML
	private Button attendanceDeleteButton;
	@FXML
	private Text usernameTexOfAttendance;
	@FXML
	private Text courseNameForAttendance;
	
	public void initialize() {
		// here will be connected with the database
		ObservableList<String> list = FXCollections.observableArrayList(attList);
		attendanceList.setItems(list);
		courseNameForAttendance.setText(courseNameCid);
	}
	
	/**Define methods for controllers in lecturer_attendancePage.fxml*/
	@FXML
	public void attendanceAddButton(ActionEvent event) throws IOException{
		/**TO DO:
		 * turn to lecturer_createAttendance.fxml
		 * and set the Text usernameForCreateAttendance=client.username, Text courserDetail=courseNameForAttendance.getText()*/
	
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/lecturer_createAttendance.fxml"));

		LecturerCreateAttendanceController controller = new LecturerCreateAttendanceController(courseNameForAttendance.getText());
		loader.setController(controller);

		Parent mainPageParent = loader.load();

		//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene mainPageScene = new Scene(mainPageParent);
		mainStage.setScene(mainPageScene);
		mainStage.show();
	}
	
	@FXML
	public void attendanceDeleteButton(ActionEvent event) throws IOException{
		if(getChosenAttendance()==null) {
			//for now im making it go back
			return;
		}
		else {
			System.out.println("we are here 1");
			String attendanceDetail = getChosenAttendance().split(":")[0];
			attendanceDetail += "&" + courseNameForAttendance.getText();
			System.out.println(attendanceDetail);
			//get chosen attendance details and then delete from database and then reload attendance page
			LoginPageController.client.out.write(Protocal.LECTURER_DELETE_AN_ATTENDENCE+attendanceDetail+Protocal.LECTURER_DELETE_AN_ATTENDENCE + "\n" + "\n");
			LoginPageController.client.out.flush();
			System.out.println(LoginPageController.client.br.read());
			//if(LoginPageController.client.br.read()==10) {
				System.out.println("we are here 2");
				System.out.println(courseNameForAttendance.getText());
				//load new page 
				LoginPageController.client.out.write(Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER+courseNameForAttendance.getText()+Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER+"\n");
				LoginPageController.client.out.flush();
				ArrayList<String> attendances=new ArrayList<>();
				
				String forme = LoginPageController.client.br.readLine();
				//forme = forme.substring(0, forme.length());
				System.out.println(forme);
				for(String attendanceDetails:forme.split("&")) {
					
					attendances.add(attendanceDetails);
				}
				
				System.out.println("HERE3");
				/**TO DO: 
				 * turn to lecturer_attendancePage.fxml and fill the attendances ViewList by attendances
				 * and mark the usernameTexOfAttendance up right*/
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));

				LecturerAttendanceController controller = new LecturerAttendanceController(attendances,courseNameForAttendance.getText());
				loader.setController(controller);

				Parent mainPageParent = loader.load();

				//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
				Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				Scene mainPageScene = new Scene(mainPageParent);
				mainStage.setScene(mainPageScene);
				mainStage.show();
		//	}
		}
//		String attendancePeriod=getChosenAttendance().split(":")[0];
//		/*this real message is"beginTime-endTime&coursename cid"*/
//		LoginPageController.client.out.write(Protocal.LECTURER_DELETE_AN_ATTENDENCE+attendancePeriod+"&"+courseNameForAttendance.getText()+Protocal.LECTURER_DELETE_AN_ATTENDENCE);
//		LoginPageController.client.out.flush();
//		/**要不要直接更新attendanceList的内容呢，要的话再运行userProfilePage里的attendanceButton方法应该就行*/
		
	}
	
	/**this method get the chosen String in ListView: lectureList*/
	public String getChosenAttendance() {
		String attendanceDetails=attendanceList.getSelectionModel().getSelectedItem();
		
		return attendanceDetails;
	}
	
	public void backButton(ActionEvent event) throws IOException {
		LoginPageController.client.out.write(Protocal.LECTURER_BACK_TO_USER_PROFILE+"\n");
		LoginPageController.client.out.flush();
		ArrayList<String> lecturerCourses=new ArrayList<>();
		
		//System.out.println(LoginPageController.client.br.readLine());
		
		String courseString=LoginPageController.client.br.readLine();
		String[] courses=courseString.split("&");
		System.out.println(courses.toString());
		if(courses.length>1) {
			for(int i=1;i<courses.length;i++) {
				String coursenameCid=courses[i];
				lecturerCourses.add(coursenameCid);
			}
		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/userProfilePage.fxml"));
		userProfilePageController controller = new userProfilePageController(lecturerCourses,LoginPageController.client.getUsername());
		loader.setController(controller);
		Parent mainPageParent = loader.load();
		//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/userProfilePage.fxml"));
		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene mainPageScene = new Scene(mainPageParent);
		mainStage.setScene(mainPageScene);
		mainStage.show();
	}
}
