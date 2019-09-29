package Controller;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginPageErrorController {

	public void retry(ActionEvent event) throws IOException {
		Parent loginPageParent = FXMLLoader.load(getClass().getClassLoader().getResource("LoginPage.fxml"));
		Scene loginPageScene = new Scene(loginPageParent);
		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		mainStage.setScene(loginPageScene);
		mainStage.show();
	}
	
}