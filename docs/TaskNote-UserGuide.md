## TaskNote User Guide
----------------------
TaskNote is a desktop application that keeps track of your task. In this user guide, we will delve into the commands that TaskNote uses, so that you can get up to speed with using TaskNote.

You may notice that the content page is somewhat long, so do use the [table of content] to help navigate your way through this user guide.

### Table of Content
-------------------
- __1. Commands__
  - __1.1. Adding Tasks__
    - 1.1.1. Adding Floating Tasks
    - 1.1.2. Adding Deadline Tasks
    - 1.1.3. Adding Event-Based Tasks
    - 1.1.4. Adding Location to Task(s)
  - __1.2. Editing Tasks__
    - 1.2.1. Editing Task Properties
    - 1.2.2. Renaming Task
  - __1.3. Marking Tasks as Done__
  - __1.4. Deleting Tasks__
    - 1.4.1. Deleting a Single Task
    - 1.4.2. Deleting Multiple Tasks
  - __1.5. Undo__
  - __1.6. Redo__
  - __1.7. Search Tasks__
  - __1.8. Exiting TaskNote__
- __2. Hotkeys and Shortcuts__
- __3. Command Cheatsheet__
- __4. Troubleshooting__
  - 4.1. How Do I Add Task Containing Keywords Like 'on', 'by', etc.?
  - 4.2. How Do I Undo Commands After Closing The Application?

### 1. Commands
---------------
#### 1.1. Adding Tasks
##### 1.1.1. Adding Floating Tasks
There are times when you would like to take note of tasks without any deadline associated with them. For instance, you have always wanted to visit the museum, though you are not sure when exactly are you able to take the time off to visit. To add a floating task (i.e. task without any specific date / time tagged), simply use the `add` keyword:

`    add Visit Museum`

or more generally,

`    add <Floating Task’s Name>`

Note that each task will be allocated a _unique task index_ to allow you to specify the task without ambiguity when you are editing the task in TaskNote.

[(Return to Table of Content)]

##### 1.1.2. Adding Deadline Tasks
TaskNote also allows you to keep track of deadlines by using the `add` keyword with the `on` and/or `by` keywords, for date and/or time, respectively. For instance:

`    add Buy friend’s birthday gift on 12/10/2016 by 12:00`

or more generally,

`    add <Deadline Task’s Name> on <Date> by <Time>`

[(Return to Table of Content)]

##### 1.1.3. Adding Event-Based Tasks
Occasionally, you may find yourself keeping track of the various events happening in your life, for instance movie dates, birthday parties, etc. To add an event in TaskNote, use the `add` keyword with the `from` and `to` keyword, for start and end time, respectively. For instance:

`    add Attend birthday party on 13/10/2016 from 9:00 to 12:00`

or more generally,

`    add <Task’s Name> on <Date> from <Start Time> to <End Time>`

[(Return to Table of Content)]

##### 1.1.4. Adding Location to Task(s)
TaskNote allows you to tag any floating tasks, deadline tasks, and event-based tasks with their relevant location. To tag the task with their location, use the `at` keyword, followed by the actual location:

`    add Interview Appointment at 1 Rocher Road #02-11`

or more generally, for each type of task,

`    add <Floating Task> at <Location>`
`    add <Deadline> on <Date> by <Time> at <Location>`
`    add <Event> on <Date> from <Start Time> to <End Time> at <Location>`

[(Return to Table of Content)]

#### 1.2. Editing Tasks
##### 1.2.1. Editing Task Properties
Sometimes you will make changes to your tasks. Hence, TaskNote will allow you to modify your tasks, using the keyword  `edit`.

Each task will have a unique task index. This index, which is found next to the task, is used to identify each task uniquely. To edit a task, use the 'edit' keyword. For instance:

`    edit 3 on 30/6/2016 by 16:00`

or more generally,

`    edit <Task Index> by <Time>`

will change the date and time property for task with index 3. This edit operation has specified the date and time properties to a floating task, which will result in the floating task turning into a deadline task. The use of the `on` and `by` keyword modifies the task’s date and time properties, respectively. Naturally, you can also use `edit` in conjunction with other keyword, such as `at` and so on.

[(Return to Table of Content)]

##### 1.2.2. Renaming Task
To rename a task, use the `edit` keyword. Enter the new task name before any keywords, or quote the task name in double quotation marks if the new task name contains a keyword. For instance:

`    edit 5 "by train, attend birthday party" at Apple Street`

or more generally,

