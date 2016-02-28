package tasknote.ui;

import java.util.Arrays;
import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class CommandLineContainer extends HBox {
    private final String BUTTON_SUBMIT_COMMAND = "enter";
    
    private final String COMMAND_ADD = "add";
    private final String COMMAND_EDIT = "edit";
    private final String COMMAND_RENAME = "rename";
    private final String COMMAND_DONE = "done";
    private final String COMMAND_DELETE = "delete";
    private final String COMMAND_UNDO = "undo";
    private final String COMMAND_SEARCH = "search";
    
    private final String DEFAULT_COMMAND = COMMAND_ADD + " ";
    
    private final String UNINITIALIZED_STRING = "";
    
    private final List<String> commands = Arrays.asList(UNINITIALIZED_STRING, COMMAND_ADD, COMMAND_EDIT, 
            COMMAND_RENAME, COMMAND_DONE, COMMAND_DELETE, COMMAND_UNDO, COMMAND_SEARCH);
    
    private final int INDEX_FIRST_COMMAND = 0;
    private final int INDEX_LAST_COMMAND = (commands.size() - 1);
    private final int INDEX_MODIFIED_COMMAND = -1;
    
    private String lastModifiedCommand = UNINITIALIZED_STRING;
    
    private final String PROPERTY_BACKGROUND_COLOR = "-fx-background-color: %1$s;";
    private final String PROPERTY_FONT_WEIGHT = "-fx-font-weight: %1$s;";
    private final String PROPERTY_TEXT_INNER_COLOR = "-fx-text-inner-color: %1$s;";
    private final String PROPERTY_TEXT_FILL = "-fx-text-fill: %1$s;";
    
    private final String COLOR_HEX_CODE_WHITE = "#ffffff";
    
    private int PADDING_HORIZONTAL = 10;
    private int PADDING_VERTICAL = 15;
    private int SPACING_BETWEEN_COMPONENTS = 10;

    private static CommandLineContainer _commandLineContainer = null;
    private TextField _commandLine = new TextField();
    private Button _enterButton = new Button();

    
    private CommandLineContainer() {
        // Only one instance of CommandLineContainer is permitted
    }
    
    /**
     * getInstance() allows user to get an instance of 
     * CommandLineContainer.
     * 
     * @return          The one instance of CommandLineContainer.
     */
    public static CommandLineContainer getInstance() {
        if (_commandLineContainer == null) {
            _commandLineContainer = new CommandLineContainer();
            _commandLineContainer.setupCommandLineContainer();
        }
        return _commandLineContainer;
    }
    
    /**
     * getCommandLine() allows user to get the command line
     * contained within CommandLineContainer.
     * 
     * @return          The command line in CommandLineContainer.
     */
    public TextField getCommandLine() {
        return _commandLine;
    }
    
    /**
     * clearLastModifiedCommand() set the last modified
     * command to be an empty string, effectively clearing it.
     */
    public void clearLastModifiedCommand() {
        setLastModifiedCommand(UNINITIALIZED_STRING);
    }
    
    /*
     * As per name, set up the command line container.
     */
    private void setupCommandLineContainer() {
        setCommandLineContainerPresentation();
        setCommandLinePresentation();
        setEnterButtonPresentation();
        
        setCommandLineBehaviour();
        setEnterButtonBehaviour();
        
        this.getChildren().addAll(_commandLine, _enterButton);
    }
    
    /*
     * Set up the presentation of the command line container.
     */
    private void setCommandLineContainerPresentation() {
        this.setPadding(new Insets(PADDING_HORIZONTAL, PADDING_VERTICAL, PADDING_HORIZONTAL, PADDING_VERTICAL));
        this.setSpacing(SPACING_BETWEEN_COMPONENTS);
        this.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#1e2123"));
    }
    
    /*
     * Set up the presentation of the command line itself.
     */
    private void setCommandLinePresentation() {
        _commandLine.setText(DEFAULT_COMMAND);
        _commandLine.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#313437") + String.format(PROPERTY_TEXT_INNER_COLOR, COLOR_HEX_CODE_WHITE));
        HBox.setHgrow(_commandLine, Priority.ALWAYS);
    }
    
    /*
     * Set up the presentation of the "enter" button.
     */
    private void setEnterButtonPresentation() {
        _enterButton.setText(BUTTON_SUBMIT_COMMAND);
        _enterButton.setStyle(String.format(PROPERTY_BACKGROUND_COLOR, "#313437") + String.format(PROPERTY_FONT_WEIGHT, "bold") + String.format(PROPERTY_TEXT_FILL, COLOR_HEX_CODE_WHITE));
        
    }
    
    /*
     * Set up the behaviour of the command line.
     */
    private void setCommandLineBehaviour() {
        _commandLine.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {
                switch (key.getCode()) {
                    case ENTER:
                        GuiController.retrieveCommand(_commandLine);
                        break;
                    case UP:
                        if (key.isControlDown()) {
                            getPrevCommand(_commandLine);
                            break;
                        }
                    case DOWN:
                        if (key.isControlDown()) {
                            getNextCommand(_commandLine);
                            break;
                        }
                    default:
                        break;
                }
            }
        });
    }
    
    /*
     * Set up the behaviour of the enter button.
     */
    private void setEnterButtonBehaviour() {
        _enterButton.setOnAction(e -> GuiController.retrieveCommand(_commandLine));
    }
    
    /*
     * getNextCommand() permits user to cycle through 
     * a list of valid commands, followed by the user's 
     * last edited command.
     */
    private void getNextCommand(TextField commandLine) {
        String originalCommand = commandLine.getText();
        String command = originalCommand.trim().toLowerCase();

        int position = commands.lastIndexOf(command);
        
        if (position == INDEX_MODIFIED_COMMAND) {
            String newCommand = commands.get(0);
            commandLine.setText(newCommand);
            setLastModifiedCommand(originalCommand);
        } else if (position == INDEX_LAST_COMMAND) {
            commandLine.setText(getLastModifiedCommand());
        } else {
            String newCommand = commands.get(position + 1);
            commandLine.setText(newCommand + " ");
        }
        // Set the cursor at the end of the text
        commandLine.end();
    }
    
    /*
     * Similarly, getPrevCommand() permits user to cycle 
     * (in reverse) through the user's last edited command, 
     * followed by a list of valid commands.
     */
    private void getPrevCommand(TextField commandLine) {
        String originalCommand = commandLine.getText();
        String command = originalCommand.trim().toLowerCase();

        int position = commands.lastIndexOf(command);

        if (position == INDEX_MODIFIED_COMMAND) {
            String newCommand = commands.get(INDEX_LAST_COMMAND);
            commandLine.setText(newCommand + " ");
            setLastModifiedCommand(originalCommand);
        } else if (position == INDEX_FIRST_COMMAND && !getLastModifiedCommand().isEmpty()) {
            commandLine.setText(getLastModifiedCommand());
        } else if(position == INDEX_FIRST_COMMAND && getLastModifiedCommand().isEmpty()) {
            String newCommand = commands.get(INDEX_LAST_COMMAND);
            commandLine.setText(newCommand + " ");
        } else {
            String newCommand = (position == 1) ? (commands.get(position - 1)) : (commands.get(position - 1) + " ");
            commandLine.setText(newCommand);
        }
        
        commandLine.end();
    }

    private void setLastModifiedCommand(String commandLine) {
        lastModifiedCommand = commandLine;
    }

    private String getLastModifiedCommand() {
        return lastModifiedCommand;
    }
}
