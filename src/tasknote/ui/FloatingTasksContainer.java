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

public class FloatingTasksContainer extends HBox {
    private static FloatingTasksContainer _floatingTasksContainer = null;
    private ListView<TaskObject> _observableListRepresentation = new ListView<TaskObject>();
    private ObservableList<TaskObject> _floatingTasksList = FXCollections.observableArrayList();
    
    private FloatingTasksContainer() {
        // Only one instance of FloatingTasksContainer is permitted
    }
    
    /**
     * getInstance() allows user to get an instance of 
     * FloatingTasksContainer.
     * 
     * @return          The one instance of FloatingTasksContainer.
     */
    public static FloatingTasksContainer getInstance() {
        if (_floatingTasksContainer == null) {
            _floatingTasksContainer = new FloatingTasksContainer();
            _floatingTasksContainer.setupFloatingTasksContainer();
        }
        return _floatingTasksContainer;
    }
    
    /**
     * getFloatingTasksList() allows user to get the observable
     * list contained within FloatingTasksContainer.
     * 
     * @return          The ObservableList in FloatingTasksContainer.
     */
    public ObservableList<TaskObject> getFloatingTasksList() {
        return _floatingTasksList;
    }
    
    /*
     * As per name, set up floating tasks container.
     */
    private void setupFloatingTasksContainer() {
        setFloatingTasksContainerPresentation();
        setFloatingTasksListPresentation();
        
        setFloatListBehaviour();
        
        this.getChildren().addAll(_observableListRepresentation);
    }
    
    /*
     * Set up the presentation of the floating tasks container.
     */
    private void setFloatingTasksContainerPresentation() {
        this.setPadding(new Insets(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL));
        this.setSpacing(SPACING_BETWEEN_COMPONENTS);
        this.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#26292c"));
    }
    
    /*
     * Set up the presentation of the (observable) list containing all the
     * floating tasks.
     */
    private void setFloatingTasksListPresentation() {
        _observableListRepresentation.setItems(_floatingTasksList);
        _observableListRepresentation.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#313437"));
        HBox.setHgrow(_observableListRepresentation, Priority.ALWAYS);
    }
    
    private void setFloatListBehaviour() {
        _observableListRepresentation.setCellFactory(new Callback<ListView<TaskObject>, ListCell<TaskObject>>() {
            @Override
            public ListCell<TaskObject> call(ListView<TaskObject>param) {
                return new ListCell<TaskObject>() {
                    @Override
                    public void updateItem(TaskObject task, boolean empty) {
                        super.updateItem(task, empty);
                        setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#313437"));
                        if (!isEmpty()) {
                            switch(task.getTaskStatus()) {
                                case TASK_OUTSTANDING:
                                    this.setTextFill(Color.RED);
                                    break;
                                default:
                                    this.setTextFill(Color.WHITE);
                                    break;
                            }
                            setText(task.formatted());
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
}
