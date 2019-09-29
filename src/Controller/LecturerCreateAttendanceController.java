package Controller;

import java.io.IOException;
import java.util.ArrayList;

import Server.CheckClient;
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
//TODO need to make sure that input is correct, i.e check that only 4 digits inputed for year etc.
public class LecturerCreateAttendanceController {
	public String course="";
	
	public LecturerCreateAttendanceController(String courseDetail) {
		course=courseDetail;
	}
	
	/**Define controllers in lecturer_createAttendance.fxml*/
	@FXML
	private Text usernameForCreateAttendance;
	@FXML
	private Text courseDetail;
	@FXML
	private TextField attendanceDate;
	@FXML
	private TextField createAttendanceBeginTime;
	@FXML
	private TextField createAttendanceEndTime;
	@FXML
	private TextField createAttendanceCode;
	@FXML
	private Button cancelCreateAttendance;
	@FXML
	private Button submitCreateAttendance;
	
	public void initialize() {
		courseDetail.setText(course);
	}
	
	/**Define methods for controllers in lecturer_createAttendance.fxml*/
	@FXML
	public void cancelCreateAttendanceButton(ActionEvent event) throws IOException{
		/**TO DO:
		 * turn back to lecturer_attendancePage.fxml remove all text */
		 attendanceDate.setText(null);
		 createAttendanceBeginTime.setText(null);
		 createAttendanceCode.setText(null);
		 createAttendanceEndTime.setText(null);
		 
		 LoginPageController.client.out.write(Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER+courseDetail.getText()+Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER + "\n");
			LoginPageController.client.out.flush();
			ArrayList<String> attendances=new ArrayList<>();
			
			String forme = LoginPageController.client.br.readLine();
			
			//forme = forme.substring(1, forme.length());
			System.out.println(forme);
			for(String attendanceDetails:forme.split("&")) {
				
				attendances.add(attendanceDetails);
			}
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));

			LecturerAttendanceController controller = new LecturerAttendanceController(attendances,courseDetail.getText());
			loader.setController(controller);

			Parent loginPageParent = loader.load();
			
			//Parent loginPageParent = FXMLLoader.load(getClass().getResource("lecturer_createAttendance.fxml"));
			Scene loginPageScene = new Scene(loginPageParent);
			Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			mainStage.setScene(loginPageScene);
			mainStage.show();
		
