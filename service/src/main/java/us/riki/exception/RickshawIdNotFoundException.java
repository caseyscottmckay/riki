package us.riki.exception;

public class RickshawIdNotFoundException extends RuntimeException{

    public RickshawIdNotFoundException(String message) {
        super(message);
    }

    public RickshawIdNotFoundException(String message, Throwable cause){
        super(message, cause);
    }

}
