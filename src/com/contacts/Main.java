package com.contacts;

import java.util.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<BaseClass> list = new ArrayList<>();

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
                            functionAddHuman(list);
                            break;
                        case ORGANIZATION:
                            functionAddOrganization(list);
                            break;
                    }
                    break;

                case REMOVE:
                    functionRemove(list);
                    break;

                case EDIT:
                    functionHumanEdit(list);
                    break;

                case COUNT:
                    functionCount(list);
                    break;

                case INFO:
                    functionInfo(list);
                    break;

            }

        } while(!menuType.equals(menuType.EXIT));

    }

    private static void functionAddHuman(List<BaseClass> list){

        final Scanner scanner = new Scanner(System.in);
        final Human.humanBuilder humanBuilder = new Human.humanBuilder();

        String regex = "^([+] ?[0-9]{1,3} ?|[0-9]{1,3} ?-?|[+]?[(][A-Za-z0-9]{1,}[)] ?)( ?-?[(]?[A-Za-z0-9]{2,3}[)]?( ?-?[A-Za-z0-9]{2,3}[)]?)?( ?-?[A-Za-z0-9]{2,3})?( ?-?[A-za-z0-9]{2,4})?)?";
        String genderRegex = "^[mfMF]\\b";

        System.out.print("Enter the name: ");
        String humanName = scanner.nextLine();

        System.out.print("Enter the surname: ");
        String humanSurname = scanner.nextLine();

        System.out.print("Enter the birth date: ");
        LocalDate local = LocalDate.parse(scanner.nextLine());
        String humanDate = local.toString();

        if(humanDate.isEmpty()){
            System.out.println("Bad birth date!");
            humanDate = "[no data]";
        }

        System.out.print("Enter the gender (M, F): ");
        String humanGender = scanner.nextLine();

        if(humanGender.isEmpty() || !humanGender.matches(genderRegex)){
            System.out.println("Bad gender!");
            humanGender = "[no data]";
        }

        System.out.print("Enter the number: ");
        String humanNumber = scanner.nextLine();


        if(humanNumber.isEmpty()){
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
                .setGender(humanGender)
                .setNumber(humanNumber)
                .build();

        list.add( human);
        System.out.println("The record added.");

    }

    private static void functionAddOrganization(List<BaseClass> list) {
        final Scanner scanner = new Scanner(System.in);
        final Organization.organizationBuilder organizationBuilder = new Organization.organizationBuilder();

        String regex = "^([+] ?[0-9]{1,3} ?|[0-9]{1,3} ?-?|[+]?[(][A-Za-z0-9]{1,}[)] ?)( ?-?[(]?[A-Za-z0-9]{2,3}[)]?( ?-?[A-Za-z0-9]{2,3}[)]?)?( ?-?[A-Za-z0-9]{2,3})?( ?-?[A-za-z0-9]{2,4})?)?";

        System.out.print("Enter the organization name: ");
        String organizationName = scanner.nextLine();

        System.out.print("Enter the address: ");
        String organizationAddress = scanner.nextLine();

        System.out.print("Enter the number: ");
        String organizationNumber = scanner.nextLine();

        if(organizationNumber.isEmpty() || !organizationNumber.matches(regex)){
            organizationNumber = "[no data]";
        }

        Organization organization = organizationBuilder
                .setNameOfOrganization(organizationName)
                .setAddress(organizationAddress)
                .setNumber(organizationNumber)
                .build();

        list.add(organization);
        System.out.println("The record added.");

    }

    private static void functionRemove(List<BaseClass> list){

        Scanner scanner = new Scanner(System.in);

        if(list.size() == 0){
            System.out.println("No records to remove!");
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.print(i + 1 + ". " + list.get(i).getName());
        }

        System.out.print("Select a record: ");

        int key = scanner.nextInt();

        list.remove(key - 1);

        System.out.println("The record removed!");

    }

    private static void functionInfo(List<BaseClass> list){
        Scanner scanner = new Scanner(System.in);
        int record;

        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + ". " + list.get(i).getName());
        }

        System.out.print("Select a record: ");
        record = scanner.nextInt() - 1;

        System.out.print(list.get(record));

    }

    private static void functionCount(List<BaseClass> list){

        System.out.println("The Phone Book has " + list.size() + " records.");

    }

    private static void functionHumanEdit(List<BaseClass> list){

        Scanner scanner = new Scanner(System.in);

        if(list.isEmpty()){
            System.out.println("No records to edit!");
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + ". " + list.get(i).getName());
        }

        System.out.print("Select a record: ");

        int record = scanner.nextInt() - 1;

        System.out.print("Select a field (name, surname, birth, gender, number): ");

        Fields field = Fields.findFieldsByLabel(scanner.next());

        switch (field) {
            case NAME:
                changeName(list, record);
                break;

            case SURNAME:
                changeSurname(list, record);
                break;

            case BIRTH:
                changeBirth(list, record);

            case GENDER:
                changeGender(list, record);

            case NUMBER:
                changeNumber(list, record);
                break;

        }
    }

    private static void changeName(List<BaseClass> list, int record){

        Scanner scanner = new Scanner(System.in);

        BaseClass human = list.get(record);

        System.out.print("Enter name: ");

        String name = scanner.next();

        human.setName(name);

        System.out.println("The record updated!");

    }

    private static void changeSurname(List<BaseClass> list, int record){

        Scanner scanner = new Scanner(System.in);

        Human human = (Human) list.get(record);

        System.out.print("Enter surname: ");

        String surname = scanner.next();

        human.setSurname(surname);

        System.out.println("The record updated!");

    }

    private static void changeNumber(List<BaseClass> list, int record){

        Scanner scanner = new Scanner(System.in);

        String regex = "\\s^([+] ?[0-9]{1,3} ?|[0-9]{1,3} ?-?|[+]?[(][A-Za-z0-9]{1,}[)] ?)( ?-?[(]?[A-Za-z0-9]{2,3}[)]?( ?-?[A-Za-z0-9]{2,3}[)]?)?( ?-?[A-Za-z0-9]{2,3})?( ?-?[A-za-z0-9]{2,4})?)?";

        BaseClass human = list.get(record);

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

    private static void changeBirth(List<BaseClass> list, int record){

        Scanner scanner = new Scanner(System.in);

        Human human = (Human) list.get(record);

        System.out.print("Enter Birth date: ");

        String birth = scanner.next();

        human.setBirth(birth);

        System.out.println("The record updated!");
    }

    private static void changeGender(List<BaseClass> list, int record){

        Scanner scanner = new Scanner(System.in);

        Human human = (Human) list.get(record);

        System.out.print("Enter gender: ");

        String gender = scanner.next();

        human.setGender(gender);

        System.out.println("The record updated!");
    }

}

