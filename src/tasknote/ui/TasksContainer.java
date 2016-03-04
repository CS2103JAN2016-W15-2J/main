package tasknote.ui;

import static tasknote.ui.GuiConstant.PADDING_HORIZONTAL;
import static tasknote.ui.GuiConstant.PADDING_VERTICAL;
import static tasknote.ui.GuiConstant.PROPERTY_BACKGROUND_COLOR;
import static tasknote.ui.GuiConstant.SPACING_BETWEEN_COMPONENTS;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import tasknote.shared.TaskObject;
import tasknote.shared.TaskObject.TASK_STATUS;

public class TasksContainer extends HBox {
    
    private static TasksContainer _tasksContainer = null;
    private ListView<TaskObject> _observableListRepresentation = new ListView<TaskObject>();
    private ObservableList<TaskObject> _tasksList = FXCollections.observableArrayList();
    
    private TasksContainer() {
        // Only one instance of TasksContainer is permitted
    }
    
    /**
     * getInstance() allows user to get an instance of 
     * TasksContainer.
     * 
     * @return          The one instance of TasksContainer.
     */
    public static TasksContainer getInstance() {
        if (_tasksContainer == null) {
            _tasksContainer = new TasksContainer();
            _tasksContainer.setupTasksContainer();
        }
        return _tasksContainer;
    }
    
    /**
     * getTasksList() allows user to get the observable
     * list contained within TasksContainer.
     * 
     * @return          The ObservableList in TasksContainer.
     */
    public ObservableList<TaskObject> getTasksList() {
        return _tasksList;
    }
    
    /*
     * As per name, set up tasks container.
     */
    private void setupTasksContainer() {
        setTasksContainerPresentation();
        setTaskListPresentation();
        
        setTaskListBehaviour();
        
        this.getChildren().addAll(_observableListRepresentation);
    }
    
    /*
     * Set up the presentation of the tasks container.
     */
    private void setTasksContainerPresentation() {
        this.setPadding(new Insets(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL));
        this.setSpacing(SPACING_BETWEEN_COMPONENTS);
        this.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#26292c"));
        // this.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#26292c") + String.format("-fx-font-family: \"%1$s\";", "Consolas"));
    }
    
    /*
     * Set up the presentation of the (observable) list containing all the tasks.
     */
    private void setTaskListPresentation() {
        _observableListRepresentation.setItems(_tasksList);
        _observableListRepresentation.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#313437"));
        HBox.setHgrow(_observableListRepresentation, Priority.ALWAYS);
    }
    
    /*
     * Set up the behaviour of the (observable) list.
     */
    private void setTaskListBehaviour() {
        _observableListRepresentation.setCellFactory(new Callback<ListView<TaskObject>, ListCell<TaskObject>>() {
            @Override
            public ListCell<TaskObject> call(ListView<TaskObject>param) {
                return new ListCell<TaskObject>() {
                    @Override
                    public void updateItem(TaskObject task, boolean empty) {
                        super.updateItem(task, empty);
                        setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#313437"));
                        if (!isEmpty()) {
                            setGraphic(getFormattedText(task));
                        } else {
                            // Prevent duplicate for a single entry
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };
            }
        });
    }
    
    public static TextFlow getFormattedText(TaskObject task) {
        String newline = System.lineSeparator();
        
        TASK_STATUS taskStatus = task.getTaskStatus();
        Text taskNameValue = new Text(task.getTaskName() + newline);
        Text taskDateTimeValue = null;
        Text taskLocationValue = null;
        
        String taskDate = task.getFormattedDate();
        String taskTime = task.getFormattedTime();
        String taskLocation = task.getLocation();
        
        if(!taskDate.isEmpty() && !taskTime.isEmpty()) {
            taskDateTimeValue = new Text(taskDate + ", " + taskTime + newline);            
        } else if (!taskDate.isEmpty() && taskTime.isEmpty()){
            taskDateTimeValue = new Text(taskDate + newline);  
        }
        
        if(taskLocation == null || !taskLocation.isEmpty()) {
            taskLocationValue = new Text(taskLocation + newline);
        }
        
        return colorise(taskStatus, taskNameValue, taskDateTimeValue, taskLocationValue);
    }
    
    private static TextFlow colorise(TASK_STATUS status, Text taskNameValue, Text taskDateTimeValue, Text taskLocationValue) {
        TextFlow colorisedText = new TextFlow();
        
        switch(status) {
            case TASK_OUTSTANDING:
                taskNameValue.setFill(Color.RED);
                if(taskDateTimeValue != null) {
                    taskDateTimeValue.setFill(Color.RED);
                }
                if(taskLocationValue != null) {
                    taskLocationValue.setFill(Color.RED);
                }
                break;
            case TASK_COMPLETED:
                taskNameValue.setFill(Color.GRAY);
                if(taskDateTimeValue != null) {
                    taskDateTimeValue.setFill(Color.GRAY);
                }
                if(taskLocationValue != null) {
                    taskLocationValue.setFill(Color.GRAY);
                }
                break;
            case TASK_DEFAULT:
            default:
                taskNameValue.setFill(Color.ORANGE);
                if(taskDateTimeValue != null) {
                    taskDateTimeValue.setFill(Color.WHITE);
                }
                if(taskLocationValue != null) {
                    taskLocationValue.setFill(Color.WHITE);
                }
                break;
        }
        
        colorisedText.getChildren().addAll(taskNameValue);
        
        if(taskDateTimeValue != null) {
            colorisedText.getChildren().addAll(taskDateTimeValue);
        } 
        if (taskLocationValue != null) {
            colorisedText.getChildren().addAll(taskLocationValue);
        }
        
        return colorisedText;
    }
}
