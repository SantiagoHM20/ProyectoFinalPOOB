package dominio;

import javax.swing.*;
import javax.swing.Timer;
/**
 * La clase Peashooter representa una planta que puede disparar guisantes a los zombis
 * en el juego. Implementa la interfaz Attacker para permitir que dispare a los zombis
 * en la misma fila.
 */
public class Peashooter extends Plant implements Attacker {

    // Constantes públicas y privadas
    public static final int COST = 100; // Costo en soles o puntos para colocar la planta
    private static final int HEALTH = 300; // Salud de la planta
    private static final int FIRE_INTERVAL = 1500; // Intervalo entre disparos en milisegundos
    private static final int DAMAGE = 20; // Daño infligido a los zombis con cada disparo

    private Timer actionTimer; // Temporizador que maneja los disparos de la planta
    private ImageIcon gifPeashooter; // Imagen que representa la planta

    /**
     * Constructor de la clase Peashooter que inicializa la planta con su posición
     * en el tablero, su salud y su costo.
     *
     * @param row Fila en la que se coloca la planta en el tablero.
     * @param col Columna en la que se coloca la planta en el tablero.
     */
    public Peashooter(int row, int col) {
        super(row, col, HEALTH, COST);

        // Cargar el GIF desde el classpath que representa el Peashooter
        String gifPath = "/images/lanzaguisantes2.gif";
        gifPeashooter = new ImageIcon(getClass().getResource(gifPath)); // Carga la imagen del GIF

        action(); // Inicia el disparo de guisantes cuando se crea la planta
    }

    /**
     * Método que recibe el daño que la planta toma.
     *
     * @param damage Cantidad de daño que recibe la planta.
     */
    @Override
    public void takeDamage(int damage) {
        this.health -= damage; // Reducir la salud de la planta
        System.out.println("Plant at (" + getRow() + ", " + getCol() + ") took " + damage + " damage. Remaining health: " + this.health);

        // Si la salud llega a cero, se elimina la planta
        if (this.health <= 0) {
            this.health = 0; // Asegura que la salud no sea negativa
            stopAction(); // Detiene el temporizador de disparo antes de eliminar la planta
            Tablero.getInstance().removePlant(this); // Elimina la planta del tablero
            System.out.println("Plant at (" + getRow() + ", " + getCol() + ") has been destroyed.");
        }
    }

    /**
     * Inicia el temporizador que activa el disparo periódico de la planta.
     */
    @Override
    public void action() {
        // Crea un temporizador que dispara guisantes cada FIRE_INTERVAL milisegundos
        actionTimer = new Timer(FIRE_INTERVAL, e -> shoot());
        actionTimer.setRepeats(true); // Hace que el temporizador se repita
        actionTimer.start(); // Inicia el temporizador
    }

    /**
     * Detiene el temporizador de la planta, evitando más disparos.
     */
    private void stopAction() {
        if (actionTimer != null && actionTimer.isRunning()) {
            actionTimer.stop(); // Detiene el temporizador si está en ejecución
            System.out.println("Action timer stopped for Peashooter at (" + getRow() + ", " + getCol() + ").");
        }
    }

    /**
     * Método que maneja el disparo de guisantes a los zombis cercanos en la misma fila.
     * Si un zombi está en la fila de la planta, esta le aplica daño.
     */
    public void shoot() {
        // 1. Causar daño directo al zombie más cercano en la fila
        Zombie targetZombie = Tablero.getInstance().getClosestZombieInRow(getRow(), getCol());
        if (targetZombie != null) {
            targetZombie.takeDamage(DAMAGE);
            System.out.println("Pashooter en (" + getRow() + ", " + getCol() + ") causó daño directo a zombie en ("
                    + targetZombie.getRow() + ", " + targetZombie.getCol() + "). Salud restante: " + targetZombie.getHealth());
        }

        // 2. Disparar un proyectil hacia la derecha
        Projectile projectile = new Projectile(getRow(), getCol() + 1, DAMAGE);
        Tablero.getInstance().addProjectile(projectile);
        System.out.println("Peashooter en (" + getRow() + ", " + getCol() + ") disparó un proyectil hacia la derecha.");
    }

    /**
     * Devuelve la imagen de la planta (GIF).
     *
     * @return gifPeashooter La imagen que representa al Peashooter.
     */
    @Override
    public ImageIcon getImage() {
        return gifPeashooter;
    }
}
