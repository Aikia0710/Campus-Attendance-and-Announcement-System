package Controller;

import java.io.IOException;

import Server.Protocal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LecturerCreateAnnounceController {
	String courseDetails="";
	
	public LecturerCreateAnnounceController(String cDetail) {
		this.courseDetails=cDetail;
	}
	
	@FXML
	private Text courseDetail;
	@FXML
	private TextArea newAnnouncementContent;
	@FXML
	private Button lecturerBackFromCreateAnnounceButton;
	@FXML
	private Button lecturerSubmitNewAnnounceButton;
	@FXML
	private Button lecturerClearNewAnnounceContentButton;
	
	public void initialize() {
		courseDetail.setText(courseDetails);
	}
	
	public void lecturerSubmitNewAnnounce(ActionEvent event) throws IOException{
		if(!newAnnouncementContent.getText().equals("")) {
			String message=courseDetails+"&"+newAnnouncementContent.getText();
			LoginPageController.client.out.write(Protocal.LECTURER_ADD_NEW_ANNOUNCEMENT+message+Protocal.LECTURER_ADD_NEW_ANNOUNCEMENT+"\n");
			LoginPageController.client.out.flush();
			
			/**TO DO:
			 * turn back to announcement list page automatically*/
			LoginPageController.client.out.write(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER+courseDetail.getText()+Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER+"\n");
			LoginPageController.client.out.flush();
			String[] announcementTimePid=LoginPageController.client.br.readLine().split("&");//{"time pid"}
			String courseName=courseDetail.getText().split(":")[0];
			String courseCid=courseDetail.getText().split(":")[1];
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
		
	}
	
	public void lecturerClearNewAnnounceContent(ActionEvent event) throws IOException{
		newAnnouncementContent.clear();
	}
	
	public void lecturerBackFromCreateAnnounce(ActionEvent event) throws IOException{
		/**TO DO:
		 * turn back to announcement list page*/
//		LoginPageController.client.out.write(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER+courseDetail.getText()+Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER+"\n");
//		LoginPageController.client.out.flush();
//		String[] announcementTimePid=LoginPageController.client.br.readLine().split("&");//{"time pid"}
//		String courseName=courseDetail.getText().split(":")[0];
//		String courseCid=courseDetail.getText().split(":")[1];
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/lecturer_announcementPage.fxml"));
//		Lecturer_AnnouncementPageController controller = new Lecturer_AnnouncementPageController(announcementTimePid,courseName,courseCid);
//		loader.setController(controller);
//		Parent mainPageParent = loader.load();
//		//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
//		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//		Scene mainPageScene = new Scene(mainPageParent);
//		mainStage.setScene(mainPageScene);
//		mainStage.show();
		LoginPageController.client.out.write(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER+courseDetail.getText()+Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER+"\n");
		LoginPageController.client.out.flush();
		String announcementList=LoginPageController.client.br.readLine();//{"time pid"}
		String[] announcementTimePid=null;
		if(announcementList.length()>1) {
			announcementTimePid=announcementList.split("&");
		}
		String courseName=courseDetail.getText().split(":")[0];
		String courseCid=courseDetail.getText().split(":")[1];
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
}
