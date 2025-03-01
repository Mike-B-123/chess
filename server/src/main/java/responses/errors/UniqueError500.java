package responses.errors;

import model.Message;

public class UniqueError500 extends Exception{
    public int errorCode = 500 ;
    private final Message message = new Message("Error: Could not complete request") ;
    public int getErrorCode() {
        return errorCode;
    }
    public String getMessage(){
        return  this.message.message() ;
    }

}
