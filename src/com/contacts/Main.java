package com.contacts;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Main {

    public static void main(String[] args) {
        ContactBook contactBook = new ContactBook();
        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.print("Enter action (add, remove, edit, count, info, exit): ");

            Action action = Action.findByLabel(scanner.next());

            switch (action) {
                case ADD:
                    contactBook.add();
                    break;

                case REMOVE:
                    contactBook.remove();
                    break;

                case EDIT:
                    contactBook.edit();
                    break;

                case COUNT:
                    contactBook.count();
                    break;

                case INFO:
                    contactBook.info();
                    break;

                case EXIT:
                    return;

            }
        }
    }
}

abstract class Contact {

    private String name;
    private String number;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    Contact() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean hasNumber() {
        return !number.isEmpty();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    protected void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

class ContactBook {

    List<Contact> contacts = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);

    ContactManagerFactory managerFactory = new ContactManagerFactory();

    public void add() {

        System.out.print("Enter the type (person, organization): ");
        String type = scanner.next();

        ContactManager manager = managerFactory.createFactory(type);
        Contact contact = manager.add();
        contacts.add(contact);

        System.out.println("The record added.\n");
    }

    private int readIndex() {
        System.out.print("Select a record: ");
        int itemList = Integer.parseInt(scanner.next());
        return itemList - 1;
    }

    private int readIndexForOrg(){
        System.out.print("Enter index to show info: ");
        int itemList = Integer.parseInt(scanner.next());
        return itemList - 1;

    }

    public void edit() {
        if (contacts.size() == 0) {
            System.out.println("No records to edit!");
            return;
        }

        list();

        int index = readIndex();

        Contact contact = contacts.get(index);

        ContactManager manager = managerFactory.createFactory(contact);

        manager.edit();

        contacts.set(index, contact);

        System.out.println("The record updated!\n");

    }

    public void remove() {
        if (contacts.size() == 0) {
            System.out.println("No records to remove!");
            return;
        }

        this.list();
        int index = readIndex();
        contacts.remove(index);

        System.out.println("The record removed!");
    }

    public void count() {
        System.out.printf("The Phone Book has %d records.\n", contacts.size());
    }

    public void info() {
        if (contacts.size() == 0) {
            System.out.println("No records to list!");
            return;
        }

        list();

        int index = readIndexForOrg();

        Contact contact = contacts.get(index);

        ContactManager manager = managerFactory.createFactory(contact);

        manager.info();

    }

    public void list() {
        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            System.out.printf("%d. %s\n", i + 1, contact.getName());
        }
    }
}

class Person extends Contact {

    private String surname;
    private LocalDate birthDate;
    private Character gender;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public Character getGender() {
        return gender;
    }
}

class Organization extends Contact {

    private String address;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}

class ContactFactory {
    public Contact createContact(String type) {

        switch (type.toUpperCase()) {
            case "PERSON":
                return new Person();

            case "ORGANIZATION":
                return new Organization();

            default:
                return null;
        }

    }
}

abstract class ContactManager {

    Scanner scanner = new Scanner(System.in);
    Contact contact;

    ContactManager(Contact contact) {
        this.contact = contact;
    }

    public abstract Contact add();

    public abstract void edit();

    public abstract void info();

    void readPhone() {
        System.out.print("Enter the number: ");
        String number = scanner.nextLine();

        contact.setNumber(number);

        if (!validNumber(contact.getNumber())) {
            contact.setNumber("");
            System.out.println("Wrong number format!");
        }
    }

    void printPhone() {
        String phoneNumber = contact.hasNumber() ? contact.getNumber() : "[no data]";
        System.out.printf("Number: %s\n", phoneNumber);
    }

    void printCreatedTime() {
        String date =  contact.getCreatedAt().toString().substring(0, 16);
        System.out.printf("Time created: %s\n", date);
    }

    void printUpdatedTime() {
        String date =  contact.getUpdatedAt().toString().substring(0, 16);
        System.out.printf("Time last edit: %s\n\n", date);
    }

    private boolean validNumber(String number) {
        String regex = "\\+?(\\([0-9a-zA-Z]+\\)([-\\s][0-9a-zA-Z]{2,})*|[0-9a-zA-Z]+([-\\s]\\([0-9a-zA-Z]{2,}\\))?([-\\s][0-9a-zA-Z]{2,})*)";
        return number.matches(regex);
    }

}

class ContactManagerFactory {

    public ContactManager createFactory(String type) {

        ContactFactory factory = new ContactFactory();
        Contact contact = factory.createContact(type);

        switch (type.toUpperCase()) {

            case "PERSON":
                return new PersonContactManager(contact);

            case "ORGANIZATION":
                return new OrganizationContactManager(contact);

            default:
                return null;
        }
    }

    public ContactManager createFactory(Contact contact) {

        if (contact.getClass() == Person.class) {
            return new PersonContactManager(contact);
        }

        if (contact.getClass() == Organization.class) {
            return new OrganizationContactManager(contact);
        }

        return null;
    }
}

