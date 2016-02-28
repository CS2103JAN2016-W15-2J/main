package tasknote.ui;

import java.util.ArrayList;
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
import tasknote.logic.TaskNoteControl;
import tasknote.shared.TaskObject;

public class GuiController extends Application {
	
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
	
	private static final String DEFAULT_COMMAND = COMMAND_ADD + " ";
	
	private static final String UNINITIALIZED_STRING = "";
	
	private static final String PROPERTY_BACKGROUND_COLOR = "-fx-background-color: %1$s;";
	private static final String PROPERTY_FONT_WEIGHT = "-fx-font-weight: %1$s;";
	private static final String PROPERTY_FONT_SIZE = "-fx-font-size: %1$dpx;";
	private static final String PROPERTY_TEXT_INNER_COLOR = "-fx-text-inner-color: %1$s;";
	private static final String PROPERTY_TEXT_FILL = "-fx-text-fill: %1$s;";
	
	private static final double WINDOW_MIN_WIDTH = 700.0;
	private static final double WINDOW_MIN_HEIGHT = 450.0;
	private static int PADDING_HORIZONTAL = 10;
	private static int PADDING_VERTICAL = 15;
	private static int SPACING_BETWEEN_COMPONENTS = 10;
	
	private static final List<String> commands = Arrays.asList(UNINITIALIZED_STRING, COMMAND_ADD, COMMAND_EDIT, 
			COMMAND_RENAME, COMMAND_DONE, COMMAND_DELETE, COMMAND_UNDO, COMMAND_SEARCH);
	
	private static final int INDEX_FIRST_COMMAND = 0;
	private static final int INDEX_LAST_COMMAND = (commands.size() - 1);
	private static final int INDEX_MODIFIED_COMMAND = -1;
	
	private String lastModifiedCommand = UNINITIALIZED_STRING;
	private TextField textFieldToFocusOnStart = null;
	
	private HBox globalEventsContainer;
	private HBox globalFloatsContainer;
	private ObservableList<String> globalEventList;
	
	
	@Override
	public void start(Stage stage) {
		BorderPane frame = new BorderPane();
		Scene scene = new Scene(frame);
		
		// TODO Let's begin upon loading
		TaskNoteControl.loadTasks();
			
		frame.setCenter(setEventsContainer());
		frame.setRight(setFloatContainer());
		frame.setBottom(getCommandLineContainer());
		
		// Set and show scene
		stage.setTitle(APPLICATION_NAME);
		stage.setScene(scene);
		stage.show();
		
		focusOnTextField();
		
		stage.setMaximized(true);
		stage.setMinWidth(WINDOW_MIN_WIDTH);
		stage.setMinHeight(WINDOW_MIN_HEIGHT);
	}
	
	private HBox getCommandLineContainer() {
		HBox commandLineContainer = new HBox();
		commandLineContainer.setPadding(new Insets(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL));
		commandLineContainer.setSpacing(SPACING_BETWEEN_COMPONENTS);
		commandLineContainer.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#1e2123"));
		
		TextField commandLine = new TextField();
		commandLine.setText(DEFAULT_COMMAND);
		commandLine.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#313437") + String.format(PROPERTY_TEXT_INNER_COLOR, COLOR_HEX_CODE_WHITE));
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
		
		textFieldToFocusOnStart = commandLine;
		
		Button enterButton = new Button(BUTTON_SUBMIT_COMMAND);
		enterButton.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#313437") + String.format(PROPERTY_FONT_WEIGHT, "bold") + String.format(PROPERTY_TEXT_FILL, COLOR_HEX_CODE_WHITE));
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
		String feedback = TaskNoteControl.executeCommand(command);
		ArrayList<String> taskList = convertArrayOfTask(TaskNoteControl.getDisplayList());
		globalEventList.setAll(taskList);
		
		
		commandLine.clear();
		commandLine.setText(DEFAULT_COMMAND);
		commandLine.positionCaret(DEFAULT_COMMAND.length() + 1);
		clearLastModifiedCommand();
	}
	
