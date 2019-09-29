package Controller;

import java.io.IOException;

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

public class StudentCreateCommentController {
	private String courseDetail="";
	private String pid="";
	private String announcementTime="";
	
	public StudentCreateCommentController(String courseID,String pID,String announceTime) {
		this.courseDetail=courseID;
		this.pid=pID;
		this.announcementTime=announceTime;
	}
	
	@FXML
	private Text courseNameCid;
	@FXML
	private TextArea commentText;
	@FXML
	private Button clearButton;
	@FXML
	private Button submitCommentButton;
	@FXML
	private Button backToStudentReviewAnnouncementButton;
	
	public void initialize() {
		courseNameCid.setText(courseDetail);
	}
	
	public void backToStudentReviewAnnouncement(ActionEvent event) throws IOException{
		String announcementTimePid=announcementTime+" "+pid;
		LoginPageController.client.out.write(Protocal.STUDENT_REVIEW_AN_ANNOUNCEMENT+announcementTimePid+"&"+courseNameCid.getText().split(":")[1]+Protocal.STUDENT_REVIEW_AN_ANNOUNCEMENT+"\n");
		LoginPageController.client.out.flush();
		
		String announecementDetail=LoginPageController.client.br.readLine();
		
		String commentsStudentUsernameTime=announecementDetail.split("&")[1];
		String announcementContent=announecementDetail.split("&")[0];
		String[] comments=null;
		if(commentsStudentUsernameTime.length()>1) {
			comments=commentsStudentUsernameTime.split("&");
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
	public void clearCommentText(ActionEvent event) throws IOException{
		commentText.clear();
	}
	public void submitComment(ActionEvent event) throws IOException{
		if(!commentText.getText().equals("")) {
			String message=courseDetail.split(":")[1]+"&"+announcementTime+"&"+pid+"&"+commentText.getText();
			LoginPageController.client.out.write(Protocal.STUDENT_CREATE_COMMENT+message+Protocal.STUDENT_CREATE_COMMENT+"\n");
			LoginPageController.client.out.flush();
			System.out.println("create comment");
			String announcementTimePid=announcementTime+" "+pid;
			LoginPageController.client.out.write(Protocal.STUDENT_REVIEW_AN_ANNOUNCEMENT+announcementTimePid+"&"+courseNameCid.getText().split(":")[1]+Protocal.STUDENT_REVIEW_AN_ANNOUNCEMENT+"\n");
			LoginPageController.client.out.flush();
			
			String announecementDetail=LoginPageController.client.br.readLine();
			
			String commentsStudentUsernameTime=announecementDetail.split("&")[1];
			String announcementContent=announecementDetail.split("&")[0];
			String[] comments=null;
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
	

}
