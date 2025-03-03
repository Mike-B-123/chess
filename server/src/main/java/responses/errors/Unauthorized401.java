package responses.errors;

import model.Message;

public class Unauthorized401 extends Exception {
    public int errorCode = 401 ;
    private final Message message = new Message("Error: You are unauthorizeed") ;
    public int getErrorCode() {
        return errorCode;
    }
    public Message getErrorMessage(){
        return  this.message ;
    }

}
