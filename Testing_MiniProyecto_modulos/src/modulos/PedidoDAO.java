package modulos;

import clases.Pedido;
import clases.Producto;
import database.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private Connection connection;

    public PedidoDAO() {
        this.connection = ConexionDB.conectar();
    }

    public void agregarPedido(Pedido pedido) {
        String query = "INSERT INTO pedidos(clienteId, fecha, total, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pedido.getClienteId());
            stmt.setDate(2, new java.sql.Date(pedido.getFecha().getTime()));
            stmt.setDouble(3, pedido.getTotal());
            stmt.setString(4, pedido.getEstado());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int pedidoId = rs.getInt(1);
                agregarProductosAPedido(pedidoId, pedido.getProductos());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void agregarProductosAPedido(int pedidoId, List<Producto> productos) {
        String query = "INSERT INTO pedido_productos(pedidoId, productoId, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (Producto producto : productos) {
                stmt.setInt(1, pedidoId);
                stmt.setInt(2, producto.getId());
                stmt.setInt(3, 1);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelarPedido(int pedidoId) {
        String query = "UPDATE pedidos SET estado = 'Cancelado' WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, pedidoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarEstadoPedido(int pedidoId, String estado) {
        String query = "UPDATE pedidos SET estado = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, estado);
            stmt.setInt(2, pedidoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Pedido consultarPedido(int pedidoId) {
        String query = "SELECT * FROM pedidos WHERE id = ?";
        Pedido pedido = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, pedidoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int clienteId = rs.getInt("clienteId");
                Date fecha = rs.getDate("fecha");
                double total = rs.getDouble("total");
                String estado = rs.getString("estado");

                List<Producto> productos = obtenerProductosDePedido(pedidoId);

                pedido = new Pedido(pedidoId, clienteId, fecha, total, estado, productos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedido;
    }

    private List<Producto> obtenerProductosDePedido(int pedidoId) {
        List<Producto> productos = new ArrayList<>();
        String query = "SELECT p.id, p.nombre, p.precio, pp.cantidad FROM productos p " +
                       "JOIN pedido_productos pp ON p.id = pp.productoId WHERE pp.pedidoId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, pedidoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad");

                Producto producto = new Producto(id, nombre, precio, cantidad);
                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }
}


