package javalibrary.exception;

// Exceção geral para erros relacionados às regras da biblioteca.
 
public class LibraryException extends Exception {

    public LibraryException(String message) {
        super(message);
    }
}
