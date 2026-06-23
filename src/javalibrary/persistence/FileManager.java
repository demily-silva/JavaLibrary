package javalibrary.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javalibrary.model.Book;
import javalibrary.model.Loan;
import javalibrary.model.Patron;

// Classe responsável por salvar e carregar os dados do sistema em arquivos.
public class FileManager {

    private static final String BOOKS_FILE = "data/books.txt";
    private static final String PATRONS_FILE = "data/patrons.txt";
    private static final String LOANS_FILE = "data/loans.txt";
    private static final String SEPARATOR = ";";

    // Salva a lista de livros no arquivo de texto.
    public static void saveBooks(List<Book> books) throws IOException {
        File file = new File(BOOKS_FILE);
        File folder = file.getParentFile();

        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Book book : books) {
                writer.write(book.getTitle() + SEPARATOR
                        + book.getAuthor() + SEPARATOR
                        + book.getIsbn() + SEPARATOR
                        + book.getAvailableCopies());
                writer.newLine();
            }
        }
    }

    // Carrega os livros salvos no arquivo de texto.
    public static List<Book> loadBooks() throws IOException {
        ArrayList<Book> books = new ArrayList<>();
        File file = new File(BOOKS_FILE);

        if (!file.exists()) {
            return books;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    books.add(createBookFromLine(line));
                }
            }
        }

        return books;
    }

    // Salva a lista de usuários no arquivo de texto.
    public static void savePatrons(List<Patron> patrons) throws IOException {
        File file = new File(PATRONS_FILE);
        File folder = file.getParentFile();

        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Patron patron : patrons) {
                writer.write(patron.getId() + SEPARATOR
                        + patron.getName() + SEPARATOR
                        + patron.getContact());
                writer.newLine();
            }
        }
    }

    // Carrega os usuários salvos no arquivo de texto.
    public static List<Patron> loadPatrons() throws IOException {
        ArrayList<Patron> patrons = new ArrayList<>();
        File file = new File(PATRONS_FILE);

        if (!file.exists()) {
            return patrons;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    patrons.add(createPatronFromLine(line));
                }
            }
        }

        return patrons;
    }

    // Salva a lista de empréstimos no arquivo de texto.
    public static void saveLoans(List<Loan> loans) throws IOException {
        File file = new File(LOANS_FILE);
        File folder = file.getParentFile();

        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Loan loan : loans) {
                // No empréstimo, salvo o ISBN e o ID para ligar com o livro e o usuário.
                writer.write(loan.getBook().getIsbn() + SEPARATOR
                        + loan.getPatron().getId() + SEPARATOR
                        + loan.getLoanDate() + SEPARATOR
                        + loan.getDueDate() + SEPARATOR
                        + loan.isReturned());
                writer.newLine();
            }
        }
    }

    // Carrega os empréstimos salvos no arquivo de texto.
    public static List<Loan> loadLoans(List<Book> books, List<Patron> patrons) throws IOException {
        // Para carregar empréstimos, os livros e usuários já precisam ter sido carregados.
        ArrayList<Loan> loans = new ArrayList<>();
        File file = new File(LOANS_FILE);

        if (!file.exists()) {
            return loans;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    loans.add(createLoanFromLine(line, books, patrons));
                }
            }
        }

        return loans;
    }

    // Cria um livro a partir de uma linha do arquivo.
    private static Book createBookFromLine(String line) throws IOException {
        String[] data = line.split(SEPARATOR, -1);

        if (data.length != 4) {
            throw new IOException("Arquivo de livros com formato inválido.");
        }

        try {
            int availableCopies = Integer.parseInt(data[3]);
            return new Book(data[0], data[1], data[2], availableCopies);
        } catch (NumberFormatException exception) {
            throw new IOException("Quantidade de cópias inválida no arquivo de livros.", exception);
        }
    }

    // Cria um usuário a partir de uma linha do arquivo.
    private static Patron createPatronFromLine(String line) throws IOException {
        String[] data = line.split(SEPARATOR, -1);

        if (data.length != 3) {
            throw new IOException("Arquivo de usuários com formato inválido.");
        }

        return new Patron(data[0], data[1], data[2]);
    }

    // Cria um empréstimo a partir de uma linha do arquivo.
    private static Loan createLoanFromLine(String line, List<Book> books, List<Patron> patrons) throws IOException {
        String[] data = line.split(SEPARATOR, -1);

        if (data.length != 5) {
            throw new IOException("Arquivo de empréstimos com formato inválido.");
        }

        Book book = findBookByIsbn(books, data[0]);
        Patron patron = findPatronById(patrons, data[1]);

        if (book == null || patron == null) {
            throw new IOException("Empréstimo com livro ou usuário não encontrado.");
        }

        try {
            LocalDate loanDate = LocalDate.parse(data[2]);
            LocalDate dueDate = LocalDate.parse(data[3]);
            boolean returned = Boolean.parseBoolean(data[4]);

            return new Loan(book, patron, loanDate, dueDate, returned);
        } catch (RuntimeException exception) {
            throw new IOException("Data inválida no arquivo de empréstimos.", exception);
        }
    }

    // Procura um livro em uma lista usando o ISBN.
    private static Book findBookByIsbn(List<Book> books, String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equalsIgnoreCase(isbn)) {
                return book;
            }
        }

        return null;
    }

    // Procura um usuário em uma lista usando o ID.
    private static Patron findPatronById(List<Patron> patrons, String id) {
        for (Patron patron : patrons) {
            if (patron.getId().equalsIgnoreCase(id)) {
                return patron;
            }
        }

        return null;
    }
}
