package javalibrary.model;

import java.time.LocalDate;

// Representa o empréstimo de um livro para um usuário da biblioteca.
public class Loan {

    private Book book;
    private Patron patron;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private boolean returned;

    public Loan(Book book, Patron patron) {
        this(book, patron, LocalDate.now());
    }

    public Loan(Book book, Patron patron, LocalDate loanDate) {
        this(book, patron, loanDate, loanDate.plusDays(14), false);
    }

    public Loan(Book book, Patron patron, LocalDate loanDate, LocalDate dueDate, boolean returned) {
        if (book == null) {
            throw new IllegalArgumentException("O livro do empréstimo não pode ser vazio.");
        }

        if (patron == null) {
            throw new IllegalArgumentException("O usuário do empréstimo não pode ser vazio.");
        }

        if (loanDate == null) {
            throw new IllegalArgumentException("A data do empréstimo não pode ser vazia.");
        }

        if (dueDate == null || dueDate.isBefore(loanDate)) {
            throw new IllegalArgumentException("A data de devolução prevista é inválida.");
        }

        this.book = book;
        this.patron = patron;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returned = returned;
    }

    public Book getBook() {
        return book;
    }

    public Patron getPatron() {
        return patron;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public boolean isActive() {
        return !returned;
    }

    public void returnBook() {
        returned = true;
    }

    @Override
    public String toString() {
        return patron.getName() + " - " + book.getTitle() + " (devolver até " + dueDate + ")";
    }
}
