package reservahotelera.Modelo.Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import reservahotelera.Modelo.Reserva;
public class ReservaDAO {
    private Connection conn;

    public ReservaDAO(Connection conn) {
        this.conn = conn;
    }

    public void guardarReserva(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reservas (id_cliente, id_habitacion, fecha) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, reserva.getIdCliente());
        stmt.setInt(2, reserva.getIdHabitacion());
        stmt.setDate(3, java.sql.Date.valueOf(reserva.getFechaEntrada())); // solo fecha entrada si tu tabla solo tiene eso
        stmt.executeUpdate();
    }

    public int guardarReservaYObtenerID(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reservas (id_cliente, id_habitacion, fecha, fecha_salida, precio_total) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reserva.getIdCliente());
            stmt.setInt(2, reserva.getIdHabitacion());
            stmt.setDate(3, Date.valueOf(reserva.getFechaEntrada()));
            stmt.setDate(4, Date.valueOf(reserva.getFechaSalida()));
            stmt.setDouble(5, reserva.getPrecioTotal());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo obtener el ID de la reserva");
    }

    public void guardarServiciosReserva(int idReserva, List<Integer> idServicios) throws SQLException {
        String sql = "INSERT INTO reserva_servicio (id_reserva, id_servicio) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Integer idServicio : idServicios) {
                stmt.setInt(1, idReserva);
                stmt.setInt(2, idServicio);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public Reserva obtenerReservaPorId(int idReserva) throws SQLException {
        String sql = "SELECT * FROM reservas WHERE id_reserva = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReserva);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Reserva(
                        rs.getInt("id_reserva"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_habitacion"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getDate("fecha_salida").toLocalDate(),
                        rs.getDouble("precio_total")
                );
            }
            return null;
        }
    }

    public List<Integer> obtenerServiciosReserva(int idReserva) throws SQLException {
        List<Integer> servicios = new ArrayList<>();
        String sql = "SELECT id_servicio FROM reserva_servicio WHERE id_reserva = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReserva);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                servicios.add(rs.getInt("id_servicio"));
            }
        }
        return servicios;
    }

    public boolean actualizarReserva(Reserva reserva) throws SQLException {
        if (reserva.getIdReserva() == null) {
            throw new SQLException("No se puede actualizar una reserva sin ID");
        }

        String sql = "UPDATE reservas SET id_habitacion = ?, fecha = ?, fecha_salida = ?, " +
                "precio_total = ? WHERE id_reserva = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reserva.getIdHabitacion());
            stmt.setDate(2, Date.valueOf(reserva.getFechaEntrada()));
            stmt.setDate(3, Date.valueOf(reserva.getFechaSalida()));
            stmt.setDouble(4, reserva.getPrecioTotal());
            stmt.setInt(5, reserva.getIdReserva());

            return stmt.executeUpdate() > 0;
        }
    }

    public void actualizarServiciosReserva(int idReserva, List<Integer> nuevosServicios) throws SQLException {
        // Primero eliminar servicios existentes
        String deleteSql = "DELETE FROM reserva_servicio WHERE id_reserva = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
            stmt.setInt(1, idReserva);
            stmt.executeUpdate();
        }

        // Luego insertar los nuevos servicios
        if (!nuevosServicios.isEmpty()) {
            guardarServiciosReserva(idReserva, nuevosServicios);
        }
    }

}
