package medbot;

import medbot.command.HelpCommand;
import medbot.command.SwitchCommand;
import medbot.command.Command;
import medbot.command.ExitCommand;
import medbot.command.CommandType;

import medbot.command.personcommand.patientcommand.AddPatientCommand;
import medbot.command.personcommand.patientcommand.DeletePatientCommand;
import medbot.command.personcommand.patientcommand.EditPatientCommand;
import medbot.command.personcommand.patientcommand.FindPatientCommand;
import medbot.command.personcommand.patientcommand.ListPatientCommand;
import medbot.command.personcommand.patientcommand.ViewPatientCommand;

import medbot.command.personcommand.staffcommand.AddStaffCommand;
import medbot.command.personcommand.staffcommand.DeleteStaffCommand;
import medbot.command.personcommand.staffcommand.EditStaffCommand;
import medbot.command.personcommand.staffcommand.FindStaffCommand;
import medbot.command.personcommand.staffcommand.ListStaffCommand;
import medbot.command.personcommand.staffcommand.ViewStaffCommand;


import medbot.exceptions.MedBotParserException;
import medbot.person.Patient;
import medbot.person.Person;
import medbot.person.Staff;
import medbot.utilities.ViewType;

import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static final String END_LINE = System.lineSeparator();

    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_EDIT = "edit";
    private static final String COMMAND_VIEW = "view";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_EXIT = "exit";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_SWITCH = "switch";
    private static final String COMMAND_FIND = "find";

    private static final String PARAMETER_NAME = "n/";
    private static final String PARAMETER_PHONE = "p/";
    private static final String PARAMETER_EMAIL = "e/";
    private static final String PARAMETER_IC = "i/";
    private static final String PARAMETER_ADDRESS = "a/";
    private static final int PARAMETER_BUFFER = 2;

    private static final String VIEW_TYPE_PATIENT_VIEW = "p";
    private static final String VIEW_TYPE_MEDICAL_STAFF_VIEW = "m";
    private static final String VIEW_TYPE_SCHEDULER_VIEW = "s";

    private static final String ERROR_WRONG_COMMAND = "Unable to parse command." + END_LINE;
    private static final String ERROR_NO_VIEW_FOUND = "Unidentified view." + END_LINE;

    private static final String ERROR_NO_PARAMETER = "No parameters given" + END_LINE;

    private static final String ERROR_ID_NOT_SPECIFIED = "ID not specified or not a number." + END_LINE;
    private static final String ERROR_PATIENT_ID_NOT_SPECIFIED = "Patient ID not specified." + END_LINE;
    private static final String ERROR_STAFF_ID_NOT_SPECIFIED = "Staff ID not specified." + END_LINE;

    private static final String ERROR_INVALID_VIEW_TYPE = "Invalid view type code." + END_LINE;

    private static final String ERROR_NAME_NOT_SPECIFIED = "Name not specified." + END_LINE;

    private static final String ERROR_IC_NUMBER_NOT_SPECIFIED = "IC number not specified." + END_LINE;
    private static final String ERROR_IC_NUMBER_INCORRECT_FORMAT = "Incorrect IC number format." + END_LINE;

    private static final String ERROR_PHONE_NUMBER_NOT_SPECIFIED = "Phone number not specified." + END_LINE;
    private static final String ERROR_PHONE_NUMBER_TOO_FEW_DIGITS = "Phone number has too few digits." + END_LINE;
    private static final String ERROR_PHONE_NUMBER_TOO_MANY_DIGITS = "Phone number has too many digits." + END_LINE;
    private static final String ERROR_PHONE_NUMBER_UNEXPECTED_CHARACTERS =
            "Phone number contains unexpected characters." + END_LINE;

    private static final String ERROR_EMAIL_ADDRESS_NOT_SPECIFIED = "Email address not specified." + END_LINE;
    private static final String ERROR_EMAIL_ADDRESS_WRONG_FORMAT = "Incorrect email address format." + END_LINE;

    private static final String ERROR_ADDRESS_NOT_SPECIFIED = "Address not specified." + END_LINE;

    private static final String REGEX_VERTICAL_LINE = "\\|";
    private static final String REGEX_INPUT_PARAMETER = " [a-zA-Z]{1,2}/";
    private static final String REGEX_EMAIL = "(([a-zA-Z0-9][\\w-.]*[a-zA-Z0-9])|[a-zA-Z0-9])@([\\w]+\\.)+[\\w]+";
    private static final String REGEX_IC = "[STFGM][0-9]{7}[A-Z]";
    private static final String REGEX_PHONE_NUMBER = "[\\d]{8}";
    private static final String REGEX_PHONE_NUMBER_SEPARATOR = "[- _+()]";
    private static final String REGEX_PERSON_ID = "([0-9]+$)|([0-9]+ )";
    private static final String REGEX_CAPITALISE_POSITION = "(\\A|[ _-])[a-z]";

    private static final String VERTICAL_LINE = "|";
    private static final String SEPARATOR_SPACE = " ";

    private static final String EMPTY_STRING = "";

    private static ViewType viewType =  ViewType.PATIENT_INFO;

    public static ViewType getViewType() {
        return viewType;
    }

    public static void setViewType(ViewType viewType) {
        Parser.viewType = viewType;
    }

    /**
     * Checks the current view type that the parser is in and returns the relevant
     * parseCommand object based on the view type.
     *
     * @param userInput String containing the full user input.
     * @return the corresponding Command object.
     * @throws MedBotParserException if command is unrecognised.
     */
    public static Command parseCommand(String userInput) throws MedBotParserException {
        userInput = preprocessInput(userInput);
        //commands valid in all viewTypes
        if (userInput.startsWith(COMMAND_SWITCH)) {
            return parseSwitchCommand(userInput);
        }
        if (userInput.equals(COMMAND_EXIT)) {
            return new ExitCommand();
        }
        if (userInput.startsWith(COMMAND_HELP)) {
            return parseHelpCommand(userInput);
        }
        //commands valid in only some viewTypes
        switch (viewType) {
        case PATIENT_INFO:
            return parsePatientCommand(userInput);
        case MEDICAL_STAFF_INFO:
            return parseStaffCommand(userInput);
        case SCHEDULER:
            return parseSchedulingCommand(userInput);
        default:
            assert false;
            throw new MedBotParserException(ERROR_NO_VIEW_FOUND);
        }
    }


    /**
     * Parses the user input for patient information related commands and returns the relevant Command object.
     *
     * @param userInput String containing the full user input.
     * @return the corresponding Command object.
     * @throws MedBotParserException if command is unrecognised.
     */
    public static Command parsePatientCommand(String userInput) throws MedBotParserException {
        if (userInput.startsWith(COMMAND_ADD)) {
            return parseAddPatientCommand(userInput);
        }
        if (userInput.startsWith(COMMAND_DELETE)) {
            return parseDeletePatientCommand(userInput);
        }
        if (userInput.startsWith(COMMAND_VIEW)) {
            return parseViewPatientCommand(userInput);
        }
        if (userInput.equals(COMMAND_LIST)) {
            return new ListPatientCommand();
        }
        if (userInput.startsWith(COMMAND_EDIT)) {
            return parseEditPatientCommand(userInput);
        }
        if (userInput.startsWith(COMMAND_FIND)) {
            return parseFindPatientCommand(userInput);
        }
        throw new MedBotParserException(ERROR_WRONG_COMMAND);
    }

    /**
     * Parses the user input for patient information related commands and returns the relevant Command object.
     *
     * @param userInput String containing the full user input.
     * @return the corresponding Command object.
     * @throws MedBotParserException if command is unrecognised.
     */
    public static Command parseStaffCommand(String userInput) throws MedBotParserException {
        if (userInput.startsWith(COMMAND_ADD)) {
            return parseAddStaffCommand(userInput);
        }
        if (userInput.startsWith(COMMAND_DELETE)) {
            return parseDeleteStaffCommand(userInput);
        }
        if (userInput.startsWith(COMMAND_VIEW)) {
            return parseViewStaffCommand(userInput);
        }
        if (userInput.equals(COMMAND_LIST)) {
            return new ListStaffCommand();
        }
        if (userInput.startsWith(COMMAND_EDIT)) {
            return parseEditStaffCommand(userInput);
        }
        if (userInput.startsWith(COMMAND_FIND)) {
            return parseFindStaffCommand(userInput);
        }
        throw new MedBotParserException(ERROR_WRONG_COMMAND);
    }

    //Update with relevant scheduling commands
    public static Command parseSchedulingCommand(String userInput) throws MedBotParserException {
        throw new MedBotParserException(ERROR_WRONG_COMMAND);
    }

    //Parse Command Methods

    /**
     * Parses user input to pass relevant parameters into the HelpCommand constructor.
     *
     * @param userInput String containing the full user input.
     * @return HelpCommand object.
     * @throws MedBotParserException if parameters.length < 1 && > 2
     */
    private static HelpCommand parseHelpCommand(String userInput) throws MedBotParserException {
        String commandTypeString = EMPTY_STRING;
        try {
            commandTypeString = userInput.substring(4).strip();
        } catch (IndexOutOfBoundsException ie) {
            return new HelpCommand();
        }
        if (commandTypeString.equals(EMPTY_STRING)) {
            return new HelpCommand();
        }
        CommandType commandType = parseHelpCommandType(commandTypeString);
        return new HelpCommand(commandType);
    }

    private static CommandType parseHelpCommandType(String commandTypeString) throws MedBotParserException {
        switch (commandTypeString) {
        case COMMAND_ADD:
            return CommandType.ADD_PATIENT;
        case COMMAND_DELETE:
            return CommandType.DELETE_PATIENT;
        case COMMAND_EDIT:
            return CommandType.EDIT_PATIENT;
        case COMMAND_EXIT:
            return CommandType.EXIT;
        case COMMAND_HELP:
            return CommandType.HELP;
        case COMMAND_LIST:
            return CommandType.LIST_PATIENT;
        case COMMAND_SWITCH:
            return CommandType.SWITCH;
        case COMMAND_VIEW:
            return CommandType.VIEW_PATIENT;
        case COMMAND_FIND:
            return CommandType.FIND_PATIENT;
        default:
            throw new MedBotParserException(ERROR_WRONG_COMMAND);
        }
    }

    /**
     * Parses user input and returns ViewPatientCommand with the specified patient ID.
     *
     * @param userInput String containing the full user input.
     * @return ViewPatientCommand object.
     * @throws MedBotParserException when patient id is not specified or not a number.
     */
    private static ViewPatientCommand parseViewPatientCommand(String userInput) throws MedBotParserException {
        int personId = parsePersonId(userInput.substring(4));
        return new ViewPatientCommand(personId);
    }

    /**
     * Parses user input and returns ViewStaffCommand with the specified staff ID.
     *
     * @param userInput String containing the full user input.
     * @return ViewStaffCommand object.
     * @throws MedBotParserException when staff id is not specified or not a number.
     */
    private static ViewStaffCommand parseViewStaffCommand(String userInput) throws MedBotParserException {
        int personId = parsePersonId(userInput.substring(4));
        return new ViewStaffCommand(personId);
    }

    /**
     * Parses user input and returns DeletePatientCommand with the specified patient ID.
     *
     * @param userInput String containing the full user input.
     * @return DeletePatientCommand object.
     * @throws MedBotParserException when patient id given is not specified or not a number.
     */
    private static DeletePatientCommand parseDeletePatientCommand(String userInput) throws MedBotParserException {
        int personId = parsePersonId(userInput.substring(6));
        return new DeletePatientCommand(personId);
    }

    /**
     * Parses user input and returns DeleteStaffCommand with the specified staff ID.
     *
     * @param userInput String containing the full user input.
     * @return DeleteStaffCommand object.
     * @throws MedBotParserException when staff id given is not specified or not a number.
     */
    private static DeleteStaffCommand parseDeleteStaffCommand(String userInput) throws MedBotParserException {
        int personId = parsePersonId(userInput.substring(6));
        return new DeleteStaffCommand(personId);
    }

    /**
     * Parses user input and returns EditPatientCommand with the specified patient ID and parameters.
     *
     * @param userInput String containing the full user input.
     * @return EditPatientCommand object.
     * @throws MedBotParserException when patient id given is not specified or not a number, or when
     *                               the parameters given cannot be parsed.
     */
    private static EditPatientCommand parseEditPatientCommand(String userInput) throws MedBotParserException {
        int patientId = parsePersonId(userInput.substring(4));
        String[] parameters = getParameters(userInput);
        Patient patient = new Patient();
        patient.setNull();
        updateMultiplePersonalInformation(patient, parameters);
        return new EditPatientCommand(patientId, patient);
    }

    /**
     * Parses user input and returns EditStaffCommand with the specified patient ID and parameters.
     *
     * @param userInput String containing the full user input.
     * @return EditStaffCommand objects
     * @throws MedBotParserException when staff id given is not specified or not a number, or when
     *                               the parameters given cannot be parsed.
     */
    private static EditStaffCommand parseEditStaffCommand(String userInput) throws MedBotParserException {
        int staffId = parsePersonId(userInput.substring(4));
        String[] parameters = getParameters(userInput);
        Staff staff = new Staff();
        staff.setNull();
        updateMultiplePersonalInformation(staff, parameters);
        return new EditStaffCommand(staffId, staff);
    }

    /**
     * Parses user input and returns AddPatientCommand with the specified parameters.
     *
     * @param userInput String containing the full user input.
     * @return AddPatientCommand object.
     * @throws MedBotParserException when no parameters are specified, or when the parameters given cannot be parsed.
     */
    private static AddPatientCommand parseAddPatientCommand(String userInput) throws MedBotParserException {
        String[] parameters = getParameters(userInput);
        Patient patient = new Patient();
        updateMultiplePersonalInformation(patient, parameters);
        return new AddPatientCommand(patient);
    }

    /**
     * Parses user input and returns AddStaffCommand with the specified parameters.
     *
     * @param userInput String containing the full user input.
     * @return AddStaffCommand object.
     * @throws MedBotParserException when no parameters are specified, or when the parameters given cannot be parsed.
     */
    private static AddStaffCommand parseAddStaffCommand(String userInput) throws MedBotParserException {
        String[] parameters = getParameters(userInput);
        Staff staff = new Staff();
        updateMultiplePersonalInformation(staff, parameters);
        return new AddStaffCommand(staff);

    }

    private static FindPatientCommand parseFindPatientCommand(String userInput) throws MedBotParserException {
        String[] parameters = getParameters(userInput);
        return new FindPatientCommand(parameters);
    }

    private static FindStaffCommand parseFindStaffCommand(String userInput) throws MedBotParserException {
        String[] parameters = getParameters(userInput);
        return new FindStaffCommand(parameters);
    }

    /**
     * Processes user input and returns a SwitchCommand.
     *
     * <p>If view type is specified, returns a switch command with that new view type. If not, returns
     * a switch command that iterates through the various views.
     *
     * @param userInput String containing the full user input.
     * @return SwitchCommand with the new view type if specified
     * @throws MedBotParserException if an invalid view type code is specified
     */
    private static SwitchCommand parseSwitchCommand(String userInput) throws MedBotParserException {
        String newType;
        try {
            newType = userInput.substring(6).strip();
        } catch (IndexOutOfBoundsException ie) {
            newType = EMPTY_STRING;
        }
        switch (newType) {
        case EMPTY_STRING:
            return new SwitchCommand(ViewType.getNextView(viewType));
        case VIEW_TYPE_PATIENT_VIEW:
            return new SwitchCommand(ViewType.PATIENT_INFO);
        case VIEW_TYPE_MEDICAL_STAFF_VIEW:
            return new SwitchCommand(ViewType.MEDICAL_STAFF_INFO);
        case VIEW_TYPE_SCHEDULER_VIEW:
            return new SwitchCommand(ViewType.SCHEDULER);
        default:
            throw new MedBotParserException(ERROR_INVALID_VIEW_TYPE);
        }
    }


    /**
     * Parses user input to the correct patient information format.
     *
     * @param userInput String containing the full user input.
     * @return The parameters list given by user.
     * @throws MedBotParserException when no parameters are specified.
     */
    private static String[] getParameters(String userInput) throws MedBotParserException {
        String processedInput = preprocessMultiAttributeInput(userInput);
        String[] words = processedInput.split(REGEX_VERTICAL_LINE);
        if (words.length == 1) {
            throw new MedBotParserException(ERROR_NO_PARAMETER);
        }
        assert words.length > 1;
        String[] parameters = Arrays.copyOfRange(words, 1, words.length);
        assert parameters.length >= 1;
        return parameters;
    }

    /**
     * Preprocesses user input to remove invalid substring that can not be parsed.
     *
     * @param userInput The initial user input.
     * @return user input without leading white space and vertical lines present.
     */
    private static String preprocessInput(String userInput) {
        return userInput.strip().replace(VERTICAL_LINE, EMPTY_STRING);
    }

    /**
     * Parses attributeStrings array and modifies all the corresponding attribute in person.
     *
     * @param person           Person whose personal information will be updated
     * @param attributeStrings String Array containing Strings of an attribute specifier and the corresponding
     *                         personal information
     * @throws MedBotParserException if the attributeString contains missing/invalid information
     */
    public static void updateMultiplePersonalInformation(Person person, String[] attributeStrings)
            throws MedBotParserException {
        for (String attributeString : attributeStrings) {
            updatePersonalInformation(person, attributeString);
        }
    }

    /**
     * Parses attributeString and modifies the corresponding attribute in person.
     *
     * @param person          Person whose personal information will be updated
     * @param attributeString String containing an attribute specifier and the corresponding personal information
     * @throws MedBotParserException if the attributeString contains missing/invalid information
     */
    public static void updatePersonalInformation(Person person, String attributeString) throws MedBotParserException {
        if (attributeString.startsWith(PARAMETER_NAME)) {
            String name = parseName(attributeString.substring(PARAMETER_BUFFER));
            person.setName(name);
            return;
        }
        if (attributeString.startsWith(PARAMETER_PHONE)) {
            String phoneNumber = parsePhoneNumber(attributeString.substring(PARAMETER_BUFFER));
            person.setPhoneNumber(phoneNumber);
            return;
        }
        if (attributeString.startsWith(PARAMETER_EMAIL)) {
            String email = parseEmailAddress(attributeString.substring(PARAMETER_BUFFER));
            person.setEmailAddress(email);
            return;
        }
        if (attributeString.startsWith(PARAMETER_IC)) {
            String icNumber = parseIcNumber(attributeString.substring(PARAMETER_BUFFER));
            person.setIcNumber(icNumber);
        }
        if (attributeString.startsWith(PARAMETER_ADDRESS)) {
            String address = parseResidentialAddress(attributeString.substring(PARAMETER_BUFFER));
            person.setResidentialAddress(address);
        }
    }

    /**
     * Returns a String containing the names specified in attributeString, with each name capitalised.
     *
     * @param attributeString String containing the name to be parsed
     * @return String containing the name specified in attributeString
     * @throws MedBotParserException if no name is given
     */
    private static String parseName(String attributeString) throws MedBotParserException {
        try {
            String name = attributeString.strip();
            if (name.equals(EMPTY_STRING)) {
                throw new MedBotParserException(ERROR_NAME_NOT_SPECIFIED);
            }
            return capitaliseEachWord(name);
        } catch (IndexOutOfBoundsException ie) {
            throw new MedBotParserException(ERROR_NAME_NOT_SPECIFIED);
        }
    }

    /**
     * Returns a String containing the IC number specified in attributeString.
     *
     * <p>Checks if the resultant String is of the right IC number format
     *
     * @param attributeString String containing the IC number to be parsed
     * @return String containing the IC number specified in attributeString
     * @throws MedBotParserException if IC number is not specified, or is in the wrong format
     */
    private static String parseIcNumber(String attributeString) throws MedBotParserException {
        try {
            String icString = attributeString.toUpperCase().strip();
            if (icString.equals(EMPTY_STRING)) {
                throw new MedBotParserException(ERROR_IC_NUMBER_NOT_SPECIFIED);
            }
            if (!icString.matches(REGEX_IC)) {
                throw new MedBotParserException(ERROR_IC_NUMBER_INCORRECT_FORMAT);
            }
            assert icString.length() == 9;
            return icString;
        } catch (IndexOutOfBoundsException ie) {
            throw new MedBotParserException(ERROR_IC_NUMBER_NOT_SPECIFIED);
        }
    }

    /**
     * Returns a String containing the phone number specified in attributeString.
     *
     * <p>Removes special characters "- _+()" and checks if the length of the resultant String is 8
     *
     * @param attributeString String containing the phone number to be parsed
     * @return String containing the phone number specified in attributeString
     * @throws MedBotParserException if the phone number is not specified,
     *                               has too many/few digits or contains unexpected characters
     */
    private static String parsePhoneNumber(String attributeString) throws MedBotParserException {
        try {
            String numberString = attributeString.replaceAll(REGEX_PHONE_NUMBER_SEPARATOR, EMPTY_STRING).strip();
            if (numberString.equals(EMPTY_STRING)) {
                throw new MedBotParserException(ERROR_PHONE_NUMBER_NOT_SPECIFIED);
            }
            if (numberString.length() > 8) {
                throw new MedBotParserException(ERROR_PHONE_NUMBER_TOO_MANY_DIGITS);
            }
            if (numberString.length() < 8) {
                throw new MedBotParserException(ERROR_PHONE_NUMBER_TOO_FEW_DIGITS);
            }
            if (!numberString.matches(REGEX_PHONE_NUMBER)) {
                throw new MedBotParserException(ERROR_PHONE_NUMBER_UNEXPECTED_CHARACTERS);
            }
            return numberString;
        } catch (IndexOutOfBoundsException ie) {
            throw new MedBotParserException(ERROR_PHONE_NUMBER_NOT_SPECIFIED);
        }
    }

    /**
     * Returns a String containing the email address specified in attributeString.
     *
     * <p>Checks if the resultant String is of the right email format
     *
     * @param attributeString String containing the email address to be parsed
     * @return String containing the email address specified in attributeString
     * @throws MedBotParserException if the email address is not specified or is in the wrong format
     */
    private static String parseEmailAddress(String attributeString) throws MedBotParserException {
        try {
            String emailString = attributeString.strip();
            if (emailString.equals(EMPTY_STRING)) {
                throw new MedBotParserException(ERROR_EMAIL_ADDRESS_NOT_SPECIFIED);
            }
            if (!emailString.matches(REGEX_EMAIL)) {
                throw new MedBotParserException(ERROR_EMAIL_ADDRESS_WRONG_FORMAT);
            }
            return emailString;
        } catch (IndexOutOfBoundsException ie) {
            throw new MedBotParserException(ERROR_EMAIL_ADDRESS_NOT_SPECIFIED);
        }
    }

    /**
     * Returns the String containing the address specified in attributeString, with each word capitalised.
     *
     * <p>Capitalises each word in the address
     *
     * @param attributeString String containing the address to be parsed
     * @return String containing the address specified in attributeString
     * @throws MedBotParserException if address is not specified
     */
    private static String parseResidentialAddress(String attributeString) throws MedBotParserException {
        try {
            String addressString = attributeString.strip();
            if (addressString.equals(EMPTY_STRING)) {
                throw new MedBotParserException(ERROR_ADDRESS_NOT_SPECIFIED);
            }
            return capitaliseEachWord(addressString);
        } catch (IndexOutOfBoundsException ie) {
            throw new MedBotParserException(ERROR_ADDRESS_NOT_SPECIFIED);
        }
    }


    private static int parsePersonId(String userInput) throws MedBotParserException {
        userInput = userInput.strip();
        if (userInput.equals(EMPTY_STRING)) {
            throw new MedBotParserException(ERROR_ID_NOT_SPECIFIED);
        }
        Pattern pattern = Pattern.compile(REGEX_PERSON_ID);
        Matcher matcher = pattern.matcher(userInput);
        if (matcher.lookingAt()) {
            int id;
            try {
                id = Integer.parseInt(matcher.group().stripTrailing());
            } catch (NumberFormatException ne) {
                assert false;
                throw new MedBotParserException(ERROR_ID_NOT_SPECIFIED);
            }
            return id;
        }
        throw new MedBotParserException(ERROR_ID_NOT_SPECIFIED);
    }


    /**
     * Sets the first letter of each word of each word to uppercase and sets all others to lowercase.
     *
     * <p>A letter is considered the first letter of a word if it is the first letter of the input String or
     * is immediately after any of the characters " _-".
     *
     * @param input String which will be capitalised
     * @return String with each word capitalised
     */
    private static String capitaliseEachWord(String input) {
        input = input.toLowerCase();
        Function<MatchResult, String> multiAttributeReplacementFunction = x -> {
            String match = x.group();
            if (match.length() == 1) {
                //if substring is the first character of the string
                return match.toUpperCase();
            } else {
                //if substring consists of one of the characters " _-" followed by a letter
                return match.charAt(0) + match.substring(1).toUpperCase();
            }
        };
        Pattern pattern = Pattern.compile(REGEX_CAPITALISE_POSITION);
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll(multiAttributeReplacementFunction);
    }

    /**
     * Places a "|" separator before each attribute specifier in input and returns the resultant string.
     *
     * <p>Attribute specifiers are in the following formats "a/" or "ab/" where a and b can be any uppercase
     * or lowercase alphabet.
     *
     * @param input userInput String containing attribute specifiers
     * @return String containing added separator characters
     */
    private static String preprocessMultiAttributeInput(String input) {
        //replacement function to add a "|" character before an attribute specifier
        Function<MatchResult, String> replacementFunction = x ->
                SEPARATOR_SPACE + VERTICAL_LINE + x.group().substring(1);
        Pattern pattern = Pattern.compile(REGEX_INPUT_PARAMETER);
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll(replacementFunction);
    }

}
