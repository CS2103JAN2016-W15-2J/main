package tasknote.ui;

import static tasknote.ui.GuiConstant.COMMAND_ADD;
import static tasknote.ui.GuiConstant.COMMAND_REDO;
import static tasknote.ui.GuiConstant.COMMAND_SEARCH;
import static tasknote.ui.GuiConstant.COMMAND_UNDO;
import static tasknote.ui.GuiConstant.DEFAULT_COMMAND;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import tasknote.logic.TaskNoteControl;
import tasknote.shared.TaskObject;
import tasknote.shared.TaskObject.TASK_STATUS;

public class GuiController extends Application {
    private final String APPLICATION_NAME = "TaskNote";
    private final String APPLICATION_ICON_PATH = "resources/image/tasknote-icon.png";
    private final String APPLICATION_BACKGROUND_IMAGE = "resources/image/wood-background.png";
    
    private String CSS_GUICONTROLLER = "resources/css/theme-wunderlist.css";
    
    private final double WINDOW_MIN_WIDTH = 450.0;
    private final double WINDOW_MIN_HEIGHT = 450.0;

    private static TaskNoteControl _tasknoteControl = new TaskNoteControl();
    private static Stage _primaryWindow = null;
    
    private String SHOW_ALL_COMMAND = "show all";
    
    private static final Logger logger = Logger.getLogger(GuiController.class.getName());
    private static final String WARNING_ATTEMPT_TO_EXECUTE_INVALID_INPUT = "An invalid input (empty string, or simple \"add\") is passed for command execution.";
    
    @Override
    public void start(Stage stage) {
        CommandLineContainer _commandLineContainer = CommandLineContainer.getInstance();
        TextField _commandLine = _commandLineContainer.getCommandLine();
        TasksContainer _tasksContainer = TasksContainer.getInstance();
        FloatingTasksContainer _floatingTasksContainer = FloatingTasksContainer.getInstance();
        SidebarContainer _sidebarContainer = SidebarContainer.getInstance();
        
        BorderPane frame = new BorderPane();
        Scene scene = new Scene(frame);
        
        _primaryWindow = stage;
        
        // Scheduler
        Scheduler schedulerManager = new Scheduler(_tasknoteControl);
        schedulerManager.runOutstandingTaskCheck();
        
        scene.getStylesheets().add(getClass().getResource(CSS_GUICONTROLLER).toExternalForm());
        
        frame.setBackground(setBackground());
        frame.setLeft(_sidebarContainer);
        frame.setCenter(_tasksContainer);
        frame.setRight(_floatingTasksContainer);
        frame.setBottom(_commandLineContainer);
        
        setSidebarNavigationBehaviour();
        setStagePresentation(stage, scene);
        setSceneBehaviour(scene);
        
        changeView(SidebarContainer.NAVIGATION_TAG_OUTSTANDING);
        focusOnCommandLine(_commandLine);
    }
    
    private Background setBackground() {
        Image image = new Image(GuiController.class.getResourceAsStream(APPLICATION_BACKGROUND_IMAGE));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        
        return background;
    }

    private void setStagePresentation(Stage stage, Scene scene) {
        stage.setTitle(APPLICATION_NAME);
        stage.setScene(scene);
        stage.getIcons().add(new Image(GuiController.class.getResourceAsStream(APPLICATION_ICON_PATH)));
        stage.show();
        stage.setMaximized(true);
        stage.setMinWidth(WINDOW_MIN_WIDTH);
        stage.setMinHeight(WINDOW_MIN_HEIGHT);
    }
    
