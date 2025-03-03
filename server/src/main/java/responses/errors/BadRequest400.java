package responses.errors;

import model.Message;

public class BadRequest400 extends Exception{
    public int errorCode = 400 ;
    private final Message message = new Message("Error: This was a null request") ;
    public int getErrorCode() {
        return errorCode;
    }
    public Message getErrorMessage(){
        return  this.message ;
    }

}
/// do try catch blocks to rpocess errors in handlers
///