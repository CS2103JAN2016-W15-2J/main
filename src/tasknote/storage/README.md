### README for Storage

## SYNOPSIS
For now, it is about input/output with ArrayList<TaskObject> into String into/from file.

## API
import tasknote.storage.Storage;

# Constructor
public Storage()

# Methods
@return an arraylist of tasklist to logic
@throws IOException (error with reading)
@throws TaskListIOException (file content have error)
public loadTasks() [return ArrayList<TaskObject>]

// write arraylist from logic into file
@throws TaskListIOException (error writing)
public saveTasks(ArrayList<TaskObject> overrideTasks) [void return]
throws TaskListIOException