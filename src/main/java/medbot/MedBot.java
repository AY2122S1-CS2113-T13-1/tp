package medbot;

import medbot.command.Command;
import medbot.exceptions.MedBotException;
import medbot.parser.Parser;
import medbot.storage.StorageManager;
import medbot.ui.Ui;

import java.io.FileNotFoundException;

public class MedBot {
    public static void main(String[] args) {
        interactWithUser();
    }

    public static void interactWithUser() {

        Scheduler scheduler = new Scheduler();
        Ui ui = new Ui();
        StorageManager storageManager = null;
        boolean isInteracting = true;

        ui.printWelcomeMessageOne();
        try {
            storageManager = new StorageManager(scheduler, ui);
            ui.printWelcomeMessageTwo();

        } catch (FileNotFoundException | MedBotException e) {
            ui.printOutput(e.getMessage());
        }

        while (isInteracting) {
            String userInput = ui.readInput();
            try {
                Command command = Parser.parseCommand(userInput);
                command.execute(scheduler, ui);

                assert storageManager != null;
                storageManager.saveToStorage(scheduler);
                isInteracting = !command.isExit();

            } catch (MedBotException mbe) {
                ui.printOutput(mbe.getMessage() + System.lineSeparator());
            }
        }
    }
}
