package components.testing;

import components.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * DESCRIPTION
 *
 * @author Julio Cuello
 */
public class Overload {
    public static final int BAD_ARGS = 1;
    public static final int FILE_NOT_FOUND = 2;
    public static final int BAD_FILE_FORMAT = 3;
    public static final int UNKNOWN_COMPONENT = 4;
    public static final int REPEAT_NAME = 5;
    public static final int UNKNOWN_COMPONENT_TYPE = 6;
    public static final int UNKNOWN_USER_COMMAND = 7;
    public static final int UNSWITCHABLE_COMPONENT = 8;
    public static HashMap<String, Component> hmap = new HashMap();
    private static final String WHITESPACE_REGEX = "\\s+";
    private static final String[] NO_STRINGS = new String[ 0 ];
    private static final String PROMPT = "? ";

    public static ArrayList<String> roots= new ArrayList<>();
    static {
        Reporter.addError(
                BAD_ARGS, "Usage: java components.Overload <configFile>" );
        Reporter.addError( FILE_NOT_FOUND, "Config file not found" );
        Reporter.addError( BAD_FILE_FORMAT, "Error in config file" );
        Reporter.addError(
                UNKNOWN_COMPONENT,
                "Reference to unknown component in config file"
        );
        Reporter.addError(
                REPEAT_NAME,
                "Component name repeated in config file"
        );
        Reporter.addError(
                UNKNOWN_COMPONENT_TYPE,
                "Reference to unknown type of component in config file"
        );
        Reporter.addError(
                UNKNOWN_USER_COMMAND,
                "Unknown user command"
        );
        Reporter.addError(UNSWITCHABLE_COMPONENT, "Component toggled has to be switchable"
        );
//        Reporter.addError(NO_STRINGS, "Empty line in config file");

    }

    /**
     * this method takes care of reading through a file with several components
     * each one has specific parameters, so I iterate over the file
     * by splitting the line and then indexing through it as needed
     * I also created a hashmap to store the names of the different components
     * to avoid repetition, also name error handling
     * is done in this method and file not found error is also
     * handled here
     * @param configUFile
     */
    private static void readConfiguration( String configUFile) {
        try ( Scanner configFile = new Scanner( new File( configUFile) ) ) {
            while ( configFile.hasNextLine() ) {
                String line= configFile.nextLine();
                String []reader= line.strip().split(" ");
                if(reader.equals("")){

                }
                if(reader[0].equals("PowerSource")){
                    if(reader.length!=2){
                        Reporter.usageError(BAD_FILE_FORMAT);
                    }
                    if(hmap.containsKey(reader[1])){
                        Reporter.usageError(5);
                    }
                    roots.add(reader[1]);
                    hmap.put(reader[1],new PowerSource(reader[1]));
                }
                else if(reader[0].equals("CircuitBreaker")){
                    if(reader.length!=4){
                        Reporter.usageError(BAD_FILE_FORMAT);
                    }
                    else if(hmap.containsKey(reader[1])){
                        Reporter.usageError(5);
                    }
                    else if(!(hmap.containsKey(reader[2]))){
                        Reporter.usageError(UNKNOWN_COMPONENT);
                    }
                    hmap.put(reader[1],new CircuitBreaker(reader[1],hmap.get(reader[2]),Integer.parseInt(reader[3])));
                }
                else if(reader[0].equals("Outlet")){
                    if(reader.length!=3){
                        Reporter.usageError(BAD_FILE_FORMAT);
                    }
                    else if(hmap.containsKey(reader[1])){
                        Reporter.usageError(5);
                    }
                    else if(!(hmap.containsKey(reader[2]))){
                        Reporter.usageError(UNKNOWN_COMPONENT);
                    }
                    hmap.put(reader[1],new Outlet(reader[1],hmap.get(reader[2])));
                }
                else if(reader[0].equals("Appliance")){
                    if(reader.length!=4){
                        Reporter.usageError(BAD_FILE_FORMAT);
                    }
                    else if(hmap.containsKey(reader[1])){
                        Reporter.usageError(5);
                    }
                    else if(!(hmap.containsKey(reader[2]))){
                        Reporter.usageError(UNKNOWN_COMPONENT);
                    }
                    hmap.put(reader[1],new Appliance(reader[1],hmap.get(reader[2]),Integer.parseInt(reader[3])));
                }
                else{
                    Reporter.usageError(UNKNOWN_COMPONENT_TYPE);
                }
            }
            System.out.println(hmap.size()+ " "+ "components created.");
            System.out.println("Starting up the main circuit(s).");
        }
        // All lines have been read.
        catch( FileNotFoundException fnfe ) {
            Reporter.usageError( FILE_NOT_FOUND );
        }
    }

    /**
     * this method takes care of redng an input
     * redirected in the configurations
     * for the program after reading
     * through another file in the readFileConfigurations method
     * that creates the tree of the electric system
     */
    public static void readInput() {
        for(String root: roots){
            Reporter.report(hmap.get(root), Reporter.Msg.POWERING_UP);
            hmap.get(root).engage();
        }
        boolean run= true;
        while (run) {
            Scanner in = new Scanner(System.in);
            while (in.hasNextLine()) {
                String input = in.nextLine();
                String[] reader = input.split(" ");
                if (reader[0].equals("display")) {
                    System.out.println(PROMPT+" -> display[]");
                    for (String nameRoot : roots) {
                        hmap.get(nameRoot).display();
                    }
                } else if (reader[0].equals("toggle")) {
                    System.out.println(PROMPT+" -> toggle[" + reader[1] + "]");
                    if (hmap.get(reader[1]) instanceof Switchable) {
                        if (((Switchable) hmap.get(reader[1])).getIson()) {
                            ((Switchable) hmap.get(reader[1])).turnOff();
                        } else {
                            ((Switchable) hmap.get(reader[1])).turnOn();
                        }
                    } else if (!(hmap.get(reader[1]) instanceof Switchable)) {
                        Reporter.usageError(8);
                    }
                } else if (reader[0].equals("connect")) {
                    System.out.println(PROMPT+ " -> connect[" + reader[1] + ", " + reader[2] + ", " + reader[3] + ", " + reader[4]+"]");
                        if (reader[1].equals("Appliance")) {
                            if(!(hmap.containsKey(reader[3]))){
                                Reporter.usageError(4);
                            }
                            hmap.put(reader[2], new Appliance(reader[2], hmap.get(reader[3]), Integer.parseInt(reader[4])));
                            }
                        else if (reader[1].equals("Outlet")) {
                            if(!(hmap.containsKey(reader[3]))){
                                Reporter.usageError(4);
                            }
                            hmap.put(reader[2], new Outlet(reader[2], hmap.get(reader[3])));
                        } else if (reader[1].equals("CircuitBreaker")) {
                            if(!(hmap.containsKey(reader[3]))){
                                Reporter.usageError(4);
                            }
                            hmap.put(reader[2], new CircuitBreaker(reader[2], hmap.get(reader[3]), Integer.parseInt(reader[4])));
                        }
                } else if (reader[0].equals("quit")) {
                    System.out.println(PROMPT+ " -> quit[]");
                    run=false;
                }
                else{
                    Reporter.usageError(7);
                }
            }
        }
    }
    public static void main( String[] args ) {
        System.out.println( "Overload Project, CS2" );
        if(args.length != 1){
            Reporter.usageError(BAD_ARGS);
        }
        readConfiguration(args[0]);
        readInput();
    }
}