package reservahotelera.Modelo;

import java.time.LocalDate;

public class Reserva {

    private Integer idReserva;
    private int idCliente;
    private int idHabitacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private double precioTotal;

    // Constructor para nueva reserva (sin ID)
    public Reserva(Integer idCliente, Integer idHabitacion,
                   LocalDate fechaEntrada, LocalDate fechaSalida, Double precioTotal) {
        this(null, idCliente, idHabitacion, fechaEntrada, fechaSalida, precioTotal);
    }

    // Constructor completo (para modificación)
    public Reserva(Integer idReserva, Integer idCliente, Integer idHabitacion,
                   LocalDate fechaEntrada, LocalDate fechaSalida, Double precioTotal) {
        this.idReserva = idReserva;
        this.idCliente = idCliente;
        this.idHabitacion = idHabitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.precioTotal = precioTotal;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }
}
