// Clase ECIPlant
package dominio;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La clase ECIPlant representa una planta que produce un sol más grande
 * en su posición y tiene características específicas de costo y salud.
 */
public class ECIPlant extends Plant implements SunProducer {

    public static final int COST = 75; // Costo de ECIPlant
    private static final int HEALTH = 150; // Salud inicial de ECIPlant
    private static final int SUN_INTERVAL = 20000; // Intervalo para generar soles
    private static final int SUN_VALUE = 50; // Valor del sol generado

    private ImageIcon gifECIPlant; // Imagen de ECIPlant

    /**
     * Constructor de la clase ECIPlant.
     *
     * @param row Fila de la planta
     * @param col Columna de la planta
     */
    public ECIPlant(int row, int col) {
        super(row, col, HEALTH, COST);
        try {
            String gifPath = "/images/girabig.gif"; // Ruta del GIF
            gifECIPlant = new ImageIcon(getClass().getResource(gifPath));
        } catch (Exception e) {
            System.err.println("Error cargando el GIF de ECIPlant: " + e.getMessage());
        }
    }

    /**
     * Inicia la producción de soles más grandes a intervalos regulares.
     *
     * @param tablero El tablero donde se producen los soles
     * @param cell La celda de la planta
     */
    public void startProducingSuns(Tablero tablero, JPanel cell) {
        Timer timer = new Timer(SUN_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tablero.addSunVisual(getRow(), getCol(), SUN_VALUE); // Genera un sol más grande
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    /**
     * Método de la interfaz SunProducer.
     */
    @Override
    public void produceSun() {
        System.out.println("ECIPlant en (" + getRow() + ", " + getCol() + ") produjo un sol grande.");
        Tablero.getInstance().addSunVisual(getRow(), getCol(), SUN_VALUE);
    }

    /**
     * Maneja el daño recibido por la planta.
     *
     * @param damage El daño recibido
     */
    @Override
    public void takeDamage(int damage) {
        this.health -= damage;
        System.out.println("ECIPlant en (" + getRow() + ", " + getCol() + ") recibió " + damage + " de daño. Salud restante: " + this.health);
        if (this.health <= 0) {
            this.health = 0;
            Tablero.getInstance().removePlant(this);
            System.out.println("ECIPlant en (" + getRow() + ", " + getCol() + ") fue destruida.");
        }
    }

    /**
     * Método para obtener la imagen de ECIPlant.
     *
     * @return El GIF de la planta
     */
    @Override
    public ImageIcon getImage() {
        return gifECIPlant;
    }

    /**
     * Acción específica de ECIPlant (sin funcionalidad adicional).
     */
    @Override
    public void action() {
        // No se requiere acción específica adicional
    }
}
