package com.contacts;

import java.util.*;

class Main {
    public static void main(String[] args) {

        final Scanner scanner = new Scanner(System.in);
        Map<Integer, Human> humanHashMap = new HashMap<>();

        MenuType menuType;

        do {

            System.out.print("Enter action (add, remove, edit, count, list, exit): ");

            menuType = MenuType.findByLabel(scanner.next());

            switch (menuType){
                case ADD:
                    functionAdd(humanHashMap);
                    break;

                case REMOVE:
                    functionRemove(humanHashMap);
                    break;

                case EDIT:
                    functionEdit(humanHashMap);
                    break;

                case COUNT:
                    functionCount(humanHashMap);
                    break;

                case LIST:
                    functionList(humanHashMap);
                    break;

            }

        } while(!menuType.equals(menuType.EXIT));

    }

    public static void functionAdd(Map<Integer, Human> contacts){

        final Scanner scanner = new Scanner(System.in);
        final Human.humanBuilder humanBuilder = new Human.humanBuilder();

        System.out.print("Enter the name: ");
        String name = scanner.nextLine();

        System.out.print("Enter the surname: ");
        String surname = scanner.nextLine();

        System.out.print("Enter the number: ");
        String number = scanner.nextLine();

        String regex = "^([\\+?][0-9]{1,3} ?([ \\.\\-])?)? ?([\\(]{1}[0-9]{3}[\\)])? [0-9]{3} ?\\-?[0-9]{3} ?\\-?[A-Za-z0-9]{1,4}"; //+0 (123) 456-789-ABcd

        if(number.isEmpty()){
            number = "[no number]";
        }

        if(!number.matches(regex)){
            System.out.println("Wrong number format!");
        }

        Human human = humanBuilder
                .setName(name)
                .setSurname(surname)
                .setNumber(number)
                .build();

        contacts.put(contacts.size() + 1, human);
        System.out.println("The record added.");

    }

    public static void functionRemove(Map<Integer, Human> contacts){

        Scanner scanner = new Scanner(System.in);

        if(contacts.size() == 0){
            System.out.println("No records to remove!");
            return;
        }

        System.out.print("Select a record: ");

        int key = scanner.nextInt();

        contacts.remove(key);

        System.out.println("The record removed!");

    }

    public static void functionList(Map<Integer, Human> contacts){

        for ( Map.Entry<Integer, Human> entry : contacts.entrySet()) {
            System.out.print(entry.getKey() + ". " + entry.getValue());
        }
    }

    public static void functionEdit(Map<Integer, Human> contacts){

        Scanner scanner = new Scanner(System.in);

        if(contacts.size() == 0){
            System.out.println("No records to edit!");
        }

        for (int i = 1; i <= contacts.keySet().size(); i++) {
            System.out.print(i + ". " + contacts.get(i));
        }

        System.out.print("Select a record: ");

        int record = scanner.nextInt();

        System.out.print("Select a field (name, surname, number): ");

        Fields field = Fields.findFieldsByLabel(scanner.next());

        switch (field) {
            case NAME:
                changeName(contacts, record);
                break;

            case SURNAME:
                changeSurname(contacts, record);
                break;

            case NUMBER:
                changeNumber(contacts, record);
                break;

        }
    }

    public static void functionCount(Map<Integer, Human> contacts){
            System.out.println("The Phone Book has " + contacts.size() + " records.");
    }

    public static  void changeName(Map<Integer, Human> contacts, int record){

        Scanner scanner = new Scanner(System.in);

        Human human = contacts.get(record);
        contacts.get(record);

        System.out.print("Enter name: ");

        String name = scanner.next();

        human.setName(name);

        contacts.put(record, human);
        System.out.println("The record updated!");

        System.out.println(contacts. get(record));

    }

    public static  void changeSurname(Map<Integer, Human> contacts, int record){

        Scanner scanner = new Scanner(System.in);

        Human human = contacts.get(record);
        contacts.get(record);

        System.out.print("Enter surname: ");

        String surname = scanner.next();

        human.setSurname(surname);

        contacts.put(record, human);
        System.out.println("The record updated!");

        System.out.println(contacts. get(record));
    }

    public static  void changeNumber(Map<Integer, Human> contacts, int record){

        Scanner scanner = new Scanner(System.in);
        String regex = "^([\\+?][0-9]{1,3} ?([ \\.\\-])?)? ?([\\(]{1}[0-9]{3}[\\)])? [0-9]{3} ?\\-?[0-9]{3} ?\\-?[A-Za-z0-9]{1,4}";

        Human human = contacts.get(record);
        contacts.get(record);

        System.out.print("Enter number: ");

        String number = scanner.next();

        if(!number.matches(regex)){
            System.out.println("Wrong number format!");
        }

        human.setNumber(number);

        contacts.put(record, human);
        System.out.println("The record updated!");

        System.out.println(contacts. get(record));
    }

}

class Human {

    private String Name;
    private String Surname;
    private String Number;

    Human(String name, String surname, String number) {
        this.Name = name;
        this.Surname = surname;
        this.Number = number;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public static class humanBuilder {

        private String Name;
        private String Surname;
        private String Number;

        humanBuilder setName(String name) {
            this.Name = name;
            return this;
        }

        humanBuilder setSurname(String surname) {
            this.Surname = surname;
            return this;
        }

        humanBuilder setNumber(String number) {
            this.Number = number;
            return this;
        }

        Human build() {
            return new Human(Name, Surname, Number);
        }
    }

    @Override
    public String toString() {
        return Name + " " + Surname + ", " + Number + "\n";
    }
}

enum MenuType {

    ADD("add"),
    REMOVE("remove"),
    EDIT("edit"),
    COUNT("count"),
    LIST("list"),
    EXIT("exit");

    private String label;

    MenuType(String label){ this.label = label; }

    public String getLabel() { return label; }

    public static MenuType findByLabel(String label){
        for (MenuType type : MenuType.values()) {
            if (label.equalsIgnoreCase(type.getLabel())) {
                return type;
            }
        }
        return null;
    }

}

enum Fields{

    NAME("name"),
    SURNAME("surname"),
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
