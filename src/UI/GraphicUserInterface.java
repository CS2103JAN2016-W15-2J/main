package UI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class GraphicUserInterface extends Application {
		
	@Override
	public void start(Stage stage) {
		BorderPane frame = new BorderPane();
		Scene scene = new Scene(frame);
		
		HBox commandLineContainer = new HBox();
		commandLineContainer.setPadding(new Insets(10, 15, 10, 15));
		commandLineContainer.setSpacing(10);
		commandLineContainer.setStyle("-fx-background-color: #1e2123 ;");
		
		TextField commandLine = new TextField();
		commandLine.setStyle("-fx-background-color: #313437; " + "-fx-text-inner-color: #ffffff;");
		HBox.setHgrow(commandLine, Priority.ALWAYS);
		
		Button enterButton = new Button("enter");
		enterButton.setStyle("-fx-background-color: #313437; " + "-fx-font-weight: bold;" + "-fx-text-fill: #ffffff;");
		
		commandLineContainer.getChildren().addAll(commandLine, enterButton);
		frame.setBottom(commandLineContainer);
		
		// Set and show scene
		stage.setTitle("TrackNote");
		stage.setScene(scene);
		stage.show();
	}
}
