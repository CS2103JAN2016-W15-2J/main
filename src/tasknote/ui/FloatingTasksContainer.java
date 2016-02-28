package tasknote.ui;

import static tasknote.ui.GuiConstant.PADDING_HORIZONTAL;
import static tasknote.ui.GuiConstant.PADDING_VERTICAL;
import static tasknote.ui.GuiConstant.PROPERTY_BACKGROUND_COLOR;
import static tasknote.ui.GuiConstant.SPACING_BETWEEN_COMPONENTS;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class FloatingTasksContainer extends HBox {
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
    
    /**
     * getFloatingTasksList() allows user to get the observable
     * list contained within FloatingTasksContainer.
     * 
     * @return          The ObservableList in FloatingTasksContainer.
     */
    public ObservableList<String> getFloatingTasksList() {
        return _floatingTasksList;
    }
    
    /*
     * As per name, set up floating tasks container.
     */
    private void setupFloatingTasksContainer() {
        setFloatingTasksContainerPresentation();
        setFloatingTasksListPresentation();
        
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
        HBox.setHgrow(_observableListRepresentation, Priority.ALWAYS);
    }
}
