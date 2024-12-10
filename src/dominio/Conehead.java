package dominio;


import javax.swing.*;

/**
 * La clase Conehead extiende la clase Zombie y representa un zombie con un cono en la cabeza,
 * lo que le proporciona mayor resistencia. Implementa la interfaz Attacker para gestionar
 * ataques a plantas y desplazamientos en el tablero.
 */
public class Conehead extends Zombie implements Attacker {

    /** El costo de generar un Conehead Zombie en el tablero. */
    public static final int COST = 200;

    /** Salud inicial del Conehead Zombie. */
    private static int HEALTH = 380;

    /** Intervalo de movimiento del Conehead Zombie en milisegundos. */
    private static final int MOVE_INTERVAL = 250;

    /** Daño infligido por el Conehead Zombie a las plantas en cada ataque. */
    private static final int DAMAGE = 100;

    /** Timer que controla los movimientos periódicos del zombie. */
    private Timer timer;

    /** Timer que controla los ataques periódicos del zombie. */
    private Timer attackTimer;

    /** Ruta del archivo GIF que se usa para la animación de muerte. */
    private String gifDeath = "/images/ZombieDie.gif";

    /** Animación de muerte del Conehead Zombie. */
    private ImageIcon BasicDeath = new ImageIcon(getClass().getResource(gifDeath));

    /** Animación actual del Conehead Zombie. */
    private ImageIcon gifBasic;

    /**
     * Constructor para inicializar un Conehead Zombie.
     *
     * @param row La fila inicial en el tablero.
     * @param col La columna inicial en el tablero.
     */
    public Conehead(int row, int col) {
        super(row, col, COST);
        this.health = HEALTH;

        // Ruta relativa para cargar el GIF desde el classpath
        String gifPath = "/images/ConeheadZombie.gif";
        gifBasic = new ImageIcon(getClass().getResource(gifPath));

        // Inicia la acción del zombie
        action();
    }

    /**
     * Controla el comportamiento del zombie: si no está atacando, se mueve.
     */
    @Override
    public void shoot() {
        if (!attack()) {
            move();
        }
    }

    /**
     * Gestiona el ataque del zombie a una planta adyacente.
     *
     * @return true si hay una planta para atacar, false en caso contrario.
     */
    private boolean attack() {
        int targetCol = getCol() - 1; // Calcula la columna objetivo (una columna antes)

        // Verifica si hay una planta en la columna anterior
        Plant targetPlant = Tablero.getInstance().getPlantAt(getRow(), targetCol);
        if (targetPlant != null) {
            // Inicia el temporizador de ataque si no está activo
            if (attackTimer == null || !attackTimer.isRunning()) {
                startAttackTimer(targetPlant);
                timer.stop(); // Detiene el temporizador de movimiento
            }
            return true;
        }

        // Detiene el ataque si no hay planta y reactiva el movimiento
        if (attackTimer != null && attackTimer.isRunning()) {
            attackTimer.stop();
            timer.start();
        }
        return false;
    }

    /**
     * Inicia un temporizador para gestionar ataques periódicos a la planta objetivo.
     *
     * @param targetPlant La planta que será atacada.
     */
    private void startAttackTimer(Plant targetPlant) {
        attackTimer = new Timer(500, e -> {
            if (targetPlant.getHealth() > 0) {
                System.out.println("Zombie at (" + getRow() + ", " + getCol() + ") attacks plant at (" + getRow() + ", " + (getCol() - 1) + ").");
                targetPlant.takeDamage(DAMAGE);
            }
            if (targetPlant.getHealth() <= 0) {
                System.out.println("Plant at (" + getRow() + ", " + (getCol() - 1) + ") has been destroyed.");
                attackTimer.stop();
                timer.start(); // Reactivar movimiento
            }
        });
        attackTimer.setRepeats(true);
        attackTimer.start();
    }

    /**
     * Inicia el temporizador de movimiento del zombie.
     */
    @Override
    public void action() {
        timer = new Timer(4000, e -> shoot());
        timer.setRepeats(true);
        timer.start();
    }

    /**
     * Controla el movimiento del zombie hacia la izquierda.
     */
    private void move() {
        int oldCol = getCol();
        int newCol = oldCol - 1;

        if (HEALTH <= 0) {
            System.out.println("Zombie eliminado. Deteniendo movimiento.");
            eliminate();
            return;
        }

        if (newCol > 0) {
            setCol(newCol);
            Tablero.getInstance().notifyZombieMoved(this);

            if (newCol == 1) {
                Tablero.getInstance().instaKillZombies(getRow());
                if (HEALTH <= 0) {
                    eliminate();
                    return;
                }
            }

            System.out.println("Zombie en (" + getRow() + ", " + oldCol + ") avanza a (" + getRow() + ", " + newCol + ").");
        } else if (newCol == 0 && HEALTH > 0) {
            System.out.println("Zombie en (" + getRow() + ", " + oldCol + ") ha llegado al borde. ¡Fin del juego!");
            System.exit(0);
        }
    }

    /**
     * Cambia la animación del zombie al estado de muerte.
     */
    @Override
    public void changeGiff() {
        gifBasic = BasicDeath;
        Tablero.getInstance().updateZombieCell(this);
    }

    /**
     * Elimina completamente al zombie del tablero.
     */
    @Override
    public void eliminate() {
        stopAction();
        Tablero.getInstance().removeZombie(this);
        System.out.println("Zombie eliminado en (" + getRow() + ", " + getCol() + ").");
    }

    /**
     * Detiene el temporizador de movimiento del zombie.
     */
    public void stopAction() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    /**
     * Devuelve la animación actual del zombie.
     *
     * @return La imagen o animación del zombie.
     */
    @Override
    public ImageIcon getImage() {
        return gifBasic;
    }

    /**
     * Ajusta la salud del zombie.
     *
     * @param health La nueva salud del zombie.
     */
    @Override
    public void setHealth(int health) {
        this.HEALTH = health;
        if (this.HEALTH <= 0) {
            this.HEALTH = 0;
        }
    }

    /**
     * Verifica si el zombie está vivo.
     *
     * @return true si el zombie tiene salud mayor a 0, false en caso contrario.
     */
    public boolean isAlive() {
        return HEALTH > 0;
    }

    /**
     * Reduce la salud del zombie según el daño recibido.
     *
     * @param damage El daño a infligir.
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            System.out.println("Zombie en (" + getRow() + ", " + getCol() + ") ha sido eliminado.");
            Tablero.getInstance().removeZombie(this);
            eliminate();
        }
    }

    /**
     * Devuelve la salud actual del zombie.
     *
     * @return La salud del zombie.
     */
    public int getHealth() {
        return this.health;
    }
}