`    edit <Task Index> <New Task’s Name>`

This will rename task 5 to 'by train, attend birthday party', and adds 'Apple Street' as its new location.

[(Return to Table of Content)]

#### 1.3. Marking Tasks as Done
To mark a task as completed, using the `done` keyword command:

`    done 1`

or more generally,

`    done <Task Index>`

Marking a task as done is different from deleting it; marking it as done merely shifts the task from outstanding tasks, to completed tasks. In fact, the task will be greyed out to show that it is completed.

[(Return to Table of Content)]

#### 1.4. Deleting Tasks
##### 1.4.1. Deleting a Single Task
Occasionally, there are tasks which may no longer be relevant to you anymore - tasks which will never get done, and that you do not want to keep track of. To delete these tasks, and remove the task from the application, use the `delete` command:

`    delete 1`

or more generally,

`    delete <Task Index>`

This will remove task with index 1 from TaskNote. Tasks with bigger task indices will have their indices shifted up, to close any gaps introduced by the deletion of the task. Also note that indices of tasks _may_ change after each add, delete and/or edit operations.

[(Return to Table of Content)]

##### 1.4.2. Deleting Multiple Tasks
Deleting multiple tasks is just as easy. Use the `delete` keyword command in conjunction with all the task indices you want deleted:

`    delete 1 2 3`

or more generally,

`    delete <Task Index> [<Task Index>] [<Task Index>] ...`

This will remove task 1, 2, and 3 from TaskNote.

[(Return to Table of Content)]

#### 1.5. Undo
TaskNote is able to undo the last command that modifies the tasks in TaskNote (such as `edit`, `done`, or `delete`).

To revert the previous action, simply enter the 'undo' keyword into TaskNote:

`    undo`

Note that once you close TaskNote, then open it again, you will not be able to undo any commands that you have previously executed. Hence, make sure that you are certain of the tasks you want TaskNote to save before closing the application.

[(Return to Table of Content)]

#### 1.6. Redo
Conversely, you may redo any operation that you have undone. Suppose you reverted an operation using the `undo` keyword, but you would like to revert that change. This can be done by using the `redo` keyword:

`    redo`

You can recover any operation that you have undone (assuming that you have not closed the application after calling undo) by entering `redo` into TaskNote. You may think of `redo` as undoing the latest `undo` command.

[(Return to Table of Content)]

#### 1.7. Search Tasks
Use the `search` keyword to search for certain task(s):

`    search birthday`

or more generally,

`    search <Query>`

This will retrieve a list of task(s) that contain the word 'birthday'.

Note that the task index for a particular task will not be the same as that in the original list of tasks. The list of tasks that are retrieved after a search operation will have new task indices assigned to them. You can perform the edit or delete operations on these temporary indices assigned to the search results.

Searching for part of a word is also possible. For instance, you could type:

`    search birth`

or more generally,

`    search <Query Substring>`

and the same tasks that appeared for search birthday will appear. Nonetheless, whenever possible, we suggest that you to search for the full word to narrow down your search query.

[(Return to Table of Content)]

#### 1.8. Exiting TaskNote
If you are feeling lazy enough, use the `exit` keyword to close the program:

`    exit`

Using the above command, you will exit the program. When you exit, TaskNote will have saved your tasks in a text file called __taskContents.txt__.

[(Return to Table of Content)]


### 2. Hotkeys and Shortcuts
----------------------------

<<To be Filled In>>

###  3. Command Cheatsheet
--------------------------

<<To be Filled In>>

### 4. Troubleshooting
----------------------
#### 4.1. How Do I Add Task Containing Keywords Like 'on', 'by', etc.?
Certain tasks may contain keywords that have been reserved by TaskNote, for the purpose of adding time or location. For instance, suppose you would like to visit a cafe with the name, at 2pm. There is a potential conflict with the `at` keyword here. You may escape the task name, Visit at 2pm, by enclosing the task name to be added in double quotation marks to prevent it from being misinterpreted.

`    add "Visit at 2pm"`

This will result in the floating task, Visit at 2pm to be added.

[(Return to Table of Content)]

#### 4.2. How Do I Undo Commands After Closing The Application?
Note that the history of commands executed by you are only kept track of for that particular session. Thus, once you close TaskNote, you will not be able to undo any previous commands that you have entered in the previous session.

[(Return to Table of Content)]

[table of content]: TaskNote-UserGuide.md#table-of-content
[(Return to Table of Content)]: TaskNote-UserGuide.md#table-of-content
