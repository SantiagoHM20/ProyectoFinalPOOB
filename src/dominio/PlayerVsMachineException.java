package dominio;

/**
 * La clase PlayerVsMachineException extiende la clase Exception y se utiliza para
 * manejar excepciones específicas relacionadas con la gestión de archivos y coordenadas
 * en el contexto de un juego o simulación donde un jugador se enfrenta a una máquina.
 */
public class PlayerVsMachineException extends Exception {

    // Mensajes de error relacionados con operaciones de archivo
    public static final String OPEN_ERROR = "Ocurrió un error al abrir el archivo"; // Error al abrir un archivo
    public static final String SAVE_ERROR = "Ocurrió un error al guardar el archivo"; // Error al guardar un archivo
    public static final String SAVE_IN_CONSTRUCTION = "Guardar está en construcción"; // Guardado en proceso de construcción
    public static final String OPEN_IN_CONSTRUCTION = "Abrir está en construcción"; // Apertura de archivo en proceso de construcción
    public static final String IMPORT_IN_CONSTRUCTION = "Importar está en construcción"; // Importación en construcción
    public static final String EXPORT_IN_CONSTRUCTION = "Exportar está en Proceso"; // Exportación en proceso
    public static final String EXPORT_ERROR = "Ocurrió un error al exportar el archivo"; // Error al exportar archivo
    public static final String IMPORT_ERROR = "Ocurrió un error al importar el archivo"; // Error al importar archivo
    public static final String ARCHIVE_NULL = "No hay archivo"; // No se ha proporcionado archivo
    public static final String ARCHIVE_NOT_FOUND = "No se encontró el archivo"; // No se encuentra el archivo especificado
    public static final String INP_OUT_ERROR = "Hubo un error al guardar el archivo"; // Error en la entrada/salida del archivo
    public static final String ROUTE_INVALID = "La ruta al archivo no es valida"; // Ruta de archivo inválida
    public static final String NO_DAT = "El archivo no es .dat"; // Archivo no tiene extensión .dat
    public static final String NO_CLASS = "En el archivo no está la clase AManufacturing"; // Archivo no contiene la clase AManufacturing
    public static final String NO_TXT = "El archivo no es .txt"; // Archivo no tiene extensión .txt
    public static final String ERROR_IMPORT = "No se pudo importar el archivo:"; // Error al importar archivo
    public static final String EXISTING_FILE = "El archivo ya existe. No es posible sobreescribirlo"; // Intento de sobrescribir archivo existente

    // Mensajes de error relacionados con las coordenadas
    public static final String COORDENADA_NO_NUMERICA = "COORDENADA NO NUMÉRICA"; // Coordenada no es numérica
    public static final String COORDENADA_INVALIDA = "COORDENADA FUERA DEL RANGO PERMITIDO"; // Coordenada fuera del rango permitido
    public static final String COORDENADA_FUERA_DE_RANGO = "COORDENADA FUERA DE RANGO"; // Coordenada fuera del rango
    public static final String TIPO_DESCONOCIDO = "TIPO DESCONOCIDO EN LA LÍNEA"; // Tipo desconocido en una línea específica

    // Código de error
    public static final int WRONG_FORMAT = 1; // Formato incorrecto

    /**
     * Constructor de la clase PlayerVsMachineException.
     * Este constructor crea una nueva instancia de la excepción con un mensaje específico.
     *
     * @param message El mensaje de error que describe la excepción.
     */
    public PlayerVsMachineException(String message) {
        super(message); // Llama al constructor de la clase base Exception con el mensaje proporcionado
    }
}