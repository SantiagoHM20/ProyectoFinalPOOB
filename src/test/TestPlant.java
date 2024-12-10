package test;

import dominio.*;

import javax.swing.*;

/**
 * La clase TestPlant es una subclase de la clase abstracta Plant, utilizada para fines de prueba en el juego.
 * Esta clase implementa los métodos requeridos por la clase base Plant de manera básica para permitir la
 * creación de instancias de plantas para pruebas sin una lógica de juego real.
 */
public class TestPlant extends Plant {

    /**
     * Constructor de la clase TestPlant.
     *
     * @param row Fila en la que se colocará la planta
     * @param col Columna en la que se colocará la planta
     */
    public TestPlant(int row, int col) {
        super(row, col, 100, 50); // Inicializa con salud ficticia de 100 y costo ficticio de 50
    }

    /**
     * Método que define la acción de la planta.
     * En esta implementación, el método está vacío ya que solo se utiliza para pruebas.
     *
     * La implementación real debería definir lo que la planta hace en el juego, por ejemplo, atacar a los zombis.
     */
    @Override
    public void action() {
        // Implementación ficticia para pruebas
    }

    /**
     * Método que recibe daño y reduce la salud de la planta.
     *
     * @param damage Cantidad de daño que se le inflige a la planta
     */
    @Override
    public void takeDamage(int damage) {
        this.health -= damage; // Reduce la salud de la planta por el daño recibido
    }

    /**
     * Obtiene la imagen que representa a la planta.
     * En esta implementación, se devuelve null ya que no se requiere una imagen para pruebas.
     *
     * @return null, ya que no se necesita una imagen en esta clase de prueba
     */
    @Override
    public ImageIcon getImage() {
        return null; // No se proporciona una imagen para esta planta de prueba
    }
}