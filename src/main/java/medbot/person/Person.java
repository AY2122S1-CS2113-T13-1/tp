package medbot.person;


import static medbot.ui.Ui.VERTICAL_LINE_SPACED;
import static medbot.ui.Ui.END_LINE;

import java.util.LinkedList;

import medbot.list.ListItem;

import medbot.Appointment;
import medbot.exceptions.MedBotException;

import medbot.list.PersonalAppointmentList;
import medbot.utilities.FilterType;

public abstract class Person extends ListItem {
    private static final String PARAMETER_NAME = "n/";
    private static final String PARAMETER_PHONE = "p/";
    private static final String PARAMETER_EMAIL = "e/";
    private static final String PARAMETER_IC = "i/";
    private static final String PARAMETER_ADDRESS = "a/";
    private static final int PARAMETER_BUFFER = 2;

    private static final String SPACE = " ";

    private static final int LENGTH_ID_COLUMN = 4;
    private static final int LENGTH_IC_COLUMN = 9;
    private static final int LENGTH_NAME_COLUMN = 20;
    private static final int LENGTH_PHONE_NUM_COLUMN = 9;
    private static final int LENGTH_EMAIL_COLUMN = 20;
    private static final int LENGTH_ADDRESS_COLUMN = 20;


    private int personId = 0;
    protected String icNumber = "";
    protected String name = "";
    protected String phoneNumber = "";
    protected String emailAddress = "";
    protected String residentialAddress = "";
    protected PersonType personType;
    protected PersonalAppointmentList personalAppointmentList = new PersonalAppointmentList();
    protected boolean isArchived = false;

    public String toString() {
        return END_LINE
                + "IC: " + icNumber + END_LINE
                + "Name: " + name + END_LINE
                + "H/P: " + phoneNumber + END_LINE
                + "Email: " + emailAddress + END_LINE
                + "Address: " + residentialAddress;
    }

    public String getInfoInTableFormat() {
        return VERTICAL_LINE_SPACED + getFormattedPersonId()
                + VERTICAL_LINE_SPACED + getFormattedIcNumber()
                + VERTICAL_LINE_SPACED + getFormattedName()
                + VERTICAL_LINE_SPACED + getFormattedPhoneNumber()
                + VERTICAL_LINE_SPACED + getFormattedEmail()
                + VERTICAL_LINE_SPACED + getFormattedAddress()
                + VERTICAL_LINE_SPACED;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public void setNull() {
        icNumber = null;
        name = null;
        phoneNumber = null;
        emailAddress = null;
        residentialAddress = null;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void archive() {
        isArchived = true;
    }

    public void unarchive() {
        isArchived = false;
    }

    /**
     * Returns the appointmentId of the appointment at the specified time code, or -1 if there is none.
     *
     * @param dateTimeCode the dateTimeCode to search for
     * @return the appointmentId of the appointment with that dateTimeCode, or -1 if there is none
     */
    public int getAppointmentId(int dateTimeCode) {
        return personalAppointmentList.getAppointmentId(dateTimeCode);
    }

    /**
     * Returns an LinkedList of the appointmentId of all appointments.
     *
     * @return LinkedList of the appointmentId of all appointments
     */
    public LinkedList<Integer> getAllAppointmentIds() {
        return personalAppointmentList.getAllAppointmentIds();
    }

    /**
     * Adds the given appointment to the appointment list.
     *
     * @param appointment Appointment to be added to the appointment list
     * @throws MedBotException if there is another appointment at that time
     */
    public void addAppointment(Appointment appointment) throws MedBotException {
        personalAppointmentList.addAppointment(appointment);
    }

    /**
     * Removes the appointment with the specified dateTimeCode.
     *
     * @param dateTimeCode the dateTimeCode of the appointment to be deleted
     * @throws MedBotException if there is no appointment with that dateTimeCode.
     */
    public void deleteAppointment(int dateTimeCode) throws MedBotException {
        personalAppointmentList.deleteAppointment(dateTimeCode);
    }

    public String listAppointments(FilterType filterType, int dateTimeCode) {
        switch (filterType) {
        case BEFORE:
            return personalAppointmentList.listAppointmentsBefore(dateTimeCode);
        case AFTER:
            return personalAppointmentList.listAppointmentsAfter(dateTimeCode);
        case NONE:
        default:
            return personalAppointmentList.listAppointments();
        }
    }

    /**
     * Text to be written to storage file of a person.
     *
     * @return storageString of a person
     */
    public String getStorageString() {

        return setAsStorageParameterOrNull(icNumber) + VERTICAL_LINE_SPACED
                + setAsStorageParameterOrNull(name) + VERTICAL_LINE_SPACED
                + setAsStorageParameterOrNull(phoneNumber) + VERTICAL_LINE_SPACED
                + setAsStorageParameterOrNull(emailAddress) + VERTICAL_LINE_SPACED
                + setAsStorageParameterOrNull(residentialAddress);
    }

    /**
     * Checks whether this person contains all the parameters specified.
     *
     * @param parameters the attributes to check.
     * @return true if all parameters are found, false otherwise.
     */
    public boolean containsAllParameters(String[] parameters) {
        for (String parameter : parameters) {
            if (!containsParameter(parameter)) {
                return false;
            }
        }
        return true;
    }

    private boolean containsParameter(String parameter) {
        String trimmedParameter = parameter.substring(PARAMETER_BUFFER).trim().toLowerCase();
        if (parameter.startsWith(PARAMETER_NAME)) {
            return getName().toLowerCase().contains(trimmedParameter);
        }
        if (parameter.startsWith(PARAMETER_IC)) {
            return getIcNumber().toLowerCase().contains(trimmedParameter);
        }
        if (parameter.startsWith(PARAMETER_PHONE)) {
            return getPhoneNumber().toLowerCase().contains(trimmedParameter);
        }
        if (parameter.startsWith(PARAMETER_EMAIL)) {
            return getEmailAddress().toLowerCase().contains(trimmedParameter);
        }
        if (parameter.startsWith(PARAMETER_ADDRESS)) {
            return getResidentialAddress().toLowerCase().contains(trimmedParameter);
        }

        return false;
    }


    private String formattedAttribute(String attribute, int outputLength) {
        int attributeLength = attribute.length();
        String output = attribute;

        if (attributeLength > outputLength) {
            output = output.substring(0, outputLength - 3) + "...";
        }

        int remainingLength = outputLength - attributeLength;

        for (int i = 0; i < remainingLength; i++) {
            output += SPACE;
        }

        return output;
    }

    private String getFormattedPersonId() {
        return formattedAttribute(Integer.toString(personId), LENGTH_ID_COLUMN);
    }

    private String getFormattedIcNumber() {
        return formattedAttribute(icNumber, LENGTH_IC_COLUMN);
    }

    private String getFormattedName() {
        return formattedAttribute(name, LENGTH_NAME_COLUMN);
    }

    private String getFormattedPhoneNumber() {
        return formattedAttribute(phoneNumber, LENGTH_PHONE_NUM_COLUMN);
    }

    private String getFormattedEmail() {
        return formattedAttribute(emailAddress, LENGTH_EMAIL_COLUMN);
    }

    private String getFormattedAddress() {
        return formattedAttribute(residentialAddress, LENGTH_ADDRESS_COLUMN);
    }


    //TODO: Change these to the native methods
    @Override
    public int getId() {
        return getPersonId();
    }

    @Override
    public void setId(int personId) {
        setPersonId(personId);
    }
}
