##TaskNote User Guide
TaskNote is a desktop application that keeps track of your task. In this user guide, we will delve into the commands that TaskNote uses, so that you can get up to speed with using TaskNote.

You may notice that the content page is somewhat long, so do use the [table of content](.#table-of-content) to help navigate your way through this user guide.
###Table of Content
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

[(Return to Table of Content)](.#table-of-content)

##### 1.1.2. Adding Deadline Tasks
TaskNote also allows you to keep track of deadlines by using the `add` keyword with the `on` and/or `by` keywords, for date and/or time, respectively. For instance:

`    add Buy friend’s birthday gift on 12/10/2016 by 12:00`

or more generally,

`    add <Deadline Task’s Name> on <Date> by <Time>`

[(Return to Table of Content)](.#table-of-content)

##### 1.1.3. Adding Event-Based Tasks
Occasionally, you may find yourself keeping track of the various events happening in your life, for instance movie dates, birthday parties, etc. To add an event in TaskNote, use the `add` keyword with the `from` and `to` keyword, for start and end time, respectively. For instance:

`    add Attend birthday party on 13/10/2016 from 9:00 to 12:00`

or more generally,

`    add <Task’s Name> on <Date> from <Start Time> to <End Time>`

[(Return to Table of Content)](.#table-of-content)

##### 1.1.4. Adding Location to Task(s)
TaskNote allows you to tag any floating tasks, deadline tasks, and event-based tasks with their relevant location. To tag the task with their location, use the `at` keyword, followed by the actual location:

`    add Interview Appointment at 1 Rocher Road #02-11`

or more generally, for each type of task,

`    add <Floating Task> at <Location>`
`    add <Deadline> on <Date> by <Time> at <Location>`
`    add <Event> on <Date> from <Start Time> to <End Time> at <Location>`

[(Return to Table of Content)](.#table-of-content)
