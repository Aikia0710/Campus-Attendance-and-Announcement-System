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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StudentReviewAnnouncementController {
	private String time="";
	private String courseDetail="";
	private String announcementContent="";
	private String[] studentUsernameTimeForComments= {" "};
	private String announcementTimePid="";
	
	public StudentReviewAnnouncementController(String str1, String str3, String[] array,String aTP) {
		courseDetail=str1;
		announcementContent=str3;
		studentUsernameTimeForComments=array;
		announcementTimePid=aTP;
		time=aTP.split(" ")[0];
	}
	
	@FXML
	private Text courseNameCid;
	@FXML
	private Text announcementTime;
	@FXML
	private TextArea content;
	@FXML
	private ListView<String> comments;
	@FXML
	private Button openCommentsButton;
	@FXML
	private Button createCommentsButton;
	@FXML
	private Button backToLecturerAnnouncementListButton;
	
	public void initialize () {
		courseNameCid.setText(courseDetail);
		announcementTime.setText(time);
		content.setText(announcementContent);
//		ObservableList<String> list = FXCollections.observableArrayList(studentUsernameTimeForComments);
//		System.out.println(list);
//		comments.setItems(list);
		if(studentUsernameTimeForComments!=null) {
			ObservableList<String> unreadList = FXCollections.observableArrayList(studentUsernameTimeForComments);
			comments.setItems(unreadList);
		}
	}
	
	
	public void openComments(ActionEvent event) throws IOException{
		if(getChosenComments()!=null) {
			
			LoginPageController.client.out.write(Protocal.OPEN_COMMENT_FOR_STUDENT+courseNameCid.getText().split(":")[1]/*cid*/+"&"+announcementTimePid/*time pid*/+"&"+getChosenComments()/*studentUsername:time*/+Protocal.OPEN_COMMENT_FOR_STUDENT+"\n");
			LoginPageController.client.out.flush();
			System.out.println("start builde comment page");			
			String content=LoginPageController.client.br.readLine();
			System.out.println(content);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/student_openComment.fxml"));
			StudentOpenCommentController controller = new StudentOpenCommentController(courseNameCid.getText(),content,getChosenComments(),announcementTimePid);
			System.out.println(courseNameCid.getText()+"#"+content+"#"+getChosenComments()+"#"+announcementTimePid);
			loader.setController(controller);
			Parent mainPageParent = loader.load();
			//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
			Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene mainPageScene = new Scene(mainPageParent);
			mainStage.setScene(mainPageScene);
			mainStage.show();
		}
	}
	
	public void createComments(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/student_createComment.fxml"));
		StudentCreateCommentController controller = new StudentCreateCommentController(courseNameCid.getText(),announcementTimePid.split(" ")[1],time);
		loader.setController(controller);
		Parent mainPageParent = loader.load();
		//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene mainPageScene = new Scene(mainPageParent);
		mainStage.setScene(mainPageScene);
		mainStage.show();
	}
	
	public void bcakToAnnouncementList(ActionEvent event) throws IOException{
		/**TO DO:
		 * turn back to the lecturer announcement list page which was hiden*/
//		LoginPageController.client.out.write(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_STUDENT+courseNameCid.getText()+Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_STUDENT+"\n");
//		LoginPageController.client.out.flush();
//		
//		String[] readAnnouncementTimePid=LoginPageController.client.br.readLine().split("+")[0].split("&");//{"time pid"}
//		String[] unreadAnnouncementTimePid=LoginPageController.client.br.readLine().split("+")[1].split("&");//{"time pid"}
//		String courseName=courseNameCid.getText().split(":")[0];
//		String courseCid=courseNameCid.getText().split(":")[1];
//		
//		
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/student_announcementPage.fxml"));
//		Student_AnnouncementPageController controller = new Student_AnnouncementPageController(courseNameCid.getText(),unreadAnnouncementTimePid,readAnnouncementTimePid);
//		loader.setController(controller);
//		Parent mainPageParent = loader.load();
//		//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
//		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//		Scene mainPageScene = new Scene(mainPageParent);
//		mainStage.setScene(mainPageScene);
//		mainStage.show();
		LoginPageController.client.out.write(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_STUDENT+courseNameCid.getText()+Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_STUDENT+"\n");
		LoginPageController.client.out.flush();
		String allAnnouncementList=LoginPageController.client.br.readLine();
		System.out.println("allAnnouncementList: "+allAnnouncementList);
		String readAnnouncementTimePid=allAnnouncementList.split("#")[0];//{"time pid"}
		String unreadAnnouncementTimePid=allAnnouncementList.split("#")[1];//{"time pid"}
		String[] rATimePid=null;
		String[] urATimePid=null;
		if(readAnnouncementTimePid.length()>1) {
			rATimePid=readAnnouncementTimePid.split("&");
		}
		if(unreadAnnouncementTimePid.length()>1) {
			urATimePid=unreadAnnouncementTimePid.split("&");
		}
		String courseName=courseNameCid.getText().split(":")[0];
		String courseCid=courseNameCid.getText().split(":")[1];
		
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/student_announcementPage.fxml"));
		Student_AnnouncementPageController controller = new Student_AnnouncementPageController(courseNameCid.getText(),urATimePid,rATimePid);
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
