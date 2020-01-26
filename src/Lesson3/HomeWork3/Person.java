package Lesson3.HomeWork3;

import java.util.ArrayList;

public class Person {
    String name;
    ArrayList<String> email = new ArrayList<>();
    ArrayList<String> phone = new ArrayList<>();
    static int PERSON=0;

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, ArrayList<String> email, ArrayList<String> phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<String> getEmail() {
        return email;
    }

    public void setEmail(ArrayList<String> email) {
        this.email = email;
    }

    public ArrayList<String> getPhone() {
        return phone;
    }

    public void setPhone(ArrayList<String> phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return " {" +
                "name='" + name + '\'' +
                ", email=" + email +
                ", phone=" + phone +
                '}';
    }
}
