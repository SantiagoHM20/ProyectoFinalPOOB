package dominio;

import javax.swing.*;

/**
 * Representa al zombie Brainstein, una variante especial de zombie con características personalizables
 * dentro del juego. Esta clase extiende la funcionalidad base de la clase {@link Zombie}.
 */
public class Brainstein extends Zombie {

    /** El costo en puntos para generar este zombie. */
    public static final int COST = 50;

    /** La cantidad de salud inicial del zombie Brainstein. */
    private int HEALTH = 300;

    /** La imagen asociada con este zombie. */
    private ImageIcon gifBasic;

    /**
     * Constructor de la clase Brainstein.
     * Inicializa el zombie Brainstein en la posición especificada y configura su salud y apariencia gráfica.
     *
     * @param row la fila inicial en la que se colocará el zombie.
     * @param col la columna inicial en la que se colocará el zombie.
     */
    public Brainstein(int row, int col) {
        super(row, col, COST);
        this.HEALTH = 300;

        // Ruta relativa para cargar el GIF desde el classpath
        String gifPath = "/images/Brainstein.gif";
        gifBasic = new ImageIcon(getClass().getResource(gifPath)); // Cargar desde el classpath

        action(); // Inicia la acción al crearse
    }

    /**
     * Obtiene la imagen asociada con este zombie.
     *
     * @return un objeto {@link ImageIcon} que representa al zombie Brainstein.
     */
    @Override
    public ImageIcon getImage() {
        return gifBasic;
    }

    /**
     * Método que define las acciones específicas de este zombie.
     * Actualmente está vacío y puede ser personalizado según los requerimientos del juego.
     */
    @Override
    public void action() {
        // Definir las acciones específicas para Brainstein aquí
    }

    /**
     * Elimina el zombie del tablero.
     * Notifica al tablero para que elimine al zombie y registra un mensaje en la consola.
     */
    @Override
    public void eliminate() {
        Tablero.getInstance().removeZombie(this); // Notificar al tablero para que elimine al zombie
        System.out.println("Zombie eliminado en (" + getRow() + ", " + getCol() + ").");
    }

    /**
     * Cambia el GIF asociado con el zombie.
     * Actualmente no tiene implementación específica, pero puede ser usado para cambiar la animación del zombie.
     */
    @Override
    public void changeGiff() {
        // Implementar el cambio de GIF si es necesario
    }

    /**
     * Detiene las acciones asociadas al zombie.
     * Actualmente no tiene implementación específica.
     */
    @Override
    public void stopAction() {
        // Implementar la detención de acciones si es necesario
    }

    /**
     * Aplica daño al zombie.
     * Actualmente no tiene implementación específica, pero puede usarse para reducir la salud del zombie.
     *
     * @param i la cantidad de daño a aplicar.
     */
    @Override
    public void takeDamage(int i) {
        // Implementar la lógica de daño si es necesario
    }
}