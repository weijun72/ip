# Ultron Task Manager

Ultron is a JavaFX task manager that brings a decisive AI-command-console personality to todos, deadlines, and
events. It keeps task data locally in `data/ultron.txt`.

## Run Ultron

Start the JavaFX chat interface with:

```
./gradlew run
```

Enter task commands in the message box, such as `todo read book`,
`deadline submit report /by 20/09/2026`, `event team meeting /from 2pm /to 3pm`,
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

## Error handling

Ultron handles leading, trailing, and repeated whitespace in commands. It explains malformed commands without
ending the session, rejects invalid dates and times, and keeps valid tasks when a saved data file contains a
corrupt line. If the data file is missing, Ultron starts with an empty task list and creates the file when a task
is first saved.

## Quality checks

Run the automated test suite and coding-style checks with:

```
./gradlew test checkstyleMain checkstyleTest
```

## Credits

The JavaFX interface was created for this project. The Ultron-themed avatar and background artwork were generated
with OpenAI image generation. The Checkstyle configuration is adapted from
[se-edu/addressbook-level3](https://github.com/se-edu/addressbook-level3/tree/master/config/checkstyle).
