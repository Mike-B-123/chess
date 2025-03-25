package ui;

import java.util.Scanner;

public class HelperMethods {
    private static HelperMethods instance ;
    public HelperMethods(){}
    public String[] varLoop(String[] variables, String[] outPrompts){
        for (int count = 0; count != 3; count++) {
            System.out.println(outPrompts[count]);
            Scanner scanner = new Scanner(System.in); // this is an input stream and can be read from, and can take in a ton of different things like files
            variables[count] = scanner.next() ; // {john password email}
        }
        return variables ;
    }
    public static HelperMethods getInstance(){
        if(instance == null){
            return instance = new HelperMethods() ;
        }
        return instance ;
    }
}
