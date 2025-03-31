package modulos;

import clases.Pago;
import clases.Cliente;
import java.sql.*;
import database.ConexionDB;

public class PagoDAO {

    private Connection connection;

    public PagoDAO() {
        this.connection = ConexionDB.conectar(); 
    }

    public boolean procesarPago(Pago pago, Cliente cliente) {
        if (cliente.getSaldo() >= pago.getMonto()) {
            cliente.setSaldo(cliente.getSaldo() - pago.getMonto()); 
            pago.setEstado("Completado"); 
            actualizarSaldoCliente(cliente);
            registrarPago(pago);
            return true;
        } else {
            pago.setEstado("Fallido");
            return false;
        }
    }

    private void actualizarSaldoCliente(Cliente cliente) {
        String query = "UPDATE clientes SET saldo = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, cliente.getSaldo());
            stmt.setInt(2, cliente.getId());
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("No se pudo actualizar el saldo del cliente.");
            } else {
                System.out.println("Saldo del cliente actualizado correctamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registrarPago(Pago pago) {
        String query = "INSERT INTO pagos (pedido_id, monto, estado) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, pago.getPedidoId());
            stmt.setDouble(2, pago.getMonto());
            stmt.setString(3, pago.getEstado());
            stmt.executeUpdate();
            System.out.println("Pago registrado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Pago consultarPago(int pagoId) {
        String query = "SELECT * FROM pagos WHERE id = ?";
        Pago pago = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, pagoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int pedidoId = rs.getInt("pedido_id");
                double monto = rs.getDouble("monto");
                String estado = rs.getString("estado");

                pago = new Pago(pagoId, pedidoId, monto, estado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pago;
    }
}

