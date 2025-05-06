import javax.swing.*;                     // For all Swing GUI components
import java.awt.*;                        // For layout and color controls
import java.awt.event.*;                  // For button click handling
import java.io.PrintWriter;               // For exporting data to text file
import java.io.IOException;               // For handling file write exceptions
import java.util.ArrayList;               // To store staff objects dynamically

/**
 * RecruitmentSystem is a GUI-based staff management application.
 * 
 * It allows users to add, edit, search, display, export, and manage
 * full-time and part-time staff records using Swing UI.
 */
public class RecruitmentSystem extends JFrame implements ActionListener
{
    // Stores all staff entries (both full-time and part-time)
    private ArrayList<StaffHire> staffList = new ArrayList<>();
    
    // Form input fields (textboxes) for staff data
    private JTextField vacancyNumberField, designationField, jobTypeField, staffNameField, joiningDateField,
            qualificationField, appointedByField, salaryField, weeklyHoursField,
            workingHourField, wagesPerHourField, shiftsField, displayNumberField,
            searchVacancyField, searchNameField;

    // Joined checkbox
    private JCheckBox joinedCheckBox;

    // Buttons for user interactions
    private JButton addFullTimeButton, addPartTimeButton, setSalaryButton, setShiftsButton,
            terminateButton, displayButton, clearButton, searchButton, exportButton, summaryButton;

    // Toggle for dark mode
    private JCheckBox darkModeToggle;
    private boolean isDarkMode = false; // Tracks current theme


    /**
     * Constructs the GUI layout and initializes all components.
     */
    public RecruitmentSystem()
    {
        setTitle("Recruitment System");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel to hold all input fields and buttons
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;// Tracks current row in GridBag

        // Labels for all form fields
        JLabel[] labels = {
            new JLabel("Vacancy Number:"), new JLabel("Designation:"), new JLabel("Job Type:"),
            new JLabel("Staff Name:"), new JLabel("Joining Date (dd/mm/yyyy):"), new JLabel("Qualification:"),
            new JLabel("Appointed By:"), new JLabel("Joined:"),
            new JLabel("Salary (Full Time):"), new JLabel("Weekly Hours (Full Time):"),
            new JLabel("Working Hour (Part Time):"), new JLabel("Wages Per Hour (Part Time):"),
            new JLabel("Shifts (Part Time):"), new JLabel("Display Number:"),
            new JLabel("Search by Vacancy #:"), new JLabel("Or by Staff Name:")
        };

        // Corresponding text fields (some left null intentionally like checkbox)
        JTextField[] fields = {
            vacancyNumberField = new JTextField(15), designationField = new JTextField(15), jobTypeField = new JTextField(15),
            staffNameField = new JTextField(15), joiningDateField = new JTextField(15), qualificationField = new JTextField(15),
            appointedByField = new JTextField(15), null,
            salaryField = new JTextField(15), weeklyHoursField = new JTextField(15),
            workingHourField = new JTextField(15), wagesPerHourField = new JTextField(15),
            shiftsField = new JTextField(15), displayNumberField = new JTextField(15),
            searchVacancyField = new JTextField(15), searchNameField = new JTextField(15)
        };

        // Loop through fields and labels, placing each pair into the layout
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = row;
            mainPanel.add(labels[i], gbc);

            gbc.gridx = 1;
            if (fields[i] != null) {
                mainPanel.add(fields[i], gbc);
                fields[i].setToolTipText("Enter " + labels[i].getText().replace(":", ""));
            } else {
                // Special case: checkbox for 'Joined'
                joinedCheckBox = new JCheckBox();
                mainPanel.add(joinedCheckBox, gbc);
                joinedCheckBox.setToolTipText("Check if staff has joined");
            }
            row++;
        }

        // Button Panel holds all the action buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 10, 10));

        // Instantiate all buttons
        addFullTimeButton = new JButton("Add Full Time Staff");
        addPartTimeButton = new JButton("Add Part Time Staff");
        setSalaryButton = new JButton("Set Salary");
        setShiftsButton = new JButton("Set Working Shifts");
        terminateButton = new JButton("Terminate Part Time Staff");
        displayButton = new JButton("Display Number");
        clearButton = new JButton("Clear");
        searchButton = new JButton("Search Staff");
        exportButton = new JButton("Export All Staff");
        summaryButton = new JButton("Show Summary");

