package javalibrary.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import javalibrary.exception.LibraryException;
import javalibrary.model.Loan;
import javalibrary.persistence.FileManager;
import javalibrary.service.Library;

// Painel responsável pelos empréstimos e devoluções.
public class LoansPanel extends JPanel {

    private Library library;
    private JTextField isbnField;
    private JTextField patronIdField;
    private JTable loansTable;
    private DefaultTableModel tableModel;

    public LoansPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        createComponents();
        refreshData();
    }

    // Cria os campos, botões e tabela da aba de empréstimos.
    private void createComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(createFormPanel(), BorderLayout.CENTER);
        topPanel.add(createButtonsPanel(), BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    // Cria os campos usados para informar o livro e o usuário.
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Dados do empréstimo"));

        isbnField = new JTextField();
        patronIdField = new JTextField();

        configureTextField(isbnField);
        configureTextField(patronIdField);

        panel.add(new JLabel("ISBN do livro:"));
        panel.add(isbnField);
        panel.add(new JLabel("ID do usuário:"));
        panel.add(patronIdField);

        return panel;
    }

    private void configureTextField(JTextField field) {
        field.setColumns(25);
        field.setPreferredSize(new Dimension(250, 28));
    }

    // Cria os botões principais da aba.
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JButton checkoutButton = new JButton("Emprestar");
        JButton returnButton = new JButton("Devolver");
        JButton clearButton = new JButton("Limpar");
        JButton refreshButton = new JButton("Atualizar");

        checkoutButton.addActionListener(event -> checkoutBook());
        returnButton.addActionListener(event -> returnBook());
        clearButton.addActionListener(event -> clearFields());
        refreshButton.addActionListener(event -> refreshData());

        panel.add(checkoutButton);
        panel.add(returnButton);
        panel.add(clearButton);
        panel.add(refreshButton);

        return panel;
    }

    // Cria a tabela que mostra os empréstimos ativos.
    private JScrollPane createTablePanel() {
        String[] columns = {"Usuário", "Livro", "ISBN", "ID usuário", "Data do empréstimo", "Devolver até"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        loansTable = new JTable(tableModel);
        loansTable.setRowHeight(24);
        loansTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loansTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                fillFieldsFromSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(loansTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Empréstimos ativos"));

        return scrollPane;
    }

    // Faz o empréstimo usando o ISBN do livro e o ID do usuário.
    private void checkoutBook() {
        try {
            String isbn = isbnField.getText();
            String patronId = patronIdField.getText();

            if (isbn.isBlank() || patronId.isBlank()) {
                throw new IllegalArgumentException("Informe o ISBN do livro e o ID do usuário.");
            }

            library.checkoutBook(isbn, patronId);
            refreshData();
            clearFields();

            if (saveData()) {
                showMessage("Empréstimo realizado com sucesso.");
            }
        } catch (LibraryException | IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    // Registra a devolução de um livro emprestado.
    private void returnBook() {
        try {
            String isbn = isbnField.getText();
            String patronId = patronIdField.getText();

            if (isbn.isBlank() || patronId.isBlank()) {
                throw new IllegalArgumentException("Informe o ISBN do livro e o ID do usuário.");
            }

            library.returnBook(isbn, patronId);
            refreshData();
            clearFields();

            if (saveData()) {
                showMessage("Devolução realizada com sucesso.");
            }
        } catch (LibraryException | IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    // Atualiza a tabela com os empréstimos que ainda estão ativos.
    public void refreshData() {
        List<Loan> activeLoans = library.getActiveLoans();

        tableModel.setRowCount(0);

        for (Loan loan : activeLoans) {
            tableModel.addRow(new Object[] {
                    loan.getPatron().getName(),
                    loan.getBook().getTitle(),
                    loan.getBook().getIsbn(),
                    loan.getPatron().getId(),
                    loan.getLoanDate(),
                    loan.getDueDate()
            });
        }
    }

    // Preenche os campos quando o usuário seleciona um empréstimo na tabela.
    private void fillFieldsFromSelectedRow() {
        int row = loansTable.getSelectedRow();

        if (row >= 0) {
            isbnField.setText(tableModel.getValueAt(row, 2).toString());
            patronIdField.setText(tableModel.getValueAt(row, 3).toString());
        }
    }

    // Limpa os campos do formulário.
    private void clearFields() {
        isbnField.setText("");
        patronIdField.setText("");
        loansTable.clearSelection();
    }

    // Salva os dados depois de empréstimos ou devoluções.
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
