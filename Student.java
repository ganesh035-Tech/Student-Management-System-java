public class Student {

    int id;
    String name;
    String course;
    String phone;

    public Student(int id, String name, String course, String phone) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.phone = phone;
    }

    public String toFileString() {
        return id + "," + name + "," + course + "," + phone;
    }
}