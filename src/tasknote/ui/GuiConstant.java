/** @@author A0129561A */
package tasknote.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class GuiConstant {
    private GuiConstant() {
        // Prevent instantiation of GuiConstant.
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
    public static final String COMMAND_CATEGORY = "category";
    public static final String COMMAND_HELP = "help";
    public static final String COMMAND_UNDONE = "undone";

    public static final String UNINITIALIZED_STRING = "";
    
    public static final String DEFAULT_COMMAND = UNINITIALIZED_STRING;
    
    public static final String COMMAND_CATEGORY_ALL = "category all";
    public static final String COMMAND_CATEGORY_OUTSTANDING = "category outstanding";
    public static final String COMMAND_CATEGORY_OVERDUE = "category overdue";
    public static final String COMMAND_CATGEORY_COMPLETED = "category completed";

    public static final List<String> commands = Collections.unmodifiableList(Arrays.asList(
            UNINITIALIZED_STRING, COMMAND_ADD, COMMAND_EDIT, COMMAND_DONE, 
            COMMAND_DELETE, COMMAND_UNDO, COMMAND_HELP, COMMAND_EXIT));

    public static final String PROPERTY_BACKGROUND_COLOR = "-fx-background-color: %1$s;";
    public static final String PROPERTY_BACKGROUND_RADIUS = "-fx-background-radius: %1$d;";
    public static final String PROPERTY_FONT_WEIGHT = "-fx-font-weight: %1$s;";
    public static final String PROPERTY_FONT_SIZE = "-fx-font-size: %1$dpt;";
    public static final String PROPERTY_TEXT_INNER_COLOR = "-fx-text-inner-color: %1$s;";
    public static final String PROPERTY_TEXT_FILL = "-fx-text-fill: %1$s;";

    public static int SPACING_BETWEEN_COMPONENTS = 10;
}
