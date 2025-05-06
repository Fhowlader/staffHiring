java/**
 * Represents a general staff hire.
 * Stores common attributes shared between full-time and part-time staff.
 */
public class StaffHire
{
    // Common attributes for any staff
    private int vacancyNumber;
    private String designation;
    private String jobType;
    private String staffName;
    private String joiningDate;
    private String qualification;
    private String appointedBy;
    private boolean joined;

    // Constructor initializes all fields
    public StaffHire(int vacancyNumber, String designation, String jobType, String staffName,
                     String joiningDate, String qualification, String appointedBy, boolean joined)
    {
        this.vacancyNumber = vacancyNumber;
        this.designation = designation;
        this.jobType = jobType;
        this.staffName = staffName;
        this.joiningDate = joiningDate;
        this.qualification = qualification;
        this.appointedBy = appointedBy;
        this.joined = joined;
    }

    // Getter methods return values of attributes
    public int getVacancyNumber() { return vacancyNumber; }
    public String getDesignation() { return designation; }
    public String getJobType() { return jobType; }
    public String getStaffName() { return staffName; }
    public String getJoiningDate() { return joiningDate; }
    public String getQualification() { return qualification; }
    public String getAppointedBy() { return appointedBy; }
    public boolean getJoined() { return joined; }

    // Setter methods update values of attributes
    public void setVacancyNumber(int vacancyNumber) { this.vacancyNumber = vacancyNumber; }
    public void setDesignation(String designation) { this.designation = designation; }
    public void setJobType(String jobType) { this.jobType = jobType; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    public void setJoiningDate(String joiningDate) { this.joiningDate = joiningDate; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public void setAppointedBy(String appointedBy) { this.appointedBy = appointedBy; }
    public void setJoined(boolean joined) { this.joined = joined; }

    /**
     * Returns all relevant details of the staff in string format.
     */
    public String display()
    {
        return "Vacancy Number: " + vacancyNumber + "\n"
             + "Designation: " + designation + "\n"
             + "Job Type: " + jobType + "\n"
             + "Staff Name: " + staffName + "\n"
             + "Joining Date: " + joiningDate + "\n"
             + "Qualification: " + qualification + "\n"
             + "Appointed By: " + appointedBy + "\n"
             + "Joined: " + joined + "\n";
    }
}
