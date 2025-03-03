package responses.errors;

import model.Message;

public class Taken403 extends Exception {
    public int errorCode = 403 ;
    private final Message message = new Message("Error: This is already taken") ;
    public int getErrorCode() {
        return errorCode;
    }
    public Message getErrorMessage(){
        return  this.message ;
    }

}
