package javalibrary.exception;

// Exceção usada quando um livro não está disponível para empréstimo.
 
public class BookUnavailableException extends LibraryException {

    public BookUnavailableException(String message) {
        super(message);
    }
}
