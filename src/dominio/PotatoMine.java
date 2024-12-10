package dominio;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.BorderLayout;

/**
 * La clase PotatoMine representa una planta llamada "Papa Mina" en el juego.
 * Esta planta tiene un costo, salud, tiempo de activación y daño de explosión.
 * La papa mina se activa después de un tiempo de espera y puede explotar si un zombie está cerca.
 */
public class PotatoMine extends Plant {
    public static final int COST = 25; // Costo de la planta
    private static final int HEALTH = 100; // Salud inicial de la planta
    private static final int ACTIVATION_TIME = 14000; // Tiempo de activación en milisegundos (14 segundos)
    private static final int EXPLOSION_DAMAGE = 4000; // Daño que causa la explosión

    private ImageIcon sleepingGif; // Imagen que representa a la papa dormida
    private ImageIcon activeGif; // Imagen que representa a la papa activa
    private ImageIcon explosionGif; // Imagen que representa la explosión de la papa mina
    private boolean isActive; // Indica si la papa mina está activa

    private JLabel potatoLabel; // Label que contiene la imagen de la papa mina, utilizado para actualizar la imagen dinámicamente

    /**
     * Constructor de la clase PotatoMine.
     * Inicializa la papa mina con la salud, costo y posición en el tablero.
     * Además, carga las imágenes correspondientes y configura la planta como inactiva.
     *
     * @param row Fila en la que se coloca la papa mina.
     * @param col Columna en la que se coloca la papa mina.
     */
    public PotatoMine(int row, int col) {
        super(row, col, HEALTH, COST); // Llama al constructor de la clase base Plant

        // Cargar imágenes
        sleepingGif = new ImageIcon(getClass().getResource("/images/PapaDormida.PNG"));
        activeGif = new ImageIcon(getClass().getResource("/images/potatom.gif"));
        explosionGif = new ImageIcon(getClass().getResource("/images/EXP2.gif"));

        isActive = false; // Inicialmente, la papa mina está inactiva
        potatoLabel = new JLabel(sleepingGif); // Inicialmente, muestra la papa dormida
        activateAfterDelay(); // Configura el temporizador para activar la planta después de un tiempo
    }

    /**
     * Activa la papa mina después de un retraso determinado por el tiempo de activación.
     * Cambia la imagen a la papa activa y actualiza el tablero.
     */
    private void activateAfterDelay() {
        // Crea un temporizador que se activa después de 'ACTIVATION_TIME' milisegundos
        Timer activationTimer = new Timer(ACTIVATION_TIME, e -> {
            isActive = true; // La papa mina ahora está activa
            potatoLabel.setIcon(activeGif); // Cambia la imagen a la papa activa

            // Notifica al tablero para actualizar la celda correspondiente
            Tablero tablero = Tablero.getInstance();
            tablero.updateCell(getRow(), getCol());

            System.out.println("Potato Mine en (" + getRow() + ", " + getCol() + ") está activa.");
        });
        activationTimer.setRepeats(false); // El temporizador no se repite
        activationTimer.start(); // Inicia el temporizador
    }

    /**
     * Explota la papa mina si un zombie está cerca (en la columna adyacente).
     * Si un zombie es alcanzado, recibe el daño de la explosión.
     *
     * @param cell La celda en la que está ubicada la papa mina.
     * @param zombies La matriz de zombies del tablero.
     */
    public void explodeIfZombieNearby(JPanel cell, Zombie[][] zombies) {
        if (!isActive) return; // Si la papa mina no está activa, no hace nada

        int row = getRow(); // Obtiene la fila de la papa mina
        int col = getCol(); // Obtiene la columna de la papa mina

        // Verifica si hay un zombie a la derecha de la papa mina
        if (col + 1 < zombies[row].length && zombies[row][col + 1] != null) {
            Zombie zombie = zombies[row][col + 1]; // Obtiene el zombie adyacente
            zombie.takeDamage(EXPLOSION_DAMAGE); // El zombie recibe daño por la explosión

            System.out.println("Potato Mine explotó en (" + row + ", " + col + ") y dañó al zombie en (" + row + ", " + (col + 1) + ").");
            explode(cell); // Realiza la explosión
        }
    }

    /**
     * Verifica si la papa mina está activa.
     *
     * @return true si la papa mina está activa, false si no lo está.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Devuelve la imagen de la papa mina (activa o dormida, dependiendo del estado).
     *
     * @return La imagen de la papa mina.
     */
    @Override
    public ImageIcon getImage() {
        return isActive ? activeGif : sleepingGif;
    }

    /**
     * Obtiene el JLabel que contiene la imagen de la papa mina.
     *
     * @return El JLabel con la imagen de la papa mina.
     */
    public JLabel getPotatoLabel() {
        return potatoLabel;
    }

    /**
     * Realiza la explosión de la mina, cambiando su imagen por una de explosión
     * y eliminando la mina del tablero después de un corto tiempo.
     *
     * @param cell La celda donde se encuentra la mina, que será actualizada con la explosión.
     */
    public void explode(JPanel cell) {
        isActive = false; // La mina ya no está activa

        JLabel explosionLabel = new JLabel(explosionGif); // Crea un JLabel con la imagen de explosión
        cell.removeAll(); // Elimina cualquier componente actual en la celda
        cell.setLayout(new BorderLayout());
        cell.add(explosionLabel, BorderLayout.CENTER); // Añade la explosión en el centro de la celda
        cell.revalidate(); // Revalida el panel para que se apliquen los cambios
        cell.repaint(); // Repinta el panel para que se vea la nueva imagen

        // Crea un temporizador para limpiar la celda después de 1 segundo (1000 ms)
        Timer cleanupTimer = new Timer(1000, e -> {
            cell.removeAll(); // Elimina la imagen de la explosión
            cell.revalidate(); // Revalida la celda
            cell.repaint(); // Repinta la celda
        });
        cleanupTimer.setRepeats(false); // El temporizador solo se ejecuta una vez
        cleanupTimer.start(); // Inicia el temporizador de limpieza
    }

    /**
     * Método vacío, heredado de la clase Plant, que no realiza ninguna acción específica
     * en el contexto de PotatoMine.
     */
    @Override
    public void action() {
        // Este método está vacío y no realiza ninguna acción en esta clase.
    }

    /**
     * Método para recibir daño en la planta. Si la salud llega a 0 o menos, la planta
     * es eliminada del tablero.
     *
     * @param damage El daño que la planta recibe.
     */
    @Override
    public void takeDamage(int damage) {
        this.health -= damage; // Resta el daño a la salud de la planta
        System.out.println("La planta en (" + getRow() + ", " + getCol() + ") recibió " + damage + " de daño. Salud restante: " + this.health);
        if (this.health <= 0) {
            this.health = 0; // Evita que la salud sea negativa
            Tablero.getInstance().removePlant(this); // Elimina la planta del tablero
            System.out.println("La planta en (" + getRow() + ", " + getCol() + ") ha sido destruida.");
        }
    }
}