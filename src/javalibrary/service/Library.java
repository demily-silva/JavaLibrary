package javalibrary.service;

import java.util.ArrayList;
import java.util.List;

import javalibrary.model.Book;
import javalibrary.model.Loan;
import javalibrary.model.Patron;

// Guarda os dados principais da biblioteca.
public class Library {

    private ArrayList<Book> books;
    private ArrayList<Patron> patrons;
    private ArrayList<Loan> loans;

    public Library() {
        books = new ArrayList<>();
        patrons = new ArrayList<>();
        loans = new ArrayList<>();
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public List<Patron> getPatrons() {
        return new ArrayList<>(patrons);
    }

    public List<Loan> getLoans() {
        return new ArrayList<>(loans);
    }
}
