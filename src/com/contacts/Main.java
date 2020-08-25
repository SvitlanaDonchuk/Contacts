package com.contacts;

import java.util.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Human> humanList = new ArrayList<>();
        List<Organization> organizationList = new ArrayList<>();

        Action menuType;
        Type type;

        do {

            System.out.print("Enter action (add, remove, edit, count, info, exit): ");

            menuType = Action.findByLabel(scanner.next());

            switch (menuType){
                case ADD:
                    System.out.print("Enter the type (person, organization): ");
                    type = Type.findTypeByLabel(scanner.next());

                    switch (type){
                        case PERSON:
                            functionAddHuman(humanList);
                            break;
                        case ORGANIZATION:
                            functionAddOrganization(organizationList);
                            break;
                    }
                    break;

                case REMOVE:
                    functionRemove(humanList);
                    break;

                case EDIT:
                    functionEdit(humanList);
                    break;

                case COUNT:
                    functionCount(humanList);
                    break;

                case INFO:
                    functionInfo(humanList);
                    break;

            }

        } while(!menuType.equals(menuType.EXIT));

    }

    private static void functionAddHuman(List<Human> humanList ){

        final Scanner scanner = new Scanner(System.in);
        final Human.humanBuilder humanBuilder = new Human.humanBuilder();

        String regex = "^([+] ?[0-9]{1,3} ?|[0-9]{1,3} ?-?|[+]?[(][A-Za-z0-9]{1,}[)] ?)( ?-?[(]?[A-Za-z0-9]{2,3}[)]?( ?-?[A-Za-z0-9]{2,3}[)]?)?( ?-?[A-Za-z0-9]{2,3})?( ?-?[A-za-z0-9]{2,4})?)?";

        System.out.print("Enter the name: ");
        String humanName = scanner.nextLine();

        System.out.print("Enter the surname: ");
        String humanSurname = scanner.nextLine();

        System.out.print("Enter the birth date: ");
        LocalDate local = LocalDate.parse(scanner.next());
        String humanDate = local.toString();

        System.out.print("Enter the number: ");
        String humanNumber = scanner.next();


        if(humanNumber.isEmpty() || humanDate.isEmpty()){
            humanNumber = "[no data]";
        }

        if(!humanNumber.matches(regex)){
            humanNumber = "[no data]";
            System.out.println("Wrong number format!");
        }

        Human human = humanBuilder
                .setName(humanName)
                .setSurname(humanSurname)
                .setBirthDate(humanDate)
                .setNumber(humanNumber)
                .build();

        humanList.add( human);
        System.out.println("The record added.");

    }

    private  static void functionAddOrganization(List<Organization> organizationList) {
        final Scanner scanner = new Scanner(System.in);
        final Organization.organizationBuilder organizationBuilder = new Organization.organizationBuilder();

        String regex = "^([+] ?[0-9]{1,3} ?|[0-9]{1,3} ?-?|[+]?[(][A-Za-z0-9]{1,}[)] ?)( ?-?[(]?[A-Za-z0-9]{2,3}[)]?( ?-?[A-Za-z0-9]{2,3}[)]?)?( ?-?[A-Za-z0-9]{2,3})?( ?-?[A-za-z0-9]{2,4})?)?";

        System.out.print("Enter the organization name: ");
        String organizationName = scanner.next();

        System.out.print("Enter the address: ");
        String organizationAddress = scanner.next();

        System.out.print("Enter the number: ");
        String organizationNumber = scanner.next();

        Organization organization = organizationBuilder
                .setNameOfOrganization(organizationName)
                .setAddress(organizationAddress)
                .setNumber(organizationNumber)
                .build();

        organizationList.add(organization);
        System.out.println("The record added.");

        if(organizationNumber.isEmpty() || !organizationNumber.matches(regex)){
            organizationNumber = "[no data]";
        }

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

    private static void functionInfo(List<Human> list){

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

abstract class BaseClass{

    private String number;

    BaseClass(String number){
        this.number = number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
}

class Organization extends BaseClass{

    private String nameOfOrganization;
    private String address;

    Organization(String number, String nameOfOrganization, String address) {
        super(number);
        this.nameOfOrganization = nameOfOrganization;
        this.address = address;
    }

    public void setNameOfOrganization(String nameOfOrganization) {
        this.nameOfOrganization = nameOfOrganization;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    static class organizationBuilder {

        private String nameOfOrganization;
        private String address;
        private String number;

        organizationBuilder setNameOfOrganization(String nameOfOrganization){
            this.nameOfOrganization = nameOfOrganization;
            return this;
        }

        organizationBuilder setAddress(String address){
            this.address = address;
            return this;
        }

        organizationBuilder setNumber(String number) {
            this.number = number;
            return this;
        }

        Organization build(){
            return new Organization(nameOfOrganization, address, number);
        }

    }

    @Override
    public String toString() {
        return "Organization name: " + nameOfOrganization + "\n" +
                "Address: " + address + "\n" +
                "Number: " + getNumber() + "\n" +
                "Time created: " + "\n" +
                "Time last edit: " + "\n";
    }


}

class Human extends BaseClass{

    private String Name;
    private String Surname;
    private String BirthDate;

     Human(String name, String surname, String number, String birthDate) {
         super(number);
         this.Name = name;
         this.Surname = surname;
         this.BirthDate = birthDate;
    }

    void setName(String name) {
         Name = name;
    }

    void setSurname(String surname) {
         Surname = surname;
    }

    static class humanBuilder {

        private String Name;
        private String Surname;
        private String BirthDate;
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

        humanBuilder setBirthDate(String birthDate) {
            this.BirthDate = birthDate;
            return this;
        }

        Human build() {
            return new Human(Name, Surname, Number, BirthDate);
        }

    }

    @Override
    public String toString() {
        return "Name: " + Name + "\n" +
                "Surname: " + Surname + "\n" +
                "Birth date: " + BirthDate + "\n" +
                "Gender: " + "\n" +
                "Number: " + getNumber() + "\n" +
                "Time created: " + "\n" +
                "Time last edit: " + "\n";
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

enum Type {

    PERSON("person"),
    ORGANIZATION("organization");

    private String typeLabel;

    Type(String typeLabel){ this.typeLabel = typeLabel; }

    public String getTypeLabel() { return typeLabel; }

    public static Type findTypeByLabel(String typeLabel){
        for (Type type : Type.values()) {
            if (typeLabel.equalsIgnoreCase(type.getTypeLabel())) {
                return type;
            }
        }
        return null;
    }

}

