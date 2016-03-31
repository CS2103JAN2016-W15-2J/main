package tasknote.shared;

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

// @@ author MunKeat

public class SystemIntegrationTest {
    private static ArrayList<String> fileContent;

    @BeforeClass
    public static void initialise() throws InterruptedException {
        System.out.println("SystemIntegrationTest will thus commence...");
        fileContent = new ArrayList<String>();
        taskContentsFileTransfer();
        
        // TODO 
        Application.launch(GuiController.class, new String[0]);
    }

    private static void taskContentsFileRestore() {
        // Transfer text file to temporary file
        try {
            String taskContentsFilePath;
            BufferedReader taskContentsLocation = new BufferedReader(new FileReader("pathContents.txt"));
            // Get taskContentsFilePath
            taskContentsFilePath = taskContentsLocation.readLine();
            
            if(taskContentsFilePath != null) {
                // Get location
                PrintWriter taskContentsWriter = new PrintWriter(taskContentsFilePath);
                
                for(String content : fileContent) {
                    taskContentsWriter.println(content);
                }

                taskContentsWriter.close();
            }
            
            taskContentsLocation.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void taskContentsFileTransfer() {
        // Transfer text file to temporary file
        try {
            String taskContentsFilePath, taskContents;
            BufferedReader taskContentsLocation = new BufferedReader(new FileReader("pathContents.txt"));
            // Get taskContentsFilePath
            taskContentsFilePath = taskContentsLocation.readLine();
            
            if(taskContentsFilePath != null) {
                // Get location
                BufferedReader taskContentsReader = new BufferedReader(new FileReader(taskContentsFilePath));
                
                taskContents = taskContentsReader.readLine();
                while(taskContents != null) {
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
    
    @Test
    public void testCommandLineBehaviour() {
        // Get all the components here
        TasksContainer events = TasksContainer.getInstance();
        ObservableList<TaskObject> observableEventList = events.getTasksList();
        FloatingTasksContainer floating = FloatingTasksContainer.getInstance();
        ObservableList<TaskObject> observableFloatList = floating.getFloatingTasksList();
        CommandLineContainer commandLineContainer = CommandLineContainer.getInstance();
        TextField commandLine = commandLineContainer.getCommandLine();
        TaskNoteControl logic = new TaskNoteControl();
        Storage storage = new Storage();
        
        commandLine.setText("add floating task 0");
        GuiController.retrieveCommand(commandLine);
        // Check Parser
        TaskObject floatingTask1 = Parser.parseAdd("add floating task 0", true);
        // Check Storage
        try {
            assertTrue(storage.loadTasks().contains(floatingTask1));
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        // Check logic
        assertTrue(logic.getDisplayList().contains(floatingTask1));
        // Check Gui
        assertTrue(observableFloatList.contains(floatingTask1));
        assertTrue(observableEventList.isEmpty());
        
        //TODO
        /*
        commandLine.setText("add event task 1 at 1/1/2016 by 12:34");
        TaskObject eventTask1 = Parser.parseAdd("add event task 1 at 1/1/2016 by 12:34");
        
        try {
            Field enterButtonField = CommandLineContainer.class.getDeclaredField("_enterButton");
            enterButtonField.setAccessible(true);
            Method myMethod = enterButtonField.getClass().getDeclaredMethod("fire", new Class[]{});
            myMethod.invoke(enterButtonField);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        
        // Check Storage
        try {
            assertTrue(storage.loadTasks().contains(eventTask1));
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        // Check logic
        assertTrue(logic.getDisplayList().contains(eventTask1));
        // Check Gui
        assertTrue(observableFloatList.contains(floatingTask1));
        assertTrue(observableEventList.contains(eventTask1));
        */
        
    }
    
    @Test
    public void testAddingOfTask() {
        // Get all the components here
        TasksContainer events = TasksContainer.getInstance();
        ObservableList<TaskObject> observableEventList = events.getTasksList();
        FloatingTasksContainer floating = FloatingTasksContainer.getInstance();
        ObservableList<TaskObject> observableFloatList = floating.getFloatingTasksList();
        TaskNoteControl logic = new TaskNoteControl();
        Storage storage = new Storage();
        
        GuiController.executeCommand("add floating task 0");
        // Check Parser
        TaskObject floatingTask1 = Parser.parseAdd("add floating task 0", true);
        assertTrue(floatingTask1.getTaskType().equals(TaskObject.TASK_TYPE_FLOATING));
        // Check Storage
        try {
            assertTrue(storage.loadTasks().contains(floatingTask1));
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        // Check logic
        assertTrue(logic.getDisplayList().contains(floatingTask1));
        // Check Gui
        assertTrue(observableFloatList.contains(floatingTask1));
        assertTrue(observableFloatList.size() == 1);
        assertTrue(observableEventList.isEmpty());
        
        
        GuiController.executeCommand("add event task 1 on 1/1/2016 by 12:34");
        TaskObject eventTask1 = Parser.parseAdd("add event task 1 on 1/1/2016 by 12:34", true);
        // Check Storage
        try {
            assertTrue(storage.loadTasks().contains(floatingTask1));
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        // Check logic
        assertTrue(logic.getDisplayList().contains(floatingTask1));
        assertTrue(logic.getDisplayList().size() == 2);
        // Check Gui
        assertTrue(observableFloatList.contains(floatingTask1));
        assertTrue(observableFloatList.size() == 1);
        assertTrue(observableEventList.contains(eventTask1));
        assertTrue(observableFloatList.size() == 1);
    }
    
    @Test
    public void testSettingDoneTask() {
     // Get all the components here
        TasksContainer events = TasksContainer.getInstance();
        ObservableList<TaskObject> observableEventList = events.getTasksList();
        FloatingTasksContainer floating = FloatingTasksContainer.getInstance();
        ObservableList<TaskObject> observableFloatList = floating.getFloatingTasksList();
        TaskNoteControl logic = new TaskNoteControl();
        Storage storage = new Storage();
        
        GuiController.executeCommand("add floating task 0");
        GuiController.executeCommand("done 1");
        // Check Parser
        TaskObject floatingTask1 = Parser.parseAdd("add floating task 0", true);
        floatingTask1.setIsMarkedDone(true);
        
        // Check Storage
        try {
            assertTrue(storage.loadTasks().contains(floatingTask1));
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        // Check logic
        assertTrue(logic.getDisplayList().contains(floatingTask1));
        // Check Gui
        assertTrue(observableFloatList.contains(floatingTask1));
        assertTrue(observableFloatList.size() == 1);
        assertTrue(observableEventList.isEmpty());
    }
    
    @Test
    public void testDeletingOfTask() {
        // Get all the components here
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

        for(int count = 5; count >= 0 ; count--) {
            GuiController.executeCommand("delete 1");
            // Check Storage
            try {
                assertTrue(storage.loadTasks().size() == count);
            } catch (IOException | TaskListIOException e) {
                e.printStackTrace();
            }
            // Check logic
            assertTrue(logic.getDisplayList().size() == count);
            // Check GUI
            assertTrue(observableFloatList.size() + observableEventList.size() == count);
        }
    }
    
    @Test
    public void testUndoAndRedoOfTask() {
        // Get all the components here
        TasksContainer events = TasksContainer.getInstance();
        ObservableList<TaskObject> observableEventList = events.getTasksList();
        FloatingTasksContainer floating = FloatingTasksContainer.getInstance();
        ObservableList<TaskObject> observableFloatList = floating.getFloatingTasksList();
        TaskNoteControl logic = new TaskNoteControl();
        Storage storage = new Storage();
        
        GuiController.executeCommand("add floating task 0");
        GuiController.executeCommand("add event task 4 at 1/1/2016 by 12:34");
        // Check Storage
        try {
            assertTrue(storage.loadTasks().size() == 2);
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        // Check logic
        assertTrue(logic.getDisplayList().size() == 2);
        // Check GUI
        assertTrue(observableFloatList.size() + observableEventList.size() == 2);
        
        GuiController.executeCommand("delete 1");
        // Check Storage
        try {
            assertTrue(storage.loadTasks().size() == 1);
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        // Check logic
        assertTrue(logic.getDisplayList().size() == 1);
        // Check GUI
        assertTrue(observableFloatList.size() + observableEventList.size() == 1);
        
        GuiController.executeCommand("undo");
        try {
            assertTrue(storage.loadTasks().size() == 2);
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        // Check logic
        assertTrue(logic.getDisplayList().size() == 2);
        // Check GUI
        assertTrue(observableFloatList.size() + observableEventList.size() == 2);
        
        GuiController.executeCommand("redo");
        try {
            assertTrue(storage.loadTasks().size() == 1);
        } catch (IOException | TaskListIOException e) {
            e.printStackTrace();
        }
        // Check logic
        assertTrue(logic.getDisplayList().size() == 1);
        // Check GUI
        assertTrue(observableFloatList.size() + observableEventList.size() == 1);
    }
    
    @After
    public void clearTaskContents() {
        try {
            BufferedReader taskContentsLocation = new BufferedReader(new FileReader("pathContents.txt"));
            // Get taskContentsFilePath
            String taskContentsFilePath = taskContentsLocation.readLine();
            // Clear file
            PrintWriter pw = new PrintWriter(taskContentsFilePath);
            pw.close();
            taskContentsLocation.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @AfterClass
    public static void restore() {
        System.out.println("SystemIntegrationTest's restore is called...");
        taskContentsFileRestore();
        System.out.println("End of SystemIntegrationTest's testing.");
    }

}
