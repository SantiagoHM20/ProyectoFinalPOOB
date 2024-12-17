package dominio;

import presentation.LoseFrame;

import javax.swing.*;

/**
 * La clase ECIZombie representa un tipo específico de zombie en el juego.
 * Extiende la clase Zombie y puede atacar plantas en el tablero.
 */
public class ECIZombie extends Zombie implements Attacker {

    public static final int COST = 250; // Costo del zombie en términos de recursos del juego
    private static final int HEALTH = 200; // Salud inicial del zombie
    private static final int FIRE_INTERVAL = 1500; // Intervalo de ataque en milisegundos (1.5 segundos)
    private static final int MOVE_INTERVAL = 4000; // Intervalo de movimiento en milisegundos (4 segundos)
    private static final int DAMAGE = 20; // Daño infligido a la planta con cada ataque

    private ImageIcon gifECIZombie; // Imagen o GIF que representa al zombie
    private Timer actionTimer;
    private Timer actionTimer2; // Temporizador para el ataque periódico

    String gifDeath = "/images/ZombieDie.gif";  // Ruta del GIF que se muestra cuando el zombie muere
    private ImageIcon BasicDeath = new ImageIcon(getClass().getResource(gifDeath)); // Giff para cuando muere el Zombie

    /**
     * Constructor de ECIZombie.
     * Inicializa el zombie con su posición y características.
     *
     * @param row Fila inicial del zombie.
     * @param col Columna inicial del zombie.
     */
    public ECIZombie(int row, int col) {
        super(row, col, COST); // Llama al constructor de la clase padre Zombie
        this.health = HEALTH; // Asigna la salud inicial

        // Carga la imagen o GIF del zombie desde el classpath
        String gifPath = "/images/FlagZombie.gif";
        gifECIZombie = new ImageIcon(getClass().getResource(gifPath));

        action(); // Inicia la acción al crearse
    }

    /**
     * Inicia la acción del zombie, que incluye ataques periódicos y movimiento.
     */
    @Override
    public void action() {
        // Temporizador para el ataque
        actionTimer = new Timer(FIRE_INTERVAL, e -> shoot());
        actionTimer.setRepeats(true);
        actionTimer.start();

        // Temporizador para el movimiento
        actionTimer2 = new Timer(MOVE_INTERVAL, e -> move());
        actionTimer2.setRepeats(true);
        actionTimer2.start();
    }

    /**
     * Elimina el zombie del tablero.
     */
    @Override
    public void eliminate() {
        stopAction();
        Tablero.getInstance().removeZombie(this); // Notifica al tablero que se elimine al zombie
        System.out.println("ECIZombie eliminado en (" + getRow() + ", " + getCol() + ").");
    }

    /**
     * Cambia la representación gráfica del zombie al GIF de muerte.
     */
    @Override
    public void changeGiff() {
        gifECIZombie = BasicDeath;
        Tablero.getInstance().updateZombieCell(this);
    }

    /**
     * Detiene cualquier acción asociada al zombie.
     */
    @Override
    public void stopAction() {
        if (actionTimer != null && actionTimer.isRunning()) {
            actionTimer.stop();
        }
        if (actionTimer2 != null && actionTimer2.isRunning()) {
            actionTimer2.stop();
        }
        System.out.println("Action timers stopped for ECIZombie at (" + getRow() + ", " + getCol() + ").");
    }

    @Override
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            eliminate();
        }
    }

    /**
     * Realiza un ataque a distancia a la planta más cercana en la misma fila.
     */
    @Override
    public void shoot() {
        // Obtiene la planta más cercana en la misma fila del zombie
        Plant target = Tablero.getInstance().getClosestPlantInRow(getRow(), getCol());

        if (target != null) {
            target.takeDamage(DAMAGE); // Inflige daño a la planta
            System.out.println("ECIZombie ataca a planta en (" + target.getRow() + ", " + target.getCol() + ").");
        }

    }

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
                Tablero.getInstance().instaKillZombies(getRow()); // Llama al método para manejar zombies críticos
            }

            System.out.println("Zombie en (" + getRow() + ", " + oldCol + ") avanza a (" + getRow() + ", " + newCol + ").");
        }

        // Si el zombie alcanza el borde izquierdo y sigue vivo
        if (newCol == 0 && health > 0) {
            Tablero.getInstance().setLose(true); // Cambia el estado del juego a "perdido"
            SwingUtilities.invokeLater(() -> {
                new LoseFrame(); // Abre la interfaz de derrota
            });
        }
    }

    /**
     * Devuelve la imagen o GIF que representa al zombie.
     *
     * @return ImageIcon del zombie.
     */
    @Override
    public ImageIcon getImage() {
        return gifECIZombie;
    }
}
