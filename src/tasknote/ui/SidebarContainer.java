package tasknote.ui;

import static tasknote.ui.GuiConstant.PADDING_HORIZONTAL;
import static tasknote.ui.GuiConstant.PADDING_VERTICAL;
import static tasknote.ui.GuiConstant.PROPERTY_BACKGROUND_COLOR;
import static tasknote.ui.GuiConstant.SPACING_BETWEEN_COMPONENTS;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

public class SidebarContainer extends HBox{
    private static SidebarContainer _sidebarContainer = null;
    private ClockContainer _clock = ClockContainer.getInstance();
    
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
        
        this.getChildren().addAll(_clock);
    }
    
    private void setSidebarContainerPresentation() {
        this.setPadding(new Insets(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL));
        this.setSpacing(SPACING_BETWEEN_COMPONENTS);
        this.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#26292c"));
    }
}
