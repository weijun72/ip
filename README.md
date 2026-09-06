# Duke project template

This is a project template for a greenfield Java project. It's named after the Java mascot _Duke_. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Duke.java` file, right-click it, and choose `Run Duke.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
    ____        _        
   |  _ \ _   _| | _____ 
   | | | | | | | |/ / _ \
   | |_| | |_| |   <  __/
   |____/ \__,_|_|\_\___|
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Checkstyle

Run the following command to check production and test code against the
SE-EDU Java coding standard:

```
./gradlew checkstyleMain checkstyleTest
```

## Running Ultron

Start the JavaFX chat interface with:

```
./gradlew run
```

Enter task commands in the message box, such as `todo read book`,
`deadline submit report /by Friday`, `event team meeting /from 2pm /to 3pm`,
`list`, `find report`, `mark 1`, `unmark 1`, `delete 1`, `reschedule 2 3d`, and `bye`.

### Rescheduling deadlines

Use `reschedule` to postpone an incomplete deadline or set a new due date.

```
reschedule 2 3d
reschedule 2 /by 20/09/2026
reschedule 2 /by 20/09/2026 1800
```

`3d` adds three days to the deadline's current due date. The date must be today or later. If no replacement
time is supplied, the deadline's existing time is retained; supplied times must use 24-hour `HHmm` format.
