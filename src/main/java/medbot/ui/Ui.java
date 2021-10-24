package medbot.ui;

import medbot.exceptions.MedBotException;
import medbot.utilities.ViewType;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a UI class that interacts with User
 * (Reading user input and printing message to users).
 */
public class Ui {
    public static final String VERTICAL_LINE_SPACED_ESCAPED = " \\| ";
    public static final String VERTICAL_LINE_SPACED = " | ";
    public static final String END_LINE = System.lineSeparator();
    private static final String ERROR_VIEW_CONTEXT_NOT_FOUND = "Cannot identify the current view type" + END_LINE;
    private static final String TABLE_ROW_SEPARATOR = " ------------------------------------------------"
            + "----------------------------------------------------- " + END_LINE;

    private Scanner inputScanner = new Scanner(System.in);

    /**
     * Gets user input from terminal and returns it as a String.
     *
     * @return the String containing the user input.
     */
    public String readInput() {
        String line;
        line = inputScanner.nextLine();
        return line;
    }

    /**
     * Prints a message.
     *
     * @param outputMessage the message to be printed
     */
    public void printOutput(String outputMessage) {
        System.out.println(outputMessage);
    }

    /**
     * Prints a welcome message when MedBot is first loaded.
     */
    public void printWelcomeMessageOne() {
        printOutput("Hello, I'm MedBot!");
    }

    /**
     * Prints a welcome message when MedBot file storage is successfully loaded.
     */
    public void printWelcomeMessageTwo() {
        printOutput("How can I help you today?" + END_LINE);
    }

    /**
     * Returns a message when successfully add to a list.
     *
     * @param id       the ID of the patient to be added
     * @param viewType the viewType context of the command
     * @return the Successful Message
     */
    public static String getAddMessage(int id, ViewType viewType) throws MedBotException {
        assert id > 0;
        switch (viewType) {
        case PATIENT_INFO:
            return "Added patient with patient ID: " + id + END_LINE;
        case SCHEDULER:
            return "Added schedule with schedule ID: " + id + END_LINE;
        case MEDICAL_STAFF_INFO:
            return "Added staff with staff ID: " + id + END_LINE;
        default:
            assert false;
            throw new MedBotException(ERROR_VIEW_CONTEXT_NOT_FOUND);
        }
    }

    /**
     * Returns a message when successfully delete an object from a list.
     *
     * @param id       the ID of the object to be deleted.
     * @param viewType the viewType context of the command.
     * @return the Successful Message
     */
    public static String getDeleteMessage(int id, ViewType viewType) throws MedBotException {
        assert id > 0;
        switch (viewType) {
        case PATIENT_INFO:
            return "Patient with id " + id + " deleted from system." + END_LINE;
        case SCHEDULER:
            return "Schedule with id " + id + " deleted from system." + END_LINE;
        case MEDICAL_STAFF_INFO:
            return "Staff with id " + id + " deleted from system." + END_LINE;
        default:
            assert false;
            throw new MedBotException(ERROR_VIEW_CONTEXT_NOT_FOUND);
        }
    }

    /**
     * Returns a message when successfully edit an object in a list.
     *
     * @param id       the ID of the object to be edited.
     * @param info     the information of the new object
     * @param viewType the viewType context of the command.
     * @return the Successful Message
     */
    public static String getEditMessage(int id, String info, ViewType viewType) throws MedBotException {
        assert id > 0;
        switch (viewType) {
        case PATIENT_INFO:
            return "The information of patient with ID " + id + " has been edited to:" + END_LINE + END_LINE
                    + info + END_LINE;
        case SCHEDULER:
            return "The information of schedule with ID " + id + " has been edited to:" + END_LINE + END_LINE
                    + info + END_LINE;
        case MEDICAL_STAFF_INFO:
            return "The information of staff with ID " + id + " has been edited to:" + END_LINE + END_LINE
                    + info + END_LINE;
        default:
            assert false;
            throw new MedBotException(ERROR_VIEW_CONTEXT_NOT_FOUND);
        }
    }

