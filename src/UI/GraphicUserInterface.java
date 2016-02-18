package UI;

import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class GraphicUserInterface extends Application {
	
	private static final String APPLICATION_NAME = "TrackNote";
	
	private static final String BUTTON_SUBMIT_COMMAND = "enter";
	
	private static final String COLOR_HEX_CODE_WHITE = "#ffffff";
	
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_RENAME = "rename";
	private static final String COMMAND_DONE = "done";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_SEARCH = "search";
	
	private static final String UNINITIALIZED_STRING = "";
	
	private static final double WINDOW_MIN_WIDTH = 700.0;
	private static final double WINDOW_MIN_HEIGHT = 450.0;
	private static int PADDING_HORIZONTAL = 10;
	private static int PADDING_VERTICAL = 15;
	private static int SPACING_BETWEEN_COMPONENTS = 10;
	
	private static final List<String> commands = Arrays.asList(UNINITIALIZED_STRING, COMMAND_ADD, COMMAND_EDIT, 
			COMMAND_RENAME, COMMAND_DONE, COMMAND_DELETE, COMMAND_UNDO, COMMAND_SEARCH);
	
	@Override
	public void start(Stage stage) {
		BorderPane frame = new BorderPane();
		Scene scene = new Scene(frame);
			
		frame.setCenter(setEventsContainer());
		frame.setRight(setFloatContainer());
		frame.setBottom(getCommandLineContainer());
		
		// Set and show scene
		stage.setTitle(APPLICATION_NAME);
		stage.setScene(scene);
		stage.show();
		
		stage.setMaximized(true);
		stage.setMinWidth(WINDOW_MIN_WIDTH);
		stage.setMinHeight(WINDOW_MIN_HEIGHT);
	}
	
	private HBox getCommandLineContainer() {
		HBox commandLineContainer = new HBox();
		commandLineContainer.setPadding(new Insets(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL));
		commandLineContainer.setSpacing(SPACING_BETWEEN_COMPONENTS);
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
					case UP:
						if (key.isControlDown()) {
							getPrevCommand(commandLine);
							break;
						}
					case DOWN:
						if (key.isControlDown()) {
							getNextCommand(commandLine);
							break;
						}
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
	
	public void getNextCommand(TextField commandLine) {
		String originalCommand = commandLine.getText();
		String command = originalCommand.trim().toLowerCase();

		int position = commands.lastIndexOf(command);

		if (position == -1 || position == commands.size() - 1) {
			String newCommand = commands.get(0);
			commandLine.setText(newCommand + " ");
		} else {
			String newCommand = commands.get(position + 1);
			commandLine.setText(newCommand + " ");
		}
	}
	
	public void getPrevCommand(TextField commandLine) {
		String originalCommand = commandLine.getText();
		String command = originalCommand.trim().toLowerCase();

		int position = commands.lastIndexOf(command);

		if (position == -1 || position == 0) {
			String newCommand = commands.get(commands.size() - 1);
			commandLine.setText(newCommand + " ");
		} else {
			String newCommand = commands.get(position - 1);
			commandLine.setText(newCommand + " ");
		}
	}
	
	private HBox setEventsContainer() {
		HBox eventsContainer = new HBox();
		eventsContainer.setPadding(new Insets(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL));
		eventsContainer.setSpacing(SPACING_BETWEEN_COMPONENTS);
		eventsContainer.setStyle("-fx-background-color: #26292c;");

		ListView<String> list = new ListView<String>();
		ObservableList<String> items = FXCollections.observableArrayList("Single", "Double", "Suite", "Family App",
				"chocolate", "salmon", "gold", "coral", "darkorchid", "darkgoldenrod", "lightsalmon", "black",
				"rosybrown", "blue", "blueviolet", "brown", "chocolate", "salmon", "gold", "coral", "darkorchid",
				"darkgoldenrod", "lightsalmon", "black", "rosybrown", "blue", "darkgoldenrod", "lightsalmon", "black",
				"rosybrown", "blue", "darkgoldenrod", "lightsalmon", "black", "rosybrown", "blue", "darkgoldenrod",
				"lightsalmon", "black", "rosybrown", "blue", "darkgoldenrod", "lightsalmon", "black", "rosybrown",
				"blue", "darkgoldenrod", "lightsalmon", "black", "rosybrown", "blue", "blueviolet", "brown");
		list.setItems(items);

		HBox.setHgrow(list, Priority.ALWAYS);

		eventsContainer.getChildren().addAll(list);

		return eventsContainer;
	}
	
	private HBox setFloatContainer() {
		HBox floatContainer = new HBox();
		floatContainer.setPadding(new Insets(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL));
		floatContainer.setSpacing(SPACING_BETWEEN_COMPONENTS);
		floatContainer.setStyle("-fx-background-color: #26292c;");

		ListView<String> list = new ListView<String>();

		ObservableList<String> items = FXCollections.observableArrayList("Single", "Double", "Suite");
		list.setItems(items);

		HBox.setHgrow(list, Priority.ALWAYS);

		floatContainer.getChildren().addAll(list);

		return floatContainer;
	}
	
	public static void main(String[] argv) {
		launch(argv);
	}
}
