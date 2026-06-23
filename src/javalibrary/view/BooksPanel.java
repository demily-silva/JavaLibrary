package javalibrary.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import javalibrary.model.Book;
import javalibrary.persistence.FileManager;
import javalibrary.service.Library;

// Painel responsável pelo cadastro e busca de livros.
public class BooksPanel extends JPanel {

    private Library library;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField isbnField;
    private JTextField copiesField;
    private JTextField searchField;
    private JTable booksTable;
    private DefaultTableModel tableModel;

    public BooksPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout(10, 10));
        createComponents();
        refreshTable(library.getBooks());
    }

    // Cria os campos, botões e tabela da aba de livros.
    private void createComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(createFormPanel(), BorderLayout.CENTER);
        topPanel.add(createButtonsPanel(), BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createSearchPanel(), BorderLayout.SOUTH);
    }

    // Cria os campos usados para cadastrar ou editar um livro.
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        titleField = new JTextField();
        authorField = new JTextField();
        isbnField = new JTextField();
        copiesField = new JTextField();

        panel.add(new JLabel("Título:"));
        panel.add(titleField);
        panel.add(new JLabel("Autor:"));
        panel.add(authorField);
        panel.add(new JLabel("ISBN:"));
        panel.add(isbnField);
        panel.add(new JLabel("Cópias disponíveis:"));
        panel.add(copiesField);

        return panel;
    }

    // Cria os botões principais da aba.
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel();

        JButton addButton = new JButton("Adicionar");
        JButton updateButton = new JButton("Editar");
        JButton removeButton = new JButton("Remover");
        JButton clearButton = new JButton("Limpar");

        addButton.addActionListener(event -> addBook());
        updateButton.addActionListener(event -> updateBook());
        removeButton.addActionListener(event -> removeBook());
        clearButton.addActionListener(event -> clearFields());

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(removeButton);
        panel.add(clearButton);

        return panel;
    }

    // Cria a tabela que mostra os livros cadastrados.
    private JScrollPane createTablePanel() {
        String[] columns = {"Título", "Autor", "ISBN", "Cópias"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        booksTable = new JTable(tableModel);
        booksTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                fillFieldsFromSelectedRow();
            }
        });

        return new JScrollPane(booksTable);
    }

    // Cria a área de busca por título, autor ou ISBN.
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JPanel buttonsPanel = new JPanel();

        searchField = new JTextField();

        JButton searchButton = new JButton("Buscar");
        JButton showAllButton = new JButton("Mostrar todos");

        searchButton.addActionListener(event -> searchBooks());
        showAllButton.addActionListener(event -> {
            searchField.setText("");
            refreshTable(library.getBooks());
        });

        buttonsPanel.add(searchButton);
        buttonsPanel.add(showAllButton);

        panel.add(new JLabel("Buscar livro:"), BorderLayout.WEST);
        panel.add(searchField, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.EAST);

        return panel;
    }

    // Adiciona um livro usando os dados digitados.
    private void addBook() {
        try {
            Book book = createBookFromFields();

            library.addBook(book);
            refreshTable(library.getBooks());
            clearFields();

            if (saveData()) {
                showMessage("Livro adicionado com sucesso.");
            }
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    // Edita o livro identificado pelo ISBN.
    private void updateBook() {
        try {
            String isbn = isbnField.getText();
            int copies = readCopies();

            library.updateBook(isbn, titleField.getText(), authorField.getText(), copies);
            refreshTable(library.getBooks());
            clearFields();

            if (saveData()) {
                showMessage("Livro editado com sucesso.");
            }
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    // Remove o livro selecionado ou digitado no campo ISBN.
    private void removeBook() {
        try {
            String isbn = isbnField.getText();

            if (isbn.isBlank()) {
                throw new IllegalArgumentException("Informe o ISBN do livro que deseja remover.");
            }

            library.removeBook(isbn);
            refreshTable(library.getBooks());
            clearFields();

            if (saveData()) {
                showMessage("Livro removido com sucesso.");
            }
        } catch (Exception exception) {
            showError(exception.getMessage());
        }
    }

    // Faz a busca e atualiza a tabela com o resultado.
    private void searchBooks() {
        List<Book> results = library.searchBooks(searchField.getText());
        refreshTable(results);
    }

    // Atualiza a tabela com a lista recebida.
    private void refreshTable(List<Book> books) {
        tableModel.setRowCount(0);

        for (Book book : books) {
            tableModel.addRow(new Object[] {
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getAvailableCopies()
            });
        }
    }

    // Preenche os campos quando o usuário seleciona uma linha da tabela.
    private void fillFieldsFromSelectedRow() {
        int row = booksTable.getSelectedRow();

        if (row >= 0) {
            titleField.setText(tableModel.getValueAt(row, 0).toString());
            authorField.setText(tableModel.getValueAt(row, 1).toString());
            isbnField.setText(tableModel.getValueAt(row, 2).toString());
            copiesField.setText(tableModel.getValueAt(row, 3).toString());
        }
    }

    // Monta um objeto Book a partir dos campos da tela.
    private Book createBookFromFields() {
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        int copies = readCopies();

        return new Book(title, author, isbn, copies);
    }

    // Converte o campo de cópias para número inteiro.
    private int readCopies() {
        try {
            return Integer.parseInt(copiesField.getText());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("A quantidade de cópias deve ser um número inteiro.");
        }
    }

    // Limpa os campos do formulário.
    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        copiesField.setText("");
        booksTable.clearSelection();
    }

    // Salva os dados depois de alguma alteração.
    private boolean saveData() {
        try {
            FileManager.saveLibrary(library);
            return true;
        } catch (IOException exception) {
            showError("Erro ao salvar os dados em arquivo.");
            return false;
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
