package javalibrary.service;

import java.util.ArrayList;
import java.util.List;

import javalibrary.exception.ActiveLoanException;
import javalibrary.exception.BookUnavailableException;
import javalibrary.exception.LibraryException;
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

    // Este construtor será usado quando os dados forem carregados dos arquivos.
    public Library(List<Book> books, List<Patron> patrons, List<Loan> loans) {
        this();

        if (books != null) {
            this.books.addAll(books);
        }

        if (patrons != null) {
            this.patrons.addAll(patrons);
        }

        if (loans != null) {
            this.loans.addAll(loans);
        }
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
    public void removeBook(String isbn) throws ActiveLoanException {
        // O livro só pode ser removido se existir e não estiver emprestado.
        Book book = findBookByIsbn(isbn);

        if (book == null) {
            throw new IllegalArgumentException("Livro não encontrado.");
        }

        // Um livro com empréstimo ativo não deve ser removido do sistema.
        for (Loan loan : loans) {
            if (loan.isActive() && loan.getBook().getIsbn().equalsIgnoreCase(isbn)) {
                throw new ActiveLoanException("Não é possível remover um livro emprestado.");
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
    public void removePatron(String id) throws ActiveLoanException {
        Patron patron = findPatronById(id);

        if (patron == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        // Um usuário com empréstimo ativo não deve ser removido do sistema.
        for (Loan loan : loans) {
            if (loan.isActive() && loan.getPatron().getId().equalsIgnoreCase(id)) {
                throw new ActiveLoanException("Não é possível remover um usuário com empréstimo ativo.");
            }
        }

        patrons.remove(patron);
    }

    public List<Patron> getPatrons() {
        // Retorna uma cópia para proteger a lista original da biblioteca.
        return new ArrayList<>(patrons);
    }

    // --- Operações de empréstimos CRUD ---

    // Faz o empréstimo de um livro para um usuário.
    public Loan checkoutBook(String isbn, String patronId) throws LibraryException {
        Book book = findBookByIsbn(isbn);
        Patron patron = findPatronById(patronId);

        if (book == null) {
            throw new LibraryException("Livro não encontrado.");
        }

        if (patron == null) {
            throw new LibraryException("Usuário não encontrado.");
        }

        // Se não há cópias disponíveis, o empréstimo não pode ser feito.
        if (!book.hasAvailableCopies()) {
            throw new BookUnavailableException("Livro sem cópias disponíveis.");
        }

        // Evita cadastrar o mesmo empréstimo ativo duas vezes para o mesmo usuário.
        if (findActiveLoan(isbn, patronId) != null) {
            throw new ActiveLoanException("Esse usuário já está com esse livro emprestado.");
        }

        Loan loan = new Loan(book, patron);
        loans.add(loan);

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        return loan;
    }

    // Registra a devolução de um livro.
    public void returnBook(String isbn, String patronId) throws LibraryException {
        Loan loan = findActiveLoan(isbn, patronId);

        if (loan == null) {
            throw new LibraryException("Empréstimo ativo não encontrado.");
        }

        loan.returnBook();

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
    }

    // Retorna apenas os empréstimos que ainda não foram devolvidos.
    public List<Loan> getActiveLoans() {
        ArrayList<Loan> activeLoans = new ArrayList<>();

        for (Loan loan : loans) {
            if (loan.isActive()) {
                activeLoans.add(loan);
            }
        }

        return activeLoans;
    }

    // Procura um empréstimo ativo usando o ISBN do livro e o ID do usuário.
    private Loan findActiveLoan(String isbn, String patronId) {
        if (isbn == null || patronId == null) {
            return null;
        }

        for (Loan loan : loans) {
            boolean sameBook = loan.getBook().getIsbn().equalsIgnoreCase(isbn);
            boolean samePatron = loan.getPatron().getId().equalsIgnoreCase(patronId);

            if (loan.isActive() && sameBook && samePatron) {
                return loan;
            }
        }

        return null;
    }

    public List<Loan> getLoans() {
        // Retorna todos os empréstimos, incluindo os que já foram devolvidos.
        return new ArrayList<>(loans);
    }
}