    /**
     * Returns the information of the filtered patients.
     *
     * @param persons  the filtered patients to be printed.
     * @param viewType the viewType context of the command.
     * @return The information of the filtered patients
     */
    public static String getFindPersonsMessage(List<String> persons, ViewType viewType) throws MedBotException {
        if (persons.size() == 0) {
            return "There is no person with such attributes in this list." + END_LINE;
        }
        String output;
        switch (viewType) {
        case PATIENT_INFO:
            output = PatientUi.getPatientTableHeader();
            break;
        case MEDICAL_STAFF_INFO:
            output = StaffUi.getStaffTableHeader();
            break;
        default:
            assert false;
            throw new MedBotException(ERROR_VIEW_CONTEXT_NOT_FOUND);
        }
        for (String person : persons) {
            output += person;
        }
        output += END_LINE;
        output += TABLE_ROW_SEPARATOR;
        return output;
    }

    /**
     * Returns the successful message of archiving a person.
     *
     * @param personId  the ID of the person to be archived.
     * @param viewType the viewType context of the command.
     * @return The successful message of archiving the person
     */
    public static String getArchivePersonMessage(int personId, ViewType viewType) throws MedBotException {
        String output;
        switch (viewType) {
        case PATIENT_INFO:
            output = "The patient with ID: " + personId + " is archived successfully.";
            break;
        case MEDICAL_STAFF_INFO:
            output = "The staff with ID: " + personId + " is archived successfully.";
            break;
        default:
            assert false;
            throw new MedBotException(ERROR_VIEW_CONTEXT_NOT_FOUND);
        }

        return output;
    }

    /**
     * Returns the successful message of un-archiving a person.
     *
     * @param personId  the ID of the person to be un-archived.
     * @param viewType the viewType context of the command.
     * @return The successful message of un-archiving the person
     */
    public static String getUnarchivePersonMessage(int personId, ViewType viewType) throws MedBotException {
        String output;
        switch (viewType) {
        case PATIENT_INFO:
            output = "The patient with ID: " + personId + " is unarchived successfully.";
            break;
        case MEDICAL_STAFF_INFO:
            output = "The staff with ID: " + personId + " is unarchived successfully.";
            break;
        default:
            assert false;
            throw new MedBotException(ERROR_VIEW_CONTEXT_NOT_FOUND);
        }

        return output;
    }

    /**
     * Prints an exit message when MedBot is exiting.
     *
     * @return the exit Message
     */
    public String getExitMessage() {
        return "Thank you for using MedBot!" + END_LINE + "See you again!";
    }


    /**
     * Prints a list of all available commands.
     *
     * @return all supported commands.
     */
    public static String getCommandList() {
        return "Here are the list of commands:" + END_LINE + END_LINE
                + "help" + END_LINE + "add" + END_LINE + "list" + END_LINE + "view" + END_LINE + "edit" + END_LINE
                + "find" + END_LINE + "delete" + END_LINE + "exit" + END_LINE + "archive" + END_LINE + "unarchive"
                + END_LINE + END_LINE
                + "To obtain more information on each command and their respective required inputs, type:" + END_LINE
                + "help [COMMAND]" + END_LINE + END_LINE
                + "*Note that all commands will remove any '|' inputs for format parsing purposes" + END_LINE;
    }

    /**
     * Prints information about list command.
     *
     * @param viewType the viewType context of the command.
     * @return the information on list command.
     */
    public String getListHelpMessage(ViewType viewType) throws MedBotException {
        switch (viewType) {
        case PATIENT_INFO:
            return PatientUi.getListPatientHelpMessage();
        case MEDICAL_STAFF_INFO:
            return StaffUi.getListStaffHelpMessage();
        case SCHEDULER:
            return SchedulerUi.getListSchedulerHelpMessage();
        default:
            assert false;
            throw new MedBotException(ERROR_VIEW_CONTEXT_NOT_FOUND);
        }
    }


