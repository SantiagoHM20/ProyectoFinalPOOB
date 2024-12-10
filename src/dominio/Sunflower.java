package dominio;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La clase Sunflower representa una planta que produce soles de manera periódica
 * en el tablero de juego. Hereda de la clase Plant e implementa la interfaz SunProducer
 * para manejar la producción de soles. La planta tiene una salud, un costo, y genera soles
 * a intervalos específicos de tiempo.
 */
public class Sunflower extends Plant implements SunProducer {

    public static final int COST = 50; // Costo de la planta Sunflower
    private static final int HEALTH = 200; // Salud inicial de la planta Sunflower
    private static final int SUN_INTERVAL = 20000; // Intervalo de tiempo (en milisegundos) para la producción de soles

    private ImageIcon gifSunflower; // Imagen de la planta Sunflower

    /**
     * Constructor de la clase Sunflower.
     *
     * @param row Fila en la que la planta estará ubicada en el tablero
     * @param col Columna en la que la planta estará ubicada en el tablero
     */
    public Sunflower(int row, int col) {
        super(row, col, HEALTH, COST); // Inicializa la planta con la salud y costo definidos
        try {
            // Cargar el GIF de la planta Sunflower desde el classpath
            String gifPath = "/images/sunflower3.gif"; // Ruta relativa del GIF
            gifSunflower = new ImageIcon(getClass().getResource(gifPath)); // Cargar el GIF como un objeto ImageIcon
        } catch (Exception e) {
            System.err.println("Error cargando el GIF de Sunflower: " + e.getMessage()); // Manejo de errores si el GIF no se carga
        }
    }

    /**
     * Inicia la producción de soles a intervalos regulares (definidos por SUN_INTERVAL).
     * La producción ocurre cada 20 segundos, y se agrega un sol visual al tablero.
     *
     * @param tablero El tablero en el que se va a agregar el sol visual
     * @param cell La celda donde se ubica la planta Sunflower
     */
    public void startProducingSuns(Tablero tablero, JPanel cell) {
        Timer timer = new Timer(SUN_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tablero.addSunVisual(getRow(), getCol(), 25); // Agregar 25 soles al tablero
            }
        });
        timer.setRepeats(true); // El temporizador se repite para seguir produciendo soles
        timer.start(); // Iniciar el temporizador
    }

    /**
     * Método de la interfaz SunProducer que se encarga de la producción de soles.
     * Este método se ejecuta cada vez que la planta produce un sol.
     */
    @Override
    public void produceSun() {
        System.out.println("Sunflower en (" + getRow() + ", " + getCol() + ") produjo un sol.");
        Tablero.getInstance().addSunVisual(getRow(), getCol(), 25); // Agregar 25 soles al tablero
    }

    /**
     * Método para que la planta reciba daño. Reduce la salud de la planta y elimina
     * la planta del tablero si su salud llega a 0.
     *
     * @param damage El daño recibido por la planta
     */
    @Override
    public void takeDamage(int damage) {
        this.health -= damage; // Reducir la salud de la planta
        System.out.println("Plant at (" + getRow() + ", " + getCol() + ") took " + damage + " damage. Remaining health: " + this.health);
        if (this.health <= 0) {
            this.health = 0; // Evitar que la salud sea negativa
            Tablero.getInstance().removePlant(this); // Eliminar la planta del tablero si su salud llega a 0
            System.out.println("Plant at (" + getRow() + ", " + getCol() + ") has been destroyed.");
        }
    }

    /**
     * Método vacío que debe implementarse según la interfaz Plant. En este caso, no tiene funcionalidad.
     */
    @Override
    public void action() {
        // Implementación vacía ya que no se necesita para la Sunflower
    }

    /**
     * Obtiene la imagen que representa a la planta Sunflower.
     *
     * @return La imagen de la planta Sunflower como un objeto ImageIcon
     */
    @Override
    public ImageIcon getImage() {
        return gifSunflower; // Retorna la imagen asociada a la planta Sunflower
    }
}