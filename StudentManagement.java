import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class Student {
    int rollno;
    String name;
    float marks;
    Student next;

    Student(int rollno, String name, float marks) {
        this.rollno = rollno;
        this.name = name;
        this.marks = marks;
        this.next = null;
    }
}

class StudentDatabase {
    private Student head = null;
    private int count = 0;
    private Scanner sc = new Scanner(System.in);

    public StudentDatabase() {
        loadStudent();
    }

    public void addStudent() {
        count++;
        System.out.println("Enter the name and marks of the student:");
        String name = sc.next();
        float marks = sc.nextFloat();

        Student newStudent = new Student(count, name, marks);

        if (head == null || newStudent.rollno < head.rollno) {
            newStudent.next = head;
            head = newStudent;
        } else {
            Student temp = head;
            while (temp.next != null && newStudent.rollno > temp.next.rollno) {
                temp = temp.next;
            }
            newStudent.next = temp.next;
            temp.next = newStudent;
        }

        System.out.println("Data recorded successfully.");
    }

    public void deleteStudent() {
        if (head == null) {
            System.out.println("No Student records found.");
            return;
        }

        System.out.println("Delete student by \n1) Rollno\n2) Name");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Enter Rollno:");
                int rollno = sc.nextInt();
                deleteByRollNo(rollno);
                break;

            case 2:
                System.out.println("Enter Name:");
                String name = sc.next();
                deleteByName(name);
                break;

            default:
                System.out.println("Invalid option.");
        }
    }

    private void deleteByRollNo(int rollno) {
        Student temp = head;
        Student prev = null;

        while (temp != null) {
            if (temp.rollno == rollno) {
                if (prev == null) {
                    head = temp.next;
                } else {
                    prev.next = temp.next;
                }
                updateRollNumber();
                count--;
                System.out.println("Record is deleted.");
                return;
            }
            prev = temp;
            temp = temp.next;
        }

        System.out.println("Roll number not found.");
    }

    private void deleteByName(String name) {
        Student temp = head;
        Student prev = null;

        while (temp != null) {
            if (temp.name.equals(name)) {
                if (prev == null) {
                    head = temp.next;
                } else {
                    prev.next = temp.next;
                }
                updateRollNumber();
                count--;
                System.out.println("Record is deleted.");
                return;
            }
            prev = temp;
            temp = temp.next;
        }

        System.out.println("Name not found.");
    }

    public void showStudent() {
        if (head == null) {
            System.out.println("No records found.");
            return;
        }

        Student temp = head;
        while (temp != null) {
            System.out.println(
                "Rollno: " + temp.rollno +
                " Name: " + temp.name +
                " Marks: " + temp.marks
            );
            temp = temp.next;
        }
    }

    public void modifyStudent() {
        if (head == null) {
            System.out.println("No Student records found.");
            return;
        }

        System.out.println("Modify student by \n1) Rollno\n2) Name");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Enter Rollno:");
                int rollno = sc.nextInt();
                modifyByRollNo(rollno);
                break;

            case 2:
                System.out.println("Enter Name:");
                String name = sc.next();
                modifyByName(name);
                break;

            default:
                System.out.println("Invalid option.");
        }
    }

    private void modifyByRollNo(int rollno) {
        Student temp = head;

        while (temp != null) {
            if (temp.rollno == rollno) {
                System.out.println("Enter new name and marks:");
                temp.name = sc.next();
                temp.marks = sc.nextFloat();
                return;
            }
            temp = temp.next;
        }

        System.out.println("Record not found.");
    }

    private void modifyByName(String name) {
        Student temp = head;

        while (temp != null) {
            if (temp.name.equals(name)) {
                System.out.println("Enter new marks:");
                temp.marks = sc.nextFloat();
                return;
            }
            temp = temp.next;
        }

        System.out.println("Record not found.");
    }

    public void saveStudent() {
        try {
            FileWriter writer = new FileWriter("Studentdata.txt");
            Student temp = head;

            writer.write("RollNo->Name->Marks\n");
            while (temp != null) {
                writer.write(
                    temp.rollno + "->" +
                    temp.name + "->" +
                    temp.marks + "\n"
                );
                temp = temp.next;
            }

            writer.close();
            System.out.println("Data saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        head = null;
        count = 0;
        System.out.println("Deleted all records.");
        saveStudent();
    }

    public void reverseList() {
        if (head == null) {
            System.out.println("No records found.");
            return;
        }

        Student prev = null;
        Student curr = head;
        Student next;

        while (curr != null) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        head = prev;
        System.out.println("List is reversed.");
    }

    public void exitProgram() {
        System.exit(0);
    }

    private void updateRollNumber() {
        int rollno = 1;
        Student temp = head;

        while (temp != null) {
            temp.rollno = rollno++;
            temp = temp.next;
        }
    }

    private void loadStudent() {
        try (BufferedReader reader = new BufferedReader(
                new FileReader("Studentdata.txt"))) {

            String line = reader.readLine(); // skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("->");
                if (parts.length < 3) continue;

                int rollno = Integer.parseInt(parts[0]);
                String name = parts[1];
                float marks = Float.parseFloat(parts[2]);

                Student newStudent = new Student(rollno, name, marks);

                if (head == null) {
                    head = newStudent;
                } else {
                    Student temp = head;
                    while (temp.next != null) {
                        temp = temp.next;
                    }
                    temp.next = newStudent;
                }
                count = rollno;
            }

            System.out.println("Data loaded from file.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("No previous data found. Starting fresh.");
        }
    }
}

public class StudentManagement {
    public static void main(String[] args) {
        StudentDatabase sd = new StudentDatabase();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println(
                "\n1) AddStudent 2) DeleteStudent 3) ShowStudent " +
                "4) ModifyStudent 5) SaveStudent 6) DeleteAll " +
                "7) ReverseList 8) ExitProgram"
            );

            System.out.println("Enter your choice:");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    sd.addStudent();
                    break;
                case 2:
                    sd.deleteStudent();
                    break;
                case 3:
                    sd.showStudent();
                    break;
                case 4:
                    sd.modifyStudent();
                    break;
                case 5:
                    sd.saveStudent();
                    break;
                case 6:
                    sd.deleteAll();
                    break;
                case 7:
                    sd.reverseList();
                    break;
                case 8:
                    sd.exitProgram();
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}