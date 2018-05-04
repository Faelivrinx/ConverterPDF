package convert.exception;

public class InvalidFormatConvertion extends RuntimeException{

    public InvalidFormatConvertion(String message) {
        super(message);
    }

    public InvalidFormatConvertion(String message, Throwable cause) {
        super(message, cause);
    }

}