    private void setSceneBehaviour(Scene scene) {
        CommandLineContainer _commandLineContainer = CommandLineContainer.getInstance();
        TextField _commandLine = _commandLineContainer.getCommandLine();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {
                switch (key.getCode()) {
                    case ESCAPE:
                        executeCommand(SHOW_ALL_COMMAND);
                        break;
                    case Z:
                        if (key.isControlDown()) {
                            executeCommand(COMMAND_UNDO);
                        }
                        break;
                    case Y:
                        if (key.isControlDown()) {
                            executeCommand(COMMAND_REDO);
                        }
                        break;
                    case F:
                        if (key.isControlDown()) {
                            _commandLineContainer.resetCommandHistoryIndex();
                            _commandLine.setText(COMMAND_SEARCH + " ");
                            _commandLine.end();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public static void retrieveCommand(TextField commandLine) {
        String command = commandLine.getText();
        CommandLineContainer commandLineContainer = CommandLineContainer.getInstance();

        executeCommand(command);
        
        commandLineContainer.addCommandHistory(commandLine);

        commandLine.setText(DEFAULT_COMMAND);
        commandLine.end();
        CommandLineContainer.getInstance().clearLastModifiedCommand();
    }

    public static void executeCommand(String command) {
        if (command == null || command.trim().equals(COMMAND_ADD) || command.isEmpty()) {
            logger.log(Level.WARNING, WARNING_ATTEMPT_TO_EXECUTE_INVALID_INPUT);
            return;
        }

        String feedback = _tasknoteControl.executeCommand(command);
        Notification.setupNotification(_primaryWindow, feedback);

        changeView(SidebarContainer.NAVIGATION_TAG_VIEW_ALL);
    }

    private void focusOnCommandLine(TextField commandLine) {
        if (commandLine != null) {
            commandLine.requestFocus();
            commandLine.end();
        }
    }

    private static void displayUpdatedTaskList() {
        TasksContainer tasksContainer = TasksContainer.getInstance();
        ObservableList<TaskObject> tasksListToBeDisplayed = tasksContainer.getTasksList();
        FloatingTasksContainer floatingTasksContainer = FloatingTasksContainer.getInstance();
        ObservableList<TaskObject> floatingTasksListToBeDisplayed = floatingTasksContainer.getFloatingTasksList();

        ArrayList<TaskObject> displayList = _tasknoteControl.getDisplayList();

        for (int index = 0; index < displayList.size(); index++) {
            displayList.get(index).setTaskID(index + 1);
        }

        ArrayList<TaskObject> tasksList = new ArrayList<TaskObject>();
        ArrayList<TaskObject> floatsList = new ArrayList<TaskObject>();

        for (TaskObject task : displayList) {
            switch (task.getTaskType()) {
                case TaskObject.TASK_TYPE_FLOATING:
                    floatsList.add(task);
                    break;
                case TaskObject.TASK_TYPE_DEADLINE:
                case TaskObject.TASK_TYPE_EVENT:
                    tasksList.add(task);
                    break;
                default:
                    break;
            }
        }

        tasksListToBeDisplayed.setAll(tasksList);
        floatingTasksListToBeDisplayed.setAll(floatsList);
    }

    private static void displayTaskList(String navigationTag) {
        ArrayList<TaskObject> displayList = _tasknoteControl.getDisplayList();
        TasksContainer tasksContainer = TasksContainer.getInstance();
        ObservableList<TaskObject> tasksListToBeDisplayed = tasksContainer.getTasksList();
        FloatingTasksContainer floatingTasksContainer = FloatingTasksContainer.getInstance();
        ObservableList<TaskObject> floatingTasksListToBeDisplayed = floatingTasksContainer.getFloatingTasksList();

        for (int index = 0; index < displayList.size(); index++) {
            displayList.get(index).setTaskID(index + 1);
        }

        ArrayList<TaskObject> tasksList = new ArrayList<TaskObject>();
        ArrayList<TaskObject> floatsList = new ArrayList<TaskObject>();

        TASK_STATUS taskTypeRequired = null;

        switch (navigationTag) {
            case SidebarContainer.NAVIGATION_TAG_OUTSTANDING:
                taskTypeRequired = TASK_STATUS.TASK_DEFAULT;
                break;
            case SidebarContainer.NAVIGATION_TAG_OVERDUE:
                taskTypeRequired = TASK_STATUS.TASK_OUTSTANDING;
                break;
            case SidebarContainer.NAVIGATION_TAG_COMPLETED:
                taskTypeRequired = TASK_STATUS.TASK_COMPLETED;
        }

        for (TaskObject task : displayList) {
            if (task.getTaskStatus() != taskTypeRequired) {
                continue;
            }

            switch (task.getTaskType()) {
                case TaskObject.TASK_TYPE_FLOATING:
                    floatsList.add(task);
                    break;
                case TaskObject.TASK_TYPE_DEADLINE:
                case TaskObject.TASK_TYPE_EVENT:
                    tasksList.add(task);
                    break;
                default:
                    break;
            }
        }

        tasksListToBeDisplayed.setAll(tasksList);
        floatingTasksListToBeDisplayed.setAll(floatsList);
    }

    private void setSidebarNavigationBehaviour() {
        SidebarContainer sidebarContainer = SidebarContainer.getInstance();
        ListView<String> sidebarNavigation = sidebarContainer.getNavigationList();

        sidebarNavigation.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                changeView(newSelection);
            }
        });
    }

    private static void changeView(String selected) {
        FloatingTasksContainer floatingTasksContainer = FloatingTasksContainer.getInstance();
        SidebarContainer sidebarContainer = SidebarContainer.getInstance();

        if (selected.equals(SidebarContainer.NAVIGATION_TAG_OVERDUE)) {
            floatingTasksContainer.setVisible(false);
            floatingTasksContainer.setManaged(false);
        } else {
            floatingTasksContainer.setVisible(true);
            floatingTasksContainer.setManaged(true);
        }

        switch (selected) {
            case SidebarContainer.NAVIGATION_TAG_VIEW_ALL:
                sidebarContainer.selectNavigationCell(0);
                displayUpdatedTaskList();
                break;
            case SidebarContainer.NAVIGATION_TAG_OUTSTANDING:
                sidebarContainer.selectNavigationCell(1);
                displayTaskList(selected);
                break;
            case SidebarContainer.NAVIGATION_TAG_OVERDUE:
                sidebarContainer.selectNavigationCell(2);
                displayTaskList(selected);
                break;
            case SidebarContainer.NAVIGATION_TAG_COMPLETED:
                sidebarContainer.selectNavigationCell(3);
                displayTaskList(selected);
                break;
        }
    }
    
    public static void main(String[] argv) {
        launch(argv);
    }
}