package com.northeastern.edu.utils;

import generated.thrift.impl.RPCPacketService;
import org.apache.thrift.TException;

import java.util.*;

public class RPCPacketServiceImpl implements RPCPacketService.Iface {
    //Synchronize block variable.
    private static final Object lock = new Object();

    //Variable representing the type of packet.
    private int operationType;

    //Map to store the key and value initialized from client
    private Map<String, String> keyValuePair;

    //Enum for packet types.
    public enum OperationType {
        PUT(1),
        DELETE(2),
        GET(3),
        FAILURE (4),
        SUCCESS(5),
        NONE(6);

        //Variable representing the enum.
        private final int enumValue;
        OperationType(int val) {
            this.enumValue = val;
        }

        /*
        Returns the integer value of enum.
         */
        public int getEnumValue() {
            return this.enumValue;
        }
    }

    //Initializes the value for packet and keyValueStore.
    public RPCPacketServiceImpl() {
        operationType = 0;
        keyValuePair = new HashMap<String, String>();
    }

    @Override
    public int getType() throws TException {
        return this.operationType;
    }

    @Override
    public void setType(int type) throws TException {
        this.operationType = type;

    }

    @Override
    public List<String> getKeys() throws TException {
        return new ArrayList<String>(keyValuePair.keySet());
    }

    @Override
    public void setKeyValue(String key, String value) throws TException {
        synchronized (lock) {
            keyValuePair.put(key, value);
        }
    }

    @Override
    public String getValue(String key) throws TException {
        synchronized (lock) {
            return keyValuePair.getOrDefault(key, OperationType.NONE.name());
        }
    }

    @Override
    public int removeKey(String key) throws TException {
        synchronized (lock) {
            if (keyValuePair.remove(key) != null) {
                return OperationType.SUCCESS.getEnumValue();
            } else {
                return OperationType.FAILURE.getEnumValue();
            }
        }
    }
}
