package presentation;
import dominio.PlayerVsMachineException;
import dominio.Tablero;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GameGUI {

    public static void main(String[] args) {
        MainMenuFrame mainMenuFrame = new MainMenuFrame();
        mainMenuFrame.setVisible(true);
    }
}

class MainMenuFrame extends JFrame {

    private JLabel imageLabel;
    private JButton nuevaPartidaButton;
    private JButton reanudarPartidaButton;

    public MainMenuFrame() {
        prepareElements();
        prepareActions();
    }

    private void prepareElements() {
        setTitle("Plants vs Zombies");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(width, height));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setBounds(0, 0, width, height);
        layeredPane.add(imageLabel, JLayeredPane.DEFAULT_LAYER);

        URL imageUrl = getClass().getResource("/images/pixelcut-export.jpeg");
        if (imageUrl != null) {
            loadImage(imageUrl);
        } else {
            System.err.println("No se pudo encontrar la imagen: /images/pixelcut-export.jpeg");
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        nuevaPartidaButton = new JButton("Nueva Partida");
        reanudarPartidaButton = new JButton("Reanudar Partida");

        configureButton(nuevaPartidaButton);
        configureButton(reanudarPartidaButton);

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(nuevaPartidaButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(reanudarPartidaButton);
        buttonPanel.add(Box.createVerticalGlue());

        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);
        buttonPanel.setBounds((width - buttonPanel.getPreferredSize().width) / 2,
                (height - buttonPanel.getPreferredSize().height) / 2,
                buttonPanel.getPreferredSize().width,
                buttonPanel.getPreferredSize().height);

        add(layeredPane, BorderLayout.CENTER);
    }

    private void loadImage(URL imageUrl) {
        try {
            ImageIcon imageIcon = new ImageIcon(imageUrl);
            Image image = imageIcon.getImage();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Image scaledImage = image.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureButton(JButton button) {
        button.setBackground(Color.GRAY);
        button.setForeground(Color.BLACK);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        addHoverEffect(button);
    }

    private void addHoverEffect(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }
        });
    }

    private void prepareActions() {
        nuevaPartidaButton.addActionListener(e -> {
            NewGameFrame newGameFrame = new NewGameFrame();
            newGameFrame.setVisible(true);
            dispose();
        });

        reanudarPartidaButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Reanudar está en construcción");
        });
    }
}

class NewGameFrame extends JFrame {

    private JButton playerVsMachineButton;
    private JButton machineVsMachineButton;
    private JButton playerVsPlayerButton;

    public NewGameFrame() {
        prepareElements();
        prepareActions();
    }

    private void prepareElements() {
        setTitle("Nueva Partida - Plants vs Zombies");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width / 2;
        int height = screenSize.height / 2;
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(width, height));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setBounds(0, 0, width, height);
        layeredPane.add(imageLabel, JLayeredPane.DEFAULT_LAYER);

        URL imageUrl = getClass().getResource("/images/modos de juego.jpg");
        if (imageUrl != null) {
            loadImage(imageLabel, imageUrl);
        } else {
            System.err.println("No se pudo encontrar la imagen: /images/modos de juego.jpg");
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(null);

        playerVsMachineButton = new JButton("Player vs Machine");
        machineVsMachineButton = new JButton("Machine vs Machine");
        playerVsPlayerButton = new JButton("Player vs Player");

        configureButton(playerVsMachineButton);
        configureButton(machineVsMachineButton);
        configureButton(playerVsPlayerButton);

        int buttonWidth = 350;
        int buttonHeight = 70;
        int xOffset = width - buttonWidth - 50; // Mover los botones a la derecha

        playerVsMachineButton.setBounds(xOffset, 70, buttonWidth, buttonHeight);
        machineVsMachineButton.setBounds(xOffset, 160, buttonWidth, buttonHeight);
        playerVsPlayerButton.setBounds(xOffset, 250, buttonWidth, buttonHeight);

        buttonPanel.add(playerVsMachineButton);
        buttonPanel.add(machineVsMachineButton);
        buttonPanel.add(playerVsPlayerButton);

        buttonPanel.setBounds(0, 0, width, height);
        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);

