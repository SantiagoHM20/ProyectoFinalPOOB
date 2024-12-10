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

public class GameGUI extends JFrame {

    private JMenuBar menuBar;
    private JMenu menuArchivo;
    private JFileChooser fileChooser;
    private JLabel imageLabel;
    private JButton nuevaPartidaButton;
    private JButton reanudarPartidaButton;
    private Tablero juego;

    public GameGUI() {
        prepareElements();
        prepareElementsMenu();
        prepareActions();
        prepareActionsMenu();
        prepareButtonActions();
    }

    private void prepareElements() {
        setTitle("Plants vs Zombies");

        // Obtener el tamaño de pantalla completa
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        // Ajustar la ventana a pantalla completa
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear un JLayeredPane para superponer los componentes
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(width, height));

        // Inicializar imageLabel
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setBounds(0, 0, width, height);
        layeredPane.add(imageLabel, JLayeredPane.DEFAULT_LAYER);

        // Cargar la imagen globalmente
        URL imageUrl = getClass().getResource("/images/pixelcut-export.jpeg");
        if (imageUrl != null) {
            loadImage(imageUrl);
        } else {
            System.err.println("No se pudo encontrar la imagen: /images/pixelcut-export.jpeg");
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Hacer el panel transparente
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Usar FlowLayout para alinear los botones

        nuevaPartidaButton = new JButton("Nueva Partida");
        nuevaPartidaButton.setBackground(Color.GREEN);
        nuevaPartidaButton.setForeground(Color.BLACK);
        nuevaPartidaButton.setOpaque(true);
        nuevaPartidaButton.setBorderPainted(false);
        nuevaPartidaButton.setFocusPainted(false);
        nuevaPartidaButton.setPreferredSize(new Dimension(200, 50)); // Ajustar tamaño del botón
        nuevaPartidaButton.setFont(new Font("Arial", Font.BOLD, 16)); // Ajustar tamaño de la letra

        reanudarPartidaButton = new JButton("Reanudar Partida");
        reanudarPartidaButton.setBackground(Color.GRAY);
        reanudarPartidaButton.setForeground(Color.BLACK);
        reanudarPartidaButton.setOpaque(true);
        reanudarPartidaButton.setBorderPainted(false);
        reanudarPartidaButton.setFocusPainted(false);
        reanudarPartidaButton.setPreferredSize(new Dimension(200, 50)); // Ajustar tamaño del botón
        reanudarPartidaButton.setFont(new Font("Arial", Font.BOLD, 16)); // Ajustar tamaño de la letra

        addHoverEffect(nuevaPartidaButton);
        addHoverEffect(reanudarPartidaButton);

        buttonPanel.add(nuevaPartidaButton);
        buttonPanel.add(reanudarPartidaButton);

        // Centrar el panel de botones en el JLayeredPane
        buttonPanel.setBounds((width - buttonPanel.getPreferredSize().width) / 2,
                (height - buttonPanel.getPreferredSize().height) / 2,
                buttonPanel.getPreferredSize().width,
                buttonPanel.getPreferredSize().height);
        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);

