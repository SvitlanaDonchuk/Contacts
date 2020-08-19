package com.contacts;

import java.util.*;

class Main {
    public static void main(String[] args) {

        final Scanner scanner = new Scanner(System.in);

        MenuType menuType;

        do {

            System.out.print("Enter action (add, remove, edit, count, list, exit): ");
            menuType = MenuType.findByLabel(scanner.next());

            switch (menuType){
                case ADD:
                    functionAdd();
                    break;

                case REMOVE:
                    functionRemove();
                    break;

                case EDIT:
                    functionEdit();
                    break;

                case COUNT:
                    functionCount();
                    break;

                case LIST:
                    functionList();
                    break;

            }

        } while(!menuType.equals(menuType.EXIT));

    }

    public static void functionAdd(){ }
    public static void functionRemove(){}
    public static void functionList(){}
    public static void functionEdit(){}
    public static void functionCount(){}

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
