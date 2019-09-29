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

public class Lecturer_AnnouncementPageController {
	private String[] announces= {};
	private String courseName="";
	private String courseCid="";
	public Lecturer_AnnouncementPageController(String[] announcementList,String cName,String cCid) {
		announces=announcementList;
		courseName=cName;
		courseCid=cCid;
	}
	
	@FXML
	private	Text courseDetail;
	@FXML
	private Button lecturer_addAnnouncementButton;
	@FXML
	private Button lecturer_OpenAnnouncementButton;
	@FXML
	private Button lecturer_backFromAnnouncePage;
	@FXML
	private ListView<String> LecturerAnnouncementList;
	
	public void initialize() {
		if(announces!=null) {
			ObservableList<String> list = FXCollections.observableArrayList(announces);
			LecturerAnnouncementList.setItems(list);
		}
		courseDetail.setText(courseName+":"+courseCid);
	}

	public void lecturerAddAnnouncement(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/lecturer_createAnnouncement.fxml"));

		LecturerCreateAnnounceController controller = new LecturerCreateAnnounceController(courseDetail.getText());
		loader.setController(controller);

		Parent mainPageParent = loader.load();

		//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene mainPageScene = new Scene(mainPageParent);
		mainStage.setScene(mainPageScene);
		mainStage.show();
	}
	
	public void lecturerOpenAnnouncement(ActionEvent event) throws IOException{
		if(getChosenAnnouncement()!=null) {
			String announcementTimePid=getChosenAnnouncement();
			LoginPageController.client.out.write(Protocal.LECTURER_OPEN_AN_ANNOUNCEMENT+announcementTimePid+"&"+courseCid+Protocal.LECTURER_OPEN_AN_ANNOUNCEMENT+"\n");
			LoginPageController.client.out.flush();
			String announecementDetail=LoginPageController.client.br.readLine();
			System.out.println("GET one announcement");
			String readerCounter=announecementDetail.split("&")[0];
			String announcementContent=announecementDetail.split("&")[1];
			String commentsStudentUsernameTime=announecementDetail.split("&")[2];
			String[] comments=null;//={"Llib1905:201402151215"};
			if(commentsStudentUsernameTime.length()>1) {
				comments=commentsStudentUsernameTime.split("#");
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/lecturer_reviewAnnouncement.fxml"));
			LecturerReviewAnnouncementController controller = new LecturerReviewAnnouncementController(courseDetail.getText(),readerCounter,announcementContent,comments,announcementTimePid);
			loader.setController(controller);
			Parent mainPageParent = loader.load();
			//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
			Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene mainPageScene = new Scene(mainPageParent);
			mainStage.setScene(mainPageScene);
			mainStage.show();
		}
	}
	
	public void lecturerBackFromAnnouncePage(ActionEvent event) throws IOException{
		LoginPageController.client.out.write(Protocal.LECTURER_BACK_TO_USER_PROFILE+"\n");
		LoginPageController.client.out.flush();
		ArrayList<String> lecturerCourses=new ArrayList<>();
		String[] courses=LoginPageController.client.br.readLine().split("&");
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
	
	/**this method get the chosen String in ListView: lecturerAnnouncementList*/
	public String getChosenAnnouncement() {
		String announceTimePid=LecturerAnnouncementList.getSelectionModel().getSelectedItem();
		
		return announceTimePid;
	}
}
