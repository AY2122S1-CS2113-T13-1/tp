package medbot;

import java.util.Scanner;

/**
 * Represents a UI class that interacts with User
 * (Reading user input and printing message to users).
 */
public class Ui {
    public static final String DATA_SEPARATOR_ESCAPED = " \\| ";
    public static final String DATA_SEPARATOR_UNESCAPED = " | ";

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
    public void printWelcomeMessage() {
        printOutput("Hello, I'm MedBot!" + System.lineSeparator() + "How can I help you today?");
    }

    /**
     * Returns a message when successfully add a patient to a list.
     *
     * @param patientId the ID of the patient to be added
     * @return the Successful Message
     */
    public String getAddPatientMessage(int patientId) {
        return "Added patient with patient ID: " + patientId;
    }

    /**
     * Returns a message when successfully delete a patient from a list.
     *
     * @param patientId the ID of the patient to be deleted.
     * @return the Successful Message
     */
    public String getDeletePatientMessage(int patientId) {
        return "Patient with id " + patientId + " deleted from system.";
    }

    /**
     * Returns a message when successfully edit a patient in a list.
     *
     * @param patientId the ID of the patient to be edited.
     * @return the Successful Message
     */
    public String getEditPatientMessage(int patientId, String patientInfo) {
        return "The information of patient with ID " + patientId + " has been edited to:"
                + System.lineSeparator() + patientInfo;
    }

    /**
     * Prints an exit message when MedBot is exiting.
     *
     * @return the exit Message
     */
    public String getExitMessage() {
        return "Thank you for using MedBot!\nSee you again!";
    }

    /**
     * Prints a message when viewing the profile of a patient.
     *
     * @param patientId   the ID of the patient to be viewed.
     * @param patientInfo the Info of the patient to be printed.
     * @return the Patient information
     */
    public String getPatientInfo(int patientId, String patientInfo) {
        return "Here's the patient with id " + patientId + ": " + patientInfo;
    }

    /**
     * Prints all patients in a list.
     *
     * @param patientList the list containing patients to be printed.
     * @return all Patients' information.
     */
    public String getAllPatientsString(PatientList patientList) {
        String output = "Here is a list of all patients:\n";
        output += patientList.listPatients();

        return output;
    }

    /**
     * Prints a list of all available commands.
     */
    public void printCommandList() {
        System.out.println("Here are the list of commands:\n\n"
                + "help\n" + "add\n" + "list\n" + "view\n" + "edit\n" + "delete\n"
                + "exit\n" + "\n"
                + "To obtain more information on each command and their respective required inputs, type:\n"
                + "help [COMMAND]");
    }

    public void printListHelpMessage() {
        System.out.println("View information of all current patients.\n"
                + "Format: list\n" + "Expected Output\n" + "id: PATIENT1_ID\n"
                + "name: PATIENT1_NAME\n\n\n"
                + "id: PATIENT2_ID\n" + "name: PATIENT2_NAME\n");
    }

    public void printViewHelpMessage() {
        System.out.println("View a patient’s personal information.\n" + "Format: view PATIENT_ID\n"
                + "Expected Output: \n" + "id: PATIENT_ID\n" + "name: NAME\n"
                + "phone number: PHONE_NUMBER\n" + "email: EMAIL\n" + "address: ADDRESS\n");
    }

    public void printAddHelpMessage() {
        System.out.println("Add a patient to the patient’s list.\n"
                + "Format: \n"
                + "add i/PATIENT_ID [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS]\n"
                + "Expected output: \n"
                + "Patient with the following information has been successfully added to the list:\n"
                + "id: PATIENT_ID\n"
                + "name: NAME\n"
                + "phone number: PHONE_NUMBER\n"
                + "email: EMAIL\n" + "address: ADDRESS\n");
    }

    public void printEditHelpMessage() {
        System.out.println("Edit the personal and medical information of a patient in the list.\n"
                + "Format: edit PATIENT_ID FIELD_TO_EDIT NEW_DATA\n"
                + "Expected output: \n"
                + "The FIELD_TO_EDIT of patient PATIENT_ID has been changed from OLD_DATA to NEW_DATA\n");
    }

    public void printDeleteHelpMessage() {
        System.out.println("Delete a patient from the list.\n"
                + "Format: \n"
                + "delete PATIENT_ID\n"
                + "Expected Output:\n"
                + "Patient with the following information has been successfully deleted from the list:\n"
                + "id: PATIENT_ID\n"
                + "name: NAME\n"
                + "phone number: PHONE_NUMBER\n"
                + "email: EMAIL\n"
                + "address: ADDRESS\n");
    }

    public void printExitHelpMessage() {
        System.out.println("Exits the program.\n" + "Format: exit\n");
    }

    public void printUnrecognisedCommandHelpMessage() {
        System.out.println("Sorry, that's not a recognised command. To view a list of commands, type:\n" + "help\n");
    }


}
