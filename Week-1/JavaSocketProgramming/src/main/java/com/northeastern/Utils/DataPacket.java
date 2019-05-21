package main.java.com.northeastern.Utils;

import java.io.Serializable;

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
        DATA;

        public String toString() {
            return name();
        }
    }

    // Members of enum.
    private PacketType packetType;
    private Object data;


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
}