//		LoginPageController.client.out.write(Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER+course+Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER+"\n");
//		LoginPageController.client.out.flush();
//		ArrayList<String> attendances=new ArrayList<>();
//		
////		System.out.println(LoginPageController.client.br.readLine());			
//		for(String attendanceDetails:LoginPageController.client.br.readLine().split("&")) {
//			
//			attendances.add(attendanceDetails);
//		}
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
//
//		LecturerAttendanceController controller = new LecturerAttendanceController(attendances,course);
//		loader.setController(controller);
//
//		Parent mainPageParent = loader.load();
//
//		//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
//		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//		Scene mainPageScene = new Scene(mainPageParent);
//		mainStage.setScene(mainPageScene);
//		mainStage.show();
	}
	
	@FXML
	public void submitCreateAttendanceButton(ActionEvent event) throws IOException{
//		System.out.println(createAttendanceBeginTime.getText().length());
//		System.out.println(createAttendanceBeginTime.getText().substring(2,3));
//		System.out.println(createAttendanceEndTime.getText().length());
//		System.out.println(createAttendanceEndTime.getText().substring(2,3));
//		System.out.println(createAttendanceCode.getText().length());
//		System.out.println(attendanceDate.getText().length());
//		System.out.println(attendanceDate.getText().substring(4,5));
//		System.out.println(attendanceDate.getText().substring(7,8));
		try {
			if(createAttendanceBeginTime.getText().length()==5 && createAttendanceBeginTime.getText().substring(2, 3).equals(":") && createAttendanceEndTime.getText().length()==5 && createAttendanceEndTime.getText().substring(2, 3).equals(":") && createAttendanceCode.getText().length()==6 && attendanceDate.getText().length()==10 && attendanceDate.getText().substring(4, 5).equals("/") && attendanceDate.getText().substring(7, 8).equals("/")) {
		String beginHour=createAttendanceBeginTime.getText().split(":")[0];
		String beginMinute=createAttendanceBeginTime.getText().split(":")[1];
		String endHour=createAttendanceEndTime .getText().split(":")[0];
		String endMinute=createAttendanceEndTime .getText().split(":")[1];
		String newCode=createAttendanceCode.getText() ;
		String year=attendanceDate.getText().split("/")[0];
		String month=attendanceDate.getText().split("/")[1];
		String day=attendanceDate.getText().split("/")[2];
		
		if(isNumeric(beginHour)&&isNumeric(beginMinute)&&isNumeric(endHour)&&isNumeric(endMinute)&&isNumeric(year)&&isNumeric(month)&&isNumeric(day)
			&&Integer.parseInt(day)<31&&Integer.parseInt(day)>0&&Integer.parseInt(month)>0&&Integer.parseInt(month)<13
			&&Integer.parseInt(year)>1800&&Integer.parseInt(beginHour)>=0&&Integer.parseInt(beginHour)<24
			&&Integer.parseInt(beginMinute)>=0&&Integer.parseInt(beginMinute)<60&&Integer.parseInt(endHour)>=0
			&&Integer.parseInt(endHour)<24&&Integer.parseInt(endMinute)>=0&&Integer.parseInt(endMinute)<60) {
			
			String beginTime=year+month+day+beginHour+beginMinute;
			String endTime=year+month+day+endHour+endMinute;
			
			LoginPageController.client.out.write(Protocal.LECTURER_ADD_NEW_ATTENDENCE+courseDetail.getText()+"&"+beginTime+"&"+endTime+"&"+newCode+Protocal.LECTURER_ADD_NEW_ATTENDENCE + "\n" + "\n");
			LoginPageController.client.out.flush();
		int forme1 = LoginPageController.client.br.read();
		System.out.println(forme1);
			if(forme1==1) {
				/**TO DO:
				 * This lecturer_createAttendance.fxml disappear and the lecturer_attendancePage.fxml shows again*/
				
				LoginPageController.client.out.write(Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER+courseDetail.getText()+Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER+"\n");
				LoginPageController.client.out.flush();
				ArrayList<String> attendances=new ArrayList<>();
				
				String forme = LoginPageController.client.br.readLine();
				forme = forme.substring(1, forme.length());
				System.out.println(forme);
				for(String attendanceDetails:forme.split("&")) {
					
					attendances.add(attendanceDetails);
				}
				
				System.out.println("defo");
				/**TO DO: 
				 * turn to lecturer_attendancePage.fxml and fill the attendances ViewList by attendances
				 * and mark the usernameTexOfAttendance up right*/
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));

				LecturerAttendanceController controller = new LecturerAttendanceController(attendances,courseDetail.getText());
				loader.setController(controller);

				Parent mainPageParent = loader.load();

				//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));
				Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				Scene mainPageScene = new Scene(mainPageParent);
				mainStage.setScene(mainPageScene);
				mainStage.show();
			}
			else {
				createAttendanceCode.setText("This same attendance was created!");
			}
		}
		else {
		 FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/createAttendanceError.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.show();
			stage.setResizable(false);
		}
		}
		
		else {
			//attendanceDate.setText("Your attendance period is invalid!");
			 FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/createAttendanceError.fxml"));
				Parent root1 = (Parent) fxmlLoader.load();
				Stage stage = new Stage();
				stage.setScene(new Scene(root1));
				stage.show();
				stage.setResizable(false);

		}
		}
		catch(Exception e) {

			
			 FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/createAttendanceError.fxml"));
				Parent root1 = (Parent) fxmlLoader.load();
				Stage stage = new Stage();
				stage.setScene(new Scene(root1));
				stage.show();
				stage.setResizable(false);
		}
		
	}
	
	public boolean isNumeric(String str) {
		 for (int i = 0; i < str.length(); i++){ 
			  // System.out.println(str.charAt(i)); 
			   if (!Character.isDigit(str.charAt(i))){ 
			    return false; 
			   } 
			  } 
		 
		 
			  return true; 
	}
public void backButton(ActionEvent event) throws IOException {
		
		LoginPageController.client.out.write(Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER+courseDetail.getText()+Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER + "\n");
		LoginPageController.client.out.flush();
		ArrayList<String> attendances=new ArrayList<>();
		
		String forme = LoginPageController.client.br.readLine();
		
		//forme = forme.substring(1, forme.length());
		System.out.println(forme);
		for(String attendanceDetails:forme.split("&")) {
			
			attendances.add(attendanceDetails);
		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/lecturer_attendancePage.fxml"));

		LecturerAttendanceController controller = new LecturerAttendanceController(attendances,courseDetail.getText());
		loader.setController(controller);

		Parent loginPageParent = loader.load();
		
		//Parent loginPageParent = FXMLLoader.load(getClass().getResource("lecturer_createAttendance.fxml"));
		Scene loginPageScene = new Scene(loginPageParent);
		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		mainStage.setScene(loginPageScene);
		mainStage.show();
	}
}