	private boolean focusOnTextField() {
		if (textFieldToFocusOnStart != null) {
			textFieldToFocusOnStart.requestFocus();
			textFieldToFocusOnStart.end();
			return true;
		}
		
		return false;
	}
	
	private void getNextCommand(TextField commandLine) {
		String originalCommand = commandLine.getText();
		String command = originalCommand.trim().toLowerCase();

		int position = commands.lastIndexOf(command);
		
		if (position == INDEX_MODIFIED_COMMAND) {
			String newCommand = commands.get(0);
			commandLine.setText(newCommand);
			setLastModifiedCommand(originalCommand);
		} else if (position == INDEX_LAST_COMMAND) {
			commandLine.setText(getLastModifiedCommand());
		} else {
			String newCommand = commands.get(position + 1);
			commandLine.setText(newCommand + " ");
		}
		
		commandLine.end();
	}
	
	private void getPrevCommand(TextField commandLine) {
		String originalCommand = commandLine.getText();
		String command = originalCommand.trim().toLowerCase();

		int position = commands.lastIndexOf(command);

		if (position == INDEX_MODIFIED_COMMAND) {
			String newCommand = commands.get(INDEX_LAST_COMMAND);
			commandLine.setText(newCommand + " ");
			setLastModifiedCommand(originalCommand);
		} else if (position == INDEX_FIRST_COMMAND && !getLastModifiedCommand().isEmpty()) {
			commandLine.setText(getLastModifiedCommand());
		} else if(position == INDEX_FIRST_COMMAND && getLastModifiedCommand().isEmpty()) {
			String newCommand = commands.get(INDEX_LAST_COMMAND);
			commandLine.setText(newCommand + " ");
		} else {
			String newCommand = (position == 1) ? (commands.get(position - 1)) : (commands.get(position - 1) + " ");
			commandLine.setText(newCommand);
		}
		
		commandLine.end();
	}
	
	private void clearLastModifiedCommand() {
		lastModifiedCommand = UNINITIALIZED_STRING;
	}

	private void setLastModifiedCommand(String commandLine) {
		lastModifiedCommand = commandLine;
	}

	private String getLastModifiedCommand() {
		return lastModifiedCommand;
	}
	
	private HBox setEventsContainer() {
		HBox eventsContainer = new HBox();
		eventsContainer.setPadding(new Insets(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL));
		eventsContainer.setSpacing(SPACING_BETWEEN_COMPONENTS);
		eventsContainer.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#26292c"));

	    // TODO
        ArrayList<String> taskList = convertArrayOfTask(TaskNoteControl.getDisplayList());
		
        ListView<String> list = new ListView<String>();
		ObservableList<String> items = FXCollections.observableArrayList(taskList);
		
		
		list.setItems(items);

		HBox.setHgrow(list, Priority.ALWAYS);

		eventsContainer.getChildren().addAll(list);

		globalEventsContainer = eventsContainer;
		globalEventList = items;
		
		return eventsContainer;
	}
	
	private HBox setFloatContainer() {
		HBox floatContainer = new HBox();
		floatContainer.setPadding(new Insets(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL));
		floatContainer.setSpacing(SPACING_BETWEEN_COMPONENTS);
		floatContainer.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#26292c"));

		ListView<String> list = new ListView<String>();

		ObservableList<String> items = FXCollections.observableArrayList("Single", "Double", "Suite");
		list.setItems(items);

		HBox.setHgrow(list, Priority.ALWAYS);

		floatContainer.getChildren().addAll(list);
		
		globalFloatsContainer = floatContainer;

		return floatContainer;
	}
	
	private ArrayList<String> convertArrayOfTask(ArrayList<TaskObject> taskList) {
	    ArrayList<String> convertedArray = new ArrayList<String>();
	    
	    for(TaskObject to : taskList) {
	        convertedArray.add(to.toString());
	    }
	    
	    return convertedArray;
	}
	
	public static void main(String[] argv) {
		launch(argv);
	}
}