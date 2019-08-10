//package com.northeastern.edu.utils;
//
//import generated.thrift.impl.MessageType;
//import generated.thrift.impl.RPCPacketService;
//import org.apache.thrift.TException;
//import org.apache.thrift.protocol.TBinaryProtocol;
//import org.apache.thrift.protocol.TProtocol;
//import org.apache.thrift.transport.TSocket;
//import org.apache.thrift.transport.TTransport;
//import org.apache.thrift.transport.TTransportException;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Logger;
//
//public class RPCPacketServiceImpl2 implements RPCPacketService.Iface {
//
//    //Logger for the class.
//    private static Logger LOGGER = Logger.getLogger(RPCPacketServiceImpl2.class.getName());
//
//    //Synchronize block variable.
//    private static final Object lock = new Object();
//
//    //Variable representing the type of packet.
//    private int operationType;
//
//    //Map to store the key and value initialized from client
//    private Map<String, String> keyValuePair;
//
//    //Replica Connections.
//    private Map<RPCPacketService.Client, Boolean> replicas;
//
//    //Replica Port Numbers on localhost.
//    private List<Integer> replicaPorts;
//
//    //Host Address.
//    private String hostAddress;
//
//    //FileName to write and read data from memory.
//    private String memoryObjectFileName = "data";
//
//    //Commit Status for a transaction.
//    private MessageType commitStatus;
//
//    //Previous value for a given key
//    private String previousValue;
//
//    //Current Key being deleted or inserted.
//    private String currentKey;
//
//    //Current Value for the current key.
//    private String currentValue;
//
//    //Enum for packet types.
//    public enum OperationType {
//        PUT(1),
//        DELETE(2);
//
//        //Variable representing the enum.
//        private final int enumValue;
//
//        OperationType(int val) {
//            this.enumValue = val;
//        }
//
//        /*
//        Returns the integer value of enum.
//         */
//        public int getEnumValue() {
//            return this.enumValue;
//        }
//    }
//
//    //Initializes the value for packet and keyValueStore.
//    public RPCPacketServiceImpl2(List<Integer> replicaPorts, Integer portNumber) throws IOException {
//
//        operationType = 0;
//        keyValuePair = new HashMap<>();
//        commitStatus = MessageType.NONE;
//
//        //Host Address
//        hostAddress = "localhost";
//
//        //create connection for the clients.
//        this.replicaPorts = replicaPorts;
//        this.memoryObjectFileName += ":" + portNumber.toString() + ".json";
//
//        //Load the data from the memory.
//        Map<String, Object> temp = (Map<String, Object>) loadMemoryObject(0);
//        long action = (long) temp.get("latestAction");
//        undoOrRedo(temp, (long) action);
//    }
//
//    //Based on the previous transaction status, checks if the
//    //previously committed transaction needs to undone.
//    private void undoOrRedo(Map<String, Object> memoryObject, long action) {
//
//        //get the commit status.
//        if (this.commitStatus != MessageType.ABORT) {
//            String commit = (String) memoryObject.get("commitStatus");
//            this.commitStatus =  MessageType.valueOf(commit);
//        }
//
//        //if the commit status is false, then the transaction is committed
//        //before the crash or exiting of application. Hence, undo the last
//        //transaction.
//        if (this.commitStatus == MessageType.COMMIT_REQUEST || this.commitStatus == MessageType.ABORT) {
//            String commitKey = (String) memoryObject.get("commitKey");
//            this.previousValue = (String) memoryObject.get("previousCommitKeyValue");
//            this.commitStatus = MessageType.NONE;
//
//            if (action == OperationType.PUT.getEnumValue()) {
//                if (this.previousValue != null && !this.previousValue.isEmpty()) {
//                    writeToMemory(commitKey, null, previousValue, this.commitStatus, OperationType.PUT.getEnumValue());
//                } else {
//                    deleteFromMemory(memoryObject, commitKey, this.commitStatus, OperationType.PUT.getEnumValue());
//                }
//            } else if (action == OperationType.DELETE.getEnumValue()) {
//                writeToMemory(commitKey, null, this.previousValue, this.commitStatus, OperationType.DELETE.getEnumValue());
//            }
//        }
//    }
//
//    //Loads the key value data store from memory.
//    private Object loadMemoryObject(int mode) throws IllegalStateException, IOException {
//        try {
//            FileReader reader = new FileReader(memoryObjectFileName);
//            JSONParser jsonParser = new JSONParser();
//            return jsonParser.parse(reader);
//        } catch (IOException e) {
//            String message = "Error loading data from memory: " + e.getMessage();
//            LOGGER.severe(message);
//
//            if (mode == 0) {
//                new File(memoryObjectFileName).createNewFile();
//            } else {
//                this.commitStatus = MessageType.ABORT;
//            }
//
//        } catch (ParseException e) {
//            LOGGER.info("File: " + memoryObjectFileName + " is empty.");
//        }
//
//        return defaultMemoryObject();
//    }
//
//    //Generates the structure of a default memory object
//    private Object defaultMemoryObject() {
//        Map<String, Object> defaultMemoryObject = new HashMap<>();
//        defaultMemoryObject.put("commitKey", "");
//        defaultMemoryObject.put("commitValue", "");
//        defaultMemoryObject.put("commitStatus", MessageType.NONE.toString());
//        defaultMemoryObject.put("latestAction", (long)0);
//        defaultMemoryObject.put("data", new HashMap<String, String>());
//        return defaultMemoryObject;
//    }
//
//    //Creates or updates the data store with commit request key-values
//    //and updated data store state.
//    private void writeToMemory(String key, String value, String previousValue, MessageType commit, int action) {
//        try {
//            //Creating a map of values to store.
//            JSONObject jsonObject = new JSONObject();
//            OutputStream writer = new FileOutputStream(memoryObjectFileName);
//            jsonObject.put("commitKey", key);
//            jsonObject.put("commitStatus", commit.toString());
//            jsonObject.put("latestAction", action);
//
//            if (value == null) {
//                if (previousValue != null) {
//                    jsonObject.put("commitValue", previousValue);
//                } else {
//                    jsonObject.put("commitValue", "");
//                }
//
//                if (!key.isEmpty()) {
//                    keyValuePair.put(key, previousValue);
//                }
//
//            } else {
//                jsonObject.put("commitValue", value);
//                if (keyValuePair.containsKey(key)) {
//                    //Previous value for an existing key
//                    this.previousValue = keyValuePair.get(key);
//                    jsonObject.put("previousCommitKeyValue", this.previousValue);
//                }
//
//                if (!key.isEmpty()) {
//                    keyValuePair.put(key, value);
//                }
//            }
//
//            jsonObject.put("data", keyValuePair);
//            writer.write(jsonObject.toJSONString().getBytes());
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            LOGGER.severe("Error saving the data store to memory: " + e.getMessage());
//            this.commitStatus = MessageType.ABORT;
//        }
//
//    }
//
//    //Deletes the key and the value associated with it from the memory.
//    private void deleteFromMemory(Object memoryObject, String key, MessageType commitRequest, int action) {
//        try {
//            //Creating a map of values to store.
//            JSONObject jsonObject = new JSONObject();
//            OutputStream writer = new FileOutputStream(memoryObjectFileName);
//            jsonObject.put("commitKey", key);
//            jsonObject.put("commitValue", "");
//            jsonObject.put("commitStatus", commitRequest.toString());
//            jsonObject.put("latestAction", action);
//
//            //Stored list in memory
//            keyValuePair = (Map<String, String>) ((Map<String, Object>) memoryObject).get("data");
//
//            if (keyValuePair.containsKey(key)) {
//                //Previous value for an existing key
//                this.previousValue = keyValuePair.get(key);
//                jsonObject.put("previousCommitKeyValue", previousValue);
//            }
//
//            //Remove Key from the pair.
//            keyValuePair.remove(key);
//
//            jsonObject.put("data", keyValuePair);
//            writer.write(jsonObject.toJSONString().getBytes());
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            LOGGER.severe("Error saving the data store to memory: " + e.getMessage());
//            this.commitStatus = MessageType.ABORT;
//        }
//    }
//
//    //Creates a client connection for a given port.
//    private void createConnection(Integer replicaPort) {
//        try {
//            TTransport transport = new TSocket(this.hostAddress, replicaPort);
//            transport.open();
//
//            TProtocol protocol = new TBinaryProtocol(transport);
//
//            RPCPacketService.Client client = new RPCPacketService.Client(protocol);
//            if (client.ping() == MessageType.SUCCESS)
//                this.replicas.put(client, false);
//        } catch (TTransportException e) {
//            LOGGER.severe("Error creating connection to the client: " + e.getMessage());
//        } catch (TException e) {
//            LOGGER.warning("Error creating connection to replica server on port: " + replicaPort);
//        }
//    }
//
//    @Override
//    public int getType() throws TException {
//        return this.operationType;
//    }
//
//    @Override
//    public void setType(int type) throws TException {
//        this.operationType = type;
//
//    }
//
//    @Override
//    public List<String> getKeys() throws TException {
//        try {
//            Map<String, Object> memoryObject = (Map<String, Object>) loadMemoryObject(1);
//            this.keyValuePair = (Map<String, String>) memoryObject.get("data");
//            return new ArrayList<>(keyValuePair.keySet());
//        } catch (IOException e) {
//            LOGGER.severe("Error loading memory object");
//            throw new TException("Error loading memory object: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public List<String> replicaAddresses() throws TException {
//        //For each replica port number
//        List<String> ports = new ArrayList<>();
//        for (Integer portNum : this.replicaPorts) {
//            ports.add(portNum.toString());
//        }
//
//        return ports;
//    }
//
//    @Override
//    public MessageType ping() throws TException {
//        LOGGER.info("Ping message received");
//        return MessageType.SUCCESS;
//    }
//
//    @Override
//    public MessageType setKeyValue(String key, String value, MessageType commit) throws TException {
//        checkOrCreateConnection();
//
//        synchronized (lock) {
//
//            this.currentValue = value;
//            this.currentKey = key;
//            this.previousValue = "";
//
//            try {
//                this.commitStatus = commit;
//
//                //Commit the transaction to memory.
//                if (commit == MessageType.COORDINATOR) {
//                    writeToMemory(key, value, previousValue, MessageType.COMMIT_REQUEST, OperationType.PUT.getEnumValue());
//                    resetReplicaFlags();
//                } else {
//                    writeToMemory(key, value, previousValue, commit, OperationType.PUT.getEnumValue());
//                }
//
//                //Execute 2PC Commit Protocol.
//                return TWOPCCommitProtocolAction(key, value, this.commitStatus, OperationType.PUT.getEnumValue());
//            } catch (IOException e) {
//                LOGGER.severe("Error loading memory object");
//                throw new TException("Error loading memory object: " + e.getMessage());
//            }
//        }
//    }
//
//    //Resets the confirmation of event completion for each replica
//    private void resetReplicaFlags() {
//        for (RPCPacketService.Client replica :  this.replicas.keySet()) {
//            this.replicas.put(replica, false);
//        }
//    }
//
//    //Creates actions based on the type of message.
//    private MessageType TWOPCCommitProtocolAction(String key, String value, MessageType commit, int action) throws TException, IOException {
//        switch (commit) {
//            case COORDINATOR:
//                MessageType response = commitRequest(key, value, action);
//                if (response == MessageType.SUCCESS) {
//                    return commitTransaction(key, action);
//                } else if (response == MessageType.ABORT) {
//                    //Abort Transactions at replicas
//                    abortTransaction(action);
//
//                    //Abort Transactions locally.
//                    abort(action);
//
//                    return response;
//                }
//                break;
//            case COMMIT_REQUEST:
//                //If the workflow reached this point, then transaction has been written to disk
//                return this.commitStatus;
//            case COMMIT:
//                //Check if the transaction is already committed. If not commit the transaction.
//                Map<String, Object> memoryObject = (Map<String, Object>) loadMemoryObject(1);
//                this.keyValuePair = (Map<String, String>) memoryObject.get("data");
//
//                if (action == OperationType.PUT.getEnumValue()) {
//                    if (!this.keyValuePair.containsKey(key)) {
//                        this.commitStatus = MessageType.FAILURE;
//                    }
//                    this.commitStatus = MessageType.COMMIT;
//                    updateMemoryObject(this.commitStatus, OperationType.PUT.getEnumValue());
//                } else if (action == OperationType.DELETE.getEnumValue()) {
//                    if (this.keyValuePair.containsKey(key)) {
//                        this.commitStatus = MessageType.FAILURE;
//                    }
//                    this.commitStatus = MessageType.COMMIT;
//                    updateMemoryObject(this.commitStatus, OperationType.DELETE.getEnumValue());
//                }
//
//
//                return this.commitStatus;
//            case ABORT:
//                undoOrRedo((Map<String, Object>) loadMemoryObject(1), action);
//                break;
//            default:
//                LOGGER.info("No action needed for: " + commit);
//        }
//
//        return commit;
//    }
//
//    //Updates the memory object with the new commit and operation values.
//    private void updateMemoryObject(MessageType commitStatus, int action) throws TException {
//        try {
//            JSONObject object = (JSONObject) loadMemoryObject(1);
//            object.put("commitStatus", commitStatus.toString());
//            object.put("latestAction", action);
//            OutputStream writer = new FileOutputStream(memoryObjectFileName);
//            writer.write(object.toJSONString().getBytes());
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            LOGGER.severe("Error loading memory object: " + e.getMessage());
//            throw new TException("Error loading memory object: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public MessageType commit(String key, int action) throws TException {
//        try {
//            return TWOPCCommitProtocolAction(key, "", MessageType.COMMIT, action);
//        } catch (IOException e) {
//            LOGGER.severe("Error loading memory object");
//            throw new TException("Error loading memory object: " + e.getMessage());
//        }
//    }
//
//    //Sends commit-request message with the key and value sent
//    //from the client.
//    private MessageType commitRequest(String key, String value, int action) {
//        //for each replica.
//        MessageType cohortStatus = MessageType.NONE;
//        for (RPCPacketService.Client replica : replicas.keySet()) {
//            try {
//                if (action == OperationType.PUT.getEnumValue()) {
//                    cohortStatus = replica.setKeyValue(key, value, MessageType.COMMIT_REQUEST);
//                } else if (action == OperationType.DELETE.getEnumValue()) {
//                    cohortStatus = replica.removeKey(key, MessageType.COMMIT_REQUEST);
//                }
//
//                //When any of the client sends back an abort message, initiate abortTransaction.
//                if (cohortStatus == MessageType.ABORT) {
//                    abortTransaction(action);
//                    return MessageType.ABORT;
//                }
//
//                this.replicas.put(replica, true);
//            } catch (TException e) {
//                LOGGER.severe("Error initializing key-value pair at replica: " + getAddressForClient(replica) + e.getMessage());
//                return MessageType.ABORT;
//            }
//        }
//
//        return commitStatus;
//    }
//
//    //Sends a commit transaction request to all the cohorts
//    //and verifies their responses.
//    private MessageType commitTransaction(String key, int action) {
//        //for each replica.
//        MessageType cohortStatus = MessageType.NONE;
//        for (RPCPacketService.Client replica : replicas.keySet()) {
//            try {
//                cohortStatus = replica.commit(key, action);
//
//                //When any of the client sends back an abort message, initiate abortTransaction.
//                if (cohortStatus != MessageType.COMMIT) {
//                    abortTransaction(action);
//                }
//                this.replicas.put(replica, true);
//            } catch (TException e) {
//                LOGGER.severe("Error initializing key-value pair at replica: " + e.getMessage());
//            }
//        }
//
//        return cohortStatus;
//    }
//
//    @Override
//    public void abort(int action) throws TException {
//        try {
//            undoOrRedo((Map<String, Object>) loadMemoryObject(1), action);
//        } catch (IOException e) {
//            LOGGER.severe("Error Loading memory object");
//            throw new TException("Error Loading memory object: " + e.getMessage());
//        }
//
//    }
//
//    //For each replica, it sends a abort request.
//    private void abortTransaction(int action) {
//        //for each replica.
//        for (RPCPacketService.Client replica : replicas.keySet()) {
//            try {
//                if (this.replicas.get(replica)) {
//                    replica.abort(action);
//                }
//            } catch (TException e) {
//                LOGGER.severe("Error while invoking the abort request for replica- " + getAddressForClient(replica) + ": " + e.getMessage());
//            }
//        }
//    }
//
//    @Override
//    public String getValue(String key) throws TException {
//        synchronized (lock) {
//            try {
//                Map<String, Object> memoryObject = (Map<String, Object>) loadMemoryObject(1);
//                this.keyValuePair = (Map<String, String>) memoryObject.get("data");
//                return this.keyValuePair.getOrDefault(key, MessageType.NONE.toString());
//            } catch (IOException e) {
//                LOGGER.severe("Error loading memory object");
//                throw new TException("Error loading memory object: " + e.getMessage());
//            }
//        }
//    }
//
//    @Override
//    public MessageType removeKey(String key, MessageType commit) throws TException {
//        checkOrCreateConnection();
//
//        synchronized (lock) {
//
//            this.currentKey = key;
//            this.currentValue = "";
//            this.previousValue = "";
//
//            try {
//                //Commit the transaction to memory.
//                this.commitStatus = commit;
//                if (commit == MessageType.COORDINATOR) {
//                    deleteFromMemory(loadMemoryObject(1), key, MessageType.COMMIT_REQUEST, OperationType.DELETE.getEnumValue());
//                    resetReplicaFlags();
//                } else {
//                    deleteFromMemory(loadMemoryObject(1), key, commit, OperationType.DELETE.getEnumValue());
//                }
//
//                //Execute 2PC Commit Protocol.
//                return TWOPCCommitProtocolAction(key, "", this.commitStatus, OperationType.DELETE.getEnumValue());
//            } catch (IOException e) {
//                LOGGER.severe("Error loading memory object");
//                throw new TException("Error loading memory object: " + e.getMessage());
//            }
//
//        }
//    }
//
//    //Gets the socket address for a given client/server object.
//    private String getAddressForClient(RPCPacketService.Client client) {
//        TSocket socket = (TSocket) client.getOutputProtocol().getTransport();
//        return socket.getSocket().getRemoteSocketAddress().toString();
//    }
//}
