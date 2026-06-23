package javalibrary.exception;

// Exceção usada quando uma operação não pode ser feita por causa de empréstimo ativo.
 
public class ActiveLoanException extends LibraryException {

    public ActiveLoanException(String message) {
        super(message);
    }
}