class PersonContactManager extends ContactManager {

    final private Person person;

    PersonContactManager(Contact contact) {
        super(contact);
        person = (Person)contact;
    }

    @Override
    public Contact add() {

        readName();

        readSurname();

        readBirthDate();

        readGender();

        readPhone();

        return person;

    }

    protected void readName() {
        System.out.print("Enter the name: ");
        String name = scanner.nextLine();
        person.setName(name);
    }


    private void readSurname() {
        System.out.print("Enter the surname: ");
        String surname = scanner.nextLine();
        person.setSurname(surname);
    }

    private void readBirthDate() {
        System.out.print("Enter the birth date: ");
        String birthDateIn = scanner.nextLine();

        LocalDate birthDate = null;
        try {
            birthDate = LocalDate.parse(birthDateIn);
        } catch (DateTimeParseException e) {
            System.out.println("Bad birth date!");
        }

        person.setBirthDate(birthDate);
    }

    private void readGender() {
        System.out.print("Enter the gender (M, F): ");
        String gender = scanner.nextLine();

        if (!gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("F")) {
            System.out.println("Bad gender!");
        } else {
            person.setGender(gender.toUpperCase().charAt(0));
        }
    }

    @Override
    public void edit() {

        System.out.print("Select a field (name, surname, birth, gender, number): ");
        Fields field = Fields.findFieldsByLabel(scanner.nextLine());

        switch (field) {
            case NAME:
                readName();
                break;

            case SURNAME:
                readSurname();
                break;

            case BIRTH:
                readBirthDate();
                break;

            case GENDER:
                readGender();
                break;

            case NUMBER:
                readPhone();
                break;
        }

        person.setUpdatedAt(LocalDateTime.now());
    }

    public void info() {

        printName();

        printSurname();

        printBirthDate();

        printGender();

        printPhone();

        printCreatedTime();

        printUpdatedTime();
    }

    private void printName() {
        System.out.printf("Name: %s\n", contact.getName());
    }

    private void printBirthDate() {
        String birthDate = person.getBirthDate() != null ? person.getBirthDate().toString(): "[no data]";
        System.out.printf("Birth date: %s\n", birthDate);
    }

    private void printSurname() {
        System.out.printf("Surname: %s\n", person.getSurname());
    }

    private void printGender() {
        String gender = person.getGender() != null ? person.getGender().toString(): "[no data]";
        System.out.printf("Gender: %s\n", gender);
    }
}

class OrganizationContactManager extends  ContactManager{

    Scanner scanner = new Scanner(System.in);

    final private Organization organization;

    OrganizationContactManager(Contact contact) {
        super(contact);
        organization = (Organization) contact;
    }

    @Override
    public Contact add() {

        readName();

        readAddress();

        readPhone();

        return organization;

    }

    private void readName() {
        System.out.print("Enter the name: ");
        String name = scanner.nextLine();
        organization.setName(name);
    }

    private void readAddress(){
        System.out.print("Enter the address: ");
        String address = scanner.nextLine();
        organization.setAddress(address);
    }

    @Override
    public void edit() {

        System.out.print("Select a field (name, address, number): ");
        Fields field = Fields.findFieldsByLabel(scanner.nextLine());

        switch (field){
            case NAME:
                readName();
                break;
            case ADDRESS:
                readAddress();
                break;
            case NUMBER:
                readPhone();
                break;
        }

        organization.setUpdatedAt(LocalDateTime.now());
    }

    public void info() {

        printName();

        printAddress();

        printPhone();

        printCreatedTime();

        printUpdatedTime();

    }

    private void printName(){
        System.out.printf("Organization name: %s\n", organization.getName());
    }

    private  void printAddress(){
        System.out.printf("Address: %s\n", organization.getAddress());
    }

}

enum Fields{

    NAME("name"),
    SURNAME("surname"),
    ADDRESS("address"),
    BIRTH ("birth"),
    GENDER("gender"),
    NUMBER("number");

    private String fieldLabel;

    Fields(String fieldLabel){ this.fieldLabel = fieldLabel; }

    public String getFieldLabel() { return fieldLabel; }

    public static Fields findFieldsByLabel(String fieldLabel){
        for (Fields fieldType : Fields.values()) {
            if (fieldLabel.equalsIgnoreCase(fieldType.getFieldLabel())) {
                return fieldType;
            }
        }
        return null;
    }
}

enum Action {

    ADD("add"),
    REMOVE("remove"),
    EDIT("edit"),
    COUNT("count"),
    INFO("info"),
    EXIT("exit");

    private String actionLabel;

    Action(String actionLabel){ this.actionLabel = actionLabel; }

    public String getActionLabel() { return actionLabel; }

    public static Action findByLabel(String actionLabel){
        for (Action type : Action.values()) {
            if (actionLabel.equalsIgnoreCase(type.getActionLabel())) {
                return type;
            }
        }
        return null;
    }

}
