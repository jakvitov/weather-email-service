package cz.jakvitov.wes.utils.exception;

public class ExceptionUtils {

    public static boolean exceptionIsCausedBy(Throwable exception, Class<? extends Throwable> clazz){
        if (exception == null) {
            return false;
        }

        if (clazz.isInstance(exception)) {
            return true;
        }
        Throwable cause = exception.getCause();
        while (cause != null) {
            if (clazz.isInstance(exception)) {
                return true;
            }
            cause = cause.getCause();
        }

        return false;
    }

}
