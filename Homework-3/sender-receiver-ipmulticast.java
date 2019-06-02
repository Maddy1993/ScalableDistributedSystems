import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

/**
 * The purpose of this class is to provide design outline
 * of operations of sender to send the message and receive
 * any retramissions messages.
 * 
 * As an outline, the method definitions are not provided
 * and the comments for each method serve the purpose to
 * explain the method operation.
 */
class Sender {

    //Holds the IPAddress of the IP Mutli-cast.
    private static String ipAddress;

    //The message to be sent to the multicast.
    private static String message;

    //Buffer Size of each Datagram packet.
    private static final int packetSize = 1024;

    //Datagram socket to send and receive requests.
    DatagramSocket senderSocket;

    //Datagram packet to hold the messages to sent
    //or received on the socket.
    DatagramPacket senderPacket;

    //buffer to hold the values in the datagram packet.
    buffer[] packetBuffer;

    //HashMap of messages sent.
    Map<double, byte[]> packetsSent;

    /**
     * Initializes the datagram socket and the map
     * needed to store the packets sent.
     * Throws IOException when there is an exception
     * when creating the socket.
     */
    Sender() {
    }

    /**
     * Initializes packet to send based on the input buffer. 
     * Information greater than the size of the packet is dropeed.
     */
    private void initializePacket(byte[] buffer) {}

    /**
     * Sends the information the client or the
     * IP Multicast group using the packet initialzies
     * using the message sent as an input parameter.
     */
    private void sendPacket(String message) {
        initializePacket(message.getBytes());
    }

    /**
     * Receives the packet on the socket.
     */
    private void receivePacket() {
        initializePacket(new byte[packetSize]);
    }

    /**
     * Retrieves the message passed in as argument to
     * the program.
     */
    private String getMessage() {
        return message;
    }

    /**
     * Flushes the hash map and reinintializes the hash map
     * to store recent values.
     */
    private void resetStorageMap() {}

    /**
     * Returns the hash map for storing packets.
     */
    private HashMap<double, byte[]> getStorageMap() {}

    /**
     * Method is responsible for parsing the command line
     * arguments and retrieving the message, and the IP Address
     * of the receiver of the message.
     * 
     * Any exceptions during parsing, like mismatched value type 
     * of IPAddress or incorrect number of arguments can be handled here.
     */
    private static void parseAndVerifyArguments() {}

    //Arguments from the command line provide
    //the message and address of receiver.
    public static void main(String[] args) {
        parseAndVerifyArguments();

        //Create the instance, socket and initializes the 
        //buffer and packet.
        Sender senderObject = new Sender();

        //Send information to the IP Multicast group.
        int messageLength = senderObject.getMessage().length();

        //Random number to start the sequence to sending
        //the message.
        double sequenceNumber = Math.random();
        String outMessage 
        = "LEN: " + messageLength 
            + "SEQ_NUM: " + sequenceNumber 
            + "PACKETSIZE: " + packetSize;

        //Send the information packet to the IP Multicast.
        //This will serve as the pointer for identifying
        //any missed packets.
        senderObject.senderPacket(outMessage.getBytes());

        //When the entire message can be sent in one
        //packet. And Save the packet sent in the HashMap
        byte[] data = senderObject.getMessage().getBytes();
        if (packetSize > messageLength) {
            senderObject.senderPacket(data);
            senderObject.getStorageMap.add(sequenceNumber, data);
        } else {
            //When the entire message can be sent in one packet,
            //split the packet and associate a sequence number
            //with each packet.
            for (int index = 0; index <messageLength;) {
                byte[] sendMessage = Arrays.copyOfRange(senderObject.getMessage().getBytes(),
                                                        index,
                                                        index+packetSize);
                outMessage = "SEQ_NUM: " + sequenceNumber + "DATA: " + sendMessage;
                data = outMessage.getBytes();
                senderObject.senderPacket(data);
                senderObject.getStorageMap().add(sequenceNumber, data);
                index = index + packetSize;
                sequenceNumber = sequenceNumber++;
            }
        }

        //Sender can receive the list of dropped packets or success reply
        //from the receiver to confirm the message has been delivered successfully.
        //This reply will consist of unicast address of the receiver to respon
        //back successfully.
        receiveRequestAndSendMissedPackets()
    }
}

4.24 Outline the design of a scheme that uses message retransmissions with IP multicast to overcome
the problem of dropped messages. Your scheme should take the following points into account:
i) there may be multiple senders;
ii) generally only a small proportion of messages are dropped;
iii) unlike the request-reply protocol, recipients may not necessarily send a message within any particular

time limit.
Assume that messages that are not dropped arrive in sender ordering.

4.24 Ans.

To allow for point (i) senders must attach a sequence number to each message. Recipients record last sequence
number from each sender and check sequence numbers on each message received.

client server

cancel any outstanding
Acknowledgement on a timer
 send Request

receive Request
send Reply

receive Reply
set timer to send
Acknowledgement after delay T

receive Acknowledgement

For point (ii) a negative acknowledgement scheme is preferred (recipient requests missing messages,
rather than acknowledging all messages). When they notice a missing message, they send a message to the
sender to ask for it. To make this work, the sender must store all recently sent messages for retransmission.
The sender re-transmits the messages as a unicast datagram.

Point (iii) - refers to the fact that we can\u2019t rely on a reply as an acknowledgement. Without
acknowledgements, the sender will be left holding all sent messages in its store indefinitely. Possible solutions:
a) senders discards stored messages after a time limit b) occasional acknowledgements from recipients which
may be piggy backed on messages that are sent.

Note requests for missing messages and acknowledgments are simple - they just contain the sequence
numbers of a range of lost messages.