        add(layeredPane, BorderLayout.CENTER);
    }


    private void prepareButtonActions() {
        // Añadir acción al botón "Reanudar Partida"
        reanudarPartidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Reanudar está en construcción");
            }
        });

         // Añadir acción al botón "Nueva Partida"
        nuevaPartidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prepareNewGameElements();
            }
        });
    }

    private void loadImage(URL imageUrl) {
        try {
            ImageIcon imageIcon = new ImageIcon(imageUrl);
            Image image = imageIcon.getImage();

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = screenSize.width / 2;
            int height = screenSize.height / 2;
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(scaledImage);

            imageLabel.setIcon(imageIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    private void prepareNewGameElements() {
    // Limpiar la ventana actual
    getContentPane().removeAll();
    repaint();

    // Configurar la nueva interfaz
    setTitle("Nueva Partida - Plants vs Zombies");

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width / 2;
    int height = screenSize.height / 2;
    setSize(width, height);
    setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Crear un JLayeredPane para superponer los componentes
    JLayeredPane layeredPane = new JLayeredPane();
    layeredPane.setPreferredSize(new Dimension(width, height));

    // Inicializar imageLabel para la nueva imagen
    imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    imageLabel.setVerticalAlignment(SwingConstants.CENTER);
    imageLabel.setBounds(0, 0, width, height);
    layeredPane.add(imageLabel, JLayeredPane.DEFAULT_LAYER);

    // Cargar la imagen globalmente
    URL imageUrl = getClass().getResource("/images/modos de juego.jpg");
    if (imageUrl != null) {
        loadImage(imageUrl);
    } else {
        System.err.println("No se pudo encontrar la imagen: /images/modos de juego.jpg");
    }


    // Configurar el panel de botones con null layout
    JPanel buttonPanel = new JPanel();
    buttonPanel.setOpaque(false); // Hacer el panel transparente
    buttonPanel.setLayout(null); // Usar null layout para posicionamiento manual

    // Crear los botones
    JButton playerVsMachineButton = new JButton("Player vs Machine");
    JButton machineVsMachineButton = new JButton("Machine vs Machine");
    JButton playerVsPlayerButton = new JButton("Player vs Player");

    // Configurar los botones
    configureButton(playerVsMachineButton);
    configureButton(machineVsMachineButton);
    configureButton(playerVsPlayerButton);

    // Establecer posiciones y tamaños manualmente
    playerVsMachineButton.setBounds(500, 70, 350, 70); // Ajusta estas coordenadas según sea necesario
    machineVsMachineButton.setBounds(500, 160, 350, 70); // Ajusta estas coordenadas según sea necesario
    playerVsPlayerButton.setBounds(500, 250, 350, 70); // Ajusta estas coordenadas según sea necesario

    // Añadir los botones al panel
    buttonPanel.add(playerVsMachineButton);
    buttonPanel.add(machineVsMachineButton);
    buttonPanel.add(playerVsPlayerButton);

    // Añadir el panel de botones al JLayeredPane
    buttonPanel.setBounds(0, 0, width, height);
    layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);

    add(layeredPane, BorderLayout.CENTER);

    // Llama al método para configurar las acciones de los botones
    prepareActionsNewGame(playerVsMachineButton, machineVsMachineButton, playerVsPlayerButton);

    revalidate();
    repaint();
}

    private void prepareActionsNewGame(JButton playerVsMachineButton, JButton machineVsMachineButton, JButton playerVsPlayerButton) {
        playerVsMachineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prepareElementsBoard(); // Inicializa y muestra el tablero
            }
        });

        machineVsMachineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Modo Machine vs Machine en desarrollo.");
            }
        });


        playerVsPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Modo Player vs Player en desarrollo.");
            }
        });
    }


