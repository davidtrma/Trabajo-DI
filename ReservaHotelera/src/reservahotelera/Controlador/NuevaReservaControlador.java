package reservahotelera.Controlador;

import reservahotelera.Modelo.Dao.HabitacionDAO;
import reservahotelera.Modelo.Dao.ReservaDAO;
import reservahotelera.Modelo.Habitacion;
import reservahotelera.Modelo.Reserva;
import reservahotelera.Vista.NuevaReserva;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class NuevaReservaControlador {

    private NuevaReserva vista;
    private HabitacionDAO habitacionDAO;
    private ReservaDAO reservaDAO;
    private Connection connection;
    private double precioHabitacion; //Guardaremos el precio de la habitación aquí
    private Integer idReservaActual = null; //Para guardar el id de la reserva ya realizada que se vaya a modificar
    public NuevaReservaControlador(NuevaReserva vista, Connection connection, Integer idReserva) {
        this.vista = vista;
        this.habitacionDAO = new HabitacionDAO(connection);
        this.reservaDAO = new ReservaDAO(connection);
        this.connection = connection;
        this.idReservaActual = idReserva;
       // this.iniciarVista(); // Cargar los tamaños y calidades al iniciar la vista

        if (idReserva != null){
            cargarDatosReservaExistente(idReserva);
        }else {
            iniciarVista();
        }

    }

    public NuevaReservaControlador(NuevaReserva vista, Connection connection) {
        this(vista, connection, null); // Llama al constructor principal con idReserva null
    }

    private void cargarDatosReservaExistente(int idReserva) {
        try {
            Reserva reserva = reservaDAO.obtenerReservaPorId(idReserva);
            Habitacion habitacion = habitacionDAO.obtenerHabitacionPorId(reserva.getIdHabitacion());

            // Cargar datos en la vista
            vista.getjComboBox1().setSelectedItem(habitacion.getTamanho());
            vista.getjComboBox2().setSelectedItem(habitacion.getCalidad());
            vista.getDateEntrada().setDate(Date.valueOf(reserva.getFechaEntrada()));
            vista.getDateSalida().setDate(Date.valueOf(reserva.getFechaSalida()));

            // Cargar servicios
            List<Integer> servicios = reservaDAO.obtenerServiciosReserva(idReserva);
            vista.getCheckLimpieza().setSelected(servicios.contains(1));
            vista.getCheckBuffet().setSelected(servicios.contains(2));
            vista.getCheckWiFi().setSelected(servicios.contains(3));
            vista.getCheckSpa().setSelected(servicios.contains(4));
            vista.getCheckServiHabita().setSelected(servicios.contains(5));

            // Actualizar precio
            actualizarPrecio();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al cargar la reserva: " + e.getMessage());
        }
    }

    // Método para inicializar la vista
    private void iniciarVista() {
        try {
            // Cargar los tamaños y tipos de habitación desde la base de datos
            cargarTamanosYCalidades();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Métodos para cargar los tamaños y calidades en los ComboBox
    private void cargarTamanosYCalidades() throws SQLException {

        // Obtener los tamaños de las habitaciones
        List<String> tamanos = habitacionDAO.obtenerTamanhos();
        List<String> calidades = habitacionDAO.obtenerCalidades();

        // Llenar el ComboBox del tamaño con los datos obtenidos
        for (String tamanho : tamanos) {
            vista.getjComboBox1().addItem(tamanho);
        }

        // Llenar el ComboBox de calidad con los datos obtenidos
        for (String calidad : calidades) {
            vista.getjComboBox2().addItem(calidad);
        }
    }

    // Método que calcula el precio basado en los servicios seleccionados
    //Hay que solucionar el hecho de que no se puedan marcar los checks antes de tener establecida unas fechas porque sino no suma lo marcado
    //Hay que evitar que se marquen mal las fechas: que sean el mismo día, que se señale el pasado
    //Hay que hacer que hacer los paneles informativos
    //Hay que hacer que se muestren las reservas con los servicios seleccionados
    //Hay que hacer que el precio de los servicios se lea desde la BBDD
    public void actualizarPrecioServicios() {
        double precioServicios = 0.0;

        // Verificamos los servicios seleccionados y sumamos su precio
        if (vista.getCheckLimpieza().isSelected()) precioServicios += 10; // Ejemplo: 10€ por limpieza diaria
        if (vista.getCheckBuffet().isSelected()) precioServicios += 20;   // Ejemplo: 20€ por buffet
        if (vista.getCheckWiFi().isSelected()) precioServicios += 5;      // Ejemplo: 5€ por Wi-Fi gratuito
        if (vista.getCheckSpa().isSelected()) precioServicios += 30;      // Ejemplo: 30€ por spa
        if (vista.getCheckServiHabita().isSelected()) precioServicios += 15; // Ejemplo: 15€ por servicio a la habitación

        // Actualizamos el precio en la vista
        double precioTotal = precioHabitacion + precioServicios; //Sumamos los servicios al precio de la habitación
        vista.getLabelPrecio().setText(String.format("%.2f", precioTotal)); // Mostrar 2 decimales
    }

    // Método para calcular el precio dinámicamente y actualizar el JLabel
    public void actualizarPrecio() {
        try {
            String tamanhoSeleccionado = vista.getjComboBox1().getSelectedItem().toString();
            String calidadSeleccionada = vista.getjComboBox2().getSelectedItem().toString();
            LocalDate fechaEntrada = vista.getDateEntrada().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fechaSalida = vista.getDateSalida().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Obtener la habitación seleccionada de la base de datos
            Habitacion habitacion = habitacionDAO.obtenerHabitacion(tamanhoSeleccionado, calidadSeleccionada);

            if (habitacion != null) {
                // Calcular la duración de la estancia
                long diasEstancia = ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);

                // Calcular el precio total
               precioHabitacion = diasEstancia * habitacion.getPrecio();

                // Actualizar el JLabel con el precio calculado
                vista.getLabelPrecio().setText(String.format("%.2f", precioHabitacion));  // Actualiza el precio en el label
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores si las fechas no son válidas o si hay algún problema con la base de datos
        }
    }

    // Método para manejar el evento de hacer la reserva
    /*
    public boolean hacerReserva() {
        try {
            // Obtener los datos de la vista
            String tamanhoSeleccionado = vista.getjComboBox1().getSelectedItem().toString();
            String calidadSeleccionada = vista.getjComboBox2().getSelectedItem().toString();
            LocalDate fechaEntrada = vista.getFechaEntrada();
            LocalDate fechaSalida = vista.getFechaSalida();

            // Validar que las fechas sean correctas
            if (fechaEntrada == null || fechaSalida == null) {
                JOptionPane.showMessageDialog(vista, "Por favor, selecciona fechas de entrada y salida.");
                return false;
            }
            if (fechaEntrada.isAfter(fechaSalida)) {
                JOptionPane.showMessageDialog(vista, "La fecha de entrada no puede ser posterior a la fecha de salida.");
                return false;
            }

            // Obtener la habitación seleccionada de la base de datos
            Habitacion habitacion = habitacionDAO.obtenerHabitacion(tamanhoSeleccionado, calidadSeleccionada);

            if (habitacion != null) {

                // pillamos el dato ya actualizado
                String precioTexto = vista.getLabelPrecio().getText();
                // Reemplazar coma por punto y eliminar cualquier separador de miles
                precioTexto = precioTexto.replace(",", ".").replaceAll("[^\\d.]", "");

                try {
                    double precioTotal = Double.parseDouble(precioTexto);

                    // Crear la reserva
                    Reserva reserva = new Reserva(
                            1,  // idCliente
                            habitacion.getId(),
                            fechaEntrada,
                            fechaSalida,
                            precioTotal
                    );

                    // Guardar la reserva en la base de datos
                    int idReserva = reservaDAO.guardarReservaYObtenerID(reserva);

                    // Agregar servicios si existen
                    List<Integer> serviciosSeleccionados = vista.getServiciosSeleccionados();
                    if (serviciosSeleccionados != null && !serviciosSeleccionados.isEmpty()) {
                        reservaDAO.guardarServiciosReserva(idReserva, serviciosSeleccionados);
                    }

                    JOptionPane.showMessageDialog(vista, "Reserva realizada con éxito. ID de reserva: " + idReserva);
                    return true;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(vista, "Formato de precio inválido: " + precioTexto);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró una habitación con el tamaño y calidad seleccionados.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al realizar la reserva: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error inesperado: " + e.getMessage());
            return false;
        }*/
    public boolean hacerReserva() {
        try {
            // Obtener datos del formulario
            String tamanhoSeleccionado = vista.getjComboBox1().getSelectedItem().toString();
            String calidadSeleccionada = vista.getjComboBox2().getSelectedItem().toString();
            LocalDate fechaEntrada = vista.getFechaEntrada();
            LocalDate fechaSalida = vista.getFechaSalida();

            // Validaciones
            if (fechaEntrada == null || fechaSalida == null) {
                JOptionPane.showMessageDialog(vista, "Seleccione fechas de entrada y salida");
                return false;
            }
            if (fechaEntrada.isAfter(fechaSalida)) {
                JOptionPane.showMessageDialog(vista, "La fecha de entrada no puede ser posterior a la de salida");
                return false;
            }

            // Obtener precio
            String precioTexto = vista.getLabelPrecio().getText().replace(",", ".");
            double precioTotal = Double.parseDouble(precioTexto);

            // Obtener habitación
            Habitacion habitacion = habitacionDAO.obtenerHabitacion(tamanhoSeleccionado, calidadSeleccionada);
            if (habitacion == null) {
                JOptionPane.showMessageDialog(vista, "Habitación no disponible");
                return false;
            }

            // Obtener servicios seleccionados
            List<Integer> serviciosSeleccionados = vista.getServiciosSeleccionados();

            if (idReservaActual == null) {
                // NUEVA RESERVA
                Reserva reserva = new Reserva(1, habitacion.getId(), fechaEntrada, fechaSalida, precioTotal);
                int idReservaGenerado = reservaDAO.guardarReservaYObtenerID(reserva);

                if (!serviciosSeleccionados.isEmpty()) {
                    reservaDAO.guardarServiciosReserva(idReservaGenerado, serviciosSeleccionados);
                }

                JOptionPane.showMessageDialog(vista, "Reserva creada con éxito. ID: " + idReservaGenerado);
                return true;
            } else {
                // MODIFICAR RESERVA EXISTENTE
                Reserva reserva = new Reserva(idReservaActual, 1, habitacion.getId(),
                        fechaEntrada, fechaSalida, precioTotal);

                boolean reservaActualizada = reservaDAO.actualizarReserva(reserva);
                if (reservaActualizada) {
                    reservaDAO.actualizarServiciosReserva(idReservaActual, serviciosSeleccionados);
                    JOptionPane.showMessageDialog(vista, "Reserva actualizada con éxito");
                    return true;
                }
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "Formato de precio inválido");
            return false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, "Error de base de datos: " + e.getMessage());
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error inesperado: " + e.getMessage());
            return false;
        }
    }


}
