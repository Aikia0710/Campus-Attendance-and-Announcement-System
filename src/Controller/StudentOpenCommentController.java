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

public class StudentOpenCommentController {
	private String courseDetail="";
	private String content="";
	private String studentUsernameTime="";
	private String aTimePid="";
	
	
	public StudentOpenCommentController(String s1,String s2,String s3,String s4) {
		courseDetail=s1;
		content=s2;
		studentUsernameTime=s3;
		aTimePid=s4;
	}
	
	@FXML
	private	Text courseNameCid;
	@FXML
	private	Text studentNameCommentsTime;
	@FXML
	private	TextArea commentsContent;
	@FXML
	private Button backToAnnouncementContentPageButton;
	
	public void initialize() {
		courseNameCid.setText(courseDetail);
		studentNameCommentsTime.setText(studentUsernameTime);
		commentsContent.setText(content);
	}
	
	public void backToAnnouncementContentPage(ActionEvent event) throws IOException{
		String announcementTimePid=aTimePid;
		LoginPageController.client.out.write(Protocal.STUDENT_REVIEW_AN_ANNOUNCEMENT+announcementTimePid+"&"+courseNameCid.getText().split(":")[1]+Protocal.STUDENT_REVIEW_AN_ANNOUNCEMENT+"\n");
		LoginPageController.client.out.flush();
		
		String announecementDetail=LoginPageController.client.br.readLine();
		System.out.println("aD: "+announecementDetail);
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
