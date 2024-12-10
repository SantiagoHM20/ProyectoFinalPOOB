package dominio;


import javax.swing.*;
/**
 * Representa al zombie Buckethead, una variante de zombie con alta resistencia y capacidad de ataque.
 * Este zombie puede moverse y atacar a plantas en su camino.
 */
public class Buckethead extends Zombie implements Attacker {

    /** El costo en puntos para generar este zombie. */
    public static final int COST = 150;

    /** La salud inicial del zombie Buckethead. */
    private static int HEALTH = 800;

    /** Intervalo de tiempo entre movimientos en milisegundos. */
    private static final int MOVE_INTERVAL = 250;

    /** Cantidad de daño infligido por el zombie en cada ataque. */
    private static final int DAMAGE = 100;

    /** Temporizador para gestionar el movimiento del zombie. */
    private Timer timer;

    /** Temporizador para gestionar los ataques del zombie. */
    private Timer attackTimer;

    /** Imagen asociada al zombie en estado básico. */
    private ImageIcon gifBasic;

    /** Ruta e imagen asociada al zombie en estado de muerte. */
    String gifDeath = "/images/ZombieDie.gif";
    private ImageIcon BasicDeath = new ImageIcon(getClass().getResource(gifDeath));

    /**
     * Constructor de la clase Buckethead.
     * Inicializa al zombie en la posición especificada con su salud y animación.
     *
     * @param row la fila inicial en la que se colocará el zombie.
     * @param col la columna inicial en la que se colocará el zombie.
     */
    public Buckethead(int row, int col) {
        super(row, col, COST);
        this.health = 800;

        // Carga la imagen básica del zombie
        String gifPath = "/images/BucketheadZombie.gif";
        gifBasic = new ImageIcon(getClass().getResource(gifPath));

        action(); // Inicia el movimiento y la acción del zombie
    }

    /**
     * Gestiona las acciones del zombie, alternando entre atacar y moverse.
     */
    @Override
    public void shoot() {
        if (!attack()) {
            move(); // Si no hay planta para atacar, el zombie avanza
        }
    }

    /**
     * Gestiona el ataque del zombie a una planta en su trayectoria.
     *
     * @return `true` si hay una planta para atacar, `false` en caso contrario.
     */
    private boolean attack() {
        int targetCol = getCol() - 1; // Columna objetivo (una antes del zombie)

        // Obtiene la planta objetivo en la fila y columna correspondiente
        Plant targetPlant = Tablero.getInstance().getPlantAt(getRow(), targetCol);
        if (targetPlant != null) {
            // Si no hay temporizador de ataque activo, lo inicia
            if (attackTimer == null || !attackTimer.isRunning()) {
                startAttackTimer(targetPlant);
                timer.stop(); // Detiene el movimiento mientras ataca
            }
            return true;
        }

        // Si no hay planta, reactiva el movimiento y detiene el temporizador de ataque
        if (attackTimer != null && attackTimer.isRunning()) {
            attackTimer.stop();
            timer.start();
        }
        return false;
    }

    /**
     * Inicia el temporizador para atacar a la planta objetivo.
     *
     * @param targetPlant la planta que será atacada.
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
                timer.start();
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
        timer = new Timer(4000, e -> shoot()); // Cada 4 segundos ejecuta acciones
        timer.setRepeats(true);
        timer.start();
    }

    /**
     * Maneja el movimiento del zombie en el tablero.
     * Si alcanza la última columna o es eliminado, detiene su acción.
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
     * Cambia el GIF del zombie al correspondiente al estado de muerte.
     */
    @Override
    public void changeGiff() {
        gifBasic = BasicDeath;
        Tablero.getInstance().updateZombieCell(this);
    }

    /**
     * Elimina al zombie del tablero y detiene sus acciones.
     */
    @Override
    public void eliminate() {
        stopAction();
        Tablero.getInstance().removeZombie(this);
        System.out.println("Zombie eliminado en (" + getRow() + ", " + getCol() + ").");
    }

    /**
     * Detiene el temporizador de movimiento.
     */
    public void stopAction() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    /**
     * Devuelve la imagen asociada al zombie.
     *
     * @return un objeto {@link ImageIcon} que representa al zombie.
     */
    @Override
    public ImageIcon getImage() {
        return gifBasic;
    }

    /**
     * Establece la salud del zombie.
     *
     * @param health la nueva cantidad de salud.
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
     * @return `true` si el zombie tiene salud positiva, `false` en caso contrario.
     */
    public boolean isAlive() {
        return HEALTH > 0;
    }

    /**
     * Aplica daño al zombie y maneja su eliminación si su salud llega a cero.
     *
     * @param damage la cantidad de daño a aplicar.
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
     * @return la salud del zombie.
     */
    public int getHealth() {
        return this.health;
    }
}
