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

import javalibrary.model.Patron;
import javalibrary.persistence.FileManager;
import javalibrary.service.Library;

// Painel responsável pelo cadastro e busca de usuários.
public class PatronsPanel extends JPanel {

    private Library library;
    private JTextField idField;
    private JTextField nameField;
    private JTextField contactField;
    private JTextField searchField;
    private JTable patronsTable;
    private DefaultTableModel tableModel;

    public PatronsPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout(10, 10));
        createComponents();
        refreshTable(library.getPatrons());
    }

    // Cria os campos, botões e tabela da aba de usuários.
    private void createComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(createFormPanel(), BorderLayout.CENTER);
        topPanel.add(createButtonsPanel(), BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createSearchPanel(), BorderLayout.SOUTH);
    }

    // Cria os campos usados para cadastrar ou editar um usuário.
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        idField = new JTextField();
        nameField = new JTextField();
        contactField = new JTextField();

        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Nome:"));
        panel.add(nameField);
        panel.add(new JLabel("Contato:"));
        panel.add(contactField);

        return panel;
    }

    // Cria os botões principais da aba.
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel();

        JButton addButton = new JButton("Adicionar");
        JButton updateButton = new JButton("Editar");
        JButton removeButton = new JButton("Remover");
        JButton clearButton = new JButton("Limpar");

        addButton.addActionListener(event -> addPatron());
        updateButton.addActionListener(event -> updatePatron());
        removeButton.addActionListener(event -> removePatron());
        clearButton.addActionListener(event -> clearFields());

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(removeButton);
        panel.add(clearButton);

        return panel;
    }

    // Cria a tabela que mostra os usuários cadastrados.
    private JScrollPane createTablePanel() {
        String[] columns = {"ID", "Nome", "Contato"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        patronsTable = new JTable(tableModel);
        patronsTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                fillFieldsFromSelectedRow();
            }
        });

        return new JScrollPane(patronsTable);
    }

    // Cria a área de busca por nome ou ID.
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JPanel buttonsPanel = new JPanel();

        searchField = new JTextField();

        JButton searchButton = new JButton("Buscar");
        JButton showAllButton = new JButton("Mostrar todos");

        searchButton.addActionListener(event -> searchPatrons());
        showAllButton.addActionListener(event -> {
            searchField.setText("");
            refreshTable(library.getPatrons());
        });

        buttonsPanel.add(searchButton);
        buttonsPanel.add(showAllButton);

        panel.add(new JLabel("Buscar usuário:"), BorderLayout.WEST);
        panel.add(searchField, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.EAST);

        return panel;
    }

    // Adiciona um usuário usando os dados digitados.
    private void addPatron() {
        try {
            Patron patron = createPatronFromFields();

            library.addPatron(patron);
            refreshTable(library.getPatrons());
            clearFields();

            if (saveData()) {
                showMessage("Usuário adicionado com sucesso.");
            }
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    // Edita o usuário identificado pelo ID.
    private void updatePatron() {
        try {
            String id = idField.getText();

            library.updatePatron(id, nameField.getText(), contactField.getText());
            refreshTable(library.getPatrons());
            clearFields();

            if (saveData()) {
                showMessage("Usuário editado com sucesso.");
            }
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    // Remove o usuário selecionado ou digitado no campo ID.
    private void removePatron() {
        try {
            String id = idField.getText();

            if (id.isBlank()) {
                throw new IllegalArgumentException("Informe o ID do usuário que deseja remover.");
            }

            library.removePatron(id);
            refreshTable(library.getPatrons());
            clearFields();

            if (saveData()) {
                showMessage("Usuário removido com sucesso.");
            }
        } catch (Exception exception) {
            showError(exception.getMessage());
        }
    }

    // Faz a busca e atualiza a tabela com o resultado.
    private void searchPatrons() {
        List<Patron> results = library.searchPatrons(searchField.getText());
        refreshTable(results);
    }

    // Atualiza a tabela com a lista recebida.
    private void refreshTable(List<Patron> patrons) {
        tableModel.setRowCount(0);

        for (Patron patron : patrons) {
            tableModel.addRow(new Object[] {
                    patron.getId(),
                    patron.getName(),
                    patron.getContact()
            });
        }
    }

    // Atualiza a tabela com os dados atuais da biblioteca.
    public void refreshData() {
        refreshTable(library.getPatrons());
    }

    // Preenche os campos quando o usuário seleciona uma linha da tabela.
    private void fillFieldsFromSelectedRow() {
        int row = patronsTable.getSelectedRow();

        if (row >= 0) {
            idField.setText(tableModel.getValueAt(row, 0).toString());
            nameField.setText(tableModel.getValueAt(row, 1).toString());
            contactField.setText(tableModel.getValueAt(row, 2).toString());
        }
    }

    // Monta um objeto Patron a partir dos campos da tela.
    private Patron createPatronFromFields() {
        String id = idField.getText();
        String name = nameField.getText();
        String contact = contactField.getText();

        return new Patron(id, name, contact);
    }

    // Limpa os campos do formulário.
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        contactField.setText("");
        patronsTable.clearSelection();
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
