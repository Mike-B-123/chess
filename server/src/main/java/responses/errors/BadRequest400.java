package responses.errors;

import model.Message;

public class BadRequest400 extends Exception{
    public int errorCode = 400 ;
    private final Message message = new Message("Error: Could not complete request") ;
    public int getErrorCode() {
        return errorCode;
    }
    public String getMessage(){
        return  this.message.message() ;
    }

}
/// do try catch blocks to rpocess errors in handlers
///