//@@author A0129561A
package tasknote.shared;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import tasknote.logic.TaskNoteControl;
import tasknote.parser.Parser;
import tasknote.storage.Storage;
import tasknote.ui.CommandLineContainer;
import tasknote.ui.FloatingTasksContainer;
import tasknote.ui.GuiController;
import tasknote.ui.TasksContainer;

public class SystemIntegrationTest {
    private static ArrayList<String> fileContent;
    
    private static String FILE_PATH_CONTENT = "pathContents.txt";
    
    private static String MESSAGE_INTEGRATION_TEST_BEGINS = "SystemIntegrationTest will thus commence...";
    private static String MESSAGE_INTEGRATION_TEST_RESTORE_ORIGINAL_STORAGE_CONTENT = "SystemIntegrationTest's restore is called...";
    private static String MESSAGE_INTEGRATION_TEST_ENDS = "End of SystemIntegrationTest's testing.";

    @BeforeClass
    public static void initialise() throws InterruptedException {
        System.out.println(MESSAGE_INTEGRATION_TEST_BEGINS);
        fileContent = new ArrayList<String>();
        taskContentsFileTransfer();
 
        Application.launch(GuiController.class, new String[0]);
    }

    /*
     * taskContentsFileTransfer() is called to transfer the contents of the file
     * used by storage to a temporary file, to prevent the actual content from
     * interfering with the testing. At the conclusion of the test, its
     * counterpart, taskContentsFileRestore() will be called to undo the
     * transfer made by taskContentsFileTransfer().
     */
    private static void taskContentsFileTransfer() {
        // Transfer text file to temporary file
        try {
            String taskContentsFilePath, taskContents;
            BufferedReader taskContentsLocation = new BufferedReader(new FileReader(FILE_PATH_CONTENT));
            taskContentsFilePath = taskContentsLocation.readLine();

            if (taskContentsFilePath != null) {
                BufferedReader taskContentsReader = new BufferedReader(new FileReader(taskContentsFilePath));

                taskContents = taskContentsReader.readLine();
                while (taskContents != null) {
                    fileContent.add(taskContents);
                    taskContents = taskContentsReader.readLine();
                }
                taskContentsReader.close();
                // Clear file
                PrintWriter pw = new PrintWriter(taskContentsFilePath);
                pw.close();
            }
            taskContentsLocation.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * taskContentsFileRestore() will restore contents stored in the temp file
     * to the current file that storage is using. This method should be called
     * at the conclusion of the testing.
     */
    private static void taskContentsFileRestore() {
        try {
            String taskContentsFilePath;
            BufferedReader taskContentsLocation = new BufferedReader(new FileReader(FILE_PATH_CONTENT));
            taskContentsFilePath = taskContentsLocation.readLine();

            if (taskContentsFilePath != null) {
                PrintWriter taskContentsWriter = new PrintWriter(taskContentsFilePath);
                for (String content : fileContent) {
                    taskContentsWriter.println(content);
                }
                taskContentsWriter.close();
            }
            taskContentsLocation.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCommandLineBehaviour() {
        TasksContainer events = TasksContainer.getInstance();
        ObservableList<TaskObject> observableEventList = events.getTasksList();
        FloatingTasksContainer floating = FloatingTasksContainer.getInstance();
        ObservableList<TaskObject> observableFloatList = floating.getFloatingTasksList();
        CommandLineContainer commandLineContainer = CommandLineContainer.getInstance();
        TextField commandLine = commandLineContainer.getCommandLine();
        TaskNoteControl logic = new TaskNoteControl();
        Storage storage = new Storage();
        
        String commandAddingFloatingTask = "add floating task 0";

        commandLine.setText(commandAddingFloatingTask);
        GuiController.retrieveCommand(commandLine);
        TaskObject floatingTask1 = Parser.parseAdd(commandAddingFloatingTask, true);
        try {
            assertTrue(storage.loadTasks().contains(floatingTask1));
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        assertTrue(logic.getDisplayList().contains(floatingTask1));
        assertTrue(observableFloatList.contains(floatingTask1));
        assertTrue(observableEventList.isEmpty());
    }

    @Test
    public void testAddingOfTask() {
        TasksContainer events = TasksContainer.getInstance();
        ObservableList<TaskObject> observableEventList = events.getTasksList();
        FloatingTasksContainer floating = FloatingTasksContainer.getInstance();
        ObservableList<TaskObject> observableFloatList = floating.getFloatingTasksList();
        TaskNoteControl logic = new TaskNoteControl();
        Storage storage = new Storage();
        
        String commandAddingFloatingTask = "add floating task 0";
        String commandAddingDeadlineTask = "add deadline task 1 on 1/1/2016 by 12:34";

        // Testing Gui, Logic, Parser and Storage on adding a floating task.
        GuiController.executeCommand(commandAddingFloatingTask);
        TaskObject floatingTask1 = Parser.parseAdd(commandAddingFloatingTask, true);
        assertTrue(floatingTask1.getTaskType().equals(TaskObject.TASK_TYPE_FLOATING));
        try {
            assertTrue(storage.loadTasks().contains(floatingTask1));
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        assertTrue(logic.getDisplayList().contains(floatingTask1));
        assertTrue(observableFloatList.contains(floatingTask1));
        assertTrue(observableFloatList.size() == 1);
        assertTrue(observableEventList.isEmpty());

        // Testing Gui, Logic, Parser and Storage on adding a deadline task.
        GuiController.executeCommand(commandAddingDeadlineTask);
        TaskObject eventTask1 = Parser.parseAdd(commandAddingDeadlineTask, true);
        try {
            assertTrue(storage.loadTasks().contains(floatingTask1));
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        assertTrue(logic.getDisplayList().contains(floatingTask1));
        assertTrue(logic.getDisplayList().size() == 2);
        assertTrue(observableFloatList.contains(floatingTask1));
        assertTrue(observableFloatList.size() == 1);
        assertTrue(observableEventList.contains(eventTask1));
        assertTrue(observableFloatList.size() == 1);
    }
    
    

    @Test
    public void testDeletingOfTask() {
        TasksContainer events = TasksContainer.getInstance();
        ObservableList<TaskObject> observableEventList = events.getTasksList();
        FloatingTasksContainer floating = FloatingTasksContainer.getInstance();
        ObservableList<TaskObject> observableFloatList = floating.getFloatingTasksList();
        TaskNoteControl logic = new TaskNoteControl();
        Storage storage = new Storage();

        GuiController.executeCommand("add floating task 0");
        GuiController.executeCommand("add floating task 1");
        GuiController.executeCommand("add floating task 2");
        GuiController.executeCommand("add floating task 3");
        GuiController.executeCommand("add event task 4 at 1/1/2016 by 12:34");
        GuiController.executeCommand("add event task 5 at 1/1/2016 by 12:35");
        for (int count = 5; count >= 0; count--) {
            GuiController.executeCommand("delete 1");
            try {
                assertTrue(storage.loadTasks().size() == count);
            } catch (IOException | TaskListIOException e) {
                e.printStackTrace();
            }
            assertTrue(logic.getDisplayList().size() == count);
            assertTrue(observableFloatList.size() + observableEventList.size() == count);
        }
    }

    @Test
    public void testUndoAndRedoOfTask() {
        TasksContainer events = TasksContainer.getInstance();
        ObservableList<TaskObject> observableEventList = events.getTasksList();
        FloatingTasksContainer floating = FloatingTasksContainer.getInstance();
        ObservableList<TaskObject> observableFloatList = floating.getFloatingTasksList();
        TaskNoteControl logic = new TaskNoteControl();
        Storage storage = new Storage();

        GuiController.executeCommand("add floating task 0");
        GuiController.executeCommand("add event task 4 at 1/1/2016 by 12:34");
        try {
            assertTrue(storage.loadTasks().size() == 2);
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        assertTrue(logic.getDisplayList().size() == 2);
        assertTrue(observableFloatList.size() + observableEventList.size() == 2);
        GuiController.executeCommand("delete 1");
        try {
            assertTrue(storage.loadTasks().size() == 1);
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        assertTrue(logic.getDisplayList().size() == 1);
        assertTrue(observableFloatList.size() + observableEventList.size() == 1);

        GuiController.executeCommand("undo");
        try {
            assertTrue(storage.loadTasks().size() == 2);
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        assertTrue(logic.getDisplayList().size() == 2);
        //assertTrue()
        assertTrue(observableFloatList.size() + observableEventList.size() == 2);

        GuiController.executeCommand("redo");
        try {
            assertTrue(storage.loadTasks().size() == 1);
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        assertTrue(logic.getDisplayList().size() == 1);
        assertTrue(observableFloatList.size() + observableEventList.size() == 1);
    }

    @Test
    public void testSearchForTask() throws IOException, TaskListIOException {
        TasksContainer events = TasksContainer.getInstance();
        ObservableList<TaskObject> observableEventList = events.getTasksList();
        FloatingTasksContainer floating = FloatingTasksContainer.getInstance();
        ObservableList<TaskObject> observableFloatList = floating.getFloatingTasksList();
        TaskNoteControl logic = new TaskNoteControl();
        
        String searchQuery = "search man";
        // Search should match exact strings, case insensitive strings, and substrings.
        String expectedQueryResultExactMatch = "add Watch man";
        String expectedQueryResultIgnoreCase = "add mAN";
        String expectedQuerySubstring = "add Watch Batman vs Superman: Dawn Of Justice on 1/1/2017 by 3pm at Cathay";
        TaskObject taskObjectExactMatch = Parser.parseAdd(expectedQueryResultExactMatch, true);
        TaskObject taskObjectIgnoreCase = Parser.parseAdd(expectedQueryResultIgnoreCase, true);
        TaskObject taskObjectSubstring = Parser.parseAdd(expectedQuerySubstring, true);
        
        String taskNotContainingQuery = "add irrelevant task";
        String taskQuerySplitByPunctuation = "add M.anderson is coming to town";
        TaskObject taskObjectNotContainingQuery = Parser.parseAdd(taskNotContainingQuery, true);
        TaskObject taskObjectSplitByPunctuation = Parser.parseAdd(taskQuerySplitByPunctuation, true);
        
        ArrayList<TaskObject> searchQueryResult = null;

        GuiController.executeCommand(expectedQueryResultExactMatch);
        GuiController.executeCommand(expectedQueryResultIgnoreCase);
        GuiController.executeCommand(expectedQuerySubstring);
        GuiController.executeCommand(taskNotContainingQuery);
        GuiController.executeCommand(taskQuerySplitByPunctuation);
        
        GuiController.executeCommand(searchQuery);
        
        searchQueryResult = logic.getDisplayList();
        assertTrue(searchQueryResult.contains(taskObjectExactMatch));
        assertTrue(searchQueryResult.contains(taskObjectIgnoreCase));
        assertTrue(searchQueryResult.contains(taskObjectSubstring));
        assertFalse(searchQueryResult.contains(taskObjectNotContainingQuery));
        assertFalse(searchQueryResult.contains(taskObjectSplitByPunctuation));
        
        assertTrue(observableFloatList.size() == 2);
        assertTrue(observableEventList.size() == 1);
    }
    
    @After
    public void clearTaskContents() {
        try {
            BufferedReader taskContentsLocation = new BufferedReader(new FileReader(FILE_PATH_CONTENT));
            String taskContentsFilePath = taskContentsLocation.readLine();
            // Clear file
            PrintWriter pw = new PrintWriter(taskContentsFilePath);
            pw.close();
            taskContentsLocation.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void restore() {
        System.out.println(MESSAGE_INTEGRATION_TEST_RESTORE_ORIGINAL_STORAGE_CONTENT);
        taskContentsFileRestore();
        System.out.println(MESSAGE_INTEGRATION_TEST_ENDS);
    }
}
