package tasknote.ui;

import static tasknote.ui.GuiConstant.SPACING_BETWEEN_COMPONENTS;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class SidebarContainer extends VBox{
    private static SidebarContainer _sidebarContainer = null;
    private ClockContainer _clock = ClockContainer.getInstance();
    private ListView<String> _observableListRepresentation = new ListView<String>();
    private ObservableList<String> _navigationMenu = FXCollections.observableArrayList();
    
    private SidebarContainer() {
        // Only one instance of SidebarContainer is permitted
    }
    
    /**
     * getInstance() allows user to get an instance of 
     * SidebarContainer.
     * 
     * @return          The one instance of SidebarContainer.
     */
    public static SidebarContainer getInstance() {
        if (_sidebarContainer == null) {
            _sidebarContainer = new SidebarContainer();
            _sidebarContainer.setupSidebarContainer();
        }
        return _sidebarContainer;
    }

    private void setupSidebarContainer() {
        setSidebarContainerPresentation();
        setNavigationPresentation();
        setListBehaviour();
        
        this.getChildren().addAll(_clock);
    }
    
    private void setSidebarContainerPresentation() {
        this.getStyleClass().add("sidebar-container");
        this.setSpacing(SPACING_BETWEEN_COMPONENTS);
    }
    
    private void setNavigationPresentation() {
        _navigationMenu.addAll("Outstanding(s)", "Overdue");
        _observableListRepresentation.setItems(_navigationMenu);
        // _observableListRepresentation.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#26292c"));
    }
    
    private void setListBehaviour() {
        _observableListRepresentation.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String>param) {
                return new ListCell<String>() {
                    @Override
                    public void updateItem(String value, boolean empty) {
                        super.updateItem(value, empty);
                        
                        if (!isEmpty()) {
                            VBox box = new VBox();
                            Label nLabel = new Label(value);
                            box.getChildren().addAll(nLabel);
                            setGraphic(box);
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
