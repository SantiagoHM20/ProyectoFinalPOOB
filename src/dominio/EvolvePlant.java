package dominio;

import javax.swing.*;
import java.awt.*;

public class EvolvePlant extends Plant implements Attacker {
    //coste 200
    public static final int COST = 200;

    //vida 500
    private static final int HEALTH = 500;
    private static final int BASE_FIRE_INTERVAL = 3000;

    private static final int EVOLVE_FIRE_INTERVAL = 1000;

    private static int INITIAL_EVOLUTION_INTERVAL = 15000;

    private static final int FINAL_EVOLUTION_INTERVAL = 20000;
    private static final int DAMAGE = 20;
    private static final int MAX_EVOLUTIONS = 3;

    private Timer fireTimer;
    private Timer evolutionTimer;
    private int fireInterval;
    private int evolutionCount;

    private ImageIcon[] evolutionGifs; // Array para los GIFs de evolución
    private JLabel plantLabel; // Label para mostrar el GIF actual

    public EvolvePlant(int row, int col) {
        super(row, col, HEALTH, COST);

        this.fireInterval = 3000;
        this.evolutionCount = 0;

        // Carga los GIFs de las etapas de evolución
        evolutionGifs = new ImageIcon[] {
                new ImageIcon(getClass().getResource("/images/lanzaguisantes2.gif")),
                new ImageIcon(getClass().getResource("/images/nueva.gif")),
                new ImageIcon(getClass().getResource("/images/nueva2.gif"))
        };

        // Verificar si los GIFs se cargaron correctamente
        for (int i = 0; i < evolutionGifs.length; i++) {
            if (evolutionGifs[i] == null) {
                System.err.println("Error al cargar el GIF de evolución en la etapa " + i);
            }
        }

        // Configura el JLabel con el primer GIF
        plantLabel = new JLabel(evolutionGifs[0]);

        // Inicia los temporizadores para disparar y evolucionar

        startEvolution();
    }



    private void startFiring() {
        fireTimer = new Timer(fireInterval, e -> shoot());
        fireTimer.setRepeats(true);
        fireTimer.start();
    }

    private void startEvolution() {
        evolutionTimer = new Timer(INITIAL_EVOLUTION_INTERVAL, e -> evolve());
        evolutionTimer.setRepeats(true);
        INITIAL_EVOLUTION_INTERVAL = FINAL_EVOLUTION_INTERVAL;
        evolutionTimer.start();
    }

    private void evolve() {
        if (evolutionCount < MAX_EVOLUTIONS) {
            evolutionCount++;

            // Cambia al GIF correspondiente a la nueva etapa
            if (evolutionCount < evolutionGifs.length) {
                plantLabel.setIcon(evolutionGifs[evolutionCount]);
                Tablero tablero = Tablero.getInstance();
                tablero.updateCell(getRow(), getCol());
            }

            // Asegura que el JLabel se actualice visualmente
            plantLabel.revalidate();
            plantLabel.repaint();

            // Aumenta la velocidad de disparo
            setFireInterval(BASE_FIRE_INTERVAL);

            startFiring();

            System.out.println("EvolvePlant en (" + getRow() + ", " + getCol() + ") evolucionó a etapa " + evolutionCount);
        }

        if (evolutionCount == MAX_EVOLUTIONS) {
            setFireInterval(EVOLVE_FIRE_INTERVAL);

            Tablero tablero = Tablero.getInstance();
            tablero.updateCell(getRow(), getCol());

            evolutionTimer.stop();
            System.out.println("EvolvePlant alcanzó su evolución máxima.");
        }
    }

    @Override
    public void takeDamage(int damage) {
        this.health -= damage;
        System.out.println("EvolvePlant en (" + getRow() + ", " + getCol() + ") recibió " + damage + " de daño. Salud restante: " + this.health);

        if (this.health <= 0) {
            this.health = 0;
            stopActions();
            Tablero.getInstance().removePlant(this);
            System.out.println("EvolvePlant en (" + getRow() + ", " + getCol() + ") ha sido destruida.");
        }
    }

    private void stopActions() {
        if (fireTimer != null && fireTimer.isRunning()) {
            fireTimer.stop();
        }
        if (evolutionTimer != null && evolutionTimer.isRunning()) {
            evolutionTimer.stop();
        }
    }

    @Override
    public void action() {
        // Acción gestionada por startFiring()
    }

    public void shoot() {
        // 1. Causar daño directo al zombie más cercano en la fila
        Zombie targetZombie = Tablero.getInstance().getClosestZombieInRow(getRow(), getCol());
        if (targetZombie != null) {
            targetZombie.takeDamage(DAMAGE);
            System.out.println("EvolvePlant en (" + getRow() + ", " + getCol() + ") causó daño directo a zombie en ("
                    + targetZombie.getRow() + ", " + targetZombie.getCol() + "). Salud restante: " + targetZombie.getHealth());
        }

        // 2. Disparar un proyectil hacia la derecha
        Projectile projectile = new Projectile(getRow(), getCol() + 1, DAMAGE);
        Tablero.getInstance().addProjectile(projectile);
        System.out.println("EvolvePlant en (" + getRow() + ", " + getCol() + ") disparó un proyectil hacia la derecha.");
    }

    @Override
    public ImageIcon getImage() {
        return evolutionGifs[evolutionCount];
    }

    public JLabel getPlantLabel() {
        return plantLabel;
    }

    public void setFireInterval(int newInterval){

        this.fireInterval = newInterval;

    }
}

