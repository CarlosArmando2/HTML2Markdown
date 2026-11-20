
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public class HTML2Markdown {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       HTML2Markdown");
        System.out.println("================================");

       
        if (args.length < 1 || args.length > 2) {

            System.out.println("Uso:");
            System.out.println(
                    "java HTML2Markdown <URL> [archivo_salida.md]"
            );

            return;
        }

        try {

            
            URL url = new URL(args[0]);

            String protocolo = url.getProtocol();

            if (!protocolo.equalsIgnoreCase("http") &&
                    !protocolo.equalsIgnoreCase("https")) {

                throw new IllegalArgumentException(
                        "Solo se permiten direcciones HTTP o HTTPS."
                );
            }

            System.out.println("URL: " + url);
            System.out.println();

            
            Path destino;

            if (args.length == 2) {

                destino = Path.of(args[1]);

            } else {

                String ruta = url.getPath();

                String nombre;

                if (ruta == null || ruta.isBlank() ||
                        ruta.endsWith("/")) {

                    nombre = "index.md";

                } else {

                    nombre = ruta.substring(
                            ruta.lastIndexOf('/') + 1
                    );

                    int punto = nombre.lastIndexOf('.');

                    if (punto > 0) {
                        nombre = nombre.substring(0, punto);
                    }

                    nombre += ".md";
                }

                destino = Path.of("output", nombre);
            }

          
            System.out.println("Conectando con el servidor...");

            WebClient cliente = new WebClient();

            String html = cliente.descargarHTML(url);

            System.out.println(
                    "Respuesta HTTP: " + cliente.getCodigoHTTP()
            );

            System.out.println(
                    "Tipo de contenido: " + cliente.getTipoContenido()
            );

            System.out.println();

            
            System.out.println("Analizando HTML...");

            HtmlParser parser = new HtmlParser();

            HtmlParser.HtmlNode raiz =
                    parser.analizarHTML(html);

            
            System.out.println("Convirtiendo a Markdown...");

            MarkdownConverter conversor = new MarkdownConverter();

            String markdown = conversor.convertir(raiz, url);

           
            System.out.println("Guardando archivo...");

            MarkdownWriter escritor = new MarkdownWriter();

            escritor.guardar(destino, markdown);

           
            long tamano = escritor.obtenerTamano(destino);

            System.out.println();
            System.out.println("================================");
            System.out.println("     CONVERSION FINALIZADA");
            System.out.println("================================");

            System.out.println(
                    "Archivo generado: " + destino.toAbsolutePath()
            );

            System.out.println("Tamaño: " + tamano + " bytes");

            System.out.println("================================");

        } catch (MalformedURLException e) {

            System.out.println("Error: La URL no es válida.");

        } catch (IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());

        } catch (IOException e) {

            System.out.println("Error durante la operación: " +
                    e.getMessage());

        } catch (Exception e) {

            System.out.println("Error inesperado: " +
                    e.getMessage());
        }
    }
}
