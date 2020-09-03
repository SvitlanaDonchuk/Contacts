package com.contacts;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Scanner scanner = new Scanner(System.in);

        ContactBook contactBook;
        File file;

        try {
            file = new File("././phonebook.db");
            contactBook = (ContactBook) ContactBook.deserialize(file.getName());
            System.out.printf("open %s\n\n", file.getName());

        }catch(FileNotFoundException e){
            file = new File("././phonebook.db");
            contactBook = new ContactBook();
            System.out.printf("open %s\n\n", file.getName());
        }

        Action type;

        do {

            System.out.print("[menu] Enter action (add, list, search, count, exit): ");

            type = Action.findByLabel(scanner.next());

            if (type != null) {
                switch (type) {
                    case ADD:
                        contactBook.add();
                        break;
                    case LIST:
                        contactBook.info();
                        break;

                    case SEARCH:
                        contactBook.search();
                        break;

                    case COUNT:
                        contactBook.count();
                        break;
                }
            }
        } while (type != Action.EXIT);

        ContactBook.serialize(contactBook, "phonebook.db");

    }
}

class ContactBook implements Serializable{

    private static final long serialVersionUID = 3L;

    List<Contact> contacts = new ArrayList<>();
    List<Contact> queryList;

    public void add() {

        Scanner scanner = new Scanner(System.in);
        ContactManagerFactory managerFactory = new ContactManagerFactory();

        System.out.print("Enter the type (person, organization): ");
        String type = scanner.next();

        ContactManager manager = managerFactory.createFactory(type);
        Contact contact = manager.add();
        contacts.add(contact);

        System.out.println("A record added.\n");
    }

    public void edit(int i) {

        Scanner scanner = new Scanner(System.in);
        ContactManagerFactory managerFactory = new ContactManagerFactory();

        if (contacts.size() == 0) {
            System.out.println("No records to edit!");
            return;
        }

        Contact contact = contacts.get(i);

        ContactManager manager = managerFactory.createFactory(contact);

        manager.edit();

        contacts.set(i, contact);

        System.out.println("Saved");

        manager.info();

        System.out.print("[record] Enter action (edit, delete, menu): ");
        String recordType = scanner.next();

        if(recordType.equalsIgnoreCase(String.valueOf(Action.MENU))){
            System.out.println();
            return;
        }
        if(recordType.equalsIgnoreCase(String.valueOf(Action.EDIT))){
            edit(i);
        }
        if(recordType.equalsIgnoreCase(String.valueOf(Action.DELETE))){
            remove(i);
        }
    }

    public void editSearch(List<Contact> queryList, int i) {
        Scanner scanner = new Scanner(System.in);
        ContactManagerFactory managerFactory = new ContactManagerFactory();

        if (contacts.size() == 0) {
            System.out.println("No records to edit!");
            return;
        }

        Contact contact = queryList.get(i);

        ContactManager manager = managerFactory.createFactory(contact);

        manager.edit();

        queryList.set(i, contact);

        System.out.println("Saved");

        manager.info();

        System.out.print("[record] Enter action (edit, delete, menu): ");
        String recordType = scanner.next();

        if(recordType.equalsIgnoreCase(String.valueOf(Action.MENU))){
            System.out.println();
            return;
        }
        if(recordType.equalsIgnoreCase(String.valueOf(Action.EDIT))){
            editSearch(queryList, i);
        }
        if(recordType.equalsIgnoreCase(String.valueOf(Action.DELETE))){
            removeSearch(queryList, i);
        }
    }

    public void removeSearch(List<Contact> queryList, int i){
        if (queryList.size() == 0) {
            System.out.println("No records to remove!\n");
            return;
        }
        Contact contact = queryList.get(i);

        contacts.remove(contact);

        System.out.println("The record removed!\n");
    }

    public void remove(int i) {
        if (contacts.size() == 0) {
            System.out.println("No records to remove!\n");
            return;
        }
        Contact contact = contacts.get(i);

        contacts.remove(contact);

        System.out.println("The record removed!\n");
    }

    public void count() {
        System.out.printf("The Phone Book has %d records.\n", contacts.size());
        System.out.println();
    }

    public void info() {

        Scanner scanner = new Scanner(System.in);
        ContactManagerFactory managerFactory = new ContactManagerFactory();

        if (contacts.size() == 0) {
            System.out.println("No records to list!\n");
            return;
        }

        list();

        System.out.print("\n[list] Enter action ([number], back): ");

        String str = scanner.next();
        Action commonAction = Action.findByLabel(str);

        if(commonAction == null) {
            int index = Integer.parseInt(String.valueOf(str));

            Contact contact = contacts.get(index - 1);

            ContactManager manager = managerFactory.createFactory(contact);

            manager.info();

            System.out.print("[record] Enter Action: (edit, delete, menu): ");
            String recordType = scanner.next();

            if(recordType.equalsIgnoreCase(String.valueOf(Action.MENU))){
                System.out.println();
                return;
            }
            if(recordType.equalsIgnoreCase(String.valueOf(Action.EDIT))){
                int i = Integer.parseInt(str);
                edit(i - 1);
            }
            if(recordType.equalsIgnoreCase(String.valueOf(Action.DELETE))){
                int i = Integer.parseInt(str);
                remove(i - 1);
            }

        }

        if(commonAction == Action.BACK){
            System.out.println();
        }
    }

