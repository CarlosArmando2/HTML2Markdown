
import java.net.URL;
import java.util.Map;

public class MarkdownConverter {

    public String convertir(HtmlParser.HtmlNode raiz, URL urlBase) {

        StringBuilder resultado = new StringBuilder();

        convertirBloques(raiz, resultado, urlBase);

        return limpiarMarkdown(resultado.toString());
    }

    private void convertirBloques(
            HtmlParser.HtmlNode nodo,
            StringBuilder resultado,
            URL urlBase) {

        for (HtmlParser.HtmlNode hijo : nodo.getChildren()) {

            String etiqueta = hijo.getTag();

            switch (etiqueta) {

                case "script":
                case "style":
                case "head":
                case "meta":
                case "link":
                    break;

                case "h1":
                case "h2":
                case "h3":
                case "h4":
                case "h5":
                case "h6":

                    int nivel = Integer.parseInt(
                            etiqueta.substring(1)
                    );

                    agregarBloque(
                            resultado,
                            "#".repeat(nivel) + " " +
                                    convertirTexto(hijo, urlBase)
                    );
                    break;

                case "p":

                    agregarBloque(
                            resultado,
                            convertirTexto(hijo, urlBase)
                    );
                    break;

                case "ul":
                    agregarBloque(
                            resultado,
                            convertirLista(hijo, urlBase, 0, false)
                    );
                    break;

                case "ol":
                    agregarBloque(
                            resultado,
                            convertirLista(hijo, urlBase, 0, true)
                    );
                    break;

                case "pre":

                    String codigo = obtenerTextoPlano(hijo);

                    agregarBloque(
                            resultado,
                            "```\n" + codigo.strip() + "\n```"
                    );
                    break;

                case "blockquote":

                    String cita = convertirTexto(hijo, urlBase);

                    StringBuilder textoCita = new StringBuilder();

                    for (String linea : cita.split("\n")) {

                        if (!textoCita.isEmpty()) {
                            textoCita.append("\n");
                        }

                        textoCita.append("> ").append(linea);
                    }

                    agregarBloque(
                            resultado,
                            textoCita.toString()
                    );
                    break;

                case "hr":

                    agregarBloque(resultado, "---");
                    break;

                case "br":

                    agregarBloque(resultado, "");
                    break;

                case "div":
                case "section":
                case "article":
                case "main":
                case "body":
                case "html":
                case "document":
                case "header":
                case "footer":
                case "nav":
                case "aside":

                    convertirBloques(hijo, resultado, urlBase);
                    break;

                default:

                    String texto = convertirTexto(hijo, urlBase);

                    if (!texto.isBlank()) {
                        agregarBloque(resultado, texto);
                    }
                    break;
            }
        }
    }

    private String convertirTexto(
            HtmlParser.HtmlNode nodo,
            URL urlBase) {

        StringBuilder resultado = new StringBuilder();

        for (HtmlParser.HtmlNode hijo : nodo.getChildren()) {

            String etiqueta = hijo.getTag();

            switch (etiqueta) {

                case "#text":

                    resultado.append(
                            hijo.getText().replaceAll("\\s+", " ")
                    );
                    break;

                case "script":
                case "style":
                    break;

                case "strong":
                case "b":

                    resultado.append("**")
                            .append(convertirTexto(hijo, urlBase))
                            .append("**");
                    break;

                case "em":
                case "i":

                    resultado.append("*")
                            .append(convertirTexto(hijo, urlBase))
                            .append("*");
                    break;

                case "code":

                    resultado.append("`")
                            .append(
                                    convertirTexto(hijo, urlBase).strip()
                            )
                            .append("`");
                    break;

                case "a":

                    String enlace = obtenerAtributo(hijo, "href");

                    String contenidoEnlace =
                            convertirTexto(hijo, urlBase);

                    if (!enlace.isBlank()) {

                        resultado.append("[")
                                .append(contenidoEnlace)
                                .append("](")
                                .append(resolverURL(enlace, urlBase))
                                .append(")");

                    } else {

                        resultado.append(contenidoEnlace);
                    }
                    break;

                case "img":

                    String imagen = obtenerAtributo(hijo, "src");
                    String alt = obtenerAtributo(hijo, "alt");

                    if (!imagen.isBlank()) {

                        resultado.append("![")
                                .append(alt)
                                .append("](")
                                .append(resolverURL(imagen, urlBase))
                                .append(")");
                    }
                    break;

                case "br":

                    resultado.append("\n");
                    break;

                default:

                    resultado.append(
                            convertirTexto(hijo, urlBase)
                    );
                    break;
            }
        }

        return resultado.toString();
    }

    private String convertirLista(
            HtmlParser.HtmlNode lista,
            URL urlBase,
            int nivel,
            boolean ordenada) {

        StringBuilder resultado = new StringBuilder();

        int numero = 1;

        for (HtmlParser.HtmlNode elemento : lista.getChildren()) {

            if (!elemento.getTag().equals("li")) {
                continue;
            }

            StringBuilder contenido = new StringBuilder();
            StringBuilder sublistas = new StringBuilder();

            for (HtmlParser.HtmlNode hijo : elemento.getChildren()) {

                if (hijo.getTag().equals("ul") ||
                        hijo.getTag().equals("ol")) {

                    boolean subListaOrdenada =
                            hijo.getTag().equals("ol");

                    sublistas.append(
                            convertirLista(
                                    hijo,
                                    urlBase,
                                    nivel + 1,
                                    subListaOrdenada
                            )
                    );

                } else {

                    contenido.append(
                            convertirTexto(hijo, urlBase)
                    );
                }
            }

            String espacios = "    ".repeat(nivel);

            String marcador;

            if (ordenada) {
                marcador = numero + ". ";
            } else {
                marcador = "- ";
            }

            resultado.append(espacios)
                    .append(marcador)
                    .append(contenido.toString().strip())
                    .append("\n");

            resultado.append(sublistas);

            numero++;
        }

        return resultado.toString().stripTrailing();
    }

    private String obtenerTextoPlano(HtmlParser.HtmlNode nodo) {

        StringBuilder resultado = new StringBuilder();

        for (HtmlParser.HtmlNode hijo : nodo.getChildren()) {

            if (hijo.getTag().equals("#text")) {

                resultado.append(hijo.getText());

            } else {

                resultado.append(obtenerTextoPlano(hijo));
            }
        }

        return resultado.toString();
    }

    private String obtenerAtributo(
            HtmlParser.HtmlNode nodo,
            String nombre) {

        Map<String, String> atributos = nodo.getAttributes();

        return atributos.getOrDefault(nombre, "");
    }

    private String resolverURL(String referencia, URL urlBase) {

        try {

            URL resultado = new URL(urlBase, referencia);

            return resultado.toExternalForm()
                    .replace(" ", "%20");

        } catch (Exception e) {

            return referencia.replace(" ", "%20");
        }
    }

    private void agregarBloque(
            StringBuilder resultado,
            String contenido) {

        if (contenido == null || contenido.isBlank()) {
            return;
        }

        if (!resultado.isEmpty()) {
            resultado.append("\n\n");
        }

        resultado.append(contenido.strip());
    }

    private String limpiarMarkdown(String markdown) {

        return markdown.strip()
                .replaceAll("[ \\t]+\\n", "\n")
                .replaceAll("\\n{3,}", "\n\n");
    }
}
