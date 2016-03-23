package tasknote.ui;

import static tasknote.ui.GuiConstant.SPACING_BETWEEN_COMPONENTS;

import com.pepperonas.fxiconics.FxIconicsLabel;
import com.pepperonas.fxiconics.MaterialColor;
import com.pepperonas.fxiconics.cmd.FxFontCommunity.Icons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
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
        setNavigationBehaviour();
        
        Separator s = new Separator();
        s.setPadding(new Insets(10, 0, 0, 0));
        
        this.getChildren().addAll(_clock, s, _observableListRepresentation);
    }
    
    private void setSidebarContainerPresentation() {
        this.getStyleClass().add("sidebar-container");
        this.setSpacing(SPACING_BETWEEN_COMPONENTS);
    }
    
    private void setNavigationPresentation() {
        _observableListRepresentation.setId("sidebar-navigation");
        _navigationMenu.addAll("Outstanding", "Overdue", "Completed");
        _observableListRepresentation.setItems(_navigationMenu);
        
        // _observableListRepresentation.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#26292c"));
    }
    
    private void setNavigationBehaviour() {
        _observableListRepresentation.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String>param) {
                return new ListCell<String>() {
                    @Override
                    public void updateItem(String value, boolean empty) {
                        super.updateItem(value, empty);
                        this.getStyleClass().add("sidebar-container");
                        if (!isEmpty()) {
                            setGraphic(setNavigationCellPresentation(value));
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
    
    private HBox setNavigationCellPresentation(String value) {
        HBox box = new HBox();
        box.setPadding(new Insets(0, 0, 0, 20));
        box.setSpacing(20);
        Icons icon = getIcon(value);
        FxIconicsLabel btnDefault = (FxIconicsLabel) new FxIconicsLabel.Builder(icon).size(24).color(MaterialColor.GREY_500).build();
        Label nLabel = new Label(value);
        // TODO
        nLabel.getStyleClass().add("notification-title");
        box.getChildren().addAll(btnDefault, nLabel);
        
        return box;
    }
    
    private Icons getIcon(String value) {
        switch(value) {
            case "Outstanding":
                return Icons.cmd_alarm_multiple;
            case "Overdue":
                return Icons.cmd_comment_alert;
            case "Completed":
                return Icons.cmd_checkbox_multiple_marked;
        }
        
        return null;
    }
}
