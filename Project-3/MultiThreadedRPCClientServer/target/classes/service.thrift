namespace java thrift.impl

enum MessageType {
    COORDINATOR,
    COMMIT_REQUEST,
    COMMIT,
    ABORT,
    SUCCESS,
    FAILURE,
    NONE
}

struct RPCPacket {
    1: i32 type,
    2: string key,
    3: string value
}

service RPCPacketService {
    i32             getType(),
    void            setType(1:i32 type),
    list <string>   getKeys(),
    MessageType     setKeyValue(1:string key, 2:string value, 3:MessageType commit),
    string          getValue(1:string key),
    MessageType     removeKey(1:string key, 2:MessageType commit)
    MessageType     commit(1:string key, 2:i32 action);
    void            abort(1:i32 action);
    list <string>   replicaAddresses();
    MessageType     ping()
}