package info;

public class Pack implements java.io.Serializable {

    public Pack(int type) {
        this.type = type;
    }

    public Pack(int type, Object body) {
        this(type);
        this.body = body;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object info) {
        this.body = info;
    }

    public static final int 
            CHECK_SERVER_REQUEST = 0, CHECK_SERVER_APROVED = 1, CHECK_SERVER_REJECTED = 2, 
            JOIN = 3, JOIN_APROVED = 4, JOIN_REJECTED = 5, LEAVE = 6, MESSAGE = 7, 
            MESSAGE_SENT = 8, MESSAGE_REJECTED = 9, REMOVE_FROM_SERVER = 10;
    //static final String UNAVAILABLE_NAME="Unavailable name", UNAVAILABLE_PORT="Unavailable port";
    private int type;
//    private String body;
    private Object body;

}