    public void list() {

        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            if(contact instanceof Person) {
                Person person = (Person) contacts.get(i);
                System.out.printf("%d. %s %s\n", i + 1, person.getName(), person.getSurname());
            } else if (contact instanceof Organization){
                Organization organization = (Organization) contacts.get(i);
                System.out.printf("%d. %s\n", i + 1, organization.getName());
            }
        }

    }

    public void search(){

        Scanner scanner = new Scanner(System.in);
        ContactManagerFactory managerFactory = new ContactManagerFactory();

        System.out.print("Enter search query: ");

        String query = scanner.next();

        Pattern pattern = Pattern.compile( "(.*\\w*" + query + "\\w*.*)");

        queryList = new ArrayList<>();

        for (Contact contact : contacts) {
            if (contact.getName().toLowerCase().matches(String.valueOf(pattern))) {
                queryList.add(contact);
            }
        }

        System.out.println("Found " + queryList.size() + " results:");

        for (int i = 0; i < queryList.size(); i++) {
            System.out.println(i + 1 + ". " + queryList.get(i).getName());
        }

        System.out.println();
        System.out.print("[search] Enter action ([number], back, again): ");

        String s = scanner.next();
        Action searchAction = Action.findByLabel(s);

        if(searchAction == null) {
            int index = Integer.parseInt(String.valueOf(s));

            Contact contact = queryList.get(index - 1);

            ContactManager manager = managerFactory.createFactory(contact);

            manager.info();

            System.out.print("[record] Enter Action: (edit, delete, menu): ");
            String recordType = scanner.next();

            if(recordType.equalsIgnoreCase(String.valueOf(Action.MENU))){
                System.out.println();
                return;
            }
            if(recordType.equalsIgnoreCase(String.valueOf(Action.EDIT))){
                int i = Integer.parseInt(s);
                editSearch(queryList,i - 1);
            }
            if(recordType.equalsIgnoreCase(String.valueOf(Action.DELETE))){
                int i = Integer.parseInt(s);
                remove(i - 1);
            }
        }

        if(searchAction == Action.BACK){
            System.out.println();
        }

        if(searchAction == Action.AGAIN){
            search();
        }
    }

    public static void serialize(Object contact, String fileName) {

        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)))) {
            oos.writeObject(contact);
        } catch (IOException e) {
            System.out.println("Can't save file due to " + e);
        }

    }

    public static Object deserialize(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }
}

abstract class Contact implements Serializable {
    private static final long serialVersionUID = 3L;

    private String name;
    private String number;
    private final LocalDateTime createdAt;
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

class Person extends Contact {

    private static final long serialVersionUID = 3L;

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

    private static final long serialVersionUID = 3L;

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
        String date =  contact.getCreatedAt().toString().substring(0, 19);
        System.out.printf("Time created: %s\n", date);
    }

    void printUpdatedTime() {
        String date =  contact.getUpdatedAt().toString().substring(0, 19);
        System.out.printf("Time last edit: %s\n\n", date);
    }

    private boolean validNumber(String number) {
        String regex = "\\+?(\\([0-9a-zA-Z]+\\)([-\\s][0-9a-zA-Z]{2,})*|[0-9a-zA-Z]+([-\\s]\\([0-9a-zA-Z]{2,}\\))?([-\\s][0-9a-zA-Z]{2,})*)";
        return number.matches(regex);
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
        person.setSurname(Objects.requireNonNullElse(surname, "[no data]"));
    }

    private void readBirthDate() {
        System.out.print("Enter the birth date: ");
        String birthDateIn = scanner.nextLine();

        LocalDate birthDate = null;
        try {
            birthDate = LocalDate.parse(birthDateIn);
        } catch (DateTimeParseException e) {
            System.out.print("Bad birth date!\n");
        }

        person.setBirthDate(birthDate);
    }

    private void readGender() {
        System.out.print("Enter the gender (M, F): ");
        String gender = scanner.nextLine();

        if (gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("F")) {
            person.setGender(gender.toUpperCase().charAt(0));
        } else {
            System.out.print("Bad gender!\n");
        }
    }

    @Override
    public void edit() {

        System.out.print("Select a field (name, surname, birth, gender, number): ");
        PersonFields field = PersonFields.findFieldsByLabel(scanner.nextLine());

        if (field != null) {
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

        if(!address.equalsIgnoreCase(" ")){
            organization.setAddress(address);
        } else {
            organization.setAddress("[no data]");
        }
    }

    @Override
    public void edit() {

        System.out.print("Select a field (name, address, number): ");
        PersonFields field = PersonFields.findFieldsByLabel(scanner.nextLine());

        if (field != null) {
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

enum Action{
    MENU("menu"),
    ADD("add"),
    LIST("list"),
    SEARCH("search"),
    COUNT("count"),
    EDIT("edit"),
    DELETE("delete"),
    EXIT("exit"),
    BACK("back"),
    AGAIN("again")
    ;

    private final String label;

    Action(String label){ this.label = label; }

    public String getLabel() { return label; }

    public static Action findByLabel(String label){
        for (Action type : Action.values()) {
            if (label.equalsIgnoreCase(type.getLabel())) {
                return type;
            }
        }
        return null;
    }


}

enum PersonFields {

    NAME("name"),
    SURNAME("surname"),
    ADDRESS("address"),
    BIRTH ("birth"),
    GENDER("gender"),
    NUMBER("number");

    private final String fieldLabel;

    PersonFields(String fieldLabel){ this.fieldLabel = fieldLabel; }

    public String getFieldLabel() { return fieldLabel; }

    public static PersonFields findFieldsByLabel(String fieldLabel){
        for (PersonFields fieldType : PersonFields.values()) {
            if (fieldLabel.equalsIgnoreCase(fieldType.getFieldLabel())) {
                return fieldType;
            }
        }
        return null;
    }

}