private void configureButton(JButton button) {
    button.setBackground(Color.GRAY);
    button.setForeground(Color.BLACK);
    button.setOpaque(true);
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setFont(new Font("Arial", Font.BOLD, 16)); // Ajustar tamaño de la letra
    addHoverEffect(button);
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

        // Cargar la imagen de fondo desde la carpeta resources
        URL imageUrl = getClass().getResource("/images/pixelcut-export.png");
        if (imageUrl == null) {
            System.err.println("Error: No se pudo cargar la imagen desde /resources/images/pixelcut-export.png");
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
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight()); // Ocupa toda la ventana

        // Crear e integrar el tablero interactivo usando Singleton
        Tablero tableroInteractivo = Tablero.getInstance(); // Obtener instancia única del Tablero
        int tableroAncho = (int) (getWidth() * 0.77); // 77% del ancho de la ventana
        int tableroAlto = (int) (getHeight() * 0.7); // 70% del alto de la ventana

        int tableroX = (int) (getWidth() * 0.24); // Ajustar posición horizontal
        int tableroY = (int) (getHeight() * 0.21); // Ajustar posición vertical

        tableroInteractivo.setBounds(tableroX, tableroY, tableroAncho, tableroAlto);
        tableroInteractivo.setOpaque(false); // Transparente

        // Crear la bandeja de plantas
        PlantTrayGUI plantTray = new PlantTrayGUI();
        int bandejaAncho = (int) (getWidth() * 0.75); // 75% del ancho
        int bandejaAlto = 120; // Altura fija
        int bandejaX = (getWidth() - bandejaAncho) / 2; // Centrar horizontalmente
        int bandejaY = 10; // Posicionar ligeramente debajo del borde superior

        plantTray.setBounds(bandejaX, bandejaY, bandejaAncho, bandejaAlto);
        plantTray.setOpaque(false);

        // Conectar la bandeja como un listener para actualizar los soles
        tableroInteractivo.addSunListener(plantTray);

        // Añadir el tablero y la bandeja al fondo
        backgroundLabel.add(tableroInteractivo);
        backgroundLabel.add(plantTray);

        // Configurar el panel principal
        setContentPane(backgroundLabel);
        revalidate();
        repaint();
    }


    private void adjustComponents() {
        if (imageLabel != null && imageLabel.getIcon() != null) {
            // Escalar la imagen al nuevo tamaño de la ventana
            ImageIcon originalIcon = (ImageIcon) imageLabel.getIcon();
            Image scaledImage = originalIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            imageLabel.setBounds(0, 0, getWidth(), getHeight());
        }

        // Recalcular el tamaño y posición del tablero
        if (getContentPane() instanceof JLabel) {
            JLabel backgroundLabel = (JLabel) getContentPane();
            Component[] components = backgroundLabel.getComponents();
            for (Component component : components) {
                if (component instanceof JPanel && ((JPanel) component).getLayout() instanceof GridLayout) {
                    // Ajustar tablero
                    JPanel tablero = (JPanel) component;
                    int tableroAncho = (int) (getWidth() * 0.77); // Ajustar según proporción deseada
                    int tableroAlto = (int) (getHeight() * 0.7);
                    int tableroX = (int) (getWidth() * 0.24);
                    int tableroY = (int) (getHeight() * 0.17);

                    tablero.setBounds(tableroX, tableroY, tableroAncho, tableroAlto);
                }
            }
        }
    }




    private void prepareElementsMenu() {
        menuBar = new JMenuBar();
        menuArchivo = new JMenu("Archivo");

        JMenuItem nuevo = new JMenuItem("Nuevo");
        JMenuItem abrir = new JMenuItem("Abrir");
        JMenuItem salvar = new JMenuItem("Salvar");
        JMenuItem salir = new JMenuItem("Salir");
        menuArchivo.add(nuevo);
        menuArchivo.add(abrir);
        menuArchivo.add(salvar);
        menuArchivo.addSeparator();
        menuArchivo.add(salir);

        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);

        fileChooser = new JFileChooser();
    }

    private void prepareActions() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                exit();
            }
        });
    }

    private void prepareActionsMenu() {
        for (int i = 0; i < menuArchivo.getItemCount(); i++) {
            JMenuItem item = menuArchivo.getItem(i);
            if (item != null) {
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        switch (item.getText()) {
                            case "Nuevo":
                                JOptionPane.showMessageDialog(null, "Nuevo seleccionado");
                                break;
                            case "Abrir":
                                abrirArchivo();
                                break;
                            case "Salvar":
                                salvarArchivo();
                                break;
                            case "Salir":
                                exit();
                                break;
                        }
                    }
                });
            }
        }
    }

    private void abrirArchivo(){
        fileChooser.setVisible(true);
        fileChooser.setDialogTitle("Abrir");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo dat", "dat"));
        int confirm = fileChooser.showSaveDialog(this);
        try{
            if(confirm == JFileChooser.APPROVE_OPTION){
                Tablero tablero = Tablero.open(fileChooser.getSelectedFile());
                juego.changeGame(tablero);

            }
        }catch(PlayerVsMachineException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void salvarArchivo() {
        fileChooser.setVisible(true);
        fileChooser.setDialogTitle("Guardar");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo dat", "dat"));
        int confirm = fileChooser.showSaveDialog(this);
        try {
            if (confirm == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().toLowerCase().endsWith(".dat")) {
                    selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + ".dat");
                }
                juego.save(selectedFile);
            }
        } catch (PlayerVsMachineException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void exit() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas salir?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        GameGUI ventana = new GameGUI();
        ventana.setVisible(true);
    }
}