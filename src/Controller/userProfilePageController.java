package Controller;

import java.io.IOException;
import java.util.ArrayList;

import Client.Client;
import Server.Protocal;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class userProfilePageController {
	public ArrayList<String> cnc=new ArrayList<>();
	public String Indetity="";

	public userProfilePageController(ArrayList<String> list,String str) {
		this.cnc=list;
		this.Indetity=str;
	}
	
	@FXML
	private ListView<String> lectureList;
	@FXML
	private Button logOutButton;
	@FXML
	private Text username;
	@FXML
	private Text status;
	@FXML
	private Button windowClose;
	@FXML
	private Button attendance;
	@FXML
	private Button announcement;


	
	
	public void initialize() {
		// here will be connected with the database
		if(!cnc.isEmpty()) {
			ObservableList<String> list = FXCollections.observableArrayList(cnc);
			lectureList.setItems(list);
		}
		status.setText(Indetity);
	}


	public void logOut(ActionEvent event1) throws IOException {
		LoginPageController.client.out.write("LOG OUT"+"\n");
		LoginPageController.client.out.flush();
		LoginPageController.client=new Client();
		Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/LoginPage.fxml"));
		Stage mainStage = (Stage) ((Node) event1.getSource()).getScene().getWindow();
		Scene mainPageScene = new Scene(mainPageParent);
		mainStage.setScene(mainPageScene);
		mainStage.show();
	}

	/**Define methods for controllers in userProfilePage.fxml*/
	@FXML
	public void attendance(ActionEvent event) throws IOException{
		//chosenCourseName=getChosenCourseNameCid().split(" ")[0];
		//chosenCourseCid=getChosenCourseNameCid().split(" ")[1];
		if(getChosenCourseNameCid()!=null) {
			if(LoginPageController.client.getUsername().startsWith("L")) {
				
				LoginPageController.client.out.write(Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER+getChosenCourseNameCid()+Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER+"\n");
				LoginPageController.client.out.flush();
				ArrayList<String> attendances=new ArrayList<>();
				
//				System.out.println(LoginPageController.client.br.readLine());			
				for(String attendanceDetails:LoginPageController.client.br.readLine().split("&")) {
					
					attendances.add(attendanceDetails);
				}
				
				/**TO DO: 
				 * turn to lecturer_attendancePage.fxml and fill the attendances ViewList by attendances
				 * and mark the usernameTexOfAttendance up right*/
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));

				LecturerAttendanceController controller = new LecturerAttendanceController(attendances,this.getChosenCourseNameCid());
				loader.setController(controller);

				Parent mainPageParent = loader.load();

				//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
				Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				Scene mainPageScene = new Scene(mainPageParent);
				mainStage.setScene(mainPageScene);
				mainStage.show();
			}
			
			else if(LoginPageController.client.getUsername().startsWith("S")) {
				System.out.println("student is here");
				LoginPageController.client.out.write(Protocal.OPEN_ATTENDENT_GUI_FOR_STUDENT+getChosenCourseNameCid()+Protocal.OPEN_ATTENDENT_GUI_FOR_STUDENT+"\n");
				LoginPageController.client.out.flush();
				int validTryTime=LoginPageController.client.br.read();
				System.out.println(validTryTime);
				if(validTryTime==1) {
					/**TO DO:
					 * show student_doAttendance.fxml
					 * but the userProfilePage.fxml doesn't disappear*/
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/student_doAttendance.fxml"));

					StudentDoAttendanceController controller = new StudentDoAttendanceController(this.getChosenCourseNameCid());
					loader.setController(controller);

					Parent mainPageParent = loader.load();

					//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
					Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					Scene mainPageScene = new Scene(mainPageParent);
					mainStage.setScene(mainPageScene);
					mainStage.show();
				}
			
				else {
					System.out.println("no attendance");
					//TODO replace this with message showing no attendance
					/**TO DO:
					 * show attendanceWarning.fxml
					 * but the userProfilePage.fxml doesn't disappear*/
//					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/attendanceWarning.fxml"));
//					Parent root2 = (Parent) fxmlLoader.load();
//					Stage stage = new Stage();
//					stage.setScene(new Scene(root2));
//					stage.show();
//					stage.setResizable(false);
				}
			}
		}
	}



	public void announcement(ActionEvent event) throws IOException{
		if(getChosenCourseNameCid()!=null) {
			// clicking button, jump to the announcement window
			if(LoginPageController.client.getUsername().startsWith("L")) {
				
				LoginPageController.client.out.write(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER+getChosenCourseNameCid()+Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER+"\n");
				LoginPageController.client.out.flush();
				String announcementList=LoginPageController.client.br.readLine();//{"time pid"}
				String[] announcementTimePid=null;
				if(announcementList.length()>1) {
					announcementTimePid=announcementList.split("&");
				}
				String courseName=getChosenCourseNameCid().split(":")[0];
				String courseCid=getChosenCourseNameCid().split(":")[1];
				/**TO DO:
				 * use this String array to fill the ListView in lecturer_announcementPage.fxml
				 * and turn to the lecturer_announcementPage.fxml*/
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/lecturer_announcementPage.fxml"));
				Lecturer_AnnouncementPageController controller = new Lecturer_AnnouncementPageController(announcementTimePid,courseName,courseCid);
				loader.setController(controller);
				Parent mainPageParent = loader.load();
				//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
				Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				Scene mainPageScene = new Scene(mainPageParent);
				mainStage.setScene(mainPageScene);
				mainStage.show();
			}
			else if(LoginPageController.client.getUsername().startsWith("S")) {
				LoginPageController.client.out.write(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_STUDENT+getChosenCourseNameCid()+Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_STUDENT+"\n");
				LoginPageController.client.out.flush();
				String allAnnouncementList=LoginPageController.client.br.readLine();
				System.out.println("allAnnouncementList: "+allAnnouncementList);
				String readAnnouncementTimePid=allAnnouncementList.split("#")[0];//{"time pid"}
				String unreadAnnouncementTimePid=allAnnouncementList.split("#")[1];//{"time pid"}
				String[] rATimePid=null;
				String[] urATimePid=null;//= {"201402141105 5","205506242305 3"};
				if(readAnnouncementTimePid.length()>1) {
					rATimePid=readAnnouncementTimePid.split("&");
				}
				if(unreadAnnouncementTimePid.length()>1) {
					urATimePid=unreadAnnouncementTimePid.split("&");
				}
				String courseName=getChosenCourseNameCid().split(":")[0];
				String courseCid=getChosenCourseNameCid().split(":")[1];
				
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/student_announcementPage.fxml"));
				Student_AnnouncementPageController controller = new Student_AnnouncementPageController(getChosenCourseNameCid(),urATimePid,rATimePid);
				loader.setController(controller);
				Parent mainPageParent = loader.load();
				//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
				Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				Scene mainPageScene = new Scene(mainPageParent);
				mainStage.setScene(mainPageScene);
				mainStage.show();
			}
		}
	}
	
	/**this method get the chosen String in ListView: lectureList*/
	public String getChosenCourseNameCid() {
		String courseNameCid=lectureList.getSelectionModel().getSelectedItem();
		
		return courseNameCid;
	}

}
