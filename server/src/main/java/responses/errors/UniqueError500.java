package responses.errors;

import model.Message;

public class UniqueError500 extends Exception{
    public int errorCode = 500 ;
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
