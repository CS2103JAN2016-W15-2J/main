/** @@author A0129561A */
package tasknote.ui;

import static tasknote.ui.GuiConstant.COMMAND_ADD;
import static tasknote.ui.GuiConstant.COMMAND_REDO;
import static tasknote.ui.GuiConstant.COMMAND_SEARCH;
import static tasknote.ui.GuiConstant.COMMAND_UNDO;
import static tasknote.ui.GuiConstant.DEFAULT_COMMAND;
import static tasknote.ui.GuiConstant.COMMAND_CATEGORY_ALL;
import static tasknote.ui.GuiConstant.COMMAND_CATGEORY_COMPLETED;
import static tasknote.ui.GuiConstant.COMMAND_CATEGORY_OUTSTANDING;
import static tasknote.ui.GuiConstant.COMMAND_CATEGORY_OVERDUE;

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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tasknote.logic.ShowCategory;
import tasknote.logic.TaskNoteControl;
import tasknote.shared.TaskObject;

public class GuiController extends Application {
    private static final Logger logger = Logger.getLogger(GuiController.class.getName());
    private static final String WARNING_ATTEMPT_TO_EXECUTE_INVALID_INPUT = "An invalid input (empty string, or simple \"add\") is passed for command execution.";
    private static TaskNoteControl _tasknoteControl = new TaskNoteControl();

    private final String APPLICATION_NAME = "TaskNote";
    private final String APPLICATION_ICON_PATH = "resources/image/tasknote-icon.png";
    private final String APPLICATION_BACKGROUND_IMAGE = "resources/image/wood-background.png";

    private final String CSS_GUICONTROLLER = "resources/css/theme-wunderlist.css";

    private final double WINDOW_MIN_WIDTH = 450.0;
    private final double WINDOW_MIN_HEIGHT = 450.0;

    private final String SHOW_ALL_COMMAND = "show all";

    @Override
    public void start(Stage stage) {
        BorderPane frame = new BorderPane();
        Scene scene = new Scene(frame);

        setFramePresentation(frame);
        setSceneBehaviour(scene);
        setSceneAndStagePresentation(stage, scene);

        setSidebarNavigationBehaviour();
        changeViewOfSidebarNavigation(SidebarContainer.NAVIGATION_TAG_OUTSTANDING);

        setSchedulerBehaviour();
        focusOnCommandLine();
    }

    private void setFramePresentation(BorderPane frame) {
        CommandLineContainer commandLineContainer = CommandLineContainer.getInstance();
        TasksContainer tasksContainer = TasksContainer.getInstance();
        FloatingTasksContainer floatingTasksContainer = FloatingTasksContainer.getInstance();
        SidebarContainer sidebarContainer = SidebarContainer.getInstance();

        Background background = setBackgroundPresentation();

        frame.setBackground(background);
        frame.setLeft(sidebarContainer);
        frame.setCenter(tasksContainer);
        frame.setRight(floatingTasksContainer);
        frame.setBottom(commandLineContainer);
    }

    private Background setBackgroundPresentation() {
        Image image = new Image(GuiController.class.getResourceAsStream(APPLICATION_BACKGROUND_IMAGE));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        return background;
    }

