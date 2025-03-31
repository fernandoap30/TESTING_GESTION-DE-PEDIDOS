package clases;

public class Cliente {
    private int id;
    private String nombre;
    private String email;
    private double saldo;
    
    public Cliente(int id, String nombre, String email, double saldo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.saldo = saldo;
    }

    public int getId() {return id;}

    public void setId(int id) { this.id = id;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) { this.nombre = nombre;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public double getSaldo() {return saldo;}

    public void setSaldo(double saldo) {this.saldo = saldo; }
}
