package cn.lifehub.config.exception;

public class AuthCheckException extends Exception{

    private static final long serialVersionUID = 1L;

    private String message;

    public AuthCheckException(){

    }

    public AuthCheckException(String message){
        super(message);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
