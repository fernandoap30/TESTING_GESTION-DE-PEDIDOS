package clases;
import java.util.Date;
import java.util.List;

public class Pedido {
    private int id;
    private int clienteId;
    private Date fecha;
    private double total;
    private String estado;  
    private List<Producto> productos;
    public Pedido(int id, int clienteId, Date fecha, double total, String estado, List<Producto> productos) {
        this.id = id;
        this.clienteId = clienteId;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        this.productos = productos;
    }   
    public int getId() { return id;}

    public void setId(int id) {this.id = id;}

    public int getClienteId() {return clienteId;}

    public void setClienteId(int clienteId) { this.clienteId = clienteId;}

    public Date getFecha() { return fecha;}

    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }

    public void setTotal(double total) {this.total = total; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) {this.estado = estado; }

    public List<Producto> getProductos() { return productos;}

    public void setProductos(List<Producto> productos) {this.productos = productos; }
}

