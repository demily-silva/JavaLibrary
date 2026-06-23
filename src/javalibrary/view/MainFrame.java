package javalibrary.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

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
        setSize(900, 600);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createComponents();
    }

    // Cria as abas principais do sistema.
    private void createComponents() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 14));
        tabs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        booksPanel = new BooksPanel(library);
        patronsPanel = new PatronsPanel(library);
        loansPanel = new LoansPanel(library);

        tabs.addTab("Livros", booksPanel);
        tabs.addTab("Usuários", patronsPanel);
        tabs.addTab("Empréstimos", loansPanel);

        
        tabs.setTabComponentAt(0, createTabLabel("Livros"));
        tabs.setTabComponentAt(1, createTabLabel("Usuários"));
        tabs.setTabComponentAt(2, createTabLabel("Empréstimos"));

        tabs.addChangeListener(event -> refreshSelectedTab(tabs));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    // Cria um rótulo com margem para deixar as abas mais confortáveis.
    private JLabel createTabLabel(String text) {
        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        label.setFont(new Font("Arial", Font.BOLD, 14));

        return label;
    }

    // Cria um cabeçalho simples com o nome do sistema.
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JLabel titleLabel = new JLabel("JavaLibrary - Sistema de Biblioteca", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel subtitleLabel = new JLabel("Gerenciamento de livros, usuários e empréstimos", SwingConstants.CENTER);

        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(subtitleLabel, BorderLayout.SOUTH);

        return panel;
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
