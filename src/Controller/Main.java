package Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
//Llib1905
//lib1908

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("/GUI/LoginPage.fxml"));
			//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("/GUI/userProfilePage.fxml"));
			//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("/GUI/LogInWarning.fxml"));
			//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("/GUI/student_quizPage.fxml"));
			//ScrollPane root = (ScrollPane)FXMLLoader.load(getClass().getResource("/GUI/student_takeQuiz.fxml"));
			//ScrollPane root = (ScrollPane)FXMLLoader.load(getClass().getResource("/GUI/student_reviewQuiz.fxml"));
			//ScrollPane root = (ScrollPane)FXMLLoader.load(getClass().getResource("/GUI/lecturer_createQuiz.fxml"));
			//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("/GUI/student_announcementPage.fxml"));
			
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("/GUI/LoginPage.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			
			//Change the title to our project name
			primaryStage.setTitle("Welcome to Attenda");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}