    /**
     * Prints information about view command.
     *
     * @param viewType the viewType context of the command.
     * @return the information on view command.
     */
    public String getViewHelpMessage(ViewType viewType) throws MedBotException {
        switch (viewType) {
        case PATIENT_INFO:
            return PatientUi.getViewPatientHelpMessage();
        case MEDICAL_STAFF_INFO:
            return StaffUi.getViewStaffHelpMessage();
        case SCHEDULER:
            return SchedulerUi.getViewSchedulerHelpMessage();
        default:
            assert false;
            throw new MedBotException(ERROR_VIEW_CONTEXT_NOT_FOUND);
        }
    }


    /**
     * Prints information about add command.
     *
     * @param viewType the viewType context of the command.
     * @return the information on add command.
     */
    public String getAddHelpMessage(ViewType viewType) throws MedBotException {
        switch (viewType) {
        case PATIENT_INFO:
            return PatientUi.getAddPatientHelpMessage();
        case MEDICAL_STAFF_INFO:
            return StaffUi.getAddStaffHelpMessage();
        case SCHEDULER:
            return SchedulerUi.getAddSchedulerHelpMessage();
        default:
            assert false;
            throw new MedBotException(ERROR_VIEW_CONTEXT_NOT_FOUND);
        }
    }


    /**
     * Prints information about edit command.
     *
     * @param viewType the viewType context of the command.
     * @return the information on edit command.
     */
    public String getEditHelpMessage(ViewType viewType) throws MedBotException {
        switch (viewType) {
        case PATIENT_INFO:
            return PatientUi.getEditPatientHelpMessage();
        case MEDICAL_STAFF_INFO:
            return StaffUi.getEditStaffHelpMessage();
        case SCHEDULER:
            return SchedulerUi.getEditSchedulerHelpMessage();
        default:
            assert false;
            throw new MedBotException(ERROR_VIEW_CONTEXT_NOT_FOUND);
        }
    }

    /**
     * Prints information about delete command.
     *
     * @param viewType the viewType context of the command.
     * @return the information on delete command.
     */
    public String getDeleteHelpMessage(ViewType viewType) throws MedBotException {
        switch (viewType) {
        case PATIENT_INFO:
            return PatientUi.getDeletePatientHelpMessage();
        case MEDICAL_STAFF_INFO:
            return StaffUi.getDeleteStaffHelpMessage();
        case SCHEDULER:
            return SchedulerUi.getDeleteSchedulerHelpMessage();
        default:
            assert false;
            throw new MedBotException(ERROR_VIEW_CONTEXT_NOT_FOUND);
        }
    }


    /**
     * Prints information about find command.
     *
     * @param viewType the viewType context of the command.
     * @return the information on find command.
     */
    public String getFindHelpMessage(ViewType viewType) throws MedBotException {
        switch (viewType) {
        case PATIENT_INFO:
            return PatientUi.getFindPatientHelpMessage();
        case MEDICAL_STAFF_INFO:
            return StaffUi.getFindStaffHelpMessage();
        case SCHEDULER:
            return SchedulerUi.getFindSchedulerHelpMessage();
        default:
            assert false;
            throw new MedBotException(ERROR_VIEW_CONTEXT_NOT_FOUND);
        }
    }

    /**
     * Prints information about switch command.
     *
     * @return the information on switch command.
     */
    public String getSwitchHelpMessage() {
        return " " + END_LINE;
    }

    /**
     * Prints information about archive command.
     *
     * @return the information on archive command.
     */
    public String getArchiveHelpMessage(ViewType viewType) throws MedBotException {
        switch (viewType) {
        case PATIENT_INFO:
            return PatientUi.getArchivePatientHelpMessage();
        case MEDICAL_STAFF_INFO:
            return StaffUi.getArchiveStaffHelpMessage();
        default:
            assert false;
            throw new MedBotException(ERROR_VIEW_CONTEXT_NOT_FOUND);
        }
    }

