package responses.errors;

import model.Message;

public class UniqueError500 extends Exception{
    public int errorCode = 500 ;
    private final Message message = new Message("Error: (Unique Error) could not complete") ;
    public int getErrorCode() {
        return errorCode;
    }
    public Message getErrorMessage(){
        return  this.message ;
    }

}
