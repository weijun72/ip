package ultron;

import ultron.ui.Ui;

/**
 * A command-line task list that can add, list, mark, and unmark tasks.
 */
public class Ultron {

    /**
     * Runs the chatbot command loop.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();
        Chatbot chatbot = new Chatbot(System.getProperty("ultron.saveFile", "data/ultron.txt"));
        while (true) {
            String input = ui.readCommand();
            if (chatbot.processCommand(input, ui)) {
                ui.close();
                return;
            }
        }
    }

}
