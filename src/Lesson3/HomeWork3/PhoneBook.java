package Lesson3.HomeWork3;

import Lesson3.HomeWork3.Person;

import java.util.*;

public class PhoneBook {

    public static void main(String[] args) {

        Person[] people = new Person[6];
        people[0] = new Person("Iury");
        people[1] = new Person("Yury");
        people[2] = new Person("Uriy");
        people[3] = new Person("Yuri");
        people[4] = new Person("Yurii");
        people[5] = new Person("Iurii");

        HashMap<String, Person> phoneBook = addPersonToPhoneBook(people);

        people[0].phone.add("1212121212");
        people[0].phone.add("2323232332");
        people[1].phone.add("3434343434");
        people[2].phone.add("4545454545");
        people[3].phone.add("5656565656");
        people[4].phone.add("6767676767");
        people[5].phone.add("7878787878");
        people[0].email.add("abtank@bk.ru");
        people[1].email.add("abtank@outlook.com");
        people[2].email.add("abtank@mail.ru");
        people[3].email.add("abtank@yandex.ru");
        people[4].email.add("abtank@outlook.com");
        people[5].email.add("abtankbkru@gmail.com");

        printPhoneBook(phoneBook);

        Scanner scanner = new Scanner(System.in);
        searchPhone(phoneBook, scanner);
        searchEmail(phoneBook, scanner);

    }

    public static void searchPhone(HashMap<String, Person> phoneBook, Scanner scanner) {
        System.out.println("Введите имя для поиска тел.:");
        String key;
        key = scanner.next();
        if (phoneBook.containsKey(key)) {
            System.out.println("тел.\n" + phoneBook.get(key).phone);
        } else {
            System.out.println("нет такого.");
        }

    }

    public static void searchEmail(HashMap<String, Person> phoneBook, Scanner scanner) {
        System.out.println("Введите имя для поиска email:");
        String key;
        key = scanner.next();
        if (phoneBook.containsKey(key)) {
            System.out.println("Email: " + phoneBook.get(key).email);
        } else {
            System.out.println("нет такого.");
        }
    }

    public static void printPhoneBook(HashMap<String, Person> phoneBook) {
        System.out.println("__THE____PHONEBOOK___");
        Set<Map.Entry<String, Person>> set = phoneBook.entrySet();
        for (
                Map.Entry<String, Person> entry : set) {
            System.out.println(entry.getKey() + entry.getValue());
        }
        System.out.println("__END____BOOK___");
    }

    private static HashMap<String, Person> addPersonToPhoneBook(Person[] people) {
        HashMap<String, Person> phoneBook = new HashMap<>();
        for (Person p : people
        ) {
            phoneBook.put(p.getName(), p);
        }
        return phoneBook;
    }
}
