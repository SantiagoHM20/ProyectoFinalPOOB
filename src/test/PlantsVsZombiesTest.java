package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import dominio.*;
import presentation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import javax.swing.*;

public class PlantsVsZombiesTest {

    private Tablero tablero; // Ejemplo de objeto que representa el tablero

    @BeforeEach
    public void setup() {
        tablero = new Tablero(); // Inicializa el tablero vacío o al estado deseado
    }

    @Test
    public void getClosestZombieInRow_shouldNotReturnNonexistentZombie() {
        assertNull(tablero.getClosestZombieInRow(1,2)); // Cambia según la lógica de tu test
    }

    @Test
    void removePlant_shouldRemovePlantCorrectly() {
        Tablero tablero = Tablero.getInstance();
        Plant testPlant = new TestPlant(2, 3); // Crear una planta ficticia
        tablero.getPlants()[2][3] = testPlant; // Asignarla manualmente al tablero

        tablero.removePlant(testPlant);

        assertNull(tablero.getPlantAt(2, 3), "La planta debería haber sido eliminada del tablero.");
    }

    @Test
    void removePlant_shouldNotRemoveNonexistentPlant() {
        Tablero tablero = Tablero.getInstance();
        Plant testPlant = new TestPlant(2, 3); // Crear una planta ficticia
        tablero.removePlant(testPlant); // Intentar eliminarla sin haberla añadido

        assertNull(tablero.getPlantAt(2, 3), "No debería eliminar una planta inexistente.");
    }


    @Test
    public void removeZombie_shouldRemoveZombieCorrectly() throws InterruptedException {
        tablero.addBasic(2, 3); // Agregar un zombie de prueba
        Zombie z = tablero.getZombies()[2][3];
        tablero.removeZombie(z); // Remover el zombie

        // Espera 3.5 segundos para asegurar que el temporizador termine
        Thread.sleep(3500);

        // Verifica que el zombie haya sido eliminado
        assertNull(tablero.getZombies()[2][3], "El zombie no fue eliminado correctamente.");
    }

    @Test
    void removeZombie_shouldNotRemoveNonexistentZombie() {
        Tablero tablero = Tablero.getInstance();
        Zombie testZombie = new Basic(1, 4); // Crear un zombie ficticio
        tablero.removeZombie(testZombie); // Intentar eliminarlo sin haberlo añadido

        assertNull(tablero.getZombies()[1][4], "No debería eliminar un zombie inexistente.");
    }


    @Test
    public void instaKillZombies_shouldKillAllZombiesInRow() throws InterruptedException {

        tablero.addBasic(2, 3);
        tablero.instaKillZombies(2); // Mata todos los zombies en la fila 2
        Thread.sleep(3500);
        assertNull(tablero.getZombie(2, 3)); // Verifica que no quede ninguno
    }

    @Test
    void instaKillZombies_shouldNotKillZombiesWithoutPodadora() {
        Tablero tablero = Tablero.getInstance();
        tablero.getZombies()[3][4] = new Basic(3, 4); // Colocar un zombie en fila 3, columna 4

        tablero.instaKillZombies(3); // No hay podadora en la fila 3

        assertNotNull(tablero.getZombie(3, 4), "El zombie en (3, 4) no debería haber sido eliminado.");
    }

    @Test
    void getClosestZombieInRow_shouldFindClosestZombie() {
        Tablero tablero = Tablero.getInstance();
        Zombie testZombie = new Basic(4, 6);
        tablero.getZombies()[4][6] = testZombie; // Colocar un zombie en la fila 4, columna 6

        Zombie closestZombie = tablero.getClosestZombieInRow(4, 3);

        assertEquals(testZombie, closestZombie, "Debería encontrar al zombie más cercano en la fila.");
    }



    @Test
    void testAddBasic() {
        // Configuración inicial.
        Tablero tablero = new Tablero(); // Un tablero de 5x5.

        // Añade un zombie básico en la posición (2, 3).
        tablero.addBasic(2, 3);

        // Verifica que el zombie fue añadido al tablero.
        assertNotNull(tablero.getZombies()[2][3], "El zombie no fue añadido correctamente.");

        // Opcional: verifica que sea de tipo Basic.
        assertTrue(tablero.getZombies()[2][3] instanceof Basic, "El zombie no es del tipo esperado.");
    }

