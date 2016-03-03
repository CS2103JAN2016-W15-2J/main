package tasknote.ui;

import static tasknote.ui.GuiConstant.COMMAND_EXIT;
import static tasknote.ui.GuiConstant.COMMAND_UNDO;
import static tasknote.ui.GuiConstant.DEFAULT_COMMAND;
import static tasknote.ui.GuiConstant.PROPERTY_FONT_SIZE;
import static tasknote.ui.GuiConstant.commands;

import java.util.ArrayList;
import java.util.List;

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
    
    private final double WINDOW_MIN_WIDTH = 900.0;
    private final double WINDOW_MIN_HEIGHT = 450.0;
    
    private final static List<String> listOfCommands = new ArrayList<String>(commands);
    
    static {
        // Pre-processing of listOfCommands
        listOfCommands.remove(COMMAND_UNDO);
    }

    private static CommandLineContainer _commandLineContainer = CommandLineContainer.getInstance();
    private static TextField _commandLine = _commandLineContainer.getCommandLine();

    private static TasksContainer _tasksContainer = TasksContainer.getInstance();
    private static ObservableList<TaskObject> _tasksListToBeDisplayed = _tasksContainer.getTasksList();

    private static FloatingTasksContainer _floatingTasksContainer = FloatingTasksContainer.getInstance();
    private static ObservableList<TaskObject> _floatingTasksListToBeDisplayed = _floatingTasksContainer.getFloatingTasksList();
    
    private static SidebarContainer _sidebarContainer = SidebarContainer.getInstance();
    
    private static TaskNoteControl _tasknoteControl = new TaskNoteControl();
    
    @Override
    public void start(Stage stage) {
        BorderPane frame = new BorderPane();
        Scene scene = new Scene(frame);
        
        frame.setStyle(String.format(PROPERTY_FONT_SIZE, 15));
        
        frame.setLeft(_sidebarContainer);
        frame.setCenter(_tasksContainer);
        frame.setRight(_floatingTasksContainer);
        frame.setBottom(_commandLineContainer);
        
        // TODO
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
        
        if(listOfCommands.contains(command.trim())) {
            
            if(command.trim().equals(COMMAND_EXIT)) {
                System.exit(0);
            }
            
            // Commands that contain ONLY (single) keywords 
            // are meaningless, and will not be executed.
            return;
        }
        
        // TODO
        String feedback = _tasknoteControl.executeCommand(command);
        displayUpdatedTaskList();
        
        commandLine.setText(DEFAULT_COMMAND);
        commandLine.end();
        _commandLineContainer.clearLastModifiedCommand();
    }
    
    private void focusOnCommandLine() {
        if (_commandLine != null) {
            _commandLine.requestFocus();
            _commandLine.end();
        }
    }
    
    private static void displayUpdatedTaskList() {
        ArrayList<TaskObject> displayList = _tasknoteControl.getDisplayList();
        ArrayList<TaskObject> tasksList = new ArrayList<TaskObject>();
        ArrayList<TaskObject> floatsList = new ArrayList<TaskObject>();
        
        for(TaskObject task: displayList) {
            if(task.getTaskType().equals("floating")) {
                floatsList.add(task);
            } else {
                tasksList.add(task);
            }
        }
        
        _tasksListToBeDisplayed.setAll(tasksList);
        _floatingTasksListToBeDisplayed.setAll(floatsList);
    }
    
    public static void main(String[] argv) {
        launch(argv);
    }
}