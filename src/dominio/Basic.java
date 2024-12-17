package dominio;

import presentation.LoseFrame;

import javax.swing.*;
/**
 * La clase `Basic` representa un zombie básico en el juego.
 * Implementa la lógica de movimiento, ataque y eliminación de este tipo de zombie.
 */
public class Basic extends Zombie implements Attacker {

    /** Costo del zombie en términos del recurso del juego. */
    public static final int COST = 100;

    /** Salud inicial del zombie. */
    private int HEALTH = 100;

    /** Intervalo de movimiento en milisegundos. */
    private static final int FIRE_INTERVAL = 250;

    /** Daño que inflige el zombie al atacar. */
    private static final int DAMAGE = 100;

    /** Temporizador para manejar el movimiento del zombie. */
    private Timer timer;

    /** Temporizador para manejar los ataques del zombie. */
    private Timer attackTimer;

    /** Representación gráfica del zombie mientras está vivo. */
    private ImageIcon gifBasic;

    /** Ruta del GIF que se muestra cuando el zombie muere. */
    String gifDeath = "/images/ZombieDie.gif";

    /** Representación gráfica del zombie al morir. */
    private ImageIcon BasicDeath = new ImageIcon(getClass().getResource(gifDeath));

    /**
     * Constructor para inicializar un zombie básico en una posición específica.
     * @param row Fila inicial del zombie.
     * @param col Columna inicial del zombie.
     */
    public Basic(int row, int col) {
        super(row, col, COST);
        this.health = 100;
        String gifPath = "/images/Zombie.gif";
        gifBasic = new ImageIcon(getClass().getResource(gifPath)); // Cargar desde el classpath
        action(); // Inicia la acción al crearse
    }

    /**
     * Lógica de disparo del zombie. Si no puede atacar, se mueve.
     */
    @Override
    public void shoot() {
        if (!attack()) {
            move(); // Si no está atacando, entonces se mueve
        }
    }

    /**
     * Intenta atacar una planta en la columna inmediatamente anterior.
     * @return `true` si hay una planta para atacar; `false` en caso contrario.
     */
    private boolean attack() {
        int targetCol = getCol() - 1; // Calcula la columna objetivo
        Plant targetPlant = Tablero.getInstance().getPlantAt(getRow(), targetCol);
        if (targetPlant != null) {
            if (attackTimer == null || !attackTimer.isRunning()) {
                startAttackTimer(targetPlant); // Inicia el temporizador de ataque
                timer.stop(); // Detiene el temporizador de movimiento
            }
            return true;
        }

        // Reactiva movimiento si no hay planta para atacar
        if (attackTimer != null && attackTimer.isRunning()) {
            attackTimer.stop();
            timer.start();
        }
        return false;
    }

    /**
     * Inicia un temporizador para atacar continuamente a una planta.
     * @param targetPlant La planta que será atacada.
     */
    private void startAttackTimer(Plant targetPlant) {
        attackTimer = new Timer(500, e -> {
            if (targetPlant.getHealth() > 0) {
                targetPlant.takeDamage(DAMAGE); // Delegar el daño a la planta
            }
            if (targetPlant.getHealth() <= 0) {
                attackTimer.stop();
                timer.start(); // Reactivar movimiento
            }
        });
        attackTimer.setRepeats(true);
        attackTimer.start();
    }

    /**
     * Inicia el temporizador de acción del zombie, que lo mueve cada 4 segundos.
     */
    @Override
    public void action() {
        timer = new Timer(4000, e -> shoot());
        timer.setRepeats(true);
        timer.start();
    }

    /**
     * Mueve al zombie hacia la izquierda en el tablero 
     */
    /**
     * Mueve al zombie hacia la izquierda en el tablero.
     */
    private void move() {
        int oldCol = getCol();
        int newCol = oldCol - 1;

        if (newCol >= 0) {
            setCol(newCol); // Actualiza la posición lógica del zombie
            Tablero.getInstance().notifyZombieMoved(this); // Notifica al tablero

            // Verificar si el zombie está en la posición crítica (fila, columna 1)
            if (newCol == 1) {
                Tablero.getInstance().instaKillZombies(getRow());
            }

            System.out.println("Zombie en (" + getRow() + ", " + oldCol + ") avanza a (" + getRow() + ", " + newCol + ").");
        }

        // Si el zombie alcanza la columna 0
        if (newCol == 0 && health > 0) {
            Tablero.getInstance().setLose(true); // Cambia el estado del juego a "perdido"
            SwingUtilities.invokeLater(() -> {
                new LoseFrame(); // Abre la interfaz de derrota
            });
        }
    }



    /**
     * Cambia la representación gráfica del zombie al GIF de muerte.
     */
    @Override
    public void changeGiff() {
        gifBasic = BasicDeath;
        Tablero.getInstance().updateZombieCell(this);
    }

    /**
     * Elimina al zombie, deteniendo su movimiento y notificando al tablero.
     */
    @Override
    public void eliminate() {
        stopAction(); // Detener todos los temporizadores
        Tablero.getInstance().removeZombie(this); // Notificar al tablero para eliminación
        System.out.println("Zombie eliminado en (" + row + ", " + col + ").");
    }

    /**
     * Detiene el temporizador de movimiento del zombie.
     */
    public void stopAction() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        if (attackTimer != null && attackTimer.isRunning()) {
            attackTimer.stop();
        }
        System.out.println("Timers detenidos para zombie en (" + row + ", " + col + ").");
    }

    /**
     * Devuelve la imagen actual del zombie.
     * @return La imagen asociada al estado actual del zombie.
     */
    @Override
    public ImageIcon getImage() {
        return gifBasic;
    }

    /**
     * Establece la salud del zombie.
     * @param health Nueva salud del zombie.
     */
    @Override
    public void setHealth(int health) {
        this.HEALTH = health > 0 ? health : 0;
    }

    /**
     * Verifica si el zombie está vivo.
     * @return `true` si la salud es mayor a 0; `false` en caso contrario.
     */
    public boolean isAlive() {
        return HEALTH > 0;
    }

    /**
     * Aplica daño al zombie.
     * @param damage Cantidad de daño recibido.
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            Tablero.getInstance().removeZombie(this);
            eliminate();
        }
    }

    /**
     * Devuelve la salud actual del zombie.
     * @return Salud del zombie.
     */
    public int getHealth() {
        return this.health;
    }
}


