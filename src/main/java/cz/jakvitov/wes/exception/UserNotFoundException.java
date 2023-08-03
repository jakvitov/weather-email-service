package cz.jakvitov.wes.exception;

public class UserNotFoundException extends RuntimeException{

    private String email;

    public UserNotFoundException(String email) {
        this.email = email;
    }

    public UserNotFoundException(String message, String email) {
        super(message);
        this.email = email;
    }

    public UserNotFoundException(String message, Throwable cause, String email) {
        super(message, cause);
        this.email = email;
    }

    public UserNotFoundException(Throwable cause, String email) {
        super(cause);
        this.email = email;
    }

    public UserNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String email) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
