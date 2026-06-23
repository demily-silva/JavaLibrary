package javalibrary;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import javalibrary.persistence.FileManager;
import javalibrary.service.Library;
import javalibrary.view.MainFrame;

// Classe principal do projeto JavaLibrary.
public class Main {

    public static void main(String[] args) {
        // O Swing deve ser iniciado dentro do invokeLater.
        SwingUtilities.invokeLater(() -> {
            Library library = loadLibrary();
            MainFrame frame = new MainFrame(library);
            frame.setVisible(true);
        });
    }

    // Carrega os dados salvos. Se acontecer erro, o sistema começa vazio.
    private static Library loadLibrary() {
        try {
            return FileManager.loadLibrary();
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null,
                    "Não foi possível carregar os dados salvos. O sistema será iniciado vazio.",
                    "Erro ao carregar dados",
                    JOptionPane.ERROR_MESSAGE);

            return new Library();
        }
    }
}
