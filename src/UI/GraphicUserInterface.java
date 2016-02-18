package UI;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class GraphicUserInterface extends Application {
	
	private static String APPLICATION_NAME = "TrackNote";
	
	private static String BUTTON_SUBMIT_COMMAND = "enter";
	
	private static String COLOR_HEX_CODE_WHITE = "#ffffff";
	
	private static int HORIZONTAL_PADDING = 10;
	private static int VERTICAL_PADDING = 15;
	
	@Override
	public void start(Stage stage) {
		BorderPane frame = new BorderPane();
		Scene scene = new Scene(frame);
			
		frame.setBottom(getCommandLineContainer());
		
		// Set and show scene
		stage.setTitle(APPLICATION_NAME);
		stage.setScene(scene);
		stage.show();
		
		stage.setMaximized(true);
	}
	
	private HBox getCommandLineContainer() {
		HBox commandLineContainer = new HBox();
		commandLineContainer.setPadding(new Insets(HORIZONTAL_PADDING, VERTICAL_PADDING, HORIZONTAL_PADDING, VERTICAL_PADDING));
		commandLineContainer.setSpacing(10);
		commandLineContainer.setStyle("-fx-background-color: #1e2123 ;");
		
		TextField commandLine = new TextField();
		commandLine.setStyle("-fx-background-color: #313437; " + "-fx-text-inner-color: #ffffff;");
		HBox.setHgrow(commandLine, Priority.ALWAYS);
		commandLine.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {
				switch (key.getCode()) {
					case ENTER:
						retrieveCommand(commandLine);
						break;
					default:
						break;
				}
			}
		});
		
		Button enterButton = new Button(BUTTON_SUBMIT_COMMAND);
		enterButton.setStyle("-fx-background-color: #313437; " + "-fx-font-weight: bold;" + "-fx-text-fill: #ffffff;");
		enterButton.setOnAction(e -> retrieveCommand(commandLine));
		
		commandLineContainer.getChildren().addAll(commandLine, enterButton);
		
		return commandLineContainer;
	}
	
	private void retrieveCommand(TextField commandLine) {
		// Retrieve command from text field
		String command = commandLine.getText();
		// Ignore if command is empty
		if(command == null || command.isEmpty()) {
			return;
		}
		
		// TODO
		System.out.println(command);
		commandLine.clear();
	}
	
	public static void main(String[] argv) {
		launch(argv);
	}
}
