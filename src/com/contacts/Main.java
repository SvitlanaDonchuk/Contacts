package com.contacts;

import java.util.*;

class Main {
    public static void main(String[] args) {

        final Scanner scanner = new Scanner(System.in);
        Map<Integer, Human> contacts = new HashMap<>();

        MenuType menuType;

        do {

            System.out.print("Enter action (add, remove, edit, count, list, exit): ");

            menuType = MenuType.findByLabel(scanner.next());

            switch (menuType){
                case ADD:
                    functionAdd(contacts);
                    break;

                case REMOVE:
                    functionRemove(contacts);
                    break;

                case EDIT:
                    functionEdit(contacts);
                    break;

                case COUNT:
                    functionCount(contacts);
                    break;

                case LIST:
                    functionList(contacts);
                    break;

            }

        } while(!menuType.equals(menuType.EXIT));

    }

    public static void functionAdd(Map<Integer, Human> contacts){

        final Scanner scanner = new Scanner(System.in);
        final Human.humanBuilder robotBuilder = new Human.humanBuilder();

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

        Human human = robotBuilder
                .setName(name)
                .setSurname(surname)
                .setNumber(number)
                .build();

        contacts.put(contacts.size() + 1, human);
        System.out.println("The record added.");

    }

    public static void functionRemove(Map<Integer, Human> contacts){

        if(contacts.size() == 0){
            System.out.println("No records to remove!");
        }

    }

    public static void functionList(Map<Integer, Human> contacts){

        for (int i = 1; i <= contacts.keySet().size(); i++) {
            System.out.print(i + ". " + contacts.get(i));
        }
    }

    public static void functionEdit(Map<Integer, Human> contacts){

        if(contacts.size() == 0){
            System.out.println("No records to edit!");
        }
    }

    public static void functionCount(Map<Integer, Human> contacts){
            System.out.println("The Phone Book has " + contacts.size() + " records.");
    }

}

class Human {

    private String Name;
    private String Surname;
    private String Numer;

    Human(String name, String surname, String number) {
        this.Name = name;
        this.Surname = surname;
        this.Numer = number;
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
        return Name + " " + Surname + ", " + Numer + "\n";
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