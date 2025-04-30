package reservahotelera.Modelo.Dao;

import java.sql.*;
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
}
