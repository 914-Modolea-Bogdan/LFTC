package org.example;

public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(8);

        String[] symbols = {"x", "y", "t", "i", "y"};

        System.out.println("Add --------------");
        for (String s : symbols) {
            System.out.println(symbolTable.addSymbol(s));
        }

        System.out.println("\nContains --------------");
        for (String symbol : symbols) {
            System.out.println(symbolTable.containsSymbol(symbol));
        }

        symbols = new String[]{"i", "x", "t", "y", "z"};
        System.out.println("\nPosition --------------");
        for (String symbol : symbols) {
            System.out.println(symbolTable.getPosition(symbol));
        }

        System.out.println("\nRemove --------------");
        for (String symbol : symbols) {
            System.out.println(symbolTable.removeSymbol(symbol));
        }
    }
}