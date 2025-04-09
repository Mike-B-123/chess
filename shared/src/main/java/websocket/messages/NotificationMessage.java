package websocket.messages;

public class NotificationMessage {
    private String notificationMessage ;
    public NotificationMessage(String message){
        this.notificationMessage = message ;
    }
    public String getNotificationMessage() {
        return notificationMessage;
    }
}
