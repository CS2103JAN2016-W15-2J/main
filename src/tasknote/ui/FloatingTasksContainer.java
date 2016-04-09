/** @@author A0129561A */
package tasknote.ui;

import static tasknote.ui.GuiConstant.SPACING_BETWEEN_COMPONENTS;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import tasknote.shared.TaskObject;

public class FloatingTasksContainer extends HBox {
    private static FloatingTasksContainer _floatingTasksContainer = null;
    private ListView<TaskObject> _observableListRepresentation = new ListView<TaskObject>();
    private ObservableList<TaskObject> _floatingTasksList = FXCollections
            .observableArrayList(taskobject -> new Observable[] { taskobject.getObservableTaskStatus() });

    private String CSS_CLASS_TASKS_CONTAINER = "tasks-container";
    private String CSS_CLASS_TASKS_LIST = "tasks-list";
    private String CSS_CLASS_TASKS_LIST_CELL = "tasks-list-cell";

    private FloatingTasksContainer() {
        // Only one instance of FloatingTasksContainer is permitted
    }

    /**
     * getInstance() allows user to get an instance of FloatingTasksContainer.
     * 
     * @return The one instance of FloatingTasksContainer.
     */
    public static FloatingTasksContainer getInstance() {
        if (_floatingTasksContainer == null) {
            _floatingTasksContainer = new FloatingTasksContainer();
            _floatingTasksContainer.setupFloatingTasksContainer();
        }
        return _floatingTasksContainer;
    }

    /**
     * getFloatingTasksList() allows user to get the observable list contained
     * within FloatingTasksContainer.
     * 
     * @return The ObservableList in FloatingTasksContainer.
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
        this.getStyleClass().add(CSS_CLASS_TASKS_CONTAINER);
        this.setSpacing(SPACING_BETWEEN_COMPONENTS);
    }

    /*
     * Set up the presentation of the (observable) list containing all the
     * floating tasks.
     */
    private void setFloatingTasksListPresentation() {
        _observableListRepresentation.getStyleClass().add(CSS_CLASS_TASKS_LIST);
        _observableListRepresentation.setItems(_floatingTasksList);
        HBox.setHgrow(_observableListRepresentation, Priority.ALWAYS);
    }

    private void setFloatListBehaviour() {
        _observableListRepresentation.setCellFactory(new Callback<ListView<TaskObject>, ListCell<TaskObject>>() {
            @Override
            public ListCell<TaskObject> call(ListView<TaskObject> param) {
                return new ListCell<TaskObject>() {
                    @Override
                    public void updateItem(TaskObject task, boolean empty) {
                        super.updateItem(task, empty);
                        this.getStyleClass().add(CSS_CLASS_TASKS_LIST_CELL);
                        if (!isEmpty()) {
                            setGraphic(TasksContainer.getFormattedText(task));
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };
            }
        });
    }
}
