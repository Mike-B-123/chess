package websocket.messages;

public class ErrorMessage {
    private String errorMessage ;
    public ErrorMessage(String message){
        this.errorMessage = message ;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
