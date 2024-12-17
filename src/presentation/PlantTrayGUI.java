package presentation;

import dominio.Tablero;
import dominio.SunListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class PlantTrayGUI extends JPanel implements SunListener {
    private JLabel sunCounter;


    public PlantTrayGUI() {
        setLayout(null); // Diseño absoluto para personalización
        setBackground(new Color(101, 67, 33)); // Color café más oscuro

        // Dimensiones dinámicas para ocupar 3/4 del ancho y estar centrada
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int trayWidth = (int) (width * 0.75);
        int trayHeight = 150;
        int xOffset = (width - trayWidth) / 2;

        setBounds(xOffset, 10, trayWidth, trayHeight);

        // Contenedor general
        JPanel mainContainer = new JPanel(); // Declaración y configuración aquí
        mainContainer.setLayout(null);
        mainContainer.setBounds(0, 0, trayWidth, trayHeight);
        mainContainer.setBackground(new Color(101, 67, 33)); // Fondo igual que la bandeja
        add(mainContainer);

        // Panel de la pala
        JPanel shovelPanel = createShovelPanel();
        shovelPanel.setBounds(getWidth() - 120, 10, 100, 100); // Posición correcta de la pala
        mainContainer.add(shovelPanel);

        // Contenedor de las plantas
        JPanel plantContainer = new JPanel();
        plantContainer.setLayout(null);
        plantContainer.setBounds(200, 10, trayWidth - 210, 120); // Área para las plantas
        plantContainer.setBackground(new Color(80, 50, 20)); // Café más oscuro para contraste
        mainContainer.add(plantContainer);

        // Contador de soles
        sunCounter = new JLabel(String.valueOf(Tablero.getInstance().getCurrentSuns()));
        sunCounter.setFont(new Font("Arial", Font.BOLD, 24));
        sunCounter.setForeground(Color.WHITE);
        sunCounter.setHorizontalAlignment(SwingConstants.CENTER);
        sunCounter.setBounds(20, 30, 60, 60); // Tamaño y posición dentro del contenedor
        mainContainer.add(sunCounter);

        // Icono del sol
        JLabel sunIcon = new JLabel(new ImageIcon(getScaledImage(
                String.valueOf(getClass().getResource("/images/Diseño sin título (4).png")),
                50, 50)));
        sunIcon.setBounds(90, 30, 60, 60); // Posición al lado del contador
        mainContainer.add(sunIcon);

        // Plantas disponibles
        String[] plantImages = {
                "resources/images/gira.png",        // Imagen del Sunflower
                "resources/images/peashooter.png", // Imagen del Peashooter
                "resources/images/wall.png",       // Imagen del Wall-nut
                "resources/images/potato.png",     // Imagen del PotatoMine
                "resources/images/big.png",        // Imagen del ECIPlant
                "resources/images/exam.png"    // Imagen del EvolvePlan
        };
        String[] plantTypes = {"Sunflower", "Peashooter", "Wall-nut", "PotatoMine", "ECIPlant", "EvolvePlant"};
        int[] plantCosts = {50, 100, 50, 25, 200, 200}; // Agregar el costo de ExamPlan

        for (int i = 0; i < plantImages.length; i++) {
            JPanel plantPanel = createPlantPanel(plantImages[i], plantCosts[i], plantTypes[i]);
            plantPanel.setBounds(10 + (i * 110), 10, 100, 100);
            plantContainer.add(plantPanel);
        }
    }

    public static Tablero getInstance() {
        return null;
    }

    public void reset() {
        // Limpia cualquier planta seleccionada o estado interno
        removeAll();
        revalidate();
        repaint();
        System.out.println("La bandeja de plantas ha sido reiniciada.");
    }

    private JPanel createPlantPanel(String imagePath, int cost, String plantType) {
        JPanel plantPanel = new JPanel();
        plantPanel.setLayout(new BorderLayout());
        plantPanel.setBackground(new Color(210, 180, 140)); // Color madera clara
        plantPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel plantImage = new JLabel(new ImageIcon(getScaledImage(imagePath, 80, 80)));
        plantImage.setHorizontalAlignment(SwingConstants.CENTER);
        plantPanel.add(plantImage, BorderLayout.CENTER);

        JLabel plantCost = new JLabel(String.valueOf(cost));
        plantCost.setFont(new Font("Arial", Font.BOLD, 16));
        plantCost.setHorizontalAlignment(SwingConstants.CENTER);
        plantPanel.add(plantCost, BorderLayout.SOUTH);

        plantPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Tablero tablero = Tablero.getInstance();
                if (tablero.getCurrentSuns() >= cost) {
                    tablero.setSelectedPlantType(plantType);
                    System.out.println("Planta seleccionada: " + plantType);
                } else {
                    JOptionPane.showMessageDialog(null, "No tienes suficientes soles.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        return plantPanel;
    }

    private Image getScaledImage(String imagePath, int width, int height) {
        try {
            File imageFile = new File(imagePath);
            BufferedImage img = ImageIO.read(imageFile);
            return img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.err.println("Error cargando imagen: " + imagePath + " - " + e.getMessage());
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
    }

    @Override
    public void updateSunCount(int suns) {
        sunCounter.setText(String.valueOf(suns));
    }

    private JPanel createShovelPanel() {
        JPanel shovelPanel = new JPanel();
        shovelPanel.setLayout(new BorderLayout());
        shovelPanel.setBackground(new Color(150, 75, 0)); // Color marrón para la pala
        shovelPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel shovelImage = new JLabel(new ImageIcon(getScaledImage("resources/images/shovel.png", 80, 80)));
        shovelImage.setHorizontalAlignment(SwingConstants.CENTER);
        shovelPanel.add(shovelImage, BorderLayout.CENTER);

        shovelPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Tablero tablero = Tablero.getInstance();
                tablero.setSelectedPlantType("shovel");


            }
        });

        return shovelPanel;
    }
}

