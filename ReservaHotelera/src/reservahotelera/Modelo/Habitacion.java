package reservahotelera.Modelo;

public class Habitacion {

    private int id;
    private String numero;
    private String tamanho;
    private String calidad;
    private double precio;
    //private int idHotel;

    public Habitacion(int id, String numero, String tamanho, String calidad, double precio) {
        this.id = id;
        this.numero = numero;
        this.tamanho = tamanho;
        this.calidad = calidad;
        this.precio = precio;
        //this.idHotel = idHotel;
    }

    // Getters y setters
    public int getId() { return id; }
    public String getNumero() { return numero; }
    public String getTamanho() { return tamanho; }
    public String getCalidad() { return calidad; }
    public double getPrecio() { return precio; }
   // public int getIdHotel() { return idHotel; }

    @Override
    public String toString() {
        return tamanho + " - " + calidad + " (" + precio + "€)";
    }
}
