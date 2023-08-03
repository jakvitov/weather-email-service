package cz.jakvitov.wes.exception;

public class EmailAlreadyInDatabaseException extends RuntimeException{

    String email;

    public EmailAlreadyInDatabaseException(String email) {
        this.email = email;
    }

    public EmailAlreadyInDatabaseException(String message, String email) {
        super(message);
        this.email = email;
    }

    public EmailAlreadyInDatabaseException(String message, Throwable cause, String email) {
        super(message, cause);
        this.email = email;
    }

    public EmailAlreadyInDatabaseException(Throwable cause, String email) {
        super(cause);
        this.email = email;
    }

    public EmailAlreadyInDatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String email) {
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
