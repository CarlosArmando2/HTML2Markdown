
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServidorPrueba {

    public static void main(String[] args) throws Exception {

        HttpServer servidor = HttpServer.create(
                new InetSocketAddress(8000), 0
        );

        servidor.createContext("/", intercambio -> {

            String nombre = intercambio.getRequestURI().getPath();

            Path archivo = Path.of("tests", nombre.substring(1));

            if (Files.exists(archivo) && Files.isRegularFile(archivo)) {

                byte[] contenido = Files.readAllBytes(archivo);

                intercambio.getResponseHeaders().set(
                        "Content-Type", "text/html; charset=UTF-8"
                );

                intercambio.sendResponseHeaders(200, contenido.length);

                intercambio.getResponseBody().write(contenido);

            } else {

                byte[] mensaje = "Pagina no encontrada".getBytes();

                intercambio.sendResponseHeaders(404, mensaje.length);

                intercambio.getResponseBody().write(mensaje);
            }

            intercambio.close();
        });

        servidor.start();

        System.out.println("Servidor iniciado en http://localhost:8000");
    }
}
