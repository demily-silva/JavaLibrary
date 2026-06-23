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

    // --- Operações de livros CRUD---

    // Adiciona um novo livro à biblioteca
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("O livro não pode ser vazio.");
        }

        // O ISBN é usado como identificação do livro, então não pode repetir.
        if (findBookByIsbn(book.getIsbn()) != null) {
            throw new IllegalArgumentException("Já existe um livro com esse ISBN.");
        }

        books.add(book);
    }

    // Buscar um livro pelo ISBN. 
    public Book findBookByIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return null;
        }

        for (Book book : books) {
            if (book.getIsbn().equalsIgnoreCase(isbn)) {
                return book;
            }
        }

        // Retorna null se não encontrar.
        return null;
    }

    // Busca livros pelo título, autor ou ISBN.
    public List<Book> searchBooks(String text) {
        ArrayList<Book> results = new ArrayList<>();

        if (text == null || text.isBlank()) {
            // Se a busca estiver vazia, mostra todos os livros cadastrados.
            return getBooks();
        }

        // Deixa tudo em minúsculo para a busca não depender de maiúsculas/minúsculas.
        String searchText = text.toLowerCase();

        for (Book book : books) {
            boolean titleMatches = book.getTitle().toLowerCase().contains(searchText);
            boolean authorMatches = book.getAuthor().toLowerCase().contains(searchText);
            boolean isbnMatches = book.getIsbn().toLowerCase().contains(searchText);

            if (titleMatches || authorMatches || isbnMatches) {
                results.add(book);
            }
        }

        return results;
    }

    // Atualiza os dados de um livro existente.
    public void updateBook(String isbn, String newTitle, String newAuthor, int newAvailableCopies) {
        // Primeiro localiza o livro e depois usa os setters da classe Book.
        Book book = findBookByIsbn(isbn);

        if (book == null) {
            throw new IllegalArgumentException("Livro não encontrado.");
        }

        book.setTitle(newTitle);
        book.setAuthor(newAuthor);
        book.setAvailableCopies(newAvailableCopies);
    }

    // Remove um livro da biblioteca.
    public void removeBook(String isbn) {
        // O livro só pode ser removido se existir e não estiver emprestado.
        Book book = findBookByIsbn(isbn);

        if (book == null) {
            throw new IllegalArgumentException("Livro não encontrado.");
        }

        // Um livro com empréstimo ativo não deve ser removido do sistema.
        for (Loan loan : loans) {
            if (loan.isActive() && loan.getBook().getIsbn().equalsIgnoreCase(isbn)) {
                throw new IllegalArgumentException("Não é possível remover um livro emprestado.");
            }
        }

        books.remove(book);
    }


    public List<Book> getBooks() {
        // Retorna uma cópia para proteger a lista original da biblioteca.
        return new ArrayList<>(books);
    }

    // --- Operações de usuários CRUD ---

    // Adiciona um novo usuário à biblioteca.
    public void addPatron(Patron patron) {
        if (patron == null) {
            throw new IllegalArgumentException("O usuário não pode ser vazio.");
        }

        // O ID é usado como identificação do usuário, então não pode repetir.
        if (findPatronById(patron.getId()) != null) {
            throw new IllegalArgumentException("Já existe um usuário com esse ID.");
        }

        patrons.add(patron);
    }

    // Busca um usuário pelo ID.
    public Patron findPatronById(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }

        for (Patron patron : patrons) {
            if (patron.getId().equalsIgnoreCase(id)) {
                return patron;
            }
        }

        // Retorna null se não encontrar.
        return null;
    }

    // Busca usuários pelo nome ou ID.
    public List<Patron> searchPatrons(String text) {
        ArrayList<Patron> results = new ArrayList<>();

        if (text == null || text.isBlank()) {
            // Se a busca estiver vazia, mostra todos os usuários cadastrados.
            return getPatrons();
        }

        String searchText = text.toLowerCase();

        for (Patron patron : patrons) {
            boolean nameMatches = patron.getName().toLowerCase().contains(searchText);
            boolean idMatches = patron.getId().toLowerCase().contains(searchText);

            if (nameMatches || idMatches) {
                results.add(patron);
            }
        }

        return results;
    }

    // Atualiza os dados de um usuário existente.
    public void updatePatron(String id, String newName, String newContact) {
        Patron patron = findPatronById(id);

        if (patron == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        patron.setName(newName);
        patron.setContact(newContact);
    }

    // Remove um usuário da biblioteca.
    public void removePatron(String id) {
        Patron patron = findPatronById(id);

        if (patron == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        // Um usuário com empréstimo ativo não deve ser removido do sistema.
        for (Loan loan : loans) {
            if (loan.isActive() && loan.getPatron().getId().equalsIgnoreCase(id)) {
                throw new IllegalArgumentException("Não é possível remover um usuário com empréstimo ativo.");
            }
        }

        patrons.remove(patron);
    }

    public List<Patron> getPatrons() {
        // Retorna uma cópia para proteger a lista original da biblioteca.
        return new ArrayList<>(patrons);
    }

    public List<Loan> getLoans() {
        return new ArrayList<>(loans);
    }
}
