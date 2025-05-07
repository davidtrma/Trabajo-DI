package reservahotelera.Modelo.Dao;

import java.sql.*;
import java.util.*;
import reservahotelera.Modelo.Habitacion;

public class HabitacionDAO {
    private Connection conn;

    public HabitacionDAO(Connection conn) {
        this.conn = conn;
    }

    public List<String> obtenerTamanhos() throws SQLException {
        Set<String> tamanhos = new HashSet<>();
        String sql = "SELECT DISTINCT tamaño FROM habitaciones";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        while (rs.next()) {
            tamanhos.add(rs.getString("tamaño"));
        }
        return new ArrayList<>(tamanhos);
    }

    public List<String> obtenerCalidades() throws SQLException {
        Set<String> calidades = new HashSet<>();
        String sql = "SELECT DISTINCT calidad FROM habitaciones";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        while (rs.next()) {
            calidades.add(rs.getString("calidad"));
        }
        return new ArrayList<>(calidades);
    }

    public Habitacion obtenerHabitacion(String tamanho, String calidad) throws SQLException {
        String sql = "SELECT id_habitacion, numero, tamaño, calidad, precio FROM habitaciones WHERE tamaño = ? AND calidad = ? LIMIT 1;";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, tamanho);
        stmt.setString(2, calidad);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            // Ahora también obtenemos el numero de la habitación
            String numero = rs.getString("numero");

            // Pasamos el numero al constructor de Habitacion
            return new Habitacion(
                    rs.getInt("id_habitacion"),
                    numero,  // número de la habitación
                    tamanho, // tamaño
                    calidad, // calidad
                    rs.getDouble("precio") // precio
            );
        }
        return null;
    }

    public Habitacion obtenerHabitacionPorId(int idHabitacion) throws SQLException {
        String sql = "SELECT id_habitacion, numero, tamaño, calidad, precio FROM habitaciones WHERE id_habitacion = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idHabitacion);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Habitacion(
                        rs.getInt("id_habitacion"),
                        rs.getString("numero"),
                        rs.getString("tamaño"),
                        rs.getString("calidad"),
                        rs.getDouble("precio")
                );
            }
            return null;
        }
    }

}
