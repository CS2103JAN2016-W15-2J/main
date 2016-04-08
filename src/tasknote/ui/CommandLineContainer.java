package tasknote.ui;

import static tasknote.ui.GuiConstant.COMMAND_ADD;
import static tasknote.ui.GuiConstant.COMMAND_DELETE;
import static tasknote.ui.GuiConstant.COMMAND_DONE;
import static tasknote.ui.GuiConstant.COMMAND_EDIT;
import static tasknote.ui.GuiConstant.COMMAND_EXIT;
import static tasknote.ui.GuiConstant.COMMAND_HELP;
import static tasknote.ui.GuiConstant.COMMAND_REDO;
import static tasknote.ui.GuiConstant.COMMAND_RELOCATE;
import static tasknote.ui.GuiConstant.COMMAND_SEARCH;
import static tasknote.ui.GuiConstant.COMMAND_SHOW;
import static tasknote.ui.GuiConstant.COMMAND_UNDO;
import static tasknote.ui.GuiConstant.COMMAND_UNDONE;
import static tasknote.ui.GuiConstant.DEFAULT_COMMAND;
import static tasknote.ui.GuiConstant.SPACING_BETWEEN_COMPONENTS;
import static tasknote.ui.GuiConstant.UNINITIALIZED_STRING;
import static tasknote.ui.GuiConstant.commands;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class CommandLineContainer extends HBox {
    private final String BUTTON_SUBMIT_COMMAND = "enter";
    
    private final int INDEX_FIRST_COMMAND = 0;
    private final int INDEX_LAST_COMMAND = (commands.size() - 1);
    private final int INDEX_MODIFIED_COMMAND = -1;
    
    private String CSS_CLASS_COMMANDLINE_CONTAINER = "commandline-container";
    private String CSS_CLASS_COMMANDLINE_ENTER_BUTTON = "commandline-enter-button";
    private String CSS_CLASS_COMMANDLINE = "commandline";
    
    private String lastModifiedCommand = UNINITIALIZED_STRING;
    private ArrayList<String> historyCommandEntered = new ArrayList<String>(Arrays.asList(""));
    private int historyCommandIndex = 0;

    private static CommandLineContainer _commandLineContainer = null;
    private TextField _commandLine = new TextField();
    private Button _enterButton = new Button();

    private CommandLineContainer() {
        // Only one instance of CommandLineContainer is permitted
    }

    /**
     * getInstance() allows user to get an instance of CommandLineContainer.
     * 
     * @return The one instance of CommandLineContainer.
     */
    public static CommandLineContainer getInstance() {
        if (_commandLineContainer == null) {
            _commandLineContainer = new CommandLineContainer();
            _commandLineContainer.setupCommandLineContainer();
        }
        return _commandLineContainer;
    }

    /**
     * getCommandLine() allows user to get the command line contained within
     * CommandLineContainer.
     * 
     * @return The command line in CommandLineContainer.
     */
    public TextField getCommandLine() {
        return _commandLine;
    }

    /**
     * clearLastModifiedCommand() set the last modified command to be an empty
     * string, effectively clearing it.
     */
    public void clearLastModifiedCommand() {
        setLastModifiedCommand(UNINITIALIZED_STRING);
    }

    /*
     * As per name, set up command line container.
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
        this.getStyleClass().add(CSS_CLASS_COMMANDLINE_CONTAINER);
        this.setSpacing(SPACING_BETWEEN_COMPONENTS);
    }

    /*
     * Set up the presentation of the command line itself.
     */
    private void setCommandLinePresentation() {
        _commandLine.getStyleClass().add(CSS_CLASS_COMMANDLINE);
        _commandLine.setText(DEFAULT_COMMAND);
        HBox.setHgrow(_commandLine, Priority.ALWAYS);
    }

    /*
     * Set up the presentation of the "enter" button.
     */
    private void setEnterButtonPresentation() {
        _enterButton.getStyleClass().add(CSS_CLASS_COMMANDLINE_ENTER_BUTTON);
        _enterButton.setText(BUTTON_SUBMIT_COMMAND);
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
                        resetCommandHistoryIndex();
                        GuiController.retrieveCommand(_commandLine);
                        break;
                    case UP:
                        if (key.isControlDown()) {
                            resetCommandHistoryIndex();
                            getPrevValidCommand(_commandLine);
                        } else {
                            getPrevEnteredCommand(historyCommandEntered, _commandLine);
                            key.consume();
                        }
                        break;
                    case DOWN:
                        if (key.isControlDown()) {
                            resetCommandHistoryIndex();
                            getNextValidCommand(_commandLine);
                        } else {
                            getNextEnteredCommand(historyCommandEntered, _commandLine);
                        }
                        break;
                    case SPACE:
                        resetCommandHistoryIndex();
                        isDefaultCommandTruncated(_commandLine);
                        break;
                    case Z:
                        if (key.isControlDown()) {
                            GuiController.executeCommand(COMMAND_UNDO);
                        } else {
                            resetCommandHistoryIndex();
                        }
                        break;
                    case Y:
                        if (key.isControlDown()) {
                            GuiController.executeCommand(COMMAND_REDO);
                        } else {
                            resetCommandHistoryIndex();
                        }
                        break;
                    default:
                        resetCommandHistoryIndex();
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

    public void resetCommandHistoryIndex() {
        historyCommandIndex = 0;
    }

    public void addCommandHistory(TextField commandLine) {
        String command = commandLine.getText();
        historyCommandEntered.add(command);
    }

    private void getPrevEnteredCommand(ArrayList<String> history, TextField commandLine) {
        int numberOfCommandsEntered = history.size();

        if (historyCommandIndex == 0) {
            historyCommandIndex = (numberOfCommandsEntered - 1);
        } else {
            historyCommandIndex--;
        }

        commandLine.setText(history.get(historyCommandIndex));
        commandLine.end();
    }
    
    private void getNextEnteredCommand(ArrayList<String> history, TextField commandLine) {
        int numberOfCommandsEntered = history.size();
        
        if(historyCommandIndex == (numberOfCommandsEntered - 1)) {
            historyCommandIndex = 0;
        } else {
            historyCommandIndex++;
        }
        
        commandLine.setText(history.get(historyCommandIndex));
        commandLine.end();
    }
    
    /*
     * getNextCommand() permits user to cycle through a list of valid commands,
     * followed by the user's last edited command.
     */
    private void getNextValidCommand(TextField commandLine) {
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
        commandLine.end();
    }
    
    /*
     * Similarly, getPrevCommand() permits user to cycle (in reverse) through
     * the user's last edited command, followed by a list of valid commands.
     */
    private void getPrevValidCommand(TextField commandLine) {
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
    
    private boolean isDefaultCommandTruncated(TextField commandLine) {
        String command = commandLine.getText();
        
        switch(command) {
            case (DEFAULT_COMMAND + COMMAND_ADD):
                commandLine.setText(COMMAND_ADD);
                commandLine.end();
                return true;
            case (DEFAULT_COMMAND + COMMAND_EDIT):
                commandLine.setText(COMMAND_EDIT);
                commandLine.end();
                return true;
            case (DEFAULT_COMMAND + COMMAND_DONE):
                commandLine.setText(COMMAND_DONE);
                commandLine.end();
                return true;
            case (DEFAULT_COMMAND + COMMAND_DELETE):
                commandLine.setText(COMMAND_DELETE);
                commandLine.end();
                return true;
            case (DEFAULT_COMMAND + COMMAND_UNDO):
                commandLine.setText(COMMAND_UNDO);
                commandLine.end();
                return true;
            case (DEFAULT_COMMAND + COMMAND_REDO):
                commandLine.setText(COMMAND_REDO);
                commandLine.end();
                return true;
            case (DEFAULT_COMMAND + COMMAND_SEARCH):
                commandLine.setText(COMMAND_SEARCH);
                commandLine.end();
                return true;
            case (DEFAULT_COMMAND + COMMAND_EXIT):
                commandLine.setText(COMMAND_EXIT);
                commandLine.end();
                return true;
            case (DEFAULT_COMMAND + COMMAND_UNDONE):
                commandLine.setText(COMMAND_UNDONE);
                commandLine.end();
                return true;
            case (DEFAULT_COMMAND + COMMAND_HELP):
                commandLine.setText(COMMAND_HELP);
                commandLine.end();
                return true;
            case (DEFAULT_COMMAND + COMMAND_RELOCATE):
                commandLine.setText(COMMAND_RELOCATE);
                commandLine.end();
                return true;
            case (DEFAULT_COMMAND + COMMAND_SHOW):
                commandLine.setText(COMMAND_SHOW);
                commandLine.end();
                return true;
            default:
                break;
        }
        return false;
    }
}
