package org.example;

public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(8);

        String[] symbols = {"x", "y", "t", "i", "y"};

        System.out.println("Add --------------");
        for (int i = 0; i < symbols.length; ++i) {
            System.out.println(symbolTable.addSymbol(symbols[i]));
        }

        System.out.println("\nContains --------------");
        for (int i = 0; i < symbols.length; ++i) {
            System.out.println(symbolTable.containsSymbol(symbols[i]));
        }

        symbols = new String[]{"i", "x", "t", "y", "z"};
        System.out.println("\nPosition --------------");
        for (int i = 0; i < symbols.length; ++i) {
            System.out.println(symbolTable.getPosition(symbols[i]));
        }

        System.out.println("\nRemove --------------");
        for (int i = 0; i < symbols.length; ++i) {
            System.out.println(symbolTable.removeSymbol(symbols[i]));
        }
    }
}