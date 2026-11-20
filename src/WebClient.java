
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebClient {

    private static final int TIMEOUT_CONEXION = 10000;
    private static final int TIMEOUT_LECTURA = 15000;
    private static final int MAX_TAMANO = 5 * 1024 * 1024;

    private int codigoHTTP;
    private String tipoContenido;

    public String descargarHTML(URL url) throws IOException {

        URLConnection conexion = url.openConnection();

        conexion.setConnectTimeout(TIMEOUT_CONEXION);
        conexion.setReadTimeout(TIMEOUT_LECTURA);

        conexion.setRequestProperty("User-Agent", "HTML2Markdown/1.0");
        conexion.setRequestProperty("Accept", "text/html");

        if (!(conexion instanceof HttpURLConnection)) {
            throw new IOException("Solo se permiten conexiones HTTP o HTTPS.");
        }

        HttpURLConnection http = (HttpURLConnection) conexion;

        try {
            codigoHTTP = http.getResponseCode();

            if (codigoHTTP < 200 || codigoHTTP >= 300) {
                throw new IOException("Error HTTP: " + codigoHTTP);
            }

            tipoContenido = http.getContentType();

            if (tipoContenido == null ||
                    !tipoContenido.toLowerCase().contains("text/html")) {
                throw new IOException("El servidor no devolvio contenido HTML.");
            }

            Charset codificacion = obtenerCodificacion(tipoContenido);

            try (InputStream entrada = http.getInputStream();
                 ByteArrayOutputStream salida = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[4096];
                int bytesLeidos;
                int total = 0;

                while ((bytesLeidos = entrada.read(buffer)) != -1) {

                    total += bytesLeidos;

                    if (total > MAX_TAMANO) {
                        throw new IOException(
                                "El documento supera el limite de 5 MB."
                        );
                    }

                    salida.write(buffer, 0, bytesLeidos);
                }

                return salida.toString(codificacion);
            }

        } finally {
            http.disconnect();
        }
    }

    private Charset obtenerCodificacion(String tipoContenido) {

        Pattern patron = Pattern.compile(
                "charset\\s*=\\s*[\"']?([^;\"'\\s]+)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher coincidencia = patron.matcher(tipoContenido);

        if (coincidencia.find()) {
            try {
                return Charset.forName(coincidencia.group(1));
            } catch (Exception e) {
                System.out.println(
                        "Codificacion desconocida. Se utilizara UTF-8."
                );
            }
        }

        return StandardCharsets.UTF_8;
    }

    public int getCodigoHTTP() {
        return codigoHTTP;
    }

    public String getTipoContenido() {
        return tipoContenido;
    }
}
