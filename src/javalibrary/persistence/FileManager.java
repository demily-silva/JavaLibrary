package javalibrary.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javalibrary.model.Book;

// Classe responsável por salvar e carregar os dados do sistema em arquivos.
public class FileManager {

    private static final String BOOKS_FILE = "data/books.txt";
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
}
