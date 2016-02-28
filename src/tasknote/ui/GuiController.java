package tasknote.ui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import tasknote.logic.TaskNoteControl;
import tasknote.shared.TaskObject;

public class GuiController extends Application {
    private final String APPLICATION_NAME = "TrackNote";
    
    private static final String PROPERTY_FONT_SIZE = "-fx-font-size: %1$dpx;";
    
    private final static String COMMAND_ADD = "add";
    private final static String DEFAULT_COMMAND = COMMAND_ADD + " ";
    
    private final double WINDOW_MIN_WIDTH = 700.0;
    private final double WINDOW_MIN_HEIGHT = 450.0;

    private static CommandLineContainer _commandLineContainer = CommandLineContainer.getInstance();
    private static TextField _commandLine = _commandLineContainer.getCommandLine();

    private static TasksContainer _tasksContainer = TasksContainer.getInstance();
    private static ObservableList<TaskObject> _tasksListToBeDisplayed = _tasksContainer.getTasksList();

    private static FloatingTasksContainer _floatingTasksContainer = FloatingTasksContainer.getInstance();
    private static ObservableList<String> _floatingTasksListToBeDisplayed = _floatingTasksContainer.getFloatingTasksList();
    
    @Override
    public void start(Stage stage) {
        BorderPane frame = new BorderPane();
        Scene scene = new Scene(frame);
        
        frame.setStyle(String.format(PROPERTY_FONT_SIZE, 15));
        
        // frame.setLeft(getSideBarContainer());
        frame.setCenter(_tasksContainer);
        frame.setRight(_floatingTasksContainer);
        frame.setBottom(_commandLineContainer);
        
        // TODO
        TaskNoteControl.loadTasks();
        displayUpdatedTaskList();
        
        stage.setTitle(APPLICATION_NAME);
        stage.setScene(scene);
        stage.show();
        
        focusOnCommandLine();
        
        stage.setMaximized(true);
        stage.setMinWidth(WINDOW_MIN_WIDTH);
        stage.setMinHeight(WINDOW_MIN_HEIGHT);
    }
    
    public static void retrieveCommand(TextField commandLine) {
        String command = commandLine.getText();
        
        if(command == null || command.isEmpty()) {
            return;
        }
        
        // TODO
        String feedback = TaskNoteControl.executeCommand(command);
        displayUpdatedTaskList();
        
        commandLine.clear();
        commandLine.setText(DEFAULT_COMMAND);
        commandLine.positionCaret(DEFAULT_COMMAND.length() + 1);
        _commandLineContainer.clearLastModifiedCommand();
    }
    
    private void focusOnCommandLine() {
        if (_commandLine != null) {
            _commandLine.requestFocus();
            _commandLine.end();
        }
    }
    
    private static void displayUpdatedTaskList() {
        _tasksListToBeDisplayed.setAll(TaskNoteControl.getDisplayList());
    }
    
    public static void main(String[] argv) {
        launch(argv);
    }
}