        add(layeredPane, BorderLayout.CENTER);
    }

    private void loadImage(JLabel label, URL imageUrl) {
        try {
            ImageIcon imageIcon = new ImageIcon(imageUrl);
            Image image = imageIcon.getImage();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Image scaledImage = image.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureButton(JButton button) {
        button.setBackground(Color.GRAY);
        button.setForeground(Color.BLACK);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        addHoverEffect(button);
    }

    private void addHoverEffect(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }
        });
    }

    private void prepareActions() {
        playerVsMachineButton.addActionListener(e -> {
            prepareElementsBoard();
        });
    }

    private void prepareElementsBoard() {



        // Limpiar la ventana actual
        getContentPane().removeAll();
        repaint();

        // Configurar el JFrame para maximizar y usar toda la pantalla
        dispose(); // Cierra temporalmente el JFrame para modificar sus propiedades
        setUndecorated(true); // Eliminar bordes de la ventana
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza la ventana
        setVisible(true); // Vuelve a mostrar el JFrame

        // Cargar la imagen de fondo
        URL imageUrl = getClass().getResource("/images/Tablero.png");
        if (imageUrl == null) {
            System.err.println("Error: No se pudo cargar la imagen desde /resources/images/Tablero.png");
            return;
        }
        ImageIcon originalIcon = new ImageIcon(imageUrl);

        // Ajustar el tamaño de la imagen al tamaño del JFrame
        Image scaledImage = originalIcon.getImage().getScaledInstance(
                getWidth(),
                getHeight(),
                Image.SCALE_SMOOTH
        );
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Crear JLabel con la imagen escalada
        JLabel backgroundLabel = new JLabel(scaledIcon);
        backgroundLabel.setLayout(null); // Layout nulo para posicionar manualmente
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());

        // Configurar el cronómetro
        JLabel timerLabel = new JLabel("2:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerLabel.setForeground(Color.RED);
        timerLabel.setBounds(getWidth() - 150, 10, 140, 40);
        backgroundLabel.add(timerLabel);

        // Iniciar el cronómetro de 2 minutos
        Timer timer = new Timer(1000, null);
        final int[] timeRemaining = {120}; // Tiempo en segundos

        timer.addActionListener(e -> {
            timeRemaining[0]--;

            int minutes = timeRemaining[0] / 60;
            int seconds = timeRemaining[0] % 60;
            timerLabel.setText(String.format("%d:%02d", minutes, seconds));

            if (timeRemaining[0] <= 0) {
                timer.stop();
                dispose(); // Cierra la ventana actual
                new WinFrame().setVisible(true); // Abre la interfaz Win
            }
        });

        timer.start();

        // Crear el tablero interactivo
        Tablero tableroInteractivo = Tablero.getInstance();
        int tableroAncho = (int) (getWidth() * 0.77);
        int tableroAlto = (int) (getHeight() * 0.7);
        int tableroX = (int) (getWidth() * 0.24);
        int tableroY = (int) (getHeight() * 0.21);

        tableroInteractivo.setBounds(tableroX, tableroY, tableroAncho, tableroAlto);
        tableroInteractivo.setOpaque(false);

        // Crear la bandeja de plantas
        PlantTrayGUI plantTray = new PlantTrayGUI();
        int bandejaAncho = (int) (getWidth() * 0.75);
        int bandejaAlto = 120;
        int bandejaX = (getWidth() - bandejaAncho) / 2;
        int bandejaY = 10;

        plantTray.setBounds(bandejaX, bandejaY, bandejaAncho, bandejaAlto);
        plantTray.setOpaque(false);

        tableroInteractivo.addSunListener(plantTray);

        backgroundLabel.add(tableroInteractivo);
        backgroundLabel.add(plantTray);

        setContentPane(backgroundLabel);
        revalidate();
        repaint();
    }
}



