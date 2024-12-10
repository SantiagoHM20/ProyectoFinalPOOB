package dominio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;


/**
 * Actualmente utilizada como clase principal
 * Representa el tablero de un juego de plantas contra zombis.
 * El tablero contiene una cuadrícula donde se ubican plantas, proyectiles, zombis y otras entidades del juego.
 */
public class Tablero extends JPanel {
    private static final int ROWS = 5;
    protected static final int COLS = 10;
    private static Tablero instance;

    private Plant[][] plants;
    private List<Projectile> projectiles;
    private List<SunListener> sunListeners;
    private int currentSuns;
    private String selectedPlantType;
    private List<JLabel> suns;
    private Map<Integer, Podadora> podadoras; // Mapa para almacenar las podadoras por fila
    private Random random;
    private Zombie[][] zombie;

    /**
     * Constructor de la clase Tablero.
     * Inicializa el tablero, las plantas, los zombis, y comienza los temporizadores para los proyectiles y los zombis.
     */
    public Tablero() {
        setLayout(new GridLayout(ROWS, COLS));
        setOpaque(false);
        plants = new Plant[ROWS][COLS];
        projectiles = new ArrayList<>();
        sunListeners = new ArrayList<>();
        suns = new ArrayList<>();
        podadoras = new HashMap<>();
        zombie = new Zombie[ROWS][COLS]; // Inicializa la matriz de zombies
        currentSuns = 650;
        selectedPlantType = "";
        initializeTransparentBoard();
        initializePodadoras(); // Inicializar las podadoras al crear el tablero

        // Temporizador para mover los proyectiles
        Timer projectileTimer = new Timer(100, e -> moveProjectiles());
        projectileTimer.start();

        // Temporizador para manejar las explosiones
        Timer explosionTimer = new Timer(100, e -> handleExplosions());
        explosionTimer.start();

        // Temporizador para generar zombis cada cierto tiempo
        Timer zombiesTimer = new Timer(6000, e -> generateZombies());
        zombiesTimer.start();

        debugBoardState();
    }

    /**
     * Elimina una planta del tablero.
     *
     * @param plantToRemove Planta que se desea eliminar del tablero.
     */
    public void removePlant(Plant plantToRemove) {
        int row = plantToRemove.getRow();
        int col = plantToRemove.getCol();

        // Logs de depuración antes de intentar eliminar
        System.out.println("Intentando eliminar planta en (" + row + ", " + col + ")");
        System.out.println("Estado antes de eliminar: " + (plants[row][col] == null ? "null" : plants[row][col]));

        // Validación robusta para garantizar que sea la planta correcta
        if (plants[row][col] != null && plants[row][col].equals(plantToRemove)) {
            // Remover la planta de la matriz lógica
            plants[row][col] = null;

            // Eliminar visualmente la planta del tablero
            JPanel cell = (JPanel) getComponent(row * COLS + col);
            if (cell != null) {
                cell.removeAll();
                cell.revalidate();
                cell.repaint();
            }

            System.out.println("Planta eliminada de (" + row + ", " + col + ").");
        } else {
            // Mensaje de error si la planta no coincide o ya es null
            System.out.println("Error: No se encontró la planta esperada en (" + row + ", " + col + ").");
        }

        // Logs después de intentar eliminar
        System.out.println("Estado después de eliminar: " + (plants[row][col] == null ? "null" : plants[row][col]));
    }

