package com.northeastern.edu.server;

import com.northeastern.edu.utils.RPCPacketServiceImpl;
import generated.thrift.impl.RPCPacketService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Represents a RPC server implemented on Apache Thrift.
 *
 * @author mpothukuchi 12th June 2019
 *
 * References:
 * @see <a href="https://thrift.apache.org/tutorial/java" />
 * @see <a href="https://www.baeldung.com/apache-thrift" />
 */
public class RPCServer {

    //Logger for the class.
    private static Logger LOGGER = Logger.getLogger(RPCServer.class.getName());

    //Port Number for the server
    private static Integer portNumber;

    //Handler for incoming client requests.
    private static RPCPacketServiceImpl utils;

    //Processor for RPC service class.
    private static RPCPacketService.Processor processor;

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

    /**
     * Formats the message to be printed to the output stream.
     *
     * @param message The message to print to screen
     */
    private static void formatMessage(String  message) {
        System.out.print("<" + LocalDateTime.now() + ">> ");
        System.out.println(message);
    }

    public static void main(String[] args) {
        setup("logs/RCPServer.log");
        parseArguments(args);

        try {
            //Initialize the client handler.
            utils = new RPCPacketServiceImpl();

            //Initialize the processor for thrift server.
            processor = new RPCPacketService.Processor<>(utils);

            Runnable simple = () -> threadedServer(processor);

            new Thread(simple).start();
        } catch (Exception e) {
            LOGGER.warning("Exception in main block: " + e.getMessage());
            formatMessage("Exception in main block: " + e.getMessage());
        }
    }

    /**
     * Creates the mutli threaded server and initializes it for listening on
     * the specified port.
     *
     * @param processor Thrift processor to initialize the server with.
     */
    private static void threadedServer(RPCPacketService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(portNumber);
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            LOGGER.info("Multi-threaded thrift server initialized");
            formatMessage("Multi-threaded thrift server initialized");
            server.serve();
        } catch (TTransportException e) {
            LOGGER.severe("Error initializing server socket: " + e.getMessage());
            formatMessage("Error initializing server socket: " + e.getMessage());
        }
    }
}
