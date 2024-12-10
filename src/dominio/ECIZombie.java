package dominio;

import javax.swing.*;

/**
 * La clase ECIZombie representa un tipo específico de zombie en el juego.
 * Extiende la clase Zombie y puede atacar plantas en el tablero.
 */
public class ECIZombie extends Zombie implements Attacker {

    public static final int COST = 250; // Costo del zombie en términos de recursos del juego
    private static final int HEALTH = 200; // Salud inicial del zombie
    private static final int MOVE_INTERVAL = 250; // Intervalo de movimiento en milisegundos

    private ImageIcon gifBasic; // Imagen o GIF que representa al zombie

    /**
     * Constructor de ECIZombie.
     * Inicializa el zombie con su posición y características.
     *
     * @param row Fila inicial del zombie.
     * @param col Columna inicial del zombie.
     */
    public ECIZombie(int row, int col) {
        super(row, col, COST); // Llama al constructor de la clase padre Zombie
        this.health = 200; // Asigna la salud inicial

        // Carga la imagen o GIF del zombie desde el classpath
        String gifPath = "/images/FlagZombie.gif";
        gifBasic = new ImageIcon(getClass().getResource(gifPath));

        action(); // Inicia la acción al crearse
    }

    /**
     * Inicia la acción del zombie, que incluye movimiento periódico.
     */
    @Override
    public void action() {
        Timer timer = new Timer(MOVE_INTERVAL, e -> shoot()); // Ejecuta la acción periódicamente
        timer.setRepeats(true); // Configura el temporizador para repetir
        timer.start(); // Inicia el temporizador
    }

    /**
     * Cambia la representación gráfica del zombie.
     * (Actualmente no implementado)
     */
    @Override
    public void changeGiff() {
        // Método vacío; implementar si se requiere cambiar la animación del zombie
    }

    /**
     * Detiene cualquier acción asociada al zombie.
     * (Actualmente no implementado)
     */
    @Override
    public void stopAction() {
        // Método vacío; implementar si se requiere detener acciones
    }

    /**
     * Aplica daño al zombie.
     * (Actualmente no implementado)
     *
     * @param i Cantidad de daño a aplicar.
     */
    @Override
    public void takeDamage(int i) {
        // Método vacío; implementar si se requiere aplicar daño
    }

    /**
     * Realiza una acción de disparo. Si no está atacando, se mueve.
     */
    @Override
    public void shoot() {
        if (!attack()) { // Si no está atacando
            move(); // Procede a moverse
        }
    }

    /**
     * Elimina el zombie del tablero.
     */
    @Override
    public void eliminate() {
        Tablero.getInstance().removeZombie(this); // Notifica al tablero que se elimine al zombie
        System.out.println("Zombie eliminado en (" + getRow() + ", " + getCol() + ").");
    }

    /**
     * Realiza un ataque contra una planta adyacente si existe.
     *
     * @return true si el zombie atacó; false de lo contrario.
     */
    private boolean attack() {
        int targetCol = getCol() - 1; // Calcula la columna objetivo (una columna antes)

        // Verifica si hay una planta en la columna anterior
        Plant targetPlant = Tablero.getInstance().getPlantAt(getRow(), targetCol);
        if (targetPlant != null) {
            System.out.println("Zombie en (" + getRow() + ", " + getCol() + ") muerde a la planta en (" + getRow() + ", " + targetCol + ").");
            targetPlant.setHealth(targetPlant.getHealth() - 50); // Reduce la salud de la planta
            return true; // Indica que el zombie ha atacado
        }
        return false; // Indica que no hay planta para atacar
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
        if (newCol == 0 && HEALTH > 0) {
            System.out.println("Zombie en (" + getRow() + ", " + oldCol + ") ha llegado al borde. ¡Fin del juego!");
            System.exit(0); // Finaliza el programa
        }
    }

    /**
     * Devuelve la imagen o GIF que representa al zombie.
     *
     * @return ImageIcon del zombie.
     */
    @Override
    public ImageIcon getImage() {
        return gifBasic;
    }
}