    /**
     * Elimina un zombie del tablero.
     *
     * @param zombieToRemove Zombie que se desea eliminar.
     */
    public void removeZombie(Zombie zombieToRemove) {
        int row = zombieToRemove.getRow();
        int col = zombieToRemove.getCol();

        if (zombie[row][col] == zombieToRemove) {
            // Cambiar el GIF a la animación de muerte
            zombie[row][col].changeGiff();

            // Actualizar la celda para mostrar el GIF de muerte
            updateZombieCell(zombieToRemove);

            // Agrega un temporizador para retrasar la eliminación del zombie
            Timer delayTimer = new Timer(3000, e -> {
                // Elimina el zombie de la matriz lógica
                zombie[row][col] = null;

                // Limpia la celda visualmente
                JPanel cell = (JPanel) getComponent(row * COLS + col);
                cell.removeAll();
                cell.revalidate();
                cell.repaint();

                System.out.println("Zombie eliminado de la fila " + row + " y columna " + col);
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }

    /**
     * Muestra el estado actual del tablero en la consola (usado para depuración).
     */
    public void debugBoardState() {
        System.out.println("Estado actual del tablero:");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print((plants[i][j] == null ? "null" : "Plant") + " ");
            }
            System.out.println();
        }
    }

    /**
     * Genera un zombie de forma aleatoria en una posición válida del tablero.
     * Intenta generar un zombie en la última columna de forma aleatoria, eligiendo entre varios tipos de zombies.
     */
    private void generateZombies() {
        int maxAttempts = 10; // Número máximo de intentos para encontrar una posición válida
        int attempts = 0;

        while (attempts < maxAttempts) {
            int row = (int) (Math.random() * ROWS); // Genera una fila aleatoria
            int col = COLS - 1; // Última columna (o cualquier otra columna válida)

            // Verifica si la posición está libre antes de colocar un zombie
            if (zombie[row][col] == null) {
                // Escoge un tipo de zombie aleatorio
                int zombieType = (int) (Math.random() * 3); // 0, 1 o 2

                // Crea el zombie correspondiente
                Zombie newZombie;
                switch (zombieType) {
                    case 0:
                        newZombie = new Basic(row, col);
                        break;
                    case 1:
                        newZombie = new Buckethead(row, col);
                        break;
                    case 2:
                        newZombie = new Conehead(row, col);
                        break;
                    default:
                        continue; // Si algo falla, intenta de nuevo
                }

                // Asigna el zombie a la matriz lógica
                zombie[row][col] = newZombie;

                // Actualiza la interfaz gráfica
                JPanel cell = (JPanel) getComponent(row * COLS + col);
                JLabel zombieLabel = new JLabel(newZombie.getImage());
                cell.setLayout(new BorderLayout());
                cell.add(zombieLabel, BorderLayout.CENTER);

                cell.revalidate();
                cell.repaint();

                System.out.println(newZombie.getClass().getSimpleName() + " generado en (" + row + ", " + col + ").");
                break; // Sale del bucle después de generar un zombie
            }

            attempts++; // Incrementa el número de intentos
        }

        if (attempts == maxAttempts) {
            System.out.println("No se encontró una posición válida para generar un zombie después de " + maxAttempts + " intentos.");
        }
    }

    /**
     * Notifica que un zombie se ha movido de una columna a otra y actualiza el tablero visualmente.
     *
     * @param zombie Zombie que se ha movido.
     */
    public void notifyZombieMoved(Zombie zombie) {
        if (zombie == null || !zombie.isAlive()) {
            return; // Si el zombie no existe o está muerto, no hacemos nada
        }

        int row = zombie.getRow();
        int oldCol = zombie.getOldCol();
        int newCol = zombie.getCol();

        // Aseguramos que las posiciones sean válidas
        if (row < 0 || row >= ROWS || oldCol < 0 || oldCol >= COLS || newCol < 0 || newCol >= COLS) {
            return;
        }

        // Verificar si el zombie está en animación de muerte
        this.zombie[row][oldCol] = null;
        this.zombie[row][newCol] = zombie;

        JPanel oldCell = (JPanel) getComponent(row * COLS + oldCol);
        JPanel newCell = (JPanel) getComponent(row * COLS + newCol);

        // Verificar si el oldCell tiene un JLabel antes de intentar accederlo
        if (oldCell.getComponentCount() > 0) {
            JLabel zombieLabel = (JLabel) oldCell.getComponent(0);
            oldCell.remove(zombieLabel);
            oldCell.revalidate();
            oldCell.repaint();

            newCell.setLayout(new BorderLayout());
            newCell.add(zombieLabel, BorderLayout.CENTER);
            newCell.revalidate();
            newCell.repaint();
        }
    }

    /**
     * Obtiene la instancia única del tablero.
     * Si la instancia no ha sido creada, se inicializa una nueva.
     *
     * @return La instancia única del tablero.
     */
    public static Tablero getInstance() {
        if (instance == null) {
            instance = new Tablero();
        }
        return instance;
    }

    /**
     * Mata a todos los zombies en la fila especificada mediante el uso de una podadora.
     * La podadora elimina zombies de la fila en la que está ubicada.
     * Cada zombie muere después de que se muestra el GIF de muerte durante 2 segundos.
     *
     * @param row La fila en la que se realizará la eliminación de zombies.
     */
    public void instaKillZombies(int row) {
        if (podadoras.containsKey(row)) {
            Podadora podadora = podadoras.get(row);
            if (podadora != null) {
                // Recorre todas las columnas en la fila especificada
                for (int col = 0; col < COLS; col++) {
                    if (zombie[row][col] != null) {
                        Zombie dyingZombie = zombie[row][col];
                        dyingZombie.stopAction(); // Detener el movimiento del zombie
                        dyingZombie.changeGiff(); // Cambiar al GIF de muerte

                        // Actualizar visualmente la celda con el GIF de muerte
                        updateZombieCell(dyingZombie);

                        // Temporizador para la eliminación visual después de mostrar el GIF de muerte
                        int finalCol = col;
                        Timer delayTimer = new Timer(2000, e -> {
                            // Eliminar zombie de la lógica
                            zombie[row][finalCol] = null;

                            // Eliminar visualmente la celda
                            JPanel cell = (JPanel) getComponent(row * COLS + finalCol);
                            cell.removeAll();
                            cell.revalidate();
                            cell.repaint();

                            System.out.println("Zombie eliminado de la fila " + row + " y columna " + finalCol);
                        });
                        delayTimer.setRepeats(false);
                        delayTimer.start();
                    }
                }
                podadoras.remove(row); // Eliminar la podadora después de usarla
            }
        }
    }

    /**
     * Inicializa un tablero transparente donde cada celda es un JPanel con opacidad y borde.
     * Cada celda responde a eventos de ratón (mouseEnter, mouseExit y mouseClick) para cambiar su apariencia y ejecutar acciones.
     */
    private void initializeTransparentBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                JPanel cell = new JPanel();
                cell.setOpaque(false); // Hacer la celda transparente
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Bordear la celda

                int row = i;
                int col = j;

                // Agregar un listener para eventos de ratón
                cell.addMouseListener(new MouseAdapter() {
                    Color originalColor = cell.getBackground(); // Guardar el color original

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // Cambiar el fondo al pasar el ratón
                        cell.setOpaque(true); // Activar opacidad para que el color se muestre
                        cell.setBackground(new Color(255, 255, 255, 150)); // Blanco semitransparente
                        cell.revalidate();
                        cell.repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        // Restaurar el color original al salir
                        cell.setOpaque(false);
                        cell.setBackground(originalColor); // Volver al color original
                        cell.revalidate();
                        cell.repaint();
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleCellClick(row, col, cell); // Manejar el clic en la celda
                    }
                });