    @Test
    void peashooter_shouldDamageZombie() {
        Tablero tablero = Tablero.getInstance();
        Peashooter peashooter = new Peashooter(2, 3);
        tablero.getPlants()[2][3] = peashooter;

        Zombie zombie = new Basic(2, 4);
        tablero.getZombies()[2][4] = zombie;

        peashooter.shoot(); // Simula el disparo.

        assertEquals(80, zombie.getHealth(), "El zombie debería recibir daño de 20.");
    }


    @Test
    void peashooter_shouldNotDamageZombieOutOfRange() {
        Tablero tablero = Tablero.getInstance();
        Peashooter peashooter = new Peashooter(2, 3);
        tablero.getPlants()[2][3] = peashooter;

        Zombie zombie = new Basic(3, 4); // Zombie fuera de la fila del Peashooter.
        tablero.getZombies()[3][4] = zombie;

        peashooter.shoot(); // Simula el disparo.

        assertEquals(100, zombie.getHealth(), "El zombie no debería recibir daño por estar fuera del alcance.");
    }

    @Test
    void potatoMine_shouldExplodeAndKillZombie() throws InterruptedException {
        Tablero tablero = Tablero.getInstance();
        PotatoMine potatoMine = new PotatoMine(2, 3);
        tablero.getPlants()[2][3] = potatoMine;

        Zombie zombie = new Basic(2, 4);
        tablero.getZombies()[2][4] = zombie;

        // Simula el paso del tiempo hasta que la papa esté activa.
        Thread.sleep(15000);
        assertTrue(potatoMine.isActive(), "La PotatoMine debería estar activa.");

        potatoMine.explodeIfZombieNearby(new JPanel(), tablero.getZombies());

        assertEquals(0, zombie.getHealth(), "El zombie debería estar muerto tras la explosión.");
        assertNull(tablero.getPlants()[2][3], "La PotatoMine debería ser removida tras la explosión.");
    }

    @Test
    void potatoMine_shouldNotExplodeWhenInactive() {
        Tablero tablero = Tablero.getInstance();
        PotatoMine potatoMine = new PotatoMine(2, 3);
        tablero.getPlants()[2][3] = potatoMine;

        Zombie zombie = new Basic(2, 4);
        tablero.getZombies()[2][4] = zombie;

        // No se espera tiempo suficiente para activar la papa.
        potatoMine.explodeIfZombieNearby(new JPanel(), tablero.getZombies());

        assertEquals(100, zombie.getHealth(), "El zombie no debería recibir daño de una PotatoMine inactiva.");
        assertNotNull(tablero.getPlants()[2][3], "La PotatoMine no debería ser removida si no explota.");
    }

    @Test
    void wallNut_shouldBeEatenByZombie() {
        Tablero tablero = Tablero.getInstance();
        WallNut wallNut = new WallNut(2, 3);
        tablero.getPlants()[2][3] = wallNut;

        Zombie zombie = new Basic(2, 4);
        tablero.getZombies()[2][4] = zombie;

        // Simula múltiples ataques del zombie.
        for (int i = 0; i < 20; i++) {
            wallNut.takeDamage(200); // El zombie ataca la WallNut.
        }

        assertEquals(0, wallNut.getHealth(), "La WallNut debería ser destruida tras recibir suficiente daño.");
        assertNull(tablero.getPlants()[2][3], "La WallNut debería ser eliminada del tablero tras ser comida.");
    }

    @Test
    void wallNut_shouldNotBeDestroyedBySingleAttack() {
        Tablero tablero = Tablero.getInstance();
        WallNut wallNut = new WallNut(2, 3);
        tablero.getPlants()[2][3] = wallNut;

        Zombie zombie = new Basic(2, 4);
        tablero.getZombies()[2][4] = zombie;

        wallNut.takeDamage(200); // El zombie ataca una vez.

        assertEquals(3800, wallNut.getHealth(), "La WallNut no debería ser destruida por un solo ataque.");
        assertNotNull(tablero.getPlants()[2][3], "La WallNut debería seguir en el tablero.");
    }




}
