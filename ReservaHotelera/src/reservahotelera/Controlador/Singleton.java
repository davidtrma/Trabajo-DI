package reservahotelera.Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Singleton {

    private static Singleton instancia;
    private Connection conexion;

    private final String URL = "jdbc:mysql://localhost:3306/BDReservaHotel";
    private final String USUARIO = "root"; // <- Usuario de la BBDD
    private final String CONTRASENA = "123abc"; // <- Contraseña

    // Constructor privado para evitar instanciación directa
    private Singleton() {
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println(" Conexión establecida correctamente.");
        } catch (SQLException e) {
            System.out.println(" Error al conectar a la base de datos:");
            e.printStackTrace();
        }
    }

    // Método estático para obtener la instancia única
    public static Singleton getInstancia() {
        if (instancia == null) {
            instancia = new Singleton();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    // Método opcional para cerrar conexión
    public void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("🔒 Conexión cerrada.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
