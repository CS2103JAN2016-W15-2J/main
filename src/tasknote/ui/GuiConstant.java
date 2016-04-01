package tasknote.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class GuiConstant {
    private GuiConstant() {
        // Prevent instantiation of GuiConstant
    }
      
    public static final String COMMAND_ADD = "add";
    public static final String COMMAND_EDIT = "edit";
    public static final String COMMAND_DONE = "done";
    public static final String COMMAND_DELETE = "delete";
    public static final String COMMAND_UNDO = "undo";
    public static final String COMMAND_REDO = "redo";
    public static final String COMMAND_EXIT = "exit";
    public static final String COMMAND_SEARCH = "search";
    public static final String COMMAND_SHOW = "show";
    public static final String COMMAND_RELOCATE = "relocate";
    public static final String COMMAND_HELP = "help";
    public static final String COMMAND_UNDONE = "undone";
    
    public static final String DEFAULT_COMMAND = COMMAND_ADD + " ";
    
    public static final String UNINITIALIZED_STRING = "";
    
    public static final List<String> commands = Collections.unmodifiableList(Arrays.asList(
            UNINITIALIZED_STRING, COMMAND_ADD, COMMAND_EDIT, COMMAND_DONE, 
            COMMAND_DELETE, COMMAND_UNDO, COMMAND_HELP, COMMAND_EXIT));
    
    public static final String PROPERTY_BACKGROUND_COLOR = "-fx-background-color: %1$s;";
    public static final String PROPERTY_BACKGROUND_RADIUS = "-fx-background-radius: %1$d;";
    public static final String PROPERTY_FONT_WEIGHT = "-fx-font-weight: %1$s;";
    public static final String PROPERTY_FONT_SIZE = "-fx-font-size: %1$dpt;";
    public static final String PROPERTY_TEXT_INNER_COLOR = "-fx-text-inner-color: %1$s;";
    public static final String PROPERTY_TEXT_FILL = "-fx-text-fill: %1$s;";
    
    public static final String COLOR_HEX_CODE_WHITE = "#ffffff";
    
    public static int PADDING_HORIZONTAL = 10;
    public static int PADDING_VERTICAL = 15;
    public static int PADDING_REMOVED = 0;
    public static int SPACING_BETWEEN_COMPONENTS = 10;
}
