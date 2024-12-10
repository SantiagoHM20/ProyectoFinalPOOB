package dominio;

import javax.swing.*;

/**
 * La clase Projectile representa un proyectil que puede moverse por el tablero,
 * infligir daño a los zombies y tener una imagen asociada.
 */
public class Projectile {

    private int row; // Fila actual del proyectil en el tablero
    private int col; // Columna actual del proyectil en el tablero
    private int damage; // Daño que inflige el proyectil
    private ImageIcon gifProyectile; // Imagen del proyectil, representada como un objeto ImageIcon

    /**
     * Constructor de la clase Projectile.
     *
     * @param row Fila inicial del proyectil
     * @param col Columna inicial del proyectil
     * @param damage Daño que inflige el proyectil
     */
    public Projectile(int row, int col, int damage) {
        this.row = row;
        this.col = col;
        this.damage = damage;
        String gifPath = ""; // Ruta del GIF (debe ser proporcionada para que funcione)
        gifProyectile = new ImageIcon(getClass().getResource(gifPath)); // Carga el GIF del proyectil
    }

    /**
     * Obtiene la fila en la que se encuentra el proyectil.
     *
     * @return Fila actual del proyectil
     */
    public int getRow() {
        return row;
    }

    /**
     * Obtiene la columna en la que se encuentra el proyectil.
     *
     * @return Columna actual del proyectil
     */
    public int getCol() {
        return col;
    }

    /**
     * Obtiene el daño que inflige el proyectil.
     *
     * @return Daño del proyectil
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Mueve el proyectil una posición hacia la derecha en el tablero (incrementa la columna).
     */
    public void move() {
        this.col += 1; // Los proyectiles avanzan hacia la derecha
    }

    /**
     * Representación en cadena del proyectil.
     *
     * @return Cadena que representa la información del proyectil (fila, columna y daño)
     */
    @Override
    public String toString() {
        return "Projectile en (" + row + ", " + col + ") con daño: " + damage;
    }

    /**
     * Obtiene la imagen del proyectil en formato ImageIcon.
     *
     * @return Imagen del proyectil
     */
    public ImageIcon getImage() {
        return gifProyectile;
    }

    /**
     * Simula el impacto del proyectil sobre un zombie.
     *
     * @param zombie El zombie que recibe el impacto. Si el zombie está vivo, su salud
     *               se ve afectada por el daño del proyectil.
     */
    public void impact(Zombie zombie) {
        if (zombie != null && zombie.isAlive()) { // Si el zombie existe y está vivo
            zombie.setHealth(zombie.getHealth() - damage); // Reduce la salud del zombie por el daño
            System.out.println("Proyectil impactó a zombie en (" + zombie.getRow() + ", " + zombie.getCol() + "). Salud restante: " + zombie.getHealth());

            if (!zombie.isAlive()) { // Si el zombie muere después del impacto
                Tablero.getInstance().removeZombie(zombie); // Elimina el zombie del tablero
            }
        }
    }
}