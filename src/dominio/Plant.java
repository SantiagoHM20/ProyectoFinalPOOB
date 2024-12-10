package dominio;

import javax.swing.*;
/**
 * La clase abstracta Plant representa las plantas en el juego.
 * Todas las plantas deben extender esta clase y definir su propio comportamiento.
 * Implementa la interfaz Entity para gestionar las propiedades básicas de la entidad.
 */
public abstract class Plant implements Entity {

    // Atributos de la planta: posición, salud y costo
    protected int row; // Fila en la que se encuentra la planta
    protected int col; // Columna en la que se encuentra la planta
    protected int health; // Salud de la planta
    protected int cost; // Costo de colocar la planta

    /**
     * Constructor de la clase Plant, que inicializa la posición y los atributos básicos.
     *
     * @param row Fila en la que se coloca la planta.
     * @param col Columna en la que se coloca la planta.
     * @param health Salud inicial de la planta.
     * @param cost Costo de colocar la planta en el tablero.
     */
    public Plant(int row, int col, int health, int cost) {
        this.row = row;
        this.col = col;
        this.health = health;
        this.cost = cost;
    }

    /**
     * Devuelve la fila en la que se encuentra la planta.
     *
     * @return El valor de la fila.
     */
    @Override
    public int getRow() {
        return row;
    }

    /**
     * Devuelve la columna en la que se encuentra la planta.
     *
     * @return El valor de la columna.
     */
    @Override
    public int getCol() {
        return col;
    }

    /**
     * Devuelve la salud actual de la planta.
     *
     * @return El valor de la salud de la planta.
     */
    @Override
    public int getHealth() {
        return health;
    }

    /**
     * Establece la salud de la planta a un valor específico.
     *
     * @param health El nuevo valor de salud de la planta.
     */
    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Verifica si la planta está viva (salud mayor que cero).
     *
     * @return true si la planta está viva, false si está muerta (salud <= 0).
     */
    @Override
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Devuelve el costo de colocar la planta en el tablero.
     *
     * @return El costo de la planta.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Método abstracto que debe ser implementado por las subclases de Plant.
     * Cada planta debe definir su propio comportamiento en este método (por ejemplo,
     * disparar, moverse, etc.).
     */
    public abstract void action();

    /**
     * Método abstracto para recibir daño. Cada planta debe definir cómo manejar
     * el daño que recibe.
     *
     * @param damage La cantidad de daño que la planta recibe.
     */
    public abstract void takeDamage(int damage);
}
