package responses.errors;

import model.Message;

public class BadRequest400 extends Exception{
    public int errorCode = 400 ;
    public int getErrorCode() {
        return errorCode;
    }
    public Message setGetMessage(String message){
        Message mess = new Message(message) ;
        return  mess ;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
/// do try catch blocks to rpocess errors in handlers
///