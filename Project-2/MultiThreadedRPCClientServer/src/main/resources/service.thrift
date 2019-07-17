namespace java thrift.impl

struct RPCPacket {
    1: i32 type,
    2: string key,
    3: string value
}

service RPCPacketService {
    i32               getType(),
    void              setType(1:i32 type),
    list <string>     getKeys(),
    void              setKeyValue(2:string key, 3:string value),
    string            getValue(2:string key),
    i32               removeKey(2:string key)
}