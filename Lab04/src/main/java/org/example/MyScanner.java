package org.example;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MyScanner {
    private final String CONST_REGEX = "^0|[1-9]([0-9])*|[-|+][1-9]([0-9])*|'[1-9]'|'[a-zA-Z]'|\"[0-9]*[a-zA-Z ]*\"$";
    private final String IDENTIFIER_REGEX = "^[A-Za-z][A-Za-z0-9]*";

    private final ArrayList<String> operators = new ArrayList<>();
    private final ArrayList<String> separators = new ArrayList<>();
    private final ArrayList<String> reservedWords = new ArrayList<>();

    private final String filePath;
    private SymbolTable st;
    private List<Pair<String, String>> pif;

    public MyScanner(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.st = new SymbolTable(10);
        this.pif = new ArrayList<>();
        readTokens();
    }

    /**
     * This method reads the operator, separator, and reserved word lists from a file, populating the corresponding lists.
     * @throws FileNotFoundException
     */
    public void readTokens() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("tokens/token.in"));
        while (scanner.hasNext()) {
            String read = scanner.nextLine();
            if(Objects.equals(read, "#operators")){
                break;
            }
            operators.add(read);
        }

        while (scanner.hasNext()) {
            String read = scanner.nextLine();
            if(Objects.equals(read, "#separators")){
                break;
            }
            if(Objects.equals(read, "space")) {
                read = " ";
            }
            else if(Objects.equals(read, "newline")) {
                read = "\n";
            }
            separators.add(read);
        }

        while (scanner.hasNext()) {
            String read = scanner.nextLine();
            reservedWords.add(read);
        }
    }


    /**
     * Reads the content of the source code file.
     * @return the content of the source code as a String.
     * @throws FileNotFoundException
     */
    private String readProgram() throws FileNotFoundException {
        StringBuilder fileContent = new StringBuilder();
        Scanner scanner = new Scanner(new File(this.filePath));
        while (scanner.hasNext()) {
            fileContent.append(scanner.nextLine()).append("\n");
        }

        return fileContent.toString().replace("\t", "");
    }

    /**
     * Splits the source code into a list of tokens using separators.
     * Processes the list of tokens and categorizes them.
     * @return a list of pairs (token, line) where each token is associated with the line it appears on.
     */
    List<Pair<String, Integer>> getTokens() {
        List<String> tokens = new ArrayList<>();
        List<Pair<String, Integer>> ans = new ArrayList<>();
        int line = 1;

        try {
            String code = readProgram();
            StringBuilder separatorsString = new StringBuilder();

            for(String separator : separators) {
                separatorsString.append(separator);
            }

            StringTokenizer tokenizer = new StringTokenizer(code, separatorsString.toString(), true);

            while (tokenizer.hasMoreTokens()) {
                tokens.add(tokenizer.nextToken());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < tokens.size(); i++) {
            if(!Objects.equals(tokens.get(i), " ")) {

                if(Objects.equals(tokens.get(i), "\"")) {
                    StringBuilder string = new StringBuilder();
                    string.append(tokens.get(i));
                    int j = i + 1;
                    while (!Objects.equals(tokens.get(j), "\"")) {
                        string.append(tokens.get(j));
                        j++;
                    }
                    string.append(tokens.get(j));
                    i = j;
                    ans.add(new Pair<>(String.valueOf(string), line));
                }
                else {
                    if (Objects.equals(tokens.get(i), "\n")) {
                        line++;
                    } else {
                        ans.add(new Pair<>(tokens.get(i), line));
                    }
                }

            }
        }
        return ans;
    }

    /**
     * Scans the source code, identifies tokens, and categorizes them as reserved words, operators, separators, constants, or identifiers.
     * Any lexical errors are reported. The results are stored in the PIF and symbol table.
     */
    public void scan() {
        boolean error = false;

        List<Pair<String, Integer>> tokens = getTokens();
        for (Pair<String, Integer> t : tokens) {
            String token = t.getFirst();

            if (this.reservedWords.contains(token)) {
                this.pif.add(new Pair<>(token, "-1"));
            } else if (this.operators.contains(token)) {
                this.pif.add(new Pair<>(token, "-1"));
            } else if (this.separators.contains(token)) {
                this.pif.add(new Pair<>(token, "-1"));
            } else if (Pattern.compile(CONST_REGEX).matcher(token).matches()) {
                this.st.addSymbol(token);
                this.pif.add(new Pair<>("CONST", st.getPosition(token).getFirst().toString()));
//                this.pif.add(new Pair<>("CONST", token));
            } else if (Pattern.compile(IDENTIFIER_REGEX).matcher(token).matches()) {
                this.st.addSymbol(token);
                this.pif.add(new Pair<>("ID", st.getPosition(token).getFirst().toString()));
//                this.pif.add(new Pair<>("ID", token));
            } else {
                System.out.println("lexical error at line " + t.getSecond() + ": " + t.getFirst());
                error = true;
            }
        }
    }


    public String getPif(){
        StringBuilder computedString = new StringBuilder();
        for(int i = 0; i < this.pif.size(); i++) {
            int len1 = this.pif.get(i).getFirst().length();
            computedString
                    .append("| ")
                    .append(this.pif.get(i).getFirst());
            for(int j = 0; j < 10 - len1; j++) {
                computedString.append(" ");
            }
            computedString
                    .append("| ");
            if(!Objects.equals(this.pif.get(i).getSecond(), "-1")) {
                computedString.append(" ");
            }
            computedString
                    .append(this.pif.get(i).getSecond())
                    .append(" |")
                    .append("\n");
        }

        return computedString.toString();
    }

    public SymbolTable getSymbolTable() {
        return this.st;
    }
}