                add(cell); // Añadir la celda al tablero
            }
        }
    }

    /**
     * Maneja el clic en una celda específica para colocar una planta.
     * Si la celda está vacía y se tiene suficiente sol, se coloca una planta de acuerdo al tipo seleccionado.
     *
     * @param row  Fila de la celda seleccionada.
     * @param col  Columna de la celda seleccionada.
     * @param cell La celda seleccionada en el tablero.
     */
    private void handleCellClick(int row, int col, JPanel cell) {
        // Verificar si la celda está reservada para una podadora
        if (col == 0) {
            System.out.println("No puedes colocar plantas en la columna de las podadoras.");
            return; // Salir del método sin hacer nada
        }

        // Si la celda no tiene planta, intenta colocar una nueva planta
        if (plants[row][col] == null) {
            Plant newPlant = null;

            // Determinar el tipo de planta seleccionada y verificar el costo
            if ("Peashooter".equals(selectedPlantType) && currentSuns >= Peashooter.COST) {
                newPlant = new Peashooter(row, col);
            } else if ("Sunflower".equals(selectedPlantType) && currentSuns >= Sunflower.COST) {
                newPlant = new Sunflower(row, col);
            } else if ("Wall-nut".equals(selectedPlantType) && currentSuns >= WallNut.COST) {
                newPlant = new WallNut(row, col);
            } else if ("PotatoMine".equals(selectedPlantType) && currentSuns >= PotatoMine.COST) {
                newPlant = new PotatoMine(row, col);
            }

            if (newPlant != null) {
                // Colocar la planta en el tablero
                plants[row][col] = newPlant;
                currentSuns -= newPlant.getCost(); // Restar soles
                System.out.println(newPlant.getClass().getSimpleName() + " colocado en (" + row + ", " + col + "). Soles restantes: " + currentSuns);

                // Configurar la visualización de la planta
                JLabel plantLabel = new JLabel(newPlant.getImage());
                if (newPlant instanceof Sunflower) {
                    cell.setLayout(null);
                    plantLabel.setBounds(-15, -10, plantLabel.getPreferredSize().width, plantLabel.getPreferredSize().height);
                    cell.add(plantLabel);
                    ((Sunflower) newPlant).startProducingSuns(this, cell);
                } else {
                    cell.setLayout(new BorderLayout());
                    cell.add(plantLabel, BorderLayout.CENTER);
                }

                cell.revalidate();
                cell.repaint();
                newPlant.action(); // Iniciar la acción de la planta
                notifySunListeners(); // Notificar cambios de soles
            } else {
                System.out.println("No tienes suficientes soles o no se seleccionó una planta.");
            }
        } else {
            System.out.println("La celda ya está ocupada.");
        }
    }

    /**
     * Método para manejar las explosiones de las Potato Mines.
     * Si una Potato Mine está activa y hay un zombie cerca, la mina explotará.
     */
    private void handleExplosions() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (plants[row][col] instanceof PotatoMine) {
                    PotatoMine potatoMine = (PotatoMine) plants[row][col];
                    if (potatoMine.isActive()) {
                        JPanel cell = (JPanel) getComponent(row * COLS + col);
                        potatoMine.explodeIfZombieNearby(cell, zombie); // Explota si hay zombies cercanos
                        if (!potatoMine.isActive()) {
                            plants[row][col] = null; // Eliminar la planta después de la explosión
                        }
                    }
                }
            }
        }
    }

    /**
     * Explota una planta en la posición dada.
     * Elimina la planta del tablero y limpia la celda visualmente.
     *
     * @param row Fila de la planta.
     * @param col Columna de la planta.
     */
    private void explodePlant(int row, int col) {
        System.out.println("Potato Mine en (" + row + ", " + col + ") explotó.");
        plants[row][col] = null; // Eliminar la planta del tablero

        // Obtener la celda del tablero
        JPanel cell = (JPanel) getComponent(row * COLS + col);
        cell.removeAll(); // Limpiar la celda visualmente
        cell.revalidate();
        cell.repaint();
    }

    /**
     * Actualiza la celda del zombie visualmente en el tablero.
     * Cambia la imagen del zombie en la celda correspondiente.
     *
     * @param zombieToUpdate El zombie que se va a actualizar visualmente.
     */
    public void updateZombieCell(Zombie zombieToUpdate) {
        int row = zombieToUpdate.getRow();
        int col = zombieToUpdate.getCol();

        JPanel cell = (JPanel) getComponent(row * COLS + col); // Obtener la celda
        cell.removeAll(); // Limpiar la celda

        JLabel zombieLabel = new JLabel(zombieToUpdate.getImage()); // Crear un nuevo JLabel con el GIF actualizado
        cell.setLayout(new BorderLayout());
        cell.add(zombieLabel, BorderLayout.CENTER);

        cell.revalidate(); // Actualizar la interfaz
        cell.repaint();
    }

    /**
     * Actualiza la celda en una posición específica del tablero.
     * Si hay una planta en la celda, se actualiza su imagen visual.
     *
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     */
    public void updateCell(int row, int col) {
        JPanel cell = (JPanel) getComponent(row * COLS + col); // Obtiene la celda en el tablero
        cell.removeAll(); // Limpia la celda

        Plant plant = plants[row][col];
        if (plant != null) {
            if (plant instanceof PotatoMine) {
                PotatoMine potatoMine = (PotatoMine) plant;
                cell.add(potatoMine.getPotatoLabel(), BorderLayout.CENTER); // Usa el JLabel de la planta
            } else {
                JLabel plantLabel = new JLabel(plant.getImage());
                cell.add(plantLabel, BorderLayout.CENTER);
            }
        }

        cell.revalidate(); // Vuelve a calcular el diseño
        cell.repaint(); // Redibuja la celda
    }


    /**
     * Método para agregar un sol visual en una posición específica del tablero.
     *
     * @param row Fila en la que se agrega el sol.
     * @param col Columna en la que se agrega el sol.
     * @param sunValue El valor del sol agregado.
     */
    public void addSunVisual(int row, int col, int sunValue) {
        JPanel cell = (JPanel) getComponent(row * COLS + col);
        JLabel sunLabel = new JLabel(new ImageIcon("resources/images/cir3.png"));
        sunLabel.setBounds(10, 10, 50, 50);
        sunLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        sunLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentSuns += sunValue;
                notifySunListeners();
                cell.remove(sunLabel);
                suns.remove(sunLabel);
                cell.revalidate();
                cell.repaint();
            }
        });

        cell.add(sunLabel);
        suns.add(sunLabel);
        cell.setComponentZOrder(sunLabel, 0);
        cell.revalidate();
        cell.repaint();

        // Temporizador para eliminar el sol después de 15 segundos.
        new Timer(15000, e -> {
            if (suns.contains(sunLabel)) {
                cell.remove(sunLabel);
                suns.remove(sunLabel);
                cell.revalidate();
                cell.repaint();
            }
        }).start();
    }

    /**
     * Método que mueve los proyectiles a través del tablero.
     */
    public void moveProjectiles() {
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            int row = projectile.getRow();
            int col = projectile.getCol();
            Zombie targetZombie = getZombie(row, col);

            if (targetZombie != null && targetZombie.isAlive()) {
                // Si hay un zombie en la posición del proyectil, se maneja el impacto.
                projectile.impact(targetZombie);
                System.out.println("Proyectil impactó a zombie en (" + row + ", " + col + "). Salud restante: " + targetZombie.getHealth());

                iterator.remove(); // Elimina el proyectil tras el impacto

                if (!targetZombie.isAlive()) {
                    removeZombie(targetZombie); // Elimina el zombie si está muerto.
                    System.out.println("Zombie eliminado en (" + row + ", " + col + ").");
                }
                continue;
            }

            // Mueve el proyectil hacia la derecha.
            projectile.move();

            // Elimina proyectiles que salen del tablero.
            if (projectile.getCol() >= COLS) {
                iterator.remove();
            }
        }
        repaint();
    }

    /**
     * Método para agregar un proyectil al tablero.
     *
     * @param projectile El proyectil que se agrega al tablero.
     */
    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);

        // Representa gráficamente el proyectil en la celda correspondiente.
        JPanel cell = (JPanel) getComponent(projectile.getRow() * COLS + projectile.getCol());
        JLabel projectileLabel = new JLabel(projectile.getImage());
        cell.setLayout(new BorderLayout());
        cell.add(projectileLabel, BorderLayout.CENTER);

        cell.revalidate();
        cell.repaint();
    }

    /**
     * Método para seleccionar el tipo de planta a colocar.
     *
     * @param plantType El tipo de planta seleccionada.
     */
    public void setSelectedPlantType(String plantType) {
        this.selectedPlantType = plantType;
    }

    /**
     * Obtiene la cantidad actual de soles disponibles.
     *
     * @return La cantidad de soles.
     */
    public int getCurrentSuns() {
        return currentSuns;
    }

    /**
     * Agrega un oyente para los cambios en la cantidad de soles.
     *
     * @param listener El oyente a agregar.
     */
    public void addSunListener(SunListener listener) {
        if (!sunListeners.contains(listener)) {
            sunListeners.add(listener);
        }
    }

    /**
     * Notifica a todos los oyentes sobre un cambio en la cantidad de soles.
     */
    private void notifySunListeners() {
        for (SunListener listener : sunListeners) {
            listener.updateSunCount(currentSuns);
        }
    }

    /**
     * Inicializa las podadoras en el tablero.
     */
    private void initializePodadoras() {
        for (int i = 0; i < ROWS; i++) {
            Podadora podadora = new Podadora(i);
            podadoras.put(i, podadora);

            JPanel cell = (JPanel) getComponent(i * COLS);
            cell.setLayout(null);

            JLabel podadoraLabel = podadora.getLabel();
            cell.add(podadoraLabel);

            // Ajusta la posición de la podadora en la celda.
            cell.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    int cellWidth = cell.getWidth();
                    int cellHeight = cell.getHeight();
                    int labelWidth = podadoraLabel.getPreferredSize().width;
                    int labelHeight = podadoraLabel.getPreferredSize().height;

                    int x = (cellWidth - labelWidth) / 2;
                    int y = (cellHeight - labelHeight) / 2;

                    podadoraLabel.setBounds(x, y, labelWidth, labelHeight);
                    cell.revalidate();
                    cell.repaint();
                }
            });
        }
    }

    /**
     * Método sobrescrito para dibujar los elementos gráficos en el tablero, como los proyectiles.
     *
     * @param g El objeto gráfico utilizado para dibujar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar los proyectiles.
        for (Projectile projectile : projectiles) {
            int x = projectile.getCol() * getWidth() / COLS;
            int y = projectile.getRow() * getHeight() / ROWS;

            g.setColor(Color.GREEN);
            g.fillOval(x + 10, y + 20, 20, 20);
        }
    }

    /**
     * Obtiene la planta ubicada en una celda específica.
     *
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @return La planta en la celda, o null si no hay ninguna.
     */
    public Plant getPlantAt(int row, int col) {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
            return plants[row][col];
        }
        return null;
    }

    /**
     * Obtiene el zombie ubicado en una celda específica.
     *
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @return El zombie en la celda, o null si no hay ninguno.
     */
    public Zombie getZombie(int row, int col) {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
            return zombie[row][col];
        }
        return null;
    }

    /**
     * Obtiene el zombie más cercano en una fila específica desde una columna dada.
     *
     * @param row Fila en la que buscar.
     * @param col Columna de inicio de la búsqueda.
     * @return El zombie más cercano o null si no hay zombies en esa fila.
     */
    public Zombie getClosestZombieInRow(int row, int col) {
        Zombie closestZombie = null;
        for (int c = col; c < COLS; c++) {
            Zombie z = zombie[row][c];
            if (z != null && z.isAlive()) {
                closestZombie = z;
                break;
            }
        }
        return closestZombie;
    }

    /**
     * Abre un archivo de guardado y carga el tablero desde el archivo.
     *
     * @param archive El archivo a abrir.
     * @return El tablero cargado.
     * @throws PlayerVsMachineException Si ocurre un error al abrir el archivo.
     */
    public static Tablero open(File archive) throws PlayerVsMachineException {
        try {
            if (!archive.getName().toLowerCase().endsWith(".dat")) throw new PlayerVsMachineException(PlayerVsMachineException.NO_DAT);
            if (archive == null || !archive.exists()) throw new FileNotFoundException(PlayerVsMachineException.ARCHIVE_NULL + archive.getPath());

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(archive));
            Tablero aM = (Tablero) in.readObject();
            in.close();
            JOptionPane.showMessageDialog(null, "La construcción se ha cargado con éxito");
            return aM;
        } catch (FileNotFoundException e) {
            throw new PlayerVsMachineException(PlayerVsMachineException.ARCHIVE_NOT_FOUND + archive.getPath());

        } catch (ClassNotFoundException e) {
            throw new PlayerVsMachineException(PlayerVsMachineException.NO_CLASS);

        } catch (IOException e) {
            throw new PlayerVsMachineException(PlayerVsMachineException.INP_OUT_ERROR + e.getMessage());
        }
    }

    /**
     * Guarda el estado actual del tablero en un archivo.
     *
     * @param archive El archivo donde se guardará el estado.
     * @throws PlayerVsMachineException Si ocurre un error al guardar el archivo.
     */
    public void save(File archive) throws PlayerVsMachineException {
        try {
            if (!archive.getName().toLowerCase().endsWith(".dat")) archive = new File(archive.getAbsolutePath() + ".dat");
            if (archive == null) throw new IllegalArgumentException(PlayerVsMachineException.ARCHIVE_NULL);
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archive));
            out.writeObject(this);
            out.close();
            JOptionPane.showMessageDialog(null, "La construcción se ha guardado con éxito");
        } catch (FileNotFoundException e) {
            throw new PlayerVsMachineException(PlayerVsMachineException.ARCHIVE_NOT_FOUND);

        } catch (IllegalArgumentException e) {
            throw new PlayerVsMachineException(PlayerVsMachineException.ROUTE_INVALID + e.getMessage());

        } catch (IOException e) {
            throw new PlayerVsMachineException(PlayerVsMachineException.INP_OUT_ERROR + e.getMessage());
        }
    }

    /**
     * Cambia el tablero actual por otro tablero proporcionado.
     *
     * @param a El nuevo tablero.
     */
    public void changeGame(Tablero a) {
        instance = a.getTablero();
    }

    /**
     * Obtiene el tablero actual.
     *
     * @return El tablero actual.
     */
    public Tablero getTablero() {
        return instance;
    }

    /**
     * Obtiene la matriz de plantas en el tablero.
     *
     * @return La matriz de plantas.
     */
    public Plant[][] getPlants() {
        return plants;
    }

    /**
     * Obtiene la matriz de zombis en el tablero.
     *
     * @return La matriz de zombis.
     */
    public Zombie[][] getZombies() {
        return zombie;
    }

    /**
     * Obtiene el mapa de podadoras en el tablero.
     *
     * @return El mapa de podadoras.
     */
    public <K, V> Map<K,V> getPodadoras() {
        return (Map<K, V>) podadoras;
    }

    /**
     * Crea un Zombie Basic en una cordenada dada del tablero
     * @param row
     * @param col
     */
    public void addBasic(int row, int col) {
        // Verifica que las coordenadas sean válidas.
        if (row < 0 || row >= zombie.length || col < 0 || col >= zombie[0].length) {
            throw new IllegalArgumentException("Coordenadas fuera de rango");
        }

        // Crea un nuevo zombie básico.
        Basic basicZombie = new Basic(row, col);

        // Añade el zombie a la posición correspondiente en el tablero.
        zombie[row][col] = basicZombie;


    }

}



