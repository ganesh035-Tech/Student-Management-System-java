import javax.swing.*;
import java.awt.*;
import java.io.*;

public class StudentManagementGUI {

    JFrame frame;
    JTextField idField, nameField, courseField, phoneField;
    JTextArea displayArea;

    public StudentManagementGUI() {

        frame = new JFrame("Student Management System");
        frame.setSize(600,500);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Labels and Text Fields
        JLabel idLabel = new JLabel("ID");
        idLabel.setBounds(50,50,100,30);
        frame.add(idLabel);
        idField = new JTextField();
        idField.setBounds(150,50,150,30);
        frame.add(idField);

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(50,100,100,30);
        frame.add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(150,100,150,30);
        frame.add(nameField);

        JLabel courseLabel = new JLabel("Course");
        courseLabel.setBounds(50,150,100,30);
        frame.add(courseLabel);
        courseField = new JTextField();
        courseField.setBounds(150,150,150,30);
        frame.add(courseField);

        JLabel phoneLabel = new JLabel("Phone");
        phoneLabel.setBounds(50,200,100,30);
        frame.add(phoneLabel);
        phoneField = new JTextField();
        phoneField.setBounds(150,200,150,30);
        frame.add(phoneField);

        // Buttons
        JButton addBtn = new JButton("Add");
        addBtn.setBounds(350,50,100,30);
        frame.add(addBtn);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBounds(350,100,100,30);
        frame.add(searchBtn);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(350,150,100,30);
        frame.add(updateBtn);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(350,200,100,30);
        frame.add(deleteBtn);

        JButton displayBtn = new JButton("Display");
        displayBtn.setBounds(350,250,100,30);
        frame.add(displayBtn);

        // Display Area
        displayArea = new JTextArea();
        displayArea.setBounds(50,300,500,150);
        displayArea.setEditable(false);
        frame.add(displayArea);

        // Button Actions
        addBtn.addActionListener(e -> addStudent());
        searchBtn.addActionListener(e -> searchStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        displayBtn.addActionListener(e -> displayStudents());

        frame.setVisible(true);
    }

    // Validate input fields
    boolean validateInput() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String course = courseField.getText().trim();
        String phone = phoneField.getText().trim();

        if(id.isEmpty() || name.isEmpty() || course.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(frame,"All fields are required!");
            return false;
        }

        try {
            Integer.parseInt(id);
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,"ID must be a number!");
            return false;
        }

        if(!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(frame,"Phone must be 10 digits!");
            return false;
        }

        return true;
    }

    // Add student
    void addStudent() {
        if(!validateInput()) return;
        try (FileWriter fw = new FileWriter("Students.txt", true)) {
            fw.write(idField.getText().trim() + "," +
                     nameField.getText().trim() + "," +
                     courseField.getText().trim() + "," +
                     phoneField.getText().trim() + "\n");
            JOptionPane.showMessageDialog(frame,"Student Added");
            clearFields();
            displayStudents();
        } catch(Exception e) { System.out.println(e); }
    }

    // Display students
    void displayStudents() {
        try (BufferedReader br = new BufferedReader(new FileReader("Students.txt"))) {
            String line;
            displayArea.setText("");
            while((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) continue;
                displayArea.append(line + "\n");
            }
        } catch(Exception e){ System.out.println(e); }
    }

    // Search student by ID
    void searchStudent() {
        if(idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame,"Enter ID to search");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader("Students.txt"))) {
            String line;
            boolean found = false;
            while((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if(data.length < 4) continue;
                if(data[0].trim().equals(idField.getText().trim())) {
                    nameField.setText(data[1].trim());
                    courseField.setText(data[2].trim());
                    phoneField.setText(data[3].trim());
                    found = true;
                    break;
                }
            }
            if(!found) JOptionPane.showMessageDialog(frame,"Student Not Found");
        } catch(Exception e){ System.out.println(e); }
    }

    // Update student
    void updateStudent() {
        if(!validateInput()) return;

        int confirm = JOptionPane.showConfirmDialog(frame,"Are you sure to update this student?","Confirm Update",JOptionPane.YES_NO_OPTION);
        if(confirm != JOptionPane.YES_OPTION) return;

        try {
            File input = new File("Students.txt");
            File temp = new File("temp.txt");
            BufferedReader br = new BufferedReader(new FileReader(input));
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
            String line;
            while((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if(data.length < 4) continue;
                if(data[0].trim().equals(idField.getText().trim())) {
                    bw.write(idField.getText().trim() + "," +
                             nameField.getText().trim() + "," +
                             courseField.getText().trim() + "," +
                             phoneField.getText().trim());
                } else {
                    bw.write(line);
                }
                bw.newLine();
            }
            br.close(); bw.close();
            input.delete(); temp.renameTo(input);
            JOptionPane.showMessageDialog(frame,"Student Updated");
            clearFields();
            displayStudents();
        } catch(Exception e){ System.out.println(e); }
    }

    // Delete student
    void deleteStudent() {
        if(idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame,"Enter ID to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame,"Are you sure to delete this student?","Confirm Delete",JOptionPane.YES_NO_OPTION);
        if(confirm != JOptionPane.YES_OPTION) return;

        try {
            File input = new File("Students.txt");
            File temp = new File("temp.txt");
            BufferedReader br = new BufferedReader(new FileReader(input));
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
            String line;
            while((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if(data.length < 4) continue;
                if(!data[0].trim().equals(idField.getText().trim())) {
                    bw.write(line);
                    bw.newLine();
                }
            }
            br.close(); bw.close();
            input.delete(); temp.renameTo(input);
            JOptionPane.showMessageDialog(frame,"Student Deleted");
            clearFields();
            displayStudents();
        } catch(Exception e){ System.out.println(e); }
    }

    // Clear input fields
    void clearFields() {
        idField.setText("");
        nameField.setText("");
        courseField.setText("");
        phoneField.setText("");
    }

    public static void main(String[] args){
        new StudentManagementGUI();
    }
}