package Controller;

import java.io.IOException;
import java.util.ArrayList;

import Server.Protocal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StudentDoAttendanceController {
	public String course="";
	
	public StudentDoAttendanceController(String courseNC) {
		this.course=courseNC;
	}
	
	@FXML
	private Text coureseNameCid;
	@FXML
	private TextField studentAttendanceText;
	@FXML
	private Button submitButtonStudentAttendance;
	@FXML
	private Button cancelButtonStudentAttendance;
	
	
	@FXML
	private Button ok;
	
	
	public void initialize() {
		coureseNameCid.setText(course);
	}
	
	/**Define methods for controllers in student_doAttendance.fxml*/
	@FXML
	public void submitButtonStudentAttendance(ActionEvent event) throws IOException{
		System.out.println(studentAttendanceText.getText());
		LoginPageController.client.out.write(studentAttendanceText.getText() + "\n" + "\n");
		System.out.println("HERE MAYBE0");
		LoginPageController.client.out.flush();	
		System.out.println("HERE MAYBE1");
		int help = LoginPageController.client.br.read();
		System.out.println(help);
		//TODO for some reason help is always 0 atm despite out.write(1)
		if(help==1) {
			studentAttendanceText.setText("CodeIsRight!");
			LoginPageController.client.out.write(Protocal.STUDENT_BACK_TO_USER_PROFILE + "\n");
			LoginPageController.client.out.flush();
			ArrayList<String> Courses=new ArrayList<>();
			//System.out.println(LoginPageController.client.br.readLine());
			String[] courses = LoginPageController.client.br.readLine().split("&");
			if(courses.length>1) {
				for(int i=1;i<courses.length; i++) {
					String coursenameCid = courses[i];
					Courses.add(coursenameCid);
				}
			}
//			for(String coursenameCid:LoginPageController.client.br.readLine().split("&")) {
//				lecturerCourses.add(coursenameCid);
//			}
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/userProfilePage.fxml"));
			userProfilePageController controller = new userProfilePageController(Courses, LoginPageController.client.getUsername());
			loader.setController(controller);
			Parent mainPageParent = loader.load();
			//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/userProfilePage.fxml"));
			Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene mainPageScene = new Scene(mainPageParent);
			mainStage.setScene(mainPageScene);
			mainStage.show();
			/**TO DO:
			 * the do_attendancePage disappear
			 * there is only the userProfilePage.fxml*/
		}
		else {
			studentAttendanceText.setText("CodeIsWrong!");
		}	
	}
	
	@FXML
	public void cancelButtonStudentAttendance (ActionEvent event) throws IOException{
		studentAttendanceText.clear();
	//	LoginPageController.client.out.write("GIVEUP");
//		LoginPageController.client.out.flush();	
		/**TO DO:
		 * turn back to userProfilePage.fxml
		 * */
		LoginPageController.client.out.write("GIVEUP"+ "\n");
		LoginPageController.client.out.flush();
		
		LoginPageController.client.out.write(Protocal.STUDENT_BACK_TO_USER_PROFILE + "\n");
		LoginPageController.client.out.flush();
		ArrayList<String> Courses=new ArrayList<>();
		//System.out.println(LoginPageController.client.br.readLine());
		String[] courses = LoginPageController.client.br.readLine().split("&");
		if(courses.length>1) {
			for(int i=1;i<courses.length; i++) {
				String coursenameCid = courses[i];
				Courses.add(coursenameCid);
			}
		}
//		for(String coursenameCid:LoginPageController.client.br.readLine().split("&")) {
//			lecturerCourses.add(coursenameCid);
//		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/userProfilePage.fxml"));
		userProfilePageController controller = new userProfilePageController(Courses, LoginPageController.client.getUsername());
		loader.setController(controller);
		Parent mainPageParent = loader.load();
		//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/userProfilePage.fxml"));
		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene mainPageScene = new Scene(mainPageParent);
		mainStage.setScene(mainPageScene);
		mainStage.show();
	}
	
	@FXML
	public void warningClose(){
		/**TO DO:
		 * turn back to userProfilePage.fxml
		 * */
	}
}
