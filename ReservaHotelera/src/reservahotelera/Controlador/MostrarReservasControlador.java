package reservahotelera.Controlador;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class MostrarReservasControlador {
    private final Connection connection;

    public MostrarReservasControlador(Connection connection) {
        this.connection = connection;
    }

    public DefaultTableModel obtenerDatosReservas() {
        String[] columnNames = {"Código Reserva", "Tamaño Habitación", "Tipo Habitación",
                "Fecha Entrada", "Fecha Salida", "Precio"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        String sql = "SELECT r.id_reserva, h.tamaño, h.calidad, r.fecha, " +
                "r.fecha_salida, r.precio_total " +
                "FROM reservas r JOIN habitaciones h ON r.id_habitacion = h.id_habitacion";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id_reserva"),
                        rs.getString("tamaño"),
                        rs.getString("calidad"),
                        rs.getDate("fecha"),
                        rs.getDate("fecha_salida"),
                        String.format("%.2f €", rs.getDouble("precio_total"))
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    public boolean eliminarReserva(int idReserva) {
        String sql = "DELETE FROM reservas WHERE id_reserva = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Primero eliminar los servicios asociados
            eliminarServiciosReserva(idReserva);

            // Luego eliminar la reserva
            stmt.setInt(1, idReserva);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void eliminarServiciosReserva(int idReserva) throws SQLException {
        String sql = "DELETE FROM reserva_servicio WHERE id_reserva = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idReserva);
            stmt.executeUpdate();
        }
    }

    public boolean modificarReserva(int idReserva, LocalDate nuevaFechaEntrada,
                                    LocalDate nuevaFechaSalida, double nuevoPrecio) {
        String sql = "UPDATE reservas SET fecha = ?, fecha_salida = ?, precio_total = ? " +
                "WHERE id_reserva = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(nuevaFechaEntrada));
            stmt.setDate(2, Date.valueOf(nuevaFechaSalida));
            stmt.setDouble(3, nuevoPrecio);
            stmt.setInt(4, idReserva);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> obtenerServiciosReserva(int idReserva) {
        List<String> servicios = new ArrayList<>();
        String sql = "SELECT s.nombre FROM servicios s " +
                "JOIN reserva_servicio rs ON s.id_servicio = rs.id_servicio " +
                "WHERE rs.id_reserva = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idReserva);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                servicios.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener servicios: " + e.getMessage());
        }

        return servicios;
    }

}