    private void setSceneBehaviour(Scene scene) {
        CommandLineContainer commandLineContainer = CommandLineContainer.getInstance();
        TextField commandLine = commandLineContainer.getCommandLine();
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
                            commandLineContainer.resetCommandHistoryIndex();
                            commandLine.setText(COMMAND_SEARCH + " ");
                            commandLine.end();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setSceneAndStagePresentation(Stage stage, Scene scene) {
        scene.getStylesheets().add(getClass().getResource(CSS_GUICONTROLLER).toExternalForm());

        stage.setTitle(APPLICATION_NAME);
        stage.setScene(scene);
        stage.getIcons().add(new Image(GuiController.class.getResourceAsStream(APPLICATION_ICON_PATH)));
        stage.show();
        stage.setMaximized(true);
        stage.setMinWidth(WINDOW_MIN_WIDTH);
        stage.setMinHeight(WINDOW_MIN_HEIGHT);
    }

    private void setSchedulerBehaviour() {
        Scheduler schedulerManager = new Scheduler(_tasknoteControl);
        schedulerManager.runOutstandingTaskCheck();
    }

    /**
     * Pass commands to TaskNoteController for command execution based on text input.
     * 
     * @param commandLine Pass the command line which will extract the text for command execution.
     */
    public static void retrieveCommand(TextField commandLine) {
        String command = commandLine.getText();
        CommandLineContainer commandLineContainer = CommandLineContainer.getInstance();

        executeCommand(command);

        commandLineContainer.addCommandHistory(commandLine);
        commandLine.setText(DEFAULT_COMMAND);
        commandLine.end();
        commandLineContainer.clearLastModifiedCommand();
    }

    /**
     * Pass string to TaskNOteController for command execution.
     * 
     * @param command String meant for execution.
     */
    public static void executeCommand(String command) {
        if (command == null || command.trim().equals(COMMAND_ADD) || command.isEmpty()) {
            logger.log(Level.WARNING, WARNING_ATTEMPT_TO_EXECUTE_INVALID_INPUT);
            return;
        }

        Stage primaryWindow = (Stage) CommandLineContainer.getInstance().getScene().getWindow();

        String feedback = _tasknoteControl.executeCommand(command);
        Notification.setupNotification(primaryWindow, feedback);

        ShowCategory category = _tasknoteControl.getDisplayCategory();
        switch(category) {
            case ALL:
                changeViewOfSidebarNavigation(SidebarContainer.NAVIGATION_TAG_VIEW_ALL);
                break;
            case OUTSTANDING:
                changeViewOfSidebarNavigation(SidebarContainer.NAVIGATION_TAG_OUTSTANDING);
                break; 
            case OVERDUE:
                changeViewOfSidebarNavigation(SidebarContainer.NAVIGATION_TAG_OVERDUE);
                break; 
            case COMPLETED:
                changeViewOfSidebarNavigation(SidebarContainer.NAVIGATION_TAG_COMPLETED);
                break; 
            default:
                throw new NotImplementedException();
        }
    }

    /**
     * Pass string to TaskNOteController for command execution,
     * while suppressing notification.
     * 
     * @param command String meant for execution.
     */
    public static void executeCommandWithoutNotification(String command) {
        if (command == null || command.trim().equals(COMMAND_ADD) || command.isEmpty()) {
            logger.log(Level.WARNING, WARNING_ATTEMPT_TO_EXECUTE_INVALID_INPUT);
            return;
        }

        _tasknoteControl.executeCommand(command);
        ShowCategory category = _tasknoteControl.getDisplayCategory();
        switch(category) {
            case ALL:
                changeViewOfSidebarNavigation(SidebarContainer.NAVIGATION_TAG_VIEW_ALL);
                break;
            case OUTSTANDING:
                changeViewOfSidebarNavigation(SidebarContainer.NAVIGATION_TAG_OUTSTANDING);
                break; 
            case OVERDUE:
                changeViewOfSidebarNavigation(SidebarContainer.NAVIGATION_TAG_OVERDUE);
                break; 
            case COMPLETED:
                changeViewOfSidebarNavigation(SidebarContainer.NAVIGATION_TAG_COMPLETED);
                break; 
            default:
                throw new NotImplementedException();
        }
    }
    
    private void focusOnCommandLine() {
        CommandLineContainer commandLineContainer = CommandLineContainer.getInstance();
        TextField commandLine = commandLineContainer.getCommandLine();

        if (commandLine != null) {
            commandLine.requestFocus();
            commandLine.end();
        }
    }

    /*
     * Segregate them based on floating, or non-floating tasks.
     * This method will not filter task(s) based on task status.
     */
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

    private void setSidebarNavigationBehaviour() {
        SidebarContainer sidebarContainer = SidebarContainer.getInstance();
        ListView<String> sidebarNavigation = sidebarContainer.getNavigationList();
        
        sidebarNavigation.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                switch(newSelection) {
                    case SidebarContainer.NAVIGATION_TAG_VIEW_ALL:
                        executeCommandWithoutNotification(COMMAND_CATEGORY_ALL);
                        break;
                    case SidebarContainer.NAVIGATION_TAG_OUTSTANDING:
                        executeCommandWithoutNotification(COMMAND_CATEGORY_OUTSTANDING);
                        break;
                    case SidebarContainer.NAVIGATION_TAG_OVERDUE:
                        sidebarContainer.selectNavigationCell(SidebarContainer.NAVIGATION_TAG_OVERDUE_INDEX);
                        executeCommandWithoutNotification(COMMAND_CATEGORY_OVERDUE);
                        break;
                    case SidebarContainer.NAVIGATION_TAG_COMPLETED:
                        sidebarContainer.selectNavigationCell(SidebarContainer.NAVIGATION_TAG_COMPLETED_INDEX);
                        executeCommandWithoutNotification(COMMAND_CATGEORY_COMPLETED);
                        break;
                }
                changeViewOfSidebarNavigation(newSelection);
            }
        });
    }

    /*
     * Set the selection of the sidebar container, and the corresponding task
     * container (i.e. the appropriate tasks based on task status will be
     * displayed).
     */
    private static void changeViewOfSidebarNavigation(String selected) {
        SidebarContainer sidebarContainer = SidebarContainer.getInstance();

        switch (selected) {
            case SidebarContainer.NAVIGATION_TAG_VIEW_ALL:
                sidebarContainer.selectNavigationCell(SidebarContainer.NAVIGATION_TAG_VIEW_ALL_INDEX);
                displayUpdatedTaskList();
                break;
            case SidebarContainer.NAVIGATION_TAG_OUTSTANDING:
                sidebarContainer.selectNavigationCell(SidebarContainer.NAVIGATION_TAG_OUTSTANDING_INDEX);
                displayUpdatedTaskList();
                break;
            case SidebarContainer.NAVIGATION_TAG_OVERDUE:
                sidebarContainer.selectNavigationCell(SidebarContainer.NAVIGATION_TAG_OVERDUE_INDEX);
                displayUpdatedTaskList();
                break;
            case SidebarContainer.NAVIGATION_TAG_COMPLETED:
                sidebarContainer.selectNavigationCell(SidebarContainer.NAVIGATION_TAG_COMPLETED_INDEX);
                displayUpdatedTaskList();
                break;
        }
    }

    public static void main(String[] argv) {
        launch(argv);
    }
}