package database;

import clases.Cliente;
import clases.Pedido;
import clases.Producto;
import clases.Pago;
import modulos.ClienteDAO;
import modulos.PedidoDAO;
import modulos.ProductoDAO;
import modulos.PagoDAO;

import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClienteDAO clienteDAO = new ClienteDAO();
        ProductoDAO productoDAO = new ProductoDAO();
        PedidoDAO pedidoDAO = new PedidoDAO();
        PagoDAO pagoDAO = new PagoDAO();

        while (true) {
            System.out.println("Menú:");
            System.out.println("1. Ingresar Cliente");
            System.out.println("2. Ingresar Producto");
            System.out.println("3. Ingresar Pedido");
            System.out.println("4. Realizar Pago");
            System.out.println("5. Consultar Pedido");
            System.out.println("6. Salir");
            int opcion = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcion) {
                case 1:
                    
                    System.out.println("Ingrese el nombre del cliente:");
                    String nombreCliente = scanner.nextLine();
                    System.out.println("Ingrese el email del cliente:");
                    String emailCliente = scanner.nextLine();
                    System.out.println("Ingrese el saldo del cliente:");
                    double saldoCliente = scanner.nextDouble();
                    Cliente cliente = new Cliente(0, nombreCliente, emailCliente, saldoCliente);
                    clienteDAO.agregarCliente(cliente);
                    break;

                case 2:
                    
                    System.out.println("Ingrese el nombre del producto:");
                    String nombreProducto = scanner.nextLine();
                    System.out.println("Ingrese el precio del producto:");
                    double precioProducto = scanner.nextDouble();
                    System.out.println("Ingrese el stock del producto:");
                    int stockProducto = scanner.nextInt();
                    Producto producto_nuevo = new Producto(0, nombreProducto, precioProducto, stockProducto);
                    productoDAO.agregarProducto(producto_nuevo);
                    break;

                case 3:
                    
                    System.out.println("Ingrese el ID del cliente:");
                    int clienteId = scanner.nextInt();
                    scanner.nextLine(); 
                    System.out.println("Ingrese la fecha del pedido (Formato: dd/MM/yyyy):");
                    String fechaStr = scanner.nextLine();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date fecha = null;
                    try {
                        fecha = sdf.parse(fechaStr);
                    } catch (ParseException e) {
                        System.out.println("Fecha inválida. Asegúrese de usar el formato correcto.");
                        break;
                    }
                    List<Producto> productosPedido = new ArrayList<>();
                    double total = 0;
                    while (true) {
                        System.out.println("Ingrese el ID del producto (0 para terminar):");
                        int productoId = scanner.nextInt();
                        if (productoId == 0) break;

                        Producto producto = productoDAO.consultarProducto(productoId);
                        if (producto != null) {
                            System.out.println("Ingrese la cantidad de " + producto.getNombre() + ":");
                            int cantidad = scanner.nextInt();
                            if (producto.getStock() >= cantidad) {
                                productosPedido.add(producto);
                                total += producto.getPrecio() * cantidad;
                                
                                productoDAO.actualizarStock(productoId,cantidad);  
                            } else {
                                System.out.println("No hay suficiente stock para el producto " + producto.getNombre());
                            }
                        } else {
                            System.out.println("Producto no encontrado.");
                        }
                    }
                    Pedido pedido_nuevo = new Pedido(0, clienteId, fecha, total, "Pendiente", productosPedido);
                    pedidoDAO.agregarPedido(pedido_nuevo);
                    System.out.println("Pedido agregado correctamente.");
                    break;


                case 4:
                    
                    System.out.println("Ingrese el ID del pedido:");
                    int pedidoId = scanner.nextInt();
                    Pedido pedido = pedidoDAO.consultarPedido(pedidoId);  

                    if (pedido != null && pedido.getEstado().equals("Pendiente")) {
                        double montoPago = pedido.getTotal();
                        System.out.println("El total del pedido es: " + montoPago);
                        System.out.println("Ingrese el ID del cliente:");
                        int clienteIdPago = scanner.nextInt();
                         
                        Cliente clientePago = clienteDAO.obtenerClientePorId(clienteIdPago);
                        if (clientePago != null) {
                            if (clientePago.getSaldo() >= montoPago) {
                                
                                Pago pago = new Pago(0, pedidoId, montoPago, "Pendiente");
                                
                                if (pagoDAO.procesarPago(pago, clientePago)) {
                                   
                                    pedido.setEstado("Pagado");
                                    pedidoDAO.actualizarEstadoPedido(pedidoId, pedido.getEstado());  

                                    clientePago.setSaldo(clientePago.getSaldo() - montoPago);
                                    clienteDAO.actualizarSaldoCliente(clienteIdPago, clientePago.getSaldo() );

                                    System.out.println("Pago completado con éxito.");
                                } else {
                                    System.out.println("Error al procesar el pago.");
                                }
                            } else {
                                System.out.println("Saldo insuficiente para realizar el pago.");
                            }
                        } else {
                            System.out.println("Cliente no encontrado.");
                        }
                    } else {
                        System.out.println("Pedido no encontrado o ya está pagado.");
                    }
                    break;

                case 5:
                   
                    System.out.println("Ingrese el ID del pedido:");
                    int idPedido = scanner.nextInt();
                    Pedido pedidoConsulta = pedidoDAO.consultarPedido(idPedido);
                    System.out.println("Estado del pedido: " + pedidoConsulta.getEstado());
                    break;

                case 6:
                 
                    System.out.println("¡Hasta luego!");
                    return;

                default:
                    System.out.println("Opción inválida.");
            }
        }
    }
}
