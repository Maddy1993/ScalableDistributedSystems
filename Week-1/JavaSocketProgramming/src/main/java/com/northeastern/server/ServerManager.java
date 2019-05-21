package main.java.com.northeastern.server;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

abstract class ServerManager {

    //Logger for the class.
    static Logger LOGGER = Logger.getLogger(ServerManager.class.getName());

    //Port Number for the server
    static Integer portNumber;

    /**
     * Used for any setup required before the program execution
     * starts.
     *
     * @param name  Name of the logger to use for the
     *              overriding class.
     */
    static void setup(String name) {
        //Initialize the logger.
        try {
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(new FileHandler(name));
        } catch (IOException e) {
            System.out.println("Error initializing the logger: " + e.getMessage());
        }
    }

    /**
     * Can be used to parse any command line arguments.
     * Currently parses the port number from the command
     * line arguments.
     * @param args  List of command line arguments
     */
    static void parseArguments(String[] args) {
        if (args.length == 1) {
            try {
                portNumber = Integer.parseInt(args[0]);
            } catch (NumberFormatException exception) {
                LOGGER.severe("Port number needs to be an integer.");
                System.exit(1);
            }
        } else {
            LOGGER.severe("The server needs the following inputs only:\n\t1. Port Number");
            System.exit(1);
        }
    }

}
