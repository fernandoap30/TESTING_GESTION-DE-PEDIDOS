package modulos;

import clases.Producto;
import java.sql.*;
import database.ConexionDB;

public class ProductoDAO {

    private Connection connection;
    public ProductoDAO() {
        this.connection = ConexionDB.conectar();
    }

    public boolean validarStock(int productoId, int cantidad) {
        String query = "SELECT stock FROM productos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int stockDisponible = rs.getInt("stock");
                return stockDisponible >= cantidad; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public void actualizarStock(int productoId, int cantidad) {
        String query = "UPDATE productos SET stock = stock - ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, cantidad); 
            stmt.setInt(2, productoId);
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("No se pudo actualizar el stock, el producto no existe o hubo un error.");
            } else {
                System.out.println("Stock actualizado correctamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void agregarProducto(Producto producto) {
        String query = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getStock());
            stmt.executeUpdate();
            System.out.println("Producto agregado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Producto consultarProducto(int productoId) {
        String query = "SELECT * FROM productos WHERE id = ?";
        Producto producto = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int stock = rs.getInt("stock");

                producto = new Producto(productoId, nombre, precio, stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return producto;
    }
}

