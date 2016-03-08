package tasknote.ui;

import static tasknote.ui.GuiConstant.PROPERTY_BACKGROUND_COLOR;
import static tasknote.ui.GuiConstant.PROPERTY_FONT_SIZE;

import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

public class MainMenuContainer extends MenuBar {
    private static MainMenuContainer _mainMenuContainer = null;
    
    private MainMenuContainer() {
        // Prevent instantiation of Notification
    }
    
    public static MainMenuContainer getInstance() {
        if (_mainMenuContainer == null) {
            _mainMenuContainer = new MainMenuContainer();
            _mainMenuContainer.setupMeinMenuContainer();
        }

        return _mainMenuContainer;
    }
    
    private void setupMeinMenuContainer() {
        Menu file = new Menu("File");
        file.setStyle("-fx-padding: 0 5 0 5;");
        Menu edit = new Menu("Edit");
        edit.setStyle("-fx-padding: 0 5 0 5;");
        Menu help = new Menu("Help");
        help.setStyle("-fx-padding: 0 5 0 5;");

        this.getMenus().addAll(file, edit, help);
        this.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "white")
                + String.format(PROPERTY_FONT_SIZE, 10));
        this.setPadding(Insets.EMPTY);
    }
}
