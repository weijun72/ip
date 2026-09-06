package ultron.model;

import java.util.Optional;

/**
 * The supported categories of tasks and their display symbols.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String symbol;

    /**
     * Creates a task type with the symbol used in task-list output.
     *
     * @param symbol the one-letter task-type symbol
     */
    TaskType(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the symbol displayed for this task type.
     *
     * @return the task-type symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the task type represented by a saved symbol.
     *
     * @param symbol the saved one-letter task-type symbol
     * @return the matching task type, or an empty value when the symbol is unknown
     */
    public static Optional<TaskType> fromSymbol(String symbol) {
        for (TaskType taskType : values()) {
            if (taskType.symbol.equals(symbol)) {
                return Optional.of(taskType);
            }
        }
        return Optional.empty();
    }
}
