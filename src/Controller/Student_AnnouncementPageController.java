package Controller;

import java.io.IOException;
import java.util.ArrayList;

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

public class Student_AnnouncementPageController {
	private String courseDetail="";
	private String[] unreadAnnouncementAList= {};
	private String[] readAnnouncementAList= {};
	
	public Student_AnnouncementPageController(String cd,String[] unreadAnnounceList,String[] readAnnounceList) {
		this.courseDetail=cd;
		this.unreadAnnouncementAList=unreadAnnounceList;
		this.readAnnouncementAList=readAnnounceList;
	}
	
	@FXML
	private Text courseNameCid;
	@FXML
	private Button readNewAnnouncementButton;
	@FXML
	private Button reviewAnnouncementButton;
	@FXML
	private Button backToUserProfileButton;
	@FXML
	private ListView<String> unreadAnnouncementList;
	@FXML
	private ListView<String> readAnnouncementList;
	
	public void initialize() {
		courseNameCid.setText(courseDetail);
		if(unreadAnnouncementAList!=null) {
			ObservableList<String> unreadList = FXCollections.observableArrayList(unreadAnnouncementAList);
			unreadAnnouncementList.setItems(unreadList);
		}
		if(readAnnouncementAList!=null) {
			ObservableList<String> readList = FXCollections.observableArrayList(readAnnouncementAList);
			readAnnouncementList.setItems(readList);
		}
		
		
	}
	
	public void readNewAnnouncement(ActionEvent event) throws IOException {
		if(getChosenUnreadAnnouncement()!=null) {
			String announcementTimePid=getChosenUnreadAnnouncement();
			LoginPageController.client.out.write(Protocal.STUDENT_OPEN_NEW_ANNOUNCEMENT+announcementTimePid+"&"+courseNameCid.getText().split(":")[1]+Protocal.STUDENT_OPEN_NEW_ANNOUNCEMENT+"\n");
			LoginPageController.client.out.flush();
			
			String announecementDetail=LoginPageController.client.br.readLine();
			
			String commentsStudentUsernameTime=announecementDetail.split("&")[1];
			String announcementContent=announecementDetail.split("&")[0];
			String[] comments= null;
			if(commentsStudentUsernameTime.length()>1) {
				comments=commentsStudentUsernameTime.split("#");
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/student_reviewAnnouncement.fxml"));
			StudentReviewAnnouncementController controller = new StudentReviewAnnouncementController(courseNameCid.getText(),announcementContent,comments,announcementTimePid);
			loader.setController(controller);
			Parent mainPageParent = loader.load();
			//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
			Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene mainPageScene = new Scene(mainPageParent);
			mainStage.setScene(mainPageScene);
			mainStage.show();
		}
	}
	
	public void openAnnouncementForStudent(ActionEvent event) throws IOException {
		if(getChosenReadAnnouncement()!=null) {
			String announcementTimePid=getChosenReadAnnouncement();
			LoginPageController.client.out.write(Protocal.STUDENT_REVIEW_AN_ANNOUNCEMENT+announcementTimePid+"&"+courseNameCid.getText().split(":")[1]+Protocal.STUDENT_REVIEW_AN_ANNOUNCEMENT+"\n");
			LoginPageController.client.out.flush();
			
			String announecementDetail=LoginPageController.client.br.readLine();
			System.out.println("announecementDetail"+announecementDetail);
			
			String commentsStudentUsernameTime=announecementDetail.split("&")[1];
			String announcementContent=announecementDetail.split("&")[0];
			String[] comments=null;//={"SItalian1501:201402151215"};
			if(commentsStudentUsernameTime.length()>1) {
				comments=commentsStudentUsernameTime.split("#");
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/student_reviewAnnouncement.fxml"));
			StudentReviewAnnouncementController controller = new StudentReviewAnnouncementController(courseNameCid.getText(),announcementContent,comments,announcementTimePid);
			loader.setController(controller);
			Parent mainPageParent = loader.load();
			//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
			Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene mainPageScene = new Scene(mainPageParent);
			mainStage.setScene(mainPageScene);
			mainStage.show();
		}
	}
	
	public void backToUserProfilePage(ActionEvent event) throws IOException {
		LoginPageController.client.out.write(Protocal.STUDENT_BACK_TO_USER_PROFILE+"\n");
		LoginPageController.client.out.flush();
		
		ArrayList<String> studentCourses=new ArrayList<>();
		String[] courses=LoginPageController.client.br.readLine().split("&");
		if(courses.length>1) {
			for(int i=1;i<courses.length;i++) {
				String coursenameCid=courses[i];
				studentCourses.add(coursenameCid);
			}
		}
		System.out.println(studentCourses);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/userProfilePage.fxml"));

		userProfilePageController controller = new userProfilePageController(studentCourses,LoginPageController.client.getUsername());
		loader.setController(controller);

		Parent mainPageParent = loader.load();

		//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/userProfilePage.fxml"));
		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene mainPageScene = new Scene(mainPageParent);
		mainStage.setScene(mainPageScene);
		mainStage.show();
		
	}
	
	/**this method get the chosen String in ListView: readAnnouncementList*/
	public String getChosenReadAnnouncement() {
		String announceTimePid=readAnnouncementList.getSelectionModel().getSelectedItem();
		
		return announceTimePid;
	}
	
	/**this method get the chosen String in ListView: readAnnouncementList*/
	public String getChosenUnreadAnnouncement() {
		String announceTimePid=unreadAnnouncementList.getSelectionModel().getSelectedItem();
		
		return announceTimePid;
	}
}
