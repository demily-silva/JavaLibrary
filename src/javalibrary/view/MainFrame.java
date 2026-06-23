package javalibrary.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import javalibrary.service.Library;

// Janela principal da aplicação.
public class MainFrame extends JFrame {

    private Library library;
    private BooksPanel booksPanel;
    private PatronsPanel patronsPanel;
    private LoansPanel loansPanel;

    public MainFrame(Library library) {
        this.library = library;

        setTitle("JavaLibrary");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createComponents();
    }

    // Cria as abas principais do sistema.
    private void createComponents() {
        JTabbedPane tabs = new JTabbedPane();

        booksPanel = new BooksPanel(library);
        patronsPanel = new PatronsPanel(library);
        loansPanel = new LoansPanel(library);

        tabs.addTab("Livros", booksPanel);
        tabs.addTab("Usuários", patronsPanel);
        tabs.addTab("Empréstimos", loansPanel);

        tabs.addChangeListener(event -> refreshSelectedTab(tabs));

        add(tabs, BorderLayout.CENTER);
    }

    public Library getLibrary() {
        return library;
    }

    // Atualiza a aba selecionada para mostrar os dados mais recentes.
    private void refreshSelectedTab(JTabbedPane tabs) {
        int selectedIndex = tabs.getSelectedIndex();

        if (selectedIndex == 0) {
            booksPanel.refreshData();
        } else if (selectedIndex == 1) {
            patronsPanel.refreshData();
        } else if (selectedIndex == 2) {
            loansPanel.refreshData();
        }
    }
}
