import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This class is used to represent a server
 * which takes in requests from multiple clients
 * and yet, maintains a consistent data storage
 * between simulataneous client calls.
 */
public class Server {

    Server() {
        //Create a socket and instantiate any other
        //variables.
    }

    //Takes in the arguments to parse the port number
    //and necessary information to initialize the server.
    public static void main(String[] args) {
        parserAndVerifyArguments();

        try {
            ExecuteRPC rpc = new ExecuteRPC();
            while (1) {
                Socket clientSocket = socket.accept();
                Thread t = new ClientHandler(clientSocket, rpc);
                t.start();
            }
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }
}

/**
 * Class which handles the client logic for
 * an incoming requests.
 */
class ClientHandler extends Thread {
    //Socket for the client.
    Socket socket;

    //Utility class instance.
    ExecuteRPC executeRPC;

    ClientHandler(Socket socket, ExecuteRPC rpc) {
        //Initialize client socket for reading
        //and communicating back to the server

        //Initialzie the executeRPC instance as well.
    }

    public void run() {
        //The parameters are parsed from the input stream.
        executeRPC.execute(voterID, customerName);
    }
}

/**
 * Executes the remote procedure call based on the input values.
 */
class ExecuteRPC {

    //Constructor for intializing the streams.
    ExecuteRPC() {}

    /**
     * The synchronizes keyword makes sure the
     * the method is loaded onto the stack in the
     * memory only one instance at time.
     * 
     * Doing so, any resources, that are shared between
     * multiple threads can maintain a common and stale state. 
    */
    synchronized public String execute(int voterID, String customerName) {
        customerMap.put(voterID, customerName);
    }
}