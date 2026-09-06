# Ultron User Guide

Ultron is a task manager that supports todos, deadlines, and events through its JavaFX chat interface.

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
