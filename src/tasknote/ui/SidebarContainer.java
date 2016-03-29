package tasknote.ui;

import static tasknote.ui.GuiConstant.SPACING_BETWEEN_COMPONENTS;

import com.pepperonas.fxiconics.FxIconicsLabel;
import com.pepperonas.fxiconics.MaterialColor;
import com.pepperonas.fxiconics.cmd.FxFontCommunity.Icons;
import com.pepperonas.fxiconics.oct.FxFontOcticons;

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
    private Separator _separator = new Separator();
    
    public final String NAVIGATION_TAG_VIEW_ALL = "View All";
    public final String NAVIGATION_TAG_OUTSTANDING = "Outstanding";
    public final String NAVIGATION_TAG_OVERDUE = "Overdue";
    public final String NAVIGATION_TAG_COMPLETED = "Completed";
    public final String NAVIGATION_TAG_SETTINGS = "Settings";
    
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
    
    /**
     * getNavigationList() allows user to get the observable
     * list contained within SidebarContainer.
     * 
     * @return          The ObservableList in SidebarContainer.
     */
    public ListView<String> getNavigationList() {
        return _observableListRepresentation;
    } 
    
    /**
     * This method allows the caller to select the 
     * list item based on the item index.
     */
    public void selectNavigationCell(int index) {
        if(index > _navigationMenu.size()) {
            return;
        } else {
            _observableListRepresentation.getSelectionModel().select(index);
        }
    }

    private void setupSidebarContainer() {
        setSidebarContainerPresentation();
        setNavigationPresentation();
        setSeparatorPresentation();
        setNavigationBehaviour();
        
        this.getChildren().addAll(_clock, _separator, _observableListRepresentation);
    }
    
    private void setSidebarContainerPresentation() {
        this.getStyleClass().add("sidebar-container");
        this.setSpacing(SPACING_BETWEEN_COMPONENTS);
    }
    
    private void setSeparatorPresentation() {
        _separator.setId("sidebar-separator");
    }
    
    private void setNavigationPresentation() {
        _observableListRepresentation.setId("sidebar-navigation");
        _navigationMenu.addAll(NAVIGATION_TAG_VIEW_ALL, NAVIGATION_TAG_OUTSTANDING, NAVIGATION_TAG_OVERDUE, NAVIGATION_TAG_COMPLETED, "", NAVIGATION_TAG_SETTINGS);
        _observableListRepresentation.setItems(_navigationMenu);
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
                        if (!isEmpty() && !value.isEmpty()) {
                            setGraphic(setNavigationCellPresentation(value));
                        } else if (!isEmpty() && value.isEmpty()) {
                            this.setDisable(true);
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
        
        if(!value.isEmpty() && !value.equals(NAVIGATION_TAG_SETTINGS)) {
            Icons icon = getIcon(value);
            FxIconicsLabel nvigationIcon = (FxIconicsLabel) new FxIconicsLabel.Builder(icon).size(24).color(MaterialColor.GREY_500).build();
            Label navigationText = new Label(value);
            navigationText.getStyleClass().add("sidebar-navigation-cell");
            box.getChildren().addAll(nvigationIcon, navigationText);
        } else if (value.equals(NAVIGATION_TAG_SETTINGS)) {
            com.pepperonas.fxiconics.oct.FxFontOcticons.Icons icon = FxFontOcticons.Icons.oct_gear;
            FxIconicsLabel nvigationIcon = (FxIconicsLabel) new FxIconicsLabel.Builder(icon).size(24).color(MaterialColor.GREY_500).build();
            Label navigationText = new Label(value);
            navigationText.getStyleClass().add("sidebar-navigation-cell");
            box.getChildren().addAll(nvigationIcon, navigationText);
        }
        
        return box;
    }
    
    private Icons getIcon(String value) {
        switch(value) {
            case NAVIGATION_TAG_VIEW_ALL:
                return Icons.cmd_home;
            case NAVIGATION_TAG_OUTSTANDING:
                return Icons.cmd_alarm_multiple;
            case NAVIGATION_TAG_OVERDUE:
                return Icons.cmd_comment_alert;
            case NAVIGATION_TAG_COMPLETED:
                return Icons.cmd_checkbox_multiple_marked;
        }
        
        return null;
    }
}
