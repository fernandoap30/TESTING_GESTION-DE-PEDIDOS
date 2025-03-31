package clases;

public class Pago {
    private int id;
    private int pedidoId;
    private double monto;
    private String estado; 

    public Pago(int id, int pedidoId, double monto, String estado) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.monto = monto;
        this.estado = estado;
    }
    public int getId() {return id; }

    public void setId(int id) { this.id = id; }

    public int getPedidoId() {return pedidoId;}

    public void setPedidoId(int pedidoId) {this.pedidoId = pedidoId; }

    public double getMonto() {  return monto; }

    public void setMonto(double monto) {this.monto = monto;}

    public String getEstado() { return estado;}

    public void setEstado(String estado) {this.estado = estado;}
    
}	