package tasknote.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class TasksContainer extends HBox {
    private final String PROPERTY_BACKGROUND_COLOR = "-fx-background-color: %1$s;";
    
    private int PADDING_HORIZONTAL = 10;
    private int PADDING_VERTICAL = 15;
    private int SPACING_BETWEEN_COMPONENTS = 10;
    
    private static TasksContainer _tasksContainer = null;
    private ListView<String> _observableListRepresentation = new ListView<String>();
    private ObservableList<String> _tasksList = FXCollections.observableArrayList();
    
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
    
    public ObservableList<String> getTasksList() {
        return _tasksList;
    }
    
    private void setupTasksContainer() {
        setTasksContainerPresentation();
        setTaskListPresentation();
        
        setTaskListBehaviour();
        
        this.getChildren().addAll(_observableListRepresentation);
    }
    
    private void setTasksContainerPresentation() {
        this.setPadding(new Insets(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL));
        this.setSpacing(SPACING_BETWEEN_COMPONENTS);
        this.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#26292c"));
    }
    
    private void setTaskListPresentation() {
        _observableListRepresentation.setItems(_tasksList);
        HBox.setHgrow(_observableListRepresentation, Priority.ALWAYS);
    }
    
    private void setTaskListBehaviour() {
        _observableListRepresentation.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String>param) {
                return new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            if(item.contains("[Overdue]")) 
                                this.setTextFill(Color.RED);
                            setText(item);
                        }
                    }
                };
            }
        });
    }
}
