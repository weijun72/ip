package ultron.gui;

import javafx.application.Application;

/**
 * Launches the JavaFX interface without making it the JAR entry point directly.
 */
public final class Launcher {
    /** Prevents instantiation of this utility launcher. */
    private Launcher() {
    }

    /**
     * Starts the Ultron JavaFX application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        Application.launch(UltronApp.class, args);
    }
}
