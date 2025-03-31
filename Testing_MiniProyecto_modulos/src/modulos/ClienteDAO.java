package modulos;

import clases.Cliente;
import java.sql.*;
import database.ConexionDB;

public class ClienteDAO {

    private Connection connection;

    public ClienteDAO() {
        this.connection = ConexionDB.conectar(); 
    }

    public void agregarCliente(Cliente cliente) {
        String query = "INSERT INTO clientes (nombre, email, saldo) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setDouble(3, cliente.getSaldo());
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cliente.setId(generatedKeys.getInt(1)); 
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cliente obtenerClientePorId(int id) {
        String query = "SELECT * FROM clientes WHERE id = ?";
        Cliente cliente = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getDouble("saldo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cliente;
    }

    public void actualizarSaldoCliente(int id, double saldo) {
        String query = "UPDATE clientes SET saldo = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, saldo);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarCliente(int id) {
        String query = "DELETE FROM clientes WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
