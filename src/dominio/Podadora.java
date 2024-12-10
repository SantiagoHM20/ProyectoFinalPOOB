package dominio;

import javax.swing.*;
import java.awt.*;

/**
 * La clase Podadora representa una podadora que se mueve a lo largo de una fila del tablero.
 * La podadora tiene una imagen asociada y se activa para moverse en el tablero.
 */
public class Podadora {
    private int row; // Fila en la que se encuentra la podadora
    private boolean activated; // Estado de la podadora (activada o no)
    private JLabel podadoraLabel; // Componente gráfico que representa la podadora

    /**
     * Constructor de la clase Podadora.
     * Inicializa la fila en la que se coloca la podadora, su estado (no activada)
     * y su imagen asociada.
     *
     * @param row La fila en la que se coloca la podadora.
     */
    public Podadora(int row) {
        this.row = row;
        this.activated = false;
        // Carga la imagen de la podadora desde el directorio 'resources/images/pod2.png'
        this.podadoraLabel = new JLabel(new ImageIcon("resources/images/pod2.png"));
        // Establece el tamaño y la posición inicial de la podadora en el panel
        this.podadoraLabel.setBounds(0, 0, 75, 75);
    }

    /**
     * Obtiene el JLabel que representa la podadora.
     *
     * @return JLabel con la imagen de la podadora.
     */
    public JLabel getLabel() {
        return podadoraLabel;
    }

    /**
     * Activa la podadora, iniciando un temporizador que mueve la podadora a través
     * del tablero. Si ya está activada, no hará nada.
     *
     * @param board El panel en el que se encuentra la podadora.
     * @param cols El número de columnas del tablero.
     */
    public void activate(JPanel board, int cols) {
        if (!activated) {
            activated = true;
            // Crea un temporizador que se ejecuta cada 50 milisegundos
            Timer timer = new Timer(50, e -> move(board, cols, (Timer) e.getSource()));
            timer.start(); // Inicia el temporizador
        }
    }

    /**
     * Mueve la podadora dentro del tablero. La podadora se mueve en pasos pequeños
     * en el eje X hasta que alcanza el borde del tablero, momento en el que el temporizador se detiene.
     *
     * @param board El panel en el que se encuentra la podadora.
     * @param cols El número de columnas del tablero, utilizado para calcular el tamaño de los pasos.
     * @param timer El temporizador que controla el movimiento de la podadora.
     */
    private void move(JPanel board, int cols, Timer timer) {
        int x = podadoraLabel.getX(); // Obtiene la posición actual en el eje X de la podadora
        // Calcula el tamaño del paso en función del número de columnas
        int step = board.getWidth() / cols / 10;
        // Si la podadora no ha llegado al borde del tablero, se mueve
        if (x < board.getWidth()) {
            podadoraLabel.setLocation(x + step, podadoraLabel.getY()); // Mueve la podadora a la derecha
        } else {
            timer.stop(); // Detiene el temporizador cuando la podadora llega al borde
        }
        // Vuelve a validar y repintar el panel para mostrar los cambios
        board.revalidate();
        board.repaint();
    }

    /**
     * Verifica si la podadora está activada.
     *
     * @return true si la podadora está activada, false si no lo está.
     */
    public boolean isActivated() {
        return activated;
    }
}
