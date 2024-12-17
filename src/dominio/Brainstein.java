package dominio;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * Representa al zombie Brainstein, una variante especial de zombie.
 * Brainstein permanece estático en el tablero y genera cerebros como recurso cada 20 segundos.
 */
public class Brainstein extends Zombie {

    /** El costo en puntos para generar este zombie. */
    public static final int COST = 50;

    /** La cantidad de salud inicial del zombie Brainstein. */
    private int health = 300;

    /** La cantidad de cerebros que genera cada intervalo. */
    private static final int BRAIN_REWARD = 25;

    /** El intervalo en milisegundos para generar cerebros (20 segundos). */
    private static final int GENERATION_INTERVAL = 20000;

    /** La imagen asociada con este zombie. */
    private ImageIcon gifBrainstein;

    /** Temporizador para manejar la generación de cerebros. */
    private Timer generationTimer;

    /**
     * Constructor de la clase Brainstein.
     * Inicializa el zombie Brainstein en la posición especificada y configura su salud y apariencia gráfica.
     *
     * @param row la fila inicial en la que se colocará el zombie.
     * @param col la columna inicial en la que se colocará el zombie.
     */
    public Brainstein(int row, int col) {
        super(row, col, COST);
        this.health = 300;

        // Ruta relativa para cargar el GIF desde el classpath
        String gifPath = "/images/Brainstein.gif";
        gifBrainstein = new ImageIcon(getClass().getResource(gifPath));

        action(); // Inicia la acción al crearse
    }

    /**
     * Obtiene la imagen asociada con este zombie.
     *
     * @return un objeto {@link ImageIcon} que representa al zombie Brainstein.
     */
    @Override
    public ImageIcon getImage() {
        return gifBrainstein;
    }

    /**
     * Método que define las acciones específicas de este zombie.
     * Brainstein inicia un temporizador que genera cerebros periódicamente.
     */
    @Override
    public void action() {
        generationTimer = new Timer(GENERATION_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateBrains();
            }
        });
        generationTimer.setRepeats(true);
        generationTimer.start();
    }

    /**
     * Genera cerebros y los agrega al contador global de recursos del tablero.
     */
    private void generateBrains() {
        Tablero.getInstance().addBrains(BRAIN_REWARD);
        System.out.println("Brainstein generó " + BRAIN_REWARD + " cerebros. Recursos totales: " + Tablero.getInstance().getbrains());
    }

    /**
     * Elimina el zombie del tablero.
     * Detiene su temporizador y notifica al tablero para que lo elimine.
     */
    @Override
    public void eliminate() {
        stopAction();
        Tablero.getInstance().removeZombie(this);
        System.out.println("Brainstein eliminado en (" + getRow() + ", " + getCol() + ").");
    }

    /**
     * Cambia el GIF asociado con el zombie al estado de muerte.
     */
    @Override
    public void changeGiff() {
        try {
            URL gifURL = getClass().getResource("/images/ZombieDie.gif"); // Cambia la ruta si es necesario
            if (gifURL == null) {
                throw new IllegalArgumentException("Recurso no encontrado: /images/ZombieDie.gif");
            }
            this.gifBrainstein = new ImageIcon(gifURL); // Cambia el ícono a la animación de muerte
        } catch (Exception e) {
            System.err.println("Error al cambiar GIF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Detiene las acciones asociadas al zombie.
     * Detiene el temporizador de generación de cerebros.
     */
    @Override
    public void stopAction() {
        if (generationTimer != null && generationTimer.isRunning()) {
            generationTimer.stop();
        }
    }

    /**
     * Aplica daño al zombie.
     * Reduce su salud y verifica si debe ser eliminado.
     *
     * @param damage la cantidad de daño recibido.
     */
    @Override
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            eliminate();
        }
    }

    /**
     * Devuelve la salud actual del zombie.
     *
     * @return la salud del zombie.
     */
    @Override
    public int getHealth() {
        return this.health;
    }
}
