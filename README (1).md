# Proyecto final

Este proyecto implementa el juego de 2009 Plantas VS Zombies. 

Está organizado en tres paquetes principales: domain, shapes y test.


## Paquetes y Clases

### domain

#### Tablero: Clase principal que gestiona la lógica del juego, se encarga de manejar la logica de los Zombies y de las plantas, como lo es, agregarlos, quitarlos y su interacción entre ellos.

#### Métodos de tablero:

generateZombie: este metodo es usado para el modo de juego "Jugador vs Maquina", el cual se encarga de generar oleadas de zombies aleatorios en posiciones aleatorias.

handleCellClick: este metodo es el que permite plantar plantas, verifica si hay posiciones vacías y puede crear instancias de plantas en las celdas.

removePlant: metodo que remueve una planta.
 
removeZombie: metodo que remueve un zombie.

notifyZombieMoved: metodo que notifica en la consola cuando un zombie se mueve de posición.

instaKillZombie: metodo usado cuando un zombie llega a la columna 0 y existe una podadora, mata a todos los Zombies de esa columna.

updateZombieCell: metodo usado cuando se quiere cambiar de giff a un zombie, ya sea porque ataca o porque muere.

moveProjectiles: metodo que mueve los proyectiles de la planta peashooter en el tablero.

open: metodo que abre una partida guardada (No implementado actualmente).

save: metodo que guarda una partida. (No implementado actualmente).


#### Zombie: Clase padre de los Zombies, es una clase abstracta que maneja varios metodos que tienen los zombies en común, como lo es atacar, moverse, morir, recibir daño, cambiar de giff.

#### metodos de Zombie:

action: metodo abstracto que cada zombie implementa sobre alguna acción que realicen.

eliminate: metodo abstracto que cada zombie implementa para eliminarse 

changeGiff: metodo abstracto que cada zombie implementa para cambiar su giff

stopAction: metodo abstracto que cada zombie implementa para detener la acción que estén realizando

takeDamage: metodo abstracto que cada zombie implementa para recibir daño.

## Zombies

Cada zombie se implementó como una clase que hereda de Zombie y son los siguientes:

Basic: Zombie normal que se mueve y atacá cuando encuentre una planta una columna antes en la misma fila.

ConeHead: Igual que el zombie Basic pero con más vida.

BucketHead: Igual que el zombie ConeHead pero con más vida.

Brainstein: Zombie que se queda quieto en su posición y genera cerebros. Es la contraparte de Sunflower.

ECIZombie: Zombie que mientras se mueve, dispara proyectiles. Es la contraparte de Peashooter.

#### Plant: Clase padre de las plantas: es una clase abstracta que maneja varios metodos que tienen las plantas en común, como lo es realizar una acción única de cada planta.

#### metodos de Plant:

takeDamage: metodo abstracto que cada planta implementa para recibir daño.

action: metodo abstracto que cada planta implementa sobre alguna acción que realicen.


## Plantas

Cada planta se implementó como una clase que hereda de Plant y son las siguientes:

Sunflower: Planta que genera soles.

Wall-nut: Planta que se queda quieta y tiene más vida que el promedio.

PotatoMine: Planta que debe esperar un tiempo a que se active, y cuando un zombie la pisa, explota.

Peashooter: Planta que dispara proyectiles al primer zombie que encuentre en una filas.

EvolvePlant: Planta que evoluciona 3 veces y dispara proyectiles al primer zombie que encuentre en una filas.

ECIPlant: Planta que genera más soles que un Sunflower y cuesta más.





### Podadora

La clase podadora se encarga de crear objetos podadoras en las columnas 0 de cada fila.

## Presentation


### GameGUI:
#### BoardInitializer: Clase que inicializa con la primera Interfaz del juego con los botones "reanudar partida"(En implementación) y "nueva partida".



#### MainMenuFrame: Clase que tiene los botones con los modos de juego:

Player Vs Machine.

Player Vs Player (No implementado actualmente).

Machine Vs Machine (No implementado actualmente).

#### NewGameFrame: Clase que muestra el tablero de juego así como graficamente la logica del juego.

#### PlantTryGUI: Clase que muestra la barra de plantas disponibles para jugar

## Test
#### PuzzleTest: Clase que contiene las pruebas unitarias para la clase Tablero


## Entradas

Las entradas del juego se realizan a través de la interfaz gráfica, donde el usuario puede interactuar mediante clics de ratón. Las principales interacciones son:

Clic en el botón "Nueva partida": Inicia una nueva sesión de juego.

Clic en el botón "Reanudar partida": Carga una partida guardada (en implementación).

Clic en el tablero de juego: Permite al usuario plantar plantas en las celdas vacías.

Clic en la barra de plantas: Selecciona el tipo de planta que se desea colocar en el tablero.

Clic en los botones del menú principal: Permite seleccionar el modo de juego (Jugador vs Máquina, Jugador vs Jugador, Máquina vs Máquina).



## Salidas

La salida será una matriz 5x10 donde se podrán usar las plantas para jugar y el sistema invocará los Zombies

Se cuentan con 2 minutos para ganar antes de que un zombie llegue a la columna 0 y no haya una podadora en esa columna.