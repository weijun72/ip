# Ultron User Guide

Ultron is a task manager that supports todos, deadlines, and events through its JavaFX chat interface.

![Ultron task manager window](Ui.png)

## Getting started

Start the application with `./gradlew run`. Enter commands in the input field and select **Execute** (or press
Enter). Ultron stores tasks locally and recreates a missing data file automatically when you save your first task.

## Commands

| Command | Example |
| --- | --- |
| Add a todo | `todo read book` |
| Add a deadline | `deadline submit report /by 20/09/2026 1800` |
| Add an event | `event project meeting /from 2pm /to 3pm` |
| List tasks | `list` |
| Find tasks | `find report` |
| Mark or unmark | `mark 1`, `unmark 1` |
| Delete a task | `delete 1` |
| Exit | `bye` |

## Handling mistakes

Ultron accepts extra spaces around commands and parameters. Invalid commands, missing parameters, repeated date
or event markers, impossible dates, and invalid times are shown as command alerts; the application remains open
so you can correct the command. A malformed line in the saved data file is skipped while the other valid tasks
continue to load.

## Rescheduling deadlines

Use `reschedule` to change the due date of an incomplete deadline. The task number must be the number shown by
`list` or `find`.

### Postpone by days

```text
reschedule 2 3d
```

This adds three days to deadline 2's current due date and preserves its existing optional time.

### Set a specific date

```text
reschedule 2 /by 20/09/2026
reschedule 2 /by 20/09/2026 1800
```

Dates use `d/MM/yyyy` and must be today or later. An optional replacement time must be a 24-hour `HHmm` value.
Without a replacement time, the existing time is kept.

Ultron shows both the previous and new deadline details after a successful reschedule. Completed deadlines,
todos, and events cannot be rescheduled.
