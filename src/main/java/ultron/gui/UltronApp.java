package ultron.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ultron.Chatbot;
import ultron.ui.Ui;

/**
 * Provides the JavaFX chat interface for the Ultron task manager.
 */
public class UltronApp extends Application {
    private static final String STORAGE_PATH = System.getProperty("ultron.saveFile", "data/ultron.txt");
    private final Chatbot chatbot = new Chatbot(STORAGE_PATH);
    private VBox messages;
    private ScrollPane messageScrollPane;
    private TextField commandInput;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-root");
        root.setTop(createHeader());
        root.setCenter(createConversation());
        root.setBottom(createComposer());

        Scene scene = new Scene(root, 760, 620);
        scene.getStylesheets().add(getClass().getResource("/ultron/gui/ultron.css").toExternalForm());

        stage.setTitle("Ultron Task Manager");
        stage.setMinWidth(560);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.show();

        addBotMessage("I am Ultron. Tell me what you need to remember.\n\n"
                + "Try: todo read book, deadline submit report /by Friday, or list.");
    }

    private VBox createHeader() {
        Label title = new Label("ULTRON");
        title.getStyleClass().add("title");

        Label subtitle = new Label("Your sharp, slightly impatient task assistant");
        subtitle.getStyleClass().add("subtitle");

        VBox header = new VBox(3, title, subtitle);
        header.getStyleClass().add("header");
        return header;
    }

    private ScrollPane createConversation() {
        messages = new VBox(12);
        messages.getStyleClass().add("messages");

        messageScrollPane = new ScrollPane(messages);
        messageScrollPane.setFitToWidth(true);
        messageScrollPane.getStyleClass().add("conversation");
        return messageScrollPane;
    }

    private HBox createComposer() {
        commandInput = new TextField();
        commandInput.setPromptText("Type a command, for example: todo read book");
        commandInput.setOnAction(event -> submitCommand());
        HBox.setHgrow(commandInput, Priority.ALWAYS);

        Button sendButton = new Button("Send");
        sendButton.setDefaultButton(true);
        sendButton.setOnAction(event -> submitCommand());

        HBox composer = new HBox(10, commandInput, sendButton);
        composer.getStyleClass().add("composer");
        return composer;
    }

    private void submitCommand() {
        String command = commandInput.getText().trim();
        if (command.isEmpty()) {
            return;
        }

        addUserMessage(command);
        commandInput.clear();

        StringBuilder response = new StringBuilder();
        boolean shouldExit = chatbot.processCommand(command, new Ui(line -> appendResponse(response, line)));
        if (!response.isEmpty()) {
            addBotMessage(response.toString());
        }
        if (shouldExit) {
            Platform.exit();
        }
    }

    private void appendResponse(StringBuilder response, String line) {
        if (!response.isEmpty()) {
            response.append(System.lineSeparator());
        }
        response.append(line);
    }

    private void addUserMessage(String message) {
        addMessage(message, "user-message", Pos.CENTER_RIGHT);
    }

    private void addBotMessage(String message) {
        addMessage(message, "bot-message", Pos.CENTER_LEFT);
    }

    private void addMessage(String message, String styleClass, Pos alignment) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(540);
        messageLabel.getStyleClass().addAll("message", styleClass);

        HBox messageRow = new HBox(messageLabel);
        messageRow.setAlignment(alignment);
        messages.getChildren().add(messageRow);
        Platform.runLater(() -> messageScrollPane.setVvalue(1));
    }
}
