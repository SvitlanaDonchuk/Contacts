package com.contacts;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Human> list = new ArrayList<>();

        MenuType menuType;

        do {

            System.out.print("Enter action (add, remove, edit, count, list, exit): ");

            menuType = MenuType.findByLabel(scanner.next());

            switch (menuType){
                case ADD:
                    functionAdd(list);
                    break;

                case REMOVE:
                    functionRemove(list);
                    break;

                case EDIT:
                    functionEdit(list);
                    break;

                case COUNT:
                    functionCount(list);
                    break;

                case LIST:
                    functionList(list);
                    break;

            }

        } while(!menuType.equals(menuType.EXIT));

    }

    private static void functionAdd(List<Human> list ){

        final Scanner scanner = new Scanner(System.in);
        final Human.humanBuilder humanBuilder = new Human.humanBuilder();

        System.out.print("Enter the name: ");
        String name = scanner.nextLine();

        System.out.print("Enter the surname: ");
        String surname = scanner.nextLine();

        System.out.print("Enter the number: ");
        String number = scanner.nextLine();

        String regex = "^([+] ?[0-9]{1,3} ?|[0-9]{1,3} ?-?|[+]?[(][A-Za-z0-9]{1,}[)] ?)( ?-?[(]?[A-Za-z0-9]{2,3}[)]?( ?-?[A-Za-z0-9]{2,3}[)]?)?( ?-?[A-Za-z0-9]{2,3})?( ?-?[A-za-z0-9]{2,4})?)?";

        if(number.isEmpty()){
            number = "[no number]";
        }

        if(!number.matches(regex)){
            number = "[no number]";
            System.out.println("Wrong number format!");
        }

        Human human = humanBuilder
                .setName(name)
                .setSurname(surname)
                .setNumber(number)
                .build();

        list.add( human);
        System.out.println("The record added.");

    }

    private static void functionRemove(List<Human> list){

        Scanner scanner = new Scanner(System.in);

        if(list.size() == 0){
            System.out.println("No records to remove!");
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.print(i + 1 + ". " + list.get(i));
        }

        System.out.print("Select a record: ");

        int key = scanner.nextInt();

        list.remove(key - 1);

        System.out.println("The record removed!");

    }

    private static void functionList(List<Human> list){

        for (int i = 0; i < list.size(); i++) {
            System.out.print(i + 1 + ". " + list.get(i));
        }
    }

    private static void functionEdit(List<Human> list){

        Scanner scanner = new Scanner(System.in);

        if(list.isEmpty()){
            System.out.println("No records to edit!");
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.print(i + 1 + ". " + list.get(i));
        }

        System.out.print("Select a record: ");

        int record = scanner.nextInt() - 1;

        System.out.print("Select a field (name, surname, number): ");

        Fields field = Fields.findFieldsByLabel(scanner.next());

        switch (field) {
            case NAME:
                changeName(list, record);
                break;

            case SURNAME:
                changeSurname(list, record);
                break;

            case NUMBER:
                changeNumber(list, record);
                break;

        }
    }

    private static void functionCount(List<Human> list){

        System.out.println("The Phone Book has " + list.size() + " records.");

    }

    private static  void changeName(List<Human> list, int record){

        Scanner scanner = new Scanner(System.in);

        Human human = list.get(record);

        System.out.print("Enter name: ");

        String name = scanner.next();

        human.setName(name);

        System.out.println("The record updated!");

    }

    private static  void changeSurname(List<Human> list, int record){

        Scanner scanner = new Scanner(System.in);

        Human human = list.get(record);

        System.out.print("Enter surname: ");

        String surname = scanner.next();

        human.setSurname(surname);

        System.out.println("The record updated!");

    }

    private static  void changeNumber(List<Human> list, int record){

        Scanner scanner = new Scanner(System.in);

        String regex = "\\s^([+] ?[0-9]{1,3} ?|[0-9]{1,3} ?-?|[+]?[(][A-Za-z0-9]{1,}[)] ?)( ?-?[(]?[A-Za-z0-9]{2,3}[)]?( ?-?[A-Za-z0-9]{2,3}[)]?)?( ?-?[A-Za-z0-9]{2,3})?( ?-?[A-za-z0-9]{2,4})?)?";

        Human human = list.get(record);

        System.out.print("Enter number: ");

        String number = scanner.nextLine();

        if(!number.matches(regex)){

            number = "[no number]";
            human.setNumber(number);

            System.out.println("Wrong number format!");

        } else {
            human.setNumber(number);
        }

        System.out.println("The record updated!");

    }
}

class Human {

    private String Name;
    private String Surname;
    private String Number;

    private Human(String name, String surname, String number) {
        this.Name = name;
        this.Surname = surname;
        this.Number = number;
    }

    void setName(String name) {
        Name = name;
    }

    void setSurname(String surname) {
        Surname = surname;
    }

    void setNumber(String number) {
        Number = number;
    }

    static class humanBuilder {

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
