package medbot.list;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeSet;

import medbot.Appointment;
import medbot.exceptions.MedBotException;

public class PersonalAppointmentList {
    private static final String ERROR_APPOINTMENT_ID_NOT_SET = "Appointment ID is not set.";
    private static final String ERROR_ADD_APPOINTMENT_CLASH = "New appointment clashes with another appointment.";
    private static final String END_LINE = System.lineSeparator();

    private final NavigableSet<Appointment> appointments = new TreeSet<>((o1, o2) -> {
        if (o1.getDateTimeCode() > o2.getDateTimeCode()) {
            return 1;
        } else if (o1.getDateTimeCode() < o2.getDateTimeCode()) {
            return -1;
        }
        assert o1.getDateTimeCode() == o2.getDateTimeCode();
        return 0;
    });

    public PersonalAppointmentList() {

    }

    public void addAppointment(Appointment appointment) throws MedBotException {
        int appointmentId = appointment.getAppointmentId();
        if (appointmentId == 0) {
            throw new MedBotException(ERROR_APPOINTMENT_ID_NOT_SET);
        }
        boolean isNotClash = appointments.add(appointment);
        if (!isNotClash) {
            throw new MedBotException(ERROR_ADD_APPOINTMENT_CLASH);
        }

    }

    public int getAppointmentId(int dateTimeCode) {
        for (Appointment appointment : appointments) {
            if (appointment != null && appointment.getDateTimeCode() == dateTimeCode) {
                return appointment.getAppointmentId();
            }
        }
        return -1;
    }

    public void deleteAppointmentByTime(int dateTimeCode) throws MedBotException {
        Iterator<Appointment> it = appointments.iterator();
        while (it.hasNext()) {
            Appointment appointment = it.next();
            if (appointment.getDateTimeCode() == dateTimeCode) {
                it.remove();
                return;
            }
        }
        throw new MedBotException(getAppointmentNotFoundErrorMessage(dateTimeCode));
    }

    private String getAppointmentNotFoundErrorMessage(int dateTimeCode) {
        return "No appointment at : " + Appointment.getDateTimeString(dateTimeCode) + "found.";
    }

    public String listAppointments() {
        String output = "";
        for (Appointment appointment : appointments) {
            output += appointment + END_LINE;
        }
        return output;
    }
}
