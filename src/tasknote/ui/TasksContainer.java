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
import javafx.util.Callback;
import tasknote.shared.TaskObject;

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
    }
    
    /*
     * Set up the presentation of the (observable) list containing all the tasks.
     */
    private void setTaskListPresentation() {
        _observableListRepresentation.setItems(_tasksList);
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
                        if (!isEmpty()) {
                            switch(task.getTaskStatus()) {
                                case TASK_OUTSTANDING:
                                    this.setTextFill(Color.RED);
                                    break;
                                default:
                                    this.setTextFill(Color.BLACK);
                                    break;
                            }
                            
                            setText(task.toString());
                        }
                    }
                };
            }
        });
    }
}
