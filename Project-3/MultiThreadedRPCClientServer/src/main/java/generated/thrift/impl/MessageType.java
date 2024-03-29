/**
 * Autogenerated by Thrift Compiler (0.12.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package generated.thrift.impl;


@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.12.0)", date = "2019-07-16")
public enum MessageType implements org.apache.thrift.TEnum {
  COORDINATOR(0),
  COMMIT_REQUEST(1),
  COMMIT(2),
  ABORT(3),
  SUCCESS(4),
  FAILURE(5),
  NONE(6);

  private final int value;

  private MessageType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  @org.apache.thrift.annotation.Nullable
  public static MessageType findByValue(int value) { 
    switch (value) {
      case 0:
        return COORDINATOR;
      case 1:
        return COMMIT_REQUEST;
      case 2:
        return COMMIT;
      case 3:
        return ABORT;
      case 4:
        return SUCCESS;
      case 5:
        return FAILURE;
      case 6:
        return NONE;
      default:
        return null;
    }
  }
}
