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
import javafx.scene.layout.Region;
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
        stage.setMinWidth(500);
        stage.setMinHeight(420);
        stage.setScene(scene);
        stage.show();

        addAppMessage("I am Ultron. Tell me what you need to remember.\n\n"
                + "Try: todo read book, deadline submit report /by Friday, or list.");
    }

    private VBox createHeader() {
        Label title = new Label("ULTRON");
        title.getStyleClass().add("title");

        Label subtitle = new Label("Task manager · type a command below");
        subtitle.getStyleClass().add("subtitle");

        VBox header = new VBox(3, title, subtitle);
        header.getStyleClass().add("header");
        return header;
    }

    private ScrollPane createConversation() {
        messages = new VBox(14);
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
        StringBuilder error = new StringBuilder();
        boolean shouldExit = chatbot.processCommand(command, new Ui(
                line -> appendResponse(response, line), line -> appendResponse(error, line)));
        if (!response.isEmpty()) {
            addAppMessage(response.toString());
        }
        if (!error.isEmpty()) {
            addErrorMessage(error.toString());
        }
        if (shouldExit) {
            Platform.exit();
        }
    }

    private void appendResponse(StringBuilder response, String line) {
        if (line.matches("_+")) {
            return;
        }
        if (!response.isEmpty()) {
            response.append(System.lineSeparator());
        }
        response.append(line);
    }

    private void addUserMessage(String message) {
        addMessage(message, "user-message", Pos.CENTER_RIGHT);
    }

    private void addAppMessage(String message) {
        Label sender = new Label("ULTRON");
        sender.getStyleClass().add("app-sender");

        Label messageLabel = createMessageLabel(message, "app-message");
        VBox messageGroup = new VBox(4, sender, messageLabel);
        messageGroup.getStyleClass().add("app-message-group");

        HBox messageRow = new HBox(messageGroup);
        messageRow.setAlignment(Pos.CENTER_LEFT);
        messages.getChildren().add(messageRow);
        scrollToNewestMessage();
    }

    private void addErrorMessage(String message) {
        Label errorTitle = new Label("COMMAND NEEDS ATTENTION");
        errorTitle.getStyleClass().add("error-title");
        Label messageLabel = createMessageLabel(message, "error-message");

        VBox errorCard = new VBox(4, errorTitle, messageLabel);
        errorCard.getStyleClass().add("error-card");
        HBox messageRow = new HBox(errorCard);
        messageRow.setAlignment(Pos.CENTER_LEFT);
        messages.getChildren().add(messageRow);
        scrollToNewestMessage();
    }

    private void addMessage(String message, String styleClass, Pos alignment) {
        Label messageLabel = createMessageLabel(message, styleClass);

        HBox messageRow = new HBox(messageLabel);
        messageRow.setAlignment(alignment);
        messages.getChildren().add(messageRow);
        scrollToNewestMessage();
    }

    private Label createMessageLabel(String message, String styleClass) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMinWidth(Region.USE_PREF_SIZE);
        messageLabel.maxWidthProperty().bind(messageScrollPane.widthProperty().subtract(132));
        messageLabel.getStyleClass().addAll("message", styleClass);
        return messageLabel;
    }

    private void scrollToNewestMessage() {
        Platform.runLater(() -> messageScrollPane.setVvalue(1));
    }
}
