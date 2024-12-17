package presentation;

import dominio.Tablero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;




public class WinFrame extends JFrame {

    private JButton retryButton;
    private JButton mainMenuButton;
    private JButton exitButton;

    public WinFrame() {
        resetGameState(); // Reinicia el estado del juego al inicializar el frame
        prepareElements();
        prepareActions();
    }

    private void prepareElements() {
        setTitle("You Win!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Configurar la imagen de fondo
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setHorizontalAlignment(SwingConstants.CENTER);
        URL imageUrl = getClass().getResource("/images/YouWin.PNG");
        if (imageUrl != null) {
            ImageIcon imageIcon = new ImageIcon(imageUrl);
            backgroundLabel.setIcon(imageIcon);
        } else {
            System.err.println("No se pudo cargar la imagen: /images/YouWin.PNG");
        }

        add(backgroundLabel, BorderLayout.CENTER);

        // Configurar los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        retryButton = new JButton("Jugar de Nuevo");
        mainMenuButton = new JButton("Menú Principal");
        exitButton = new JButton("Salir");

        buttonPanel.add(retryButton);
        buttonPanel.add(mainMenuButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Configuración del frame
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void prepareActions() {
        // Manejar las acciones de los botones

        retryButton.addActionListener((ActionEvent e) -> {
            // Reiniciar el juego
            resetGameState();
            dispose();
            new NewGameFrame().setVisible(true);
        });

        mainMenuButton.addActionListener((ActionEvent e) -> {
            // Volver al menú principal
            resetGameState();
            dispose();
            new MainMenuFrame().setVisible(true);
        });

        exitButton.addActionListener((ActionEvent e) -> {
            // Salir del juego
            System.exit(0);
        });
    }

    /**
     * Método para reiniciar el estado del juego.
     */
    private void resetGameState() {
        // Reinicia el estado lógico del tablero y la interfaz
        GameController2.resetGame();

        // Reinicia el estado del tablero (plantas, zombis, soles, etc.)
        Tablero.getInstance().reset();

        System.out.println("El estado lógico y visual del juego se ha reiniciado.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WinFrame::new);
    }
}

class GameController2 {
    private static Object board = null; // Representación lógica del tablero
    private static Object boardUI = null; // Interfaz gráfica del tablero

    public static void resetGame() {
        // Reiniciar la representación lógica del tablero
        board = null;
        // Reiniciar la interfaz gráfica del tablero
        boardUI = null;

        System.out.println("El estado del juego se ha reiniciado.");
    }
}



