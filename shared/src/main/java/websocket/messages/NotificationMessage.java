package websocket.messages;

public class NotificationMessage extends ServerMessage{
    private String message ;
    public NotificationMessage(String message){
        super(ServerMessageType.NOTIFICATION);
        this.message = message ;
    }
    public String getNotificationMessage() {
        return message;
    }
    public void setNotificationMessage(String inputMessage){
        this.message = inputMessage ;
    }
}
