package UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GraphicUserInterface extends Application {
		
	@Override
	public void start(Stage stage) {
		BorderPane frame = new BorderPane();
		Scene scene = new Scene(frame);
		
		// Set and show scene
		stage.setTitle("TrackNote");
		stage.setScene(scene);
		stage.show();
	}
}
