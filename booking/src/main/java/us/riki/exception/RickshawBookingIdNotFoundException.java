package us.riki.exception;

import us.riki.model.RickshawBooking;

public class RickshawBookingIdNotFoundException extends RuntimeException{
    public RickshawBookingIdNotFoundException(String message){
        super(message);
    }

    public RickshawBookingIdNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
