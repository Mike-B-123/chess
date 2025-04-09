package websocket.messages;

public class NotificationMessage extends ServerMessage{
    private String notificationMessage ;
    public NotificationMessage(String message){
        super(ServerMessageType.NOTIFICATION);
        this.notificationMessage = message ;
    }
    public String getNotificationMessage() {
        return notificationMessage;
    }
}
