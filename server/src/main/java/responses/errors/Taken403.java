package responses.errors;

import model.Message;

public class Taken403 extends Exception {
    public int errorCode = 403 ;
    private final Message message = new Message("Error: Could not complete request") ;
    public int getErrorCode() {
        return errorCode;
    }
    public String getMessage(){
        return  this.message.message() ;
    }

}
