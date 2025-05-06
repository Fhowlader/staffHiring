/**
 * Represents a full-time staff hire.
 * Inherits from StaffHire and includes additional attributes for salary and weekly hours.
 */
public class FullTimeStaffHire extends StaffHire
{
    private double salary;          // Monthly salary for full-time staff
    private int weeklyHours;        // Weekly working hours

    // Constructor initializes both inherited and additional fields
    public FullTimeStaffHire(int vacancyNumber, String designation, String jobType, String staffName,
                             String joiningDate, String qualification, String appointedBy, boolean joined,
                             double salary, int weeklyHours)
    {
        super(vacancyNumber, designation, jobType, staffName, joiningDate, qualification, appointedBy, joined);
        this.salary = salary;
        this.weeklyHours = weeklyHours;
    }

    // Returns salary
    public double getSalary() { return salary; }

    // Sets a new salary only if the staff has already joined
    public void setSalary(double salary)
    {
        if (getJoined()) {
            this.salary = salary;
        } else {
            System.out.println("Staff is not appointed yet. Cannot set salary.");
        }
    }

    // Returns weekly hours
    public int getWeeklyHours() { return weeklyHours; }

    // Sets new weekly hours only if staff has joined
    public void setWeeklyHours(int weeklyHours)
    {
        if (getJoined()) {
            this.weeklyHours = weeklyHours;
        } else {
            System.out.println("Staff is not appointed yet. Cannot set hours.");
        }
    }

    /**
     * Displays all staff details including full-time specific fields if staff has joined.
     */
    @Override
    public String display()
    {
        String info = super.display();
        if (getJoined()) {
            info += "Salary: " + salary + "\n"
                 + "Weekly Hours: " + weeklyHours + "\n";
        }
        return info;
    }
}