        // Store buttons in array for batch setup
        JButton[] buttons = { addFullTimeButton, addPartTimeButton, setSalaryButton, setShiftsButton,
                terminateButton, displayButton, clearButton, searchButton, exportButton, summaryButton };
        
        // Add tooltip + listener to each button and add to panel
        for (JButton b : buttons) {
            b.setFont(new Font("Arial", Font.BOLD, 12));
            b.addActionListener(this);
            b.setToolTipText("Click to " + b.getText());
            buttonPanel.add(b);
        }

        // Add button panel to main layout
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);
        row++;

        // Create dark mode toggle in top-right
        darkModeToggle = new JCheckBox("Dark Mode");
        darkModeToggle.setToolTipText("Toggle between light and dark mode");
        darkModeToggle.addActionListener(e -> toggleDarkMode(mainPanel));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.add(darkModeToggle, BorderLayout.EAST);
        
        // Center mainPanel in the window using wrapper
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.add(mainPanel);

        // Add everything to the JFrame
        add(topBar, BorderLayout.NORTH);
        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        setVisible(true);
    }

     /**
     * Validates that a required input field is not left empty.
     * Displays a message if the field is empty and returns true (error).
     */
    private boolean isEmpty(JTextField field, String fieldName)
    {
        if (field.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, fieldName + " cannot be empty.");
            return true;
        }
        return false;
    }

    /**
     * Validates whether the joining date is in the format dd/mm/yyyy.
     * This helps maintain consistency and avoids invalid input.
     */
    private boolean isValidDateFormat(String date)
    {
        return date.matches("\\d{2}/\\d{2}/\\d{4}");
    }

    /**
     * Checks if a vacancy number already exists in the staff list.
     * Prevents duplication of staff entries with the same ID.
     */
    private boolean isVacancyNumberDuplicate(int vacancyNumber) {
        for (StaffHire staff : staffList) {
            // If it's a terminated part-time staff, skip the check
            if (staff instanceof PartTimeStaffHire) {
                if (((PartTimeStaffHire) staff).isTerminated()) {
                    continue;
                }
            }
            if (staff.getVacancyNumber() == vacancyNumber) {
                return true;
            }
        }
        return false;
    }

    /**
     * Applies styling for dark or light mode across all components.
     * Dynamically adjusts colors for background, text, and buttons.
     */
    private void toggleDarkMode(JPanel panel)
    {
        isDarkMode = !isDarkMode;
        Color bg = isDarkMode ? new Color(40, 40, 40) : Color.LIGHT_GRAY;
        Color fg = isDarkMode ? Color.WHITE : Color.BLACK;
        Color buttonBg = isDarkMode ? new Color(70, 70, 70) : UIManager.getColor("Button.background");

        panel.setBackground(bg);
        
        // Traverse and restyle components
        for (Component c : panel.getComponents()) {
            if (c instanceof JLabel || c instanceof JTextField || c instanceof JCheckBox) {
                c.setBackground(bg);
                c.setForeground(fg);
            }
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                b.setBackground(buttonBg);
                b.setForeground(fg);
                b.setFocusPainted(false);
                b.setOpaque(true);
                b.setBorderPainted(true);
            }
            // Also recursively style inner panels (like the button grid)
            if (c instanceof JPanel) {
                c.setBackground(bg);
                for (Component inner : ((JPanel) c).getComponents()) {
                    if (inner instanceof JButton) {
                        inner.setBackground(buttonBg);
                        inner.setForeground(fg);
                    } else if (inner instanceof JLabel || inner instanceof JTextField || inner instanceof JCheckBox) {
                        inner.setBackground(bg);
                        inner.setForeground(fg);
                    }
                }
            }
        }
    }


    /**
     * Opens a scrollable dialog displaying all staff records in formatted form.
     * This acts as a quick summary viewer for all entries in the system.
     */
    private void showSummary() {
        StringBuilder sb = new StringBuilder();
        for (StaffHire s : staffList) {
            // Skip terminated part-time staff
            if (s instanceof PartTimeStaffHire) {
                if (((PartTimeStaffHire) s).isTerminated()) {
                    continue;
                }
            }
            sb.append(s.display()).append("\n--------------------------\n");
        }
    
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(this, scroll, "Staff Summary", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * This method handles all button actions using event source checks.
     * Each branch corresponds to a button in the interface.
     */
    public void actionPerformed(ActionEvent e)
    {
        try {
            // === ADD FULL-TIME STAFF ===
            if (e.getSource() == addFullTimeButton) {
                // Validate required fields
                if (isEmpty(vacancyNumberField, "Vacancy Number") ||
                    isEmpty(designationField, "Designation") ||
                    isEmpty(jobTypeField, "Job Type") ||
                    isEmpty(staffNameField, "Staff Name") ||
                    isEmpty(joiningDateField, "Joining Date") ||
                    isEmpty(qualificationField, "Qualification") ||
                    isEmpty(appointedByField, "Appointed By") ||
                    isEmpty(salaryField, "Salary") ||
                    isEmpty(weeklyHoursField, "Weekly Hours")) return;
                
                // Check if date format is valid
                if (!isValidDateFormat(joiningDateField.getText())) {
                    JOptionPane.showMessageDialog(this, "Joining Date must be in dd/mm/yyyy format.");
                    return;
                }

                int vacancyNumber = Integer.parseInt(vacancyNumberField.getText());

                // Prevent duplicate vacancy numbers
                if (isVacancyNumberDuplicate(vacancyNumber)) {
                    JOptionPane.showMessageDialog(this, "Vacancy Number already exists.");
                    return;
                }
                
                // Create and add new FullTimeStaffHire object
                FullTimeStaffHire fullTime = new FullTimeStaffHire(
                        vacancyNumber,
                        designationField.getText(),
                        jobTypeField.getText(),
                        staffNameField.getText(),
                        joiningDateField.getText(),
                        qualificationField.getText(),
                        appointedByField.getText(),
                        joinedCheckBox.isSelected(),
                        Double.parseDouble(salaryField.getText()),
                        Integer.parseInt(weeklyHoursField.getText())
                );
                staffList.add(fullTime);
                JOptionPane.showMessageDialog(this, "Full Time Staff added successfully.");
            
             // === ADD PART-TIME STAFF ===
            } else if (e.getSource() == addPartTimeButton) {
                if (isEmpty(vacancyNumberField, "Vacancy Number") ||
                    isEmpty(designationField, "Designation") ||
                    isEmpty(jobTypeField, "Job Type") ||
                    isEmpty(staffNameField, "Staff Name") ||
                    isEmpty(joiningDateField, "Joining Date") ||
                    isEmpty(qualificationField, "Qualification") ||
                    isEmpty(appointedByField, "Appointed By") ||
                    isEmpty(workingHourField, "Working Hour") ||
                    isEmpty(wagesPerHourField, "Wages Per Hour") ||
                    isEmpty(shiftsField, "Shifts")) return;

                if (!isValidDateFormat(joiningDateField.getText())) {
                    JOptionPane.showMessageDialog(this, "Joining Date must be in dd/mm/yyyy format.");
                    return;
                }

                int vacancyNumber = Integer.parseInt(vacancyNumberField.getText());
                if (isVacancyNumberDuplicate(vacancyNumber)) {
                    JOptionPane.showMessageDialog(this, "Vacancy Number already exists.");
                    return;
                }

                PartTimeStaffHire partTime = new PartTimeStaffHire(
                        vacancyNumber,
                        designationField.getText(),
                        jobTypeField.getText(),
                        staffNameField.getText(),
                        joiningDateField.getText(),
                        qualificationField.getText(),
                        appointedByField.getText(),
                        joinedCheckBox.isSelected(),
                        Integer.parseInt(workingHourField.getText()),
                        Double.parseDouble(wagesPerHourField.getText()),
                        shiftsField.getText()
                );
                staffList.add(partTime);
                JOptionPane.showMessageDialog(this, "Part Time Staff added successfully.");
            
            // === SET SALARY FOR FULL-TIME ===
            } else if (e.getSource() == setSalaryButton) {
                if (isEmpty(vacancyNumberField, "Vacancy Number") || isEmpty(salaryField, "Salary")) return;
                int confirm = JOptionPane.showConfirmDialog(this, "Update salary?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;

                int vacancy = Integer.parseInt(vacancyNumberField.getText());
                for (StaffHire s : staffList) {
                    if (s.getVacancyNumber() == vacancy && s instanceof FullTimeStaffHire) {
                        ((FullTimeStaffHire) s).setSalary(Double.parseDouble(salaryField.getText()));
                        JOptionPane.showMessageDialog(this, "Salary updated.");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "No matching Full Time Staff found.");
            
             // === SET SHIFT FOR PART-TIME ===
            } else if (e.getSource() == setShiftsButton) {
                if (isEmpty(vacancyNumberField, "Vacancy Number") || isEmpty(shiftsField, "Shifts")) return;
                int confirm = JOptionPane.showConfirmDialog(this, "Update shifts?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;

                int vacancy = Integer.parseInt(vacancyNumberField.getText());
                for (StaffHire s : staffList) {
                    if (s.getVacancyNumber() == vacancy && s instanceof PartTimeStaffHire) {
                        ((PartTimeStaffHire) s).setShifts(shiftsField.getText());
                        JOptionPane.showMessageDialog(this, "Shifts updated.");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "No matching Part Time Staff found.");
            
            // === TERMINATE PART-TIME ===
            } else if (e.getSource() == terminateButton) {
                if (isEmpty(vacancyNumberField, "Vacancy Number")) return;
                int confirm = JOptionPane.showConfirmDialog(this, "Terminate staff?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;

                int vacancy = Integer.parseInt(vacancyNumberField.getText());
                for (StaffHire s : staffList) {
                    if (s.getVacancyNumber() == vacancy && s instanceof PartTimeStaffHire) {
                        ((PartTimeStaffHire) s).terminate();
                        JOptionPane.showMessageDialog(this, "Staff terminated.");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "No matching Part Time Staff found.");
            
             // === DISPLAY STAFF BY INDEX ===
            } else if (e.getSource() == displayButton) {
                if (isEmpty(displayNumberField, "Display Number")) return;
                int index = Integer.parseInt(displayNumberField.getText());
                if (index >= 0 && index < staffList.size()) {
                    JOptionPane.showMessageDialog(this, staffList.get(index).display());
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid index.");
                }
            
             // === SEARCH STAFF ===
            } else if (e.getSource() == searchButton) {
                String vacancyInput = searchVacancyField.getText().trim();
                String nameInput = searchNameField.getText().trim().toLowerCase();

                for (StaffHire s : staffList) {
                    if (!vacancyInput.isEmpty()) {
                        try {
                            if (s.getVacancyNumber() == Integer.parseInt(vacancyInput)) {
                                JOptionPane.showMessageDialog(this, s.display());
                                return;
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                    if (!nameInput.isEmpty() && s.getStaffName().toLowerCase().contains(nameInput)) {
                        JOptionPane.showMessageDialog(this, s.display());
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Staff not found.");
            
            // === EXPORT STAFF TO FILE ===
            } else if (e.getSource() == exportButton) {
                try (PrintWriter writer = new PrintWriter("staff_list.txt")) {
                    for (StaffHire s : staffList) {
                        writer.println(s.display());
                        writer.println("------------------------------");
                    }
                    JOptionPane.showMessageDialog(this, "Exported to staff_list.txt");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage());
                }
            // === SHOW SUMMARY (scrollable view) ===
            } else if (e.getSource() == summaryButton) {
                showSummary();
            
            // === CLEAR FORM ===
            } else if (e.getSource() == clearButton) {
                // Reset all form inputs to empty
                vacancyNumberField.setText("");
                designationField.setText("");
                jobTypeField.setText("");
                staffNameField.setText("");
                joiningDateField.setText("");
                qualificationField.setText("");
                appointedByField.setText("");
                salaryField.setText("");
                weeklyHoursField.setText("");
                workingHourField.setText("");
                wagesPerHourField.setText("");
                shiftsField.setText("");
                displayNumberField.setText("");
                searchVacancyField.setText("");
                searchNameField.setText("");
                joinedCheckBox.setSelected(false);
            }
        } catch (Exception ex) {
            // Generic error handler to catch unanticipated input problems
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage());
        }
    }

    /**
     * Entry point to launch the GUI application.
     */
    public static void main(String[] args)
    {
        new RecruitmentSystem();
    }
}
