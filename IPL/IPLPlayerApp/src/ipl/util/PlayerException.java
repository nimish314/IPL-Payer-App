package ipl.util;

/**
 * Custom checked exception for player data validation errors.
 * Demonstrates Custom Exception Handling.
 */
public class PlayerException extends Exception {

    private static final long serialVersionUID = 1L;

    public PlayerException(String message) {
        super(message);
    }

    public PlayerException(String message, Throwable cause) {
        super(message, cause);
    }
}
