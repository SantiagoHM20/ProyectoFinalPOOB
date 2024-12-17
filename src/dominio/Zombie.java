package dominio;

/**
 * La clase abstracta Zombie representa una entidad de tipo zombi en el juego.
 * Esta clase implementa la interfaz Entity y proporciona métodos para manejar las propiedades y acciones comunes de los zombis.
 */
public abstract class Zombie implements Entity {

    protected int row;       // Fila en la que se encuentra el zombi en el tablero
    protected int col;       // Columna en la que se encuentra el zombi en el tablero
    protected int health;    // Salud del zombi
    protected int cost;      // Costo de crear o invocar al zombi
    protected int oldCol;    // Columna anterior del zombi, antes de moverse


    /**
     * Constructor de la clase Zombie.
     *
     * @param row Fila en la que el zombi se colocará inicialmente
     * @param col Columna en la que el zombi se colocará inicialmente
     * @param cost Costo de invocar o crear el zombi
     */
    public Zombie(int row, int col, int cost) {
        this.row = row;
        this.col = col;
        this.health = 100; // La salud por defecto del zombi puede ser 100
        this.cost = cost;
        this.oldCol = col; // Inicializar la columna anterior al valor actual
    }

    /**
     * Obtiene la fila en la que se encuentra el zombi.
     *
     * @return La fila en la que está el zombi
     */
    @Override
    public int getRow() {
        return row;
    }

    /**
     * Obtiene la columna en la que se encuentra el zombi.
     *
     * @return La columna en la que está el zombi
     */
    @Override
    public int getCol() {
        return col;
    }

    /**
     * Obtiene la salud del zombi.
     *
     * @return La salud del zombi
     */
    @Override
    public int getHealth() {
        return health;
    }

    /**
     * Establece la salud del zombi.
     *
     * @param health La nueva salud del zombi
     */
    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Verifica si el zombi está vivo.
     *
     * @return true si el zombi tiene salud mayor que 0, de lo contrario false
     */
    @Override
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Obtiene el costo de crear o invocar al zombi.
     *
     * @return El costo del zombi
     */
    public int getCost() {
        return cost;
    }

    /**
     * Establece la nueva columna del zombi.
     *
     * @param newCol La nueva columna en la que se moverá el zombi
     */
    public void setCol(int newCol) {
        this.oldCol = this.col; // Al cambiar de columna, se guarda la columna anterior
        this.col = newCol;
    }

    /**
     * Obtiene la columna anterior del zombi.
     *
     * @return La columna en la que estaba el zombi antes del cambio
     */
    public int getOldCol() {
        return oldCol;
    }

    /**
     * Método abstracto que define el comportamiento de un zombi.
     * Cada tipo de zombi debe implementar su propio comportamiento en este método.
     */
    public abstract void action();

    /**
     * Verifica si la salud del zombi es menor o igual a 0.
     * Si es así, elimina al zombi del juego.
     */
    public void checkHealth() {
        if (health <= 0) {
            removeZombie(); // Elimina el zombi si su salud llega a 0
        }
    }

    /**
     * Elimina al zombi del tablero y muestra un mensaje en la consola.
     */
    public void removeZombie() {
        System.out.println("Zombie eliminado de la fila " + row + " y columna " + col);
    }

    /**
     * Método abstracto para eliminar al zombi, que puede ser implementado por las subclases.
     */
    public abstract void eliminate();

    /**
     * Método abstracto para cambiar el GIF del zombi, que puede ser implementado por las subclases.
     */
    public abstract void changeGiff();

    /**
     * Método abstracto para detener la acción del zombi, que puede ser implementado por las subclases.
     */
    public abstract void stopAction();

    /**
     * Método abstracto para que el zombi reciba daño.
     *
     * @param i La cantidad de daño que recibe el zombi
     */
    public abstract void takeDamage(int i);
}
