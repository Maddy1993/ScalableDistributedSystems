package main.java.com.northeastern.Utils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents the packet being transported over the
 * network.
 * Each instance will have the ability to distinguish
 * between the packet type and the pack data as a serialized
 * object.
 */

public class DataPacket implements Serializable {

    //Enum for packet types.
    public enum PacketType {
        PUT,
        DELETE,
        GET,
        DATA,
        SUCCESS,
        KEYS,
        EXIT;

        public String toString() {
            return name();
        }
    }

    // Members of enum.
    private PacketType packetType;

    //Data sent over as an generic type.
    //Based on the packet type, the data
    //variable is casted to it's needed
    //type.
    private Object data;

    //Provides the time of packet generation.
    private LocalDateTime packetCreationTime;

    //Provides the ID of the packet.
    //It is unique for each packet
    //and the client responsible generates
    //the requestID.
    private long requestID;

    /**
     * Constructor for the instance which
     *  Initializes the PacketType for the packet instance.
     *  Initializes the data for the packet instance.
     * @param type PacketType value for the instance.
     * @param data Represents the data to be put in the packet.
     */
    public DataPacket (PacketType type, Object data) {
        this.packetType = type;
        this.data = data;
        packetCreationTime = LocalDateTime.now();
    }

    /**
     * Gets the packet type of the packet.
     * @return  PacketType value of the packet instance.
     */
    public PacketType getPacketType() {
        return packetType;
    }

    /**
     * Returns the data of the packet instance.
     * @return  Object formatted data.
     */
    public Object getData() {
        return data;
    }

    /**
     * Returns the packet creation time. Can be used
     * by the client or server to verify the freshness
     * of packet for a requested operation.
     * @return
     */
    public LocalDateTime getPacketCreationTime() {
        return packetCreationTime;
    }

    /**
     * Returns a random long integer to represent
     * the packet ID for an operation. TIme along
     * with packet ID can guarantee the freshness of
     * packet for an operation.
     * @return
     */
    public long getRequestID() {
        return requestID;
    }
}
