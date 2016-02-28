package tasknote.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class FloatingTasksContainer extends HBox {
    private final String PROPERTY_BACKGROUND_COLOR = "-fx-background-color: %1$s;";
    
    private int PADDING_HORIZONTAL = 10;
    private int PADDING_VERTICAL = 15;
    private int SPACING_BETWEEN_COMPONENTS = 10;
    
    private static FloatingTasksContainer _floatingTasksContainer = null;
    private ListView<String> _observableListRepresentation = new ListView<String>();
    private ObservableList<String> _floatingTasksList = FXCollections.observableArrayList();
    
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
    
    public ObservableList<String> getFloatingTasksList() {
        return _floatingTasksList;
    }
    
    private void setupFloatingTasksContainer() {
        setFloatingTasksContainerPresentation();
        setFloatingTasksListPresentation();
        
        this.getChildren().addAll(_observableListRepresentation);
    }
    
    private void setFloatingTasksContainerPresentation() {
        this.setPadding(new Insets(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL));
        this.setSpacing(SPACING_BETWEEN_COMPONENTS);
        this.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#26292c"));
    }
    
    private void setFloatingTasksListPresentation() {
        _observableListRepresentation.setItems(_floatingTasksList);
        HBox.setHgrow(_observableListRepresentation, Priority.ALWAYS);
    }
}
