package dominio;


import javax.swing.*;

/**
 * La clase WallNut representa una planta de defensa que se coloca en el tablero y
 * actúa como una barrera resistente para detener a los zombis. Tiene una gran cantidad de salud
 * y puede recibir daño antes de ser destruida. Hereda de la clase Plant.
 */
public class WallNut extends Plant {

    public static final int COST = 50; // Costo de la planta WallNut
    private static final int HEALTH = 4000; // Salud de la planta WallNut (resistencia)
    private ImageIcon gifWallNut; // Imagen de la planta WallNut

    /**
     * Constructor de la clase WallNut.
     *
     * @param row Fila en la que la planta estará ubicada en el tablero
     * @param col Columna en la que la planta estará ubicada en el tablero
     */
    public WallNut(int row, int col) {
        super(row, col, HEALTH, COST); // Inicializa la planta con la salud y costo definidos

        // Cargar el GIF de la planta WallNut desde el classpath
        String gifPath = "/images/wall-nut.gif"; // Ruta relativa del GIF
        gifWallNut = new ImageIcon(getClass().getResource(gifPath)); // Cargar el GIF como un objeto ImageIcon
        System.out.println("WallNut colocada en (" + getRow() + ", " + getCol() + ")."); // Mensaje de colocación
    }

    /**
     * Método vacío que debe implementarse según la interfaz Plant. En este caso, no tiene funcionalidad.
     */
    @Override
    public void action() {
        // Implementación vacía ya que no se necesita para la WallNut
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
     * Obtiene la imagen que representa a la planta WallNut.
     *
     * @return La imagen de la planta WallNut como un objeto ImageIcon
     */
    @Override
    public ImageIcon getImage() {
        return gifWallNut; // Retorna la imagen asociada a la planta WallNut
    }
}
