package Source;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.function.IntBinaryOperator;

public class Appointment
{
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int custID;
    private int userID;
    private int contactID;

    /**
     * Lambda: here I used a Lambda expression to shorten my code so I didn't have to write the same subtraction over and over again.
     * @param appointmentID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param custID
     * @param userID
     * @param contactID
     */
    public Appointment(int appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int custID, int userID, int contactID) {
        LocalDateTime nowUTC = LocalDateTime.now(Clock.systemUTC());
        LocalDateTime nowLocal = LocalDateTime.now(Clock.systemDefaultZone());
        int offSet = 0;
        IntBinaryOperator diff = (n1, n2) -> n1 - n2;

        if(nowUTC.isAfter(nowLocal) && diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()) > 0)
        {
            offSet = 0 - (diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()));
        }
        else if (nowUTC.isAfter(nowLocal) && diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()) < 0)
        {
            offSet = 0 - (24 + (diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour())));
        }
        else if (nowLocal.isAfter(nowUTC) && diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()) > 0)
        {
            offSet = 0 - (diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()));
        }
        else if (nowLocal.isAfter(nowUTC) && diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour()) < 0)
        {
            offSet = (24 - (diff.applyAsInt(nowUTC.getHour(), nowLocal.getHour())));
        }

        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start.plusHours(offSet);
        this.end = end.plusHours(offSet);
        this.custID = custID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     * @return the appointment ID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * @param appointmentID
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * @return the appointment title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the appointment description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the appointment location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the appointment type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the appointment start date and time
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * @param start
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * @return the appointment end date and time
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * @param end
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * @return the customer ID
     */
    public int getCustID() {
        return custID;
    }

    /**
     * @param custID
     */
    public void setCustID(int custID) {
        this.custID = custID;
    }

    /**
     * @return the user ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return the contact ID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * @param contactID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
}
