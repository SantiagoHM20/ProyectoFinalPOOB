package dominio;

import javax.swing.*;

/**
 * La interfaz Entity define los métodos comunes para cualquier entidad en el juego,
 * como zombies, plantas u otros objetos que puedan tener una representación visual,
 * una posición en el tablero, una cantidad de salud y un estado de vida.
 */
public interface Entity {

    /**
     * Devuelve la imagen o icono que representa visualmente la entidad.
     *
     * @return ImageIcon que representa la entidad en el juego.
     */
    ImageIcon getImage();

    /**
     * Devuelve la fila en la que se encuentra la entidad en el tablero.
     *
     * @return La fila de la entidad.
     */
    int getRow();

    /**
     * Devuelve la columna en la que se encuentra la entidad en el tablero.
     *
     * @return La columna de la entidad.
     */
    int getCol();

    /**
     * Devuelve la salud actual de la entidad.
     *
     * @return La salud actual de la entidad.
     */
    int getHealth();

    /**
     * Establece la salud de la entidad.
     *
     * @param health El valor de salud a establecer.
     */
    void setHealth(int health);

    /**
     * Verifica si la entidad sigue viva (es decir, si tiene salud mayor a 0).
     *
     * @return true si la entidad está viva, false si está muerta.
     */
    boolean isAlive();
}