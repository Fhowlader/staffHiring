/**
 * Represents a part-time staff hire.
 * Extends StaffHire with part-time-specific attributes and logic for termination and shifts.
 */
public class PartTimeStaffHire extends StaffHire
{
    private int workingHour;        // Daily working hours
    private double wagesPerHour;    // Hourly wage
    private String shifts;          // Shift type (e.g., Morning)
    private boolean terminated;     // Indicates if the staff has been terminated

    // Constructor initializes all fields including inherited and part-time-specific ones
    public PartTimeStaffHire(int vacancyNumber, String designation, String jobType, String staffName,
                             String joiningDate, String qualification, String appointedBy, boolean joined,
                             int workingHour, double wagesPerHour, String shifts)
    {
        super(vacancyNumber, designation, jobType, staffName, joiningDate, qualification, appointedBy, joined);
        this.workingHour = workingHour;
        this.wagesPerHour = wagesPerHour;
        this.shifts = shifts;
        this.terminated = false;
    }

    // Accessor methods for part-time attributes
    public int getWorkingHour() { return workingHour; }
    public void setWorkingHour(int workingHour) { this.workingHour = workingHour; }

    public double getWagesPerHour() { return wagesPerHour; }
    public void setWagesPerHour(double wagesPerHour) { this.wagesPerHour = wagesPerHour; }

    public String getShifts() { return shifts; }

    // Sets shifts only if the staff has joined
    public void setShifts(String shifts)
    {
        if (getJoined()) {
            this.shifts = shifts;
        } else {
            System.out.println("Staff has not joined yet. Cannot set shifts.");
        }
    }

    public boolean isTerminated() { return terminated; }

    /**
     * Terminates the staff and clears personal details.
     */
    public void terminate()
    {
        if (terminated) {
            System.out.println("Staff is already terminated.");
        } else {
            setStaffName("");
            setJoiningDate("");
            setQualification("");
            setAppointedBy("");
            setJoined(false);
            terminated = true;
        }
    }

    /**
     * Displays all staff details, including part-time specific attributes and income.
     */
    @Override
    public String display()
    {
        String info = super.display();
        if (getJoined()) {
            double incomePerDay = workingHour * wagesPerHour;
            info += "Working Hour: " + workingHour + "\n"
                 + "Wages Per Hour: " + wagesPerHour + "\n"
                 + "Shifts: " + shifts + "\n"
                 + "Terminated: " + terminated + "\n"
                 + "Income Per Day: " + incomePerDay + "\n";
        }
        return info;
    }
}
