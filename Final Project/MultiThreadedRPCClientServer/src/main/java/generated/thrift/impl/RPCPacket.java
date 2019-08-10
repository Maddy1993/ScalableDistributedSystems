/**
 * Autogenerated by Thrift Compiler (0.12.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package generated.thrift.impl;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.12.0)", date = "2019-08-04")
public class RPCPacket implements org.apache.thrift.TBase<RPCPacket, RPCPacket._Fields>, java.io.Serializable, Cloneable, Comparable<RPCPacket> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("RPCPacket");

  private static final org.apache.thrift.protocol.TField TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("type", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField SEQUENCE_NUMBER_FIELD_DESC = new org.apache.thrift.protocol.TField("sequence_number", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField KEY_VALUE_FIELD_DESC = new org.apache.thrift.protocol.TField("keyValue", org.apache.thrift.protocol.TType.MAP, (short)3);
  private static final org.apache.thrift.protocol.TField OPERATION_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("operationType", org.apache.thrift.protocol.TType.I32, (short)4);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new RPCPacketStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new RPCPacketTupleSchemeFactory();

  public int type; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String sequence_number; // required
  public @org.apache.thrift.annotation.Nullable java.util.Map<java.lang.String,java.lang.String> keyValue; // required
  public int operationType; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TYPE((short)1, "type"),
    SEQUENCE_NUMBER((short)2, "sequence_number"),
    KEY_VALUE((short)3, "keyValue"),
    OPERATION_TYPE((short)4, "operationType");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // TYPE
          return TYPE;
        case 2: // SEQUENCE_NUMBER
          return SEQUENCE_NUMBER;
        case 3: // KEY_VALUE
          return KEY_VALUE;
        case 4: // OPERATION_TYPE
          return OPERATION_TYPE;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __TYPE_ISSET_ID = 0;
  private static final int __OPERATIONTYPE_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TYPE, new org.apache.thrift.meta_data.FieldMetaData("type", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.SEQUENCE_NUMBER, new org.apache.thrift.meta_data.FieldMetaData("sequence_number", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.KEY_VALUE, new org.apache.thrift.meta_data.FieldMetaData("keyValue", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    tmpMap.put(_Fields.OPERATION_TYPE, new org.apache.thrift.meta_data.FieldMetaData("operationType", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(RPCPacket.class, metaDataMap);
  }

  public RPCPacket() {
  }

  public RPCPacket(
    int type,
    java.lang.String sequence_number,
    java.util.Map<java.lang.String,java.lang.String> keyValue,
    int operationType)
  {
    this();
    this.type = type;
    setTypeIsSet(true);
    this.sequence_number = sequence_number;
    this.keyValue = keyValue;
    this.operationType = operationType;
    setOperationTypeIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public RPCPacket(RPCPacket other) {
    __isset_bitfield = other.__isset_bitfield;
    this.type = other.type;
    if (other.isSetSequence_number()) {
      this.sequence_number = other.sequence_number;
    }
    if (other.isSetKeyValue()) {
      java.util.Map<java.lang.String,java.lang.String> __this__keyValue = new java.util.HashMap<java.lang.String,java.lang.String>(other.keyValue);
      this.keyValue = __this__keyValue;
    }
    this.operationType = other.operationType;
  }

  public RPCPacket deepCopy() {
    return new RPCPacket(this);
  }

  @Override
  public void clear() {
    setTypeIsSet(false);
    this.type = 0;
    this.sequence_number = null;
    this.keyValue = null;
    setOperationTypeIsSet(false);
    this.operationType = 0;
  }

  public int getType() {
    return this.type;
  }

  public RPCPacket setType(int type) {
    this.type = type;
    setTypeIsSet(true);
    return this;
  }

  public void unsetType() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __TYPE_ISSET_ID);
  }

  /** Returns true if field type is set (has been assigned a value) and false otherwise */
  public boolean isSetType() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __TYPE_ISSET_ID);
  }

  public void setTypeIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __TYPE_ISSET_ID, value);
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getSequence_number() {
    return this.sequence_number;
  }

  public RPCPacket setSequence_number(@org.apache.thrift.annotation.Nullable java.lang.String sequence_number) {
    this.sequence_number = sequence_number;
    return this;
  }

  public void unsetSequence_number() {
    this.sequence_number = null;
  }

  /** Returns true if field sequence_number is set (has been assigned a value) and false otherwise */
  public boolean isSetSequence_number() {
    return this.sequence_number != null;
  }

  public void setSequence_numberIsSet(boolean value) {
    if (!value) {
      this.sequence_number = null;
    }
  }

  public int getKeyValueSize() {
    return (this.keyValue == null) ? 0 : this.keyValue.size();
  }

  public void putToKeyValue(java.lang.String key, java.lang.String val) {
    if (this.keyValue == null) {
      this.keyValue = new java.util.HashMap<java.lang.String,java.lang.String>();
    }
    this.keyValue.put(key, val);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Map<java.lang.String,java.lang.String> getKeyValue() {
    return this.keyValue;
  }

  public RPCPacket setKeyValue(@org.apache.thrift.annotation.Nullable java.util.Map<java.lang.String,java.lang.String> keyValue) {
    this.keyValue = keyValue;
    return this;
  }

  public void unsetKeyValue() {
    this.keyValue = null;
  }

  /** Returns true if field keyValue is set (has been assigned a value) and false otherwise */
  public boolean isSetKeyValue() {
    return this.keyValue != null;
  }

  public void setKeyValueIsSet(boolean value) {
    if (!value) {
      this.keyValue = null;
    }
  }

  public int getOperationType() {
    return this.operationType;
  }

  public RPCPacket setOperationType(int operationType) {
    this.operationType = operationType;
    setOperationTypeIsSet(true);
    return this;
  }

  public void unsetOperationType() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __OPERATIONTYPE_ISSET_ID);
  }

  /** Returns true if field operationType is set (has been assigned a value) and false otherwise */
  public boolean isSetOperationType() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __OPERATIONTYPE_ISSET_ID);
  }

  public void setOperationTypeIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __OPERATIONTYPE_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case TYPE:
      if (value == null) {
        unsetType();
      } else {
        setType((java.lang.Integer)value);
      }
      break;

    case SEQUENCE_NUMBER:
      if (value == null) {
        unsetSequence_number();
      } else {
        setSequence_number((java.lang.String)value);
      }
      break;

    case KEY_VALUE:
      if (value == null) {
        unsetKeyValue();
      } else {
        setKeyValue((java.util.Map<java.lang.String,java.lang.String>)value);
      }
      break;

    case OPERATION_TYPE:
      if (value == null) {
        unsetOperationType();
      } else {
        setOperationType((java.lang.Integer)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case TYPE:
      return getType();

    case SEQUENCE_NUMBER:
      return getSequence_number();

    case KEY_VALUE:
      return getKeyValue();

    case OPERATION_TYPE:
      return getOperationType();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case TYPE:
      return isSetType();
    case SEQUENCE_NUMBER:
      return isSetSequence_number();
    case KEY_VALUE:
      return isSetKeyValue();
    case OPERATION_TYPE:
      return isSetOperationType();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof RPCPacket)
      return this.equals((RPCPacket)that);
    return false;
  }

  public boolean equals(RPCPacket that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_type = true;
    boolean that_present_type = true;
    if (this_present_type || that_present_type) {
      if (!(this_present_type && that_present_type))
        return false;
      if (this.type != that.type)
        return false;
    }

    boolean this_present_sequence_number = true && this.isSetSequence_number();
    boolean that_present_sequence_number = true && that.isSetSequence_number();
    if (this_present_sequence_number || that_present_sequence_number) {
      if (!(this_present_sequence_number && that_present_sequence_number))
        return false;
      if (!this.sequence_number.equals(that.sequence_number))
        return false;
    }

    boolean this_present_keyValue = true && this.isSetKeyValue();
    boolean that_present_keyValue = true && that.isSetKeyValue();
    if (this_present_keyValue || that_present_keyValue) {
      if (!(this_present_keyValue && that_present_keyValue))
        return false;
      if (!this.keyValue.equals(that.keyValue))
        return false;
    }

    boolean this_present_operationType = true;
    boolean that_present_operationType = true;
    if (this_present_operationType || that_present_operationType) {
      if (!(this_present_operationType && that_present_operationType))
        return false;
      if (this.operationType != that.operationType)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + type;

    hashCode = hashCode * 8191 + ((isSetSequence_number()) ? 131071 : 524287);
    if (isSetSequence_number())
      hashCode = hashCode * 8191 + sequence_number.hashCode();

    hashCode = hashCode * 8191 + ((isSetKeyValue()) ? 131071 : 524287);
    if (isSetKeyValue())
      hashCode = hashCode * 8191 + keyValue.hashCode();

    hashCode = hashCode * 8191 + operationType;

    return hashCode;
  }

  @Override
  public int compareTo(RPCPacket other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetType()).compareTo(other.isSetType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.type, other.type);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetSequence_number()).compareTo(other.isSetSequence_number());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSequence_number()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.sequence_number, other.sequence_number);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetKeyValue()).compareTo(other.isSetKeyValue());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetKeyValue()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.keyValue, other.keyValue);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetOperationType()).compareTo(other.isSetOperationType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOperationType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.operationType, other.operationType);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  @org.apache.thrift.annotation.Nullable
  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("RPCPacket(");
    boolean first = true;

    sb.append("type:");
    sb.append(this.type);
    first = false;
    if (!first) sb.append(", ");
    sb.append("sequence_number:");
    if (this.sequence_number == null) {
      sb.append("null");
    } else {
      sb.append(this.sequence_number);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("keyValue:");
    if (this.keyValue == null) {
      sb.append("null");
    } else {
      sb.append(this.keyValue);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("operationType:");
    sb.append(this.operationType);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class RPCPacketStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public RPCPacketStandardScheme getScheme() {
      return new RPCPacketStandardScheme();
    }
  }

  private static class RPCPacketStandardScheme extends org.apache.thrift.scheme.StandardScheme<RPCPacket> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, RPCPacket struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.type = iprot.readI32();
              struct.setTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // SEQUENCE_NUMBER
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.sequence_number = iprot.readString();
              struct.setSequence_numberIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // KEY_VALUE
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map0 = iprot.readMapBegin();
                struct.keyValue = new java.util.HashMap<java.lang.String,java.lang.String>(2*_map0.size);
                @org.apache.thrift.annotation.Nullable java.lang.String _key1;
                @org.apache.thrift.annotation.Nullable java.lang.String _val2;
                for (int _i3 = 0; _i3 < _map0.size; ++_i3)
                {
                  _key1 = iprot.readString();
                  _val2 = iprot.readString();
                  struct.keyValue.put(_key1, _val2);
                }
                iprot.readMapEnd();
              }
              struct.setKeyValueIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // OPERATION_TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.operationType = iprot.readI32();
              struct.setOperationTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, RPCPacket struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(TYPE_FIELD_DESC);
      oprot.writeI32(struct.type);
      oprot.writeFieldEnd();
      if (struct.sequence_number != null) {
        oprot.writeFieldBegin(SEQUENCE_NUMBER_FIELD_DESC);
        oprot.writeString(struct.sequence_number);
        oprot.writeFieldEnd();
      }
      if (struct.keyValue != null) {
        oprot.writeFieldBegin(KEY_VALUE_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING, struct.keyValue.size()));
          for (java.util.Map.Entry<java.lang.String, java.lang.String> _iter4 : struct.keyValue.entrySet())
          {
            oprot.writeString(_iter4.getKey());
            oprot.writeString(_iter4.getValue());
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(OPERATION_TYPE_FIELD_DESC);
      oprot.writeI32(struct.operationType);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class RPCPacketTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public RPCPacketTupleScheme getScheme() {
      return new RPCPacketTupleScheme();
    }
  }

  private static class RPCPacketTupleScheme extends org.apache.thrift.scheme.TupleScheme<RPCPacket> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, RPCPacket struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetType()) {
        optionals.set(0);
      }
      if (struct.isSetSequence_number()) {
        optionals.set(1);
      }
      if (struct.isSetKeyValue()) {
        optionals.set(2);
      }
      if (struct.isSetOperationType()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetType()) {
        oprot.writeI32(struct.type);
      }
      if (struct.isSetSequence_number()) {
        oprot.writeString(struct.sequence_number);
      }
      if (struct.isSetKeyValue()) {
        {
          oprot.writeI32(struct.keyValue.size());
          for (java.util.Map.Entry<java.lang.String, java.lang.String> _iter5 : struct.keyValue.entrySet())
          {
            oprot.writeString(_iter5.getKey());
            oprot.writeString(_iter5.getValue());
          }
        }
      }
      if (struct.isSetOperationType()) {
        oprot.writeI32(struct.operationType);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, RPCPacket struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.type = iprot.readI32();
        struct.setTypeIsSet(true);
      }
      if (incoming.get(1)) {
        struct.sequence_number = iprot.readString();
        struct.setSequence_numberIsSet(true);
      }
      if (incoming.get(2)) {
        {
          org.apache.thrift.protocol.TMap _map6 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.keyValue = new java.util.HashMap<java.lang.String,java.lang.String>(2*_map6.size);
          @org.apache.thrift.annotation.Nullable java.lang.String _key7;
          @org.apache.thrift.annotation.Nullable java.lang.String _val8;
          for (int _i9 = 0; _i9 < _map6.size; ++_i9)
          {
            _key7 = iprot.readString();
            _val8 = iprot.readString();
            struct.keyValue.put(_key7, _val8);
          }
        }
        struct.setKeyValueIsSet(true);
      }
      if (incoming.get(3)) {
        struct.operationType = iprot.readI32();
        struct.setOperationTypeIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}