abstract class BaseClass{

    private String number;
    private String name;

    BaseClass(String number, String name){
        this.number = number;
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}

class Organization extends BaseClass{

    private String address;

    Organization(String name, String address, String number) {
        super(number, name);
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
        return "Organization name: " + getName() + "\n" +
                "Address: " + address + "\n" +
                "Number: " + getNumber() + "\n" +
                "Time created: " + "\n" +
                "Time last edit: " + "\n";
    }


}

class Human extends BaseClass{

    private String Surname;
    private String BirthDate;
    private String gender;

     Human(String name, String surname, String number, String birthDate, String gender) {
         super(number, name);
         this.Surname = surname;
         this.BirthDate = birthDate;
         this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setSurname(String surname) {
         this.Surname = surname;
    }

    public void setBirth(String birth) {
         this.BirthDate = birth;
    }

    public void setGender(String gender) {
         this.gender = gender;
    }

    static class humanBuilder {

        private String Name;
        private String Surname;
        private String BirthDate;
        private String Number;
        private String gender;

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

        humanBuilder setGender(String gender) {
            this.gender = gender;
            return this;
        }

        Human build() {
            return new Human(Name, Surname, Number, BirthDate, gender);
        }

    }

    @Override
    public String toString() {
        return "Name: " + getName() + "\n" +
                "Surname: " + Surname + "\n" +
                "Birth date: " + BirthDate + "\n" +
                "Gender: " + getGender() + "\n" +
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

enum Type {

    PERSON("person"),
    ORGANIZATION("org");

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

