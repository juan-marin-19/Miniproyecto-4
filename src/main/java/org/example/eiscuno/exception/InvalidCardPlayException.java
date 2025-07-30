package org.example.eiscuno.exception;

/**
 * Excepción lanzada cuando una carta no puede ser jugada según las reglas del UNO.
 */
public class InvalidCardPlayException extends RuntimeException {

    public InvalidCardPlayException(String message) {
        super(message);
    }

    // Opcional: Constructor con causa (útil para encadenar excepciones)
    public InvalidCardPlayException(String message, Throwable cause) {
        super(message, cause);
    }
}
