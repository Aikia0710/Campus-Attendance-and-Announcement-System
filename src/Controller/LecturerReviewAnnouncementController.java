package Controller;

import java.io.IOException;

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
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LecturerReviewAnnouncementController {
	private String time="";
	private String courseDetail="";
	private String counter="";
	private String announcementContent="";
	private String[] studentUsernameTimeForComments= {" "};
	private String announcementTimePid="";
	
	public LecturerReviewAnnouncementController(String str1, String str2, String str3, String[] array,String aTP) {
		courseDetail=str1;
		counter=str2;
		announcementContent=str3;
		studentUsernameTimeForComments=array;
		announcementTimePid=aTP;
	}
	
	@FXML
	private Text courseNameCid;
	@FXML
	private Text readerCounter;
	@FXML
	private Text announcementTime;
	@FXML
	private TextArea content;
	@FXML
	private ListView<String> comments;
	@FXML
	private Button openCommentsButton;
	@FXML
	private Button backToLecturerAnnouncementListButton;
	
	public void initialize () {
		courseNameCid.setText(courseDetail);
		readerCounter.setText("ReaderCounter: "+counter);
		announcementTime.setText(time);
		content.setText(announcementContent);
		if(studentUsernameTimeForComments!=null) {
			ObservableList<String> list = FXCollections.observableArrayList(studentUsernameTimeForComments);
			comments.setItems(list);
		}
		
	}
	
	public void openComments(ActionEvent event) throws IOException{
		if(getChosenComments()!=null) {
			LoginPageController.client.out.write(Protocal.OPEN_COMMENT_FOR_LECTURER+courseNameCid.getText().split(":")[1]/*cid*/+"&"+announcementTimePid/*time pid*/+"&"+getChosenComments()/*studentUsername:time*/+Protocal.OPEN_COMMENT_FOR_LECTURER+"\n");
			LoginPageController.client.out.flush();
			
			String content=LoginPageController.client.br.readLine();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/lecturer_openComment.fxml"));
			LecturerOpenCommentController controller = new LecturerOpenCommentController(courseNameCid.getText(),content,getChosenComments(),announcementTimePid);
			loader.setController(controller);
			Parent mainPageParent = loader.load();
			//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
			Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene mainPageScene = new Scene(mainPageParent);
			mainStage.setScene(mainPageScene);
			mainStage.show();
		}
	}
	
	public void backToLecturerAnnouncementList(ActionEvent event) throws IOException{
//		LoginPageController.client.out.write(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER+courseNameCid.getText()+Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER+"\n");
//		LoginPageController.client.out.flush();
//		String[] announcementTimePid=LoginPageController.client.br.readLine().split("&");//{"time pid"}
//		String courseName=courseNameCid.getText().split(":")[0];
//		String courseCid=courseNameCid.getText().split(":")[1];
//		/**TO DO:
//		 * use this String array to fill the ListView in lecturer_announcementPage.fxml
//		 * and turn to the lecturer_announcementPage.fxml*/
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/lecturer_announcementPage.fxml"));
//		Lecturer_AnnouncementPageController controller = new Lecturer_AnnouncementPageController(announcementTimePid,courseName,courseCid);
//		loader.setController(controller);
//		Parent mainPageParent = loader.load();
//		//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
//		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//		Scene mainPageScene = new Scene(mainPageParent);
//		mainStage.setScene(mainPageScene);
//		mainStage.show();
		LoginPageController.client.out.write(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER+courseNameCid.getText()+Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER+"\n");
		LoginPageController.client.out.flush();
		String announcementList=LoginPageController.client.br.readLine();//{"time pid"}
		String[] announcementTimePid=null;
		if(announcementList.length()>1) {
			announcementTimePid=announcementList.split("&");
		}
		String courseName=courseNameCid.getText().split(":")[0];
		String courseCid=courseNameCid.getText().split(":")[1];
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
	
	
	/**this method get the chosen comments*/
	public String getChosenComments() {
		String studentUsernameCommentsTime=comments.getSelectionModel().getSelectedItem();
		
		return studentUsernameCommentsTime;
	}
}
