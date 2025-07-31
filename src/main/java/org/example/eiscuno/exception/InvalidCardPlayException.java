package org.example.eiscuno.exception;

/**
 * Exception thrown when a card cannot be played according to UN rules.
 */
public class InvalidCardPlayException extends RuntimeException {

    public InvalidCardPlayException(String message) {
        super(message);
    }


    // Optional: Constructor with cause (useful for chaining exceptions)
    public InvalidCardPlayException(String message, Throwable cause) {
        super(message, cause);
    }
}
