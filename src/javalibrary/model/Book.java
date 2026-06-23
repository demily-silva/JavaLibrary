package javalibrary.model;

// Representa um livro cadastrado na biblioteca.
public class Book {

    private String title;
    private String author;
    private String isbn;
    private int availableCopies;

    public Book(String title, String author, String isbn, int availableCopies) {
        setTitle(title);
        setAuthor(author);
        setIsbn(isbn);
        setAvailableCopies(availableCopies);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("O título do livro não pode ficar vazio.");
        }

        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("O autor do livro não pode ficar vazio.");
        }

        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("O ISBN do livro não pode ficar vazio.");
        }

        this.isbn = isbn;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        if (availableCopies < 0) {
            throw new IllegalArgumentException("A quantidade de cópias não pode ser negativa.");
        }

        this.availableCopies = availableCopies;
    }

    public boolean hasAvailableCopies() {
        return availableCopies > 0;
    }

    @Override
    public String toString() {
        return title + " - " + author + " (ISBN: " + isbn + ")";
    }
}
