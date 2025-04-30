/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reservahotelera.Controlador;

/**
 *
 * @author a22angeldtm
 */

import reservahotelera.Vista.InicioSesion;

public class Controlador {
    private InicioSesion vista;
    //private ClienteDAO clienteDAO;
    
    public Controlador(InicioSesion vista){
        this.vista=vista;
        //this.clienteDAO = new ClienteDAO();
    }

    public void iniciarSesion(String usuario , String contrasena){
        // Cliente cliente = clienteDAO.obtenerClientePorUsuario(usuario);
        /*
        if (cliente != null && cliente.getContrasena().equals(contrasena)) {
            // Si el usuario y la contraseña son correctos
            vista.mostrarMensaje("¡Inicio de sesión exitoso!");
            // Aquí puedes redirigir a otra vista, o cargar la ventana principal
        } else {
            // Si las credenciales no coinciden
            vista.mostrarMensaje("Usuario o contraseña incorrectos.");
        }
        */
    }
}

