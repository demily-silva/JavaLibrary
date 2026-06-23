package javalibrary.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import javalibrary.service.Library;

// Janela principal da aplicação.
public class MainFrame extends JFrame {

    private Library library;

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

        tabs.addTab("Livros", new BooksPanel(library));
        tabs.addTab("Usuários", new PatronsPanel(library));
        tabs.addTab("Empréstimos", createPlaceholderPanel("Controle de empréstimos"));

        add(tabs, BorderLayout.CENTER);
    }

    // Painel temporário
    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text, SwingConstants.CENTER);

        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    public Library getLibrary() {
        return library;
    }
}
