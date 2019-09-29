package Controller;

import java.io.IOException;
import java.util.ArrayList;

import Client.Client;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginPageController {
	/**All controllers use the same client object, which has username, password, valid and a socket object*/
	static Client client=new Client();
	
	/**Define controllers in LoginPage.fxml*/
	@FXML
	private TextField usernameInput;
	@FXML
	private PasswordField passwordInput;
	@FXML
	private Button register;
	@FXML
	private Button login;
	@FXML
	private Button clearLogin;
	
	
	/**Define methods for controllers in LoginPage.fxml*/
	@FXML
	public void loginButtonSubmit(ActionEvent event) throws IOException{
		if(!usernameInput.getText().equals("")&&!passwordInput.getText().equals("")) {
			/*set username and passwords in client with what are input in TextField and  PasswordField*/
			client.setUsername(usernameInput.getText());
			client.setPassword(passwordInput.getText()); 
			
			/*combine username and passwords together and send to Server*/
			try {
				client.out.write(client.getUsername()+"&"+client.getPassword()+"\n");
				client.out.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			/*wait for the integer sent from Server and set (int)valid with the integer from client.read()*/
			try {
				client.setValid(client.br.read());
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			
			/*if valid is 2 now, client receives courses' names of LECTURER and stores them in an arraylist*/
			if(client.getValid()==2) {
				ArrayList<String> lecturerCourses=new ArrayList<>();
				String[] courses=client.br.readLine().split("&");
				if(courses.length>1) {
					for(int i=1;i<courses.length;i++) {
						String coursenameCid=courses[i];
						lecturerCourses.add(coursenameCid);
					}
				}
				/**TO DO: turn to userProfilePage.fxml and fill the courses list by lecturerCourses*/
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/userProfilePage.fxml"));

				userProfilePageController controller = new userProfilePageController(lecturerCourses,client.getUsername());
				loader.setController(controller);

				Parent mainPageParent = loader.load();

				//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/userProfilePage.fxml"));
				Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				Scene mainPageScene = new Scene(mainPageParent);
				mainStage.setScene(mainPageScene);
				mainStage.show();
			}
			/*if valid is 1 now, client receives courses' names of STUDENT and stores them in an arraylist*/
			else if(client.getValid()==1) {
				ArrayList<String> studentCourses=new ArrayList<>();
				String[] courses=client.br.readLine().split("&");
				if(courses.length>1) {
					for(int i=1;i<courses.length;i++) {
						String coursenameCid=courses[i];
						studentCourses.add(coursenameCid);
					}
				}
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/userProfilePage.fxml"));
				userProfilePageController controller = new userProfilePageController(studentCourses,client.getUsername());
				loader.setController(controller);

				Parent mainPageParent = loader.load();

				//Parent mainPageParent = FXMLLoader.load(getClass().getResource("/GUI/userProfilePage.fxml"));
				Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				Scene mainPageScene = new Scene(mainPageParent);
				mainStage.setScene(mainPageScene);
				mainStage.show();
				

			}
			/*if valid is 0 now, set the username and password in client object empty and show the LogInWarning.fxml*/
			else if(client.getValid()==0) {
				/*set the username and password in client object empty*/
			    client.setUsername("");
			    client.setPassword("");
			    
			    /**TO DO: show LogInWarning.fxml
			     * but don't make loginPage.fxml disappear*/
			    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/LogInWarning.fxml"));
				Parent root1 = (Parent) fxmlLoader.load();
				Stage stage = new Stage();
				stage.setScene(new Scene(root1));
				stage.show();
				stage.setResizable(false);
			}
			else {
				client.setUsername("");
			    client.setPassword("");
			    
			    /**TO DO: show a fxml to tell user that this account has connected with Server untill now
			     * but don't make loginPage.fxml disappear*/
//			    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/LoginSuccess.fxml"));
//				Parent root1 = (Parent) fxmlLoader.load();
//				Stage stage = new Stage();
//				stage.setScene(new Scene(root1));
//				stage.show();
//				stage.setResizable(false);
			    usernameInput.setText("this account is online now!");
				
		}
			
			
		}
        	
	}
	
	@FXML
	public void deleteInput() {
		usernameInput.setText(null);
		passwordInput.setText(null);
	}

}