    /**
     * Prints information about archive command.
     *
     * @return the information on archive command.
     */
    public String getUnarchiveHelpMessage(ViewType viewType) throws MedBotException {
        switch (viewType) {
        case PATIENT_INFO:
            return PatientUi.getUnarchivePatientHelpMessage();
        case MEDICAL_STAFF_INFO:
            return StaffUi.getUnarchiveStaffHelpMessage();
        default:
            assert false;
            throw new MedBotException(ERROR_VIEW_CONTEXT_NOT_FOUND);
        }
    }


    /**
     * Prints information about exit command.
     *
     * @return the information on exit command.
     */
    public String getExitHelpMessage() {
        return "Exits the program." + END_LINE + "Format: exit" + END_LINE;
    }

    /**
     * Prints unrecognised command message.
     *
     * @return the error message on unrecognised command.
     */
    public String getUnrecognisedCommandHelpMessage() {
        return "Sorry, that's not a recognised command. To view a list of commands, type:"
                + END_LINE + "help" + END_LINE;
    }

    /**
     * Utility function that performs a pseudo-clear of the console. Use this for testing from within
     * the IDE.
     */
    public static void clearConsoleFromIde() {
        System.out.print(END_LINE + END_LINE + END_LINE + END_LINE + END_LINE);
    }

    /**
     * Utility function that clears the console. Does not work within the IDE console.
     */
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error clearing the console");
        }
    }

    /**
     * Prints switched view message.
     */
    public void printSwitchedViewMessage(ViewType viewType) {
        switch (viewType) {
        case PATIENT_INFO:
            System.out.println("  ___  _ _____ ___ ___ _  _ _____ \n"
                    + " | _ \\/_\\_   _|_ _| __| \\| |_   _|\n"
                    + " |  _/ _ \\| |  | || _|| .` | | |  \n"
                    + " |_|/_/ \\_\\_|_|___|___|_|\\_| |_|  \n"
                    + " |_ _| \\| | __/ _ \\               \n"
                    + "  | || .` | _| (_) |              \n"
                    + " |___|_|\\_|_|_\\___/    __         \n"
                    + " \\ \\ / /_ _| __\\ \\    / /         \n"
                    + "  \\ V / | || _| \\ \\/\\/ /          \n"
                    + "   \\_/ |___|___| \\_/\\_/           \n"
                    + "                                  ");
            break;
        case SCHEDULER:
            System.out.println("  ___  ___ _  _ ___ ___  _   _ _    ___ ___ \n"
                    + " / __|/ __| || | __|   \\| | | | |  | __| _ \\\n"
                    + " \\__ \\ (__| __ | _|| |) | |_| | |__| _||   /\n"
                    + " |___/\\___|_||_|___|___/_\\___/|____|___|_|_\\\n"
                    + " \\ \\ / /_ _| __\\ \\    / /                   \n"
                    + "  \\ V / | || _| \\ \\/\\/ /                    \n"
                    + "   \\_/ |___|___| \\_/\\_/                     \n"
                    + "                                            ");
            break;
        case MEDICAL_STAFF_INFO:
            System.out.println("  ___ _____ _   ___ ___  \n"
                    + " / __|_   _/_\\ | __| __| \n"
                    + " \\__ \\ | |/ _ \\| _|| _|  \n"
                    + " |___/_|_/_/_\\_\\_| |_|   \n"
                    + " |_ _| \\| | __/ _ \\      \n"
                    + "  | || .` | _| (_) |     \n"
                    + " |___|_|\\_|_|_\\___/    __\n"
                    + " \\ \\ / /_ _| __\\ \\    / /\n"
                    + "  \\ V / | || _| \\ \\/\\/ / \n"
                    + "   \\_/ |___|___| \\_/\\_/  \n"
                    + "                         ");
            break;
        default:
            break;

        }

        System.out.println("View has been switched to " + viewType);
    }
}
