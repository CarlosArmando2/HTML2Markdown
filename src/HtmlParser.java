
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class HtmlParser {

    public static class HtmlNode {

        String tag;
        String text;
        Map<String, String> attributes;
        List<HtmlNode> children;

        public HtmlNode(String tag, String text) {
            this.tag = tag;
            this.text = text;
            this.attributes = new LinkedHashMap<>();
            this.children = new ArrayList<>();
        }

        public String getTag() {
            return tag;
        }

        public String getText() {
            return text;
        }

        public Map<String, String> getAttributes() {
            return attributes;
        }

        public List<HtmlNode> getChildren() {
            return children;
        }
    }

    public HtmlNode analizarHTML(String html) throws IOException {

        HtmlNode raiz = new HtmlNode("document", "");
        Deque<HtmlNode> pila = new ArrayDeque<>();

        pila.push(raiz);

        ParserDelegator parser = new ParserDelegator();

        HTMLEditorKit.ParserCallback callback =
                new HTMLEditorKit.ParserCallback() {

            @Override
            public void handleStartTag(
                    HTML.Tag etiqueta,
                    MutableAttributeSet atributos,
                    int posicion) {

                String nombre = etiqueta.toString().toLowerCase();

                HtmlNode nodo = new HtmlNode(nombre, "");

                agregarAtributos(nodo, atributos);

                pila.peek().children.add(nodo);

                pila.push(nodo);
            }

            @Override
            public void handleEndTag(HTML.Tag etiqueta, int posicion) {

                String nombre = etiqueta.toString().toLowerCase();

                while (pila.size() > 1) {

                    HtmlNode actual = pila.pop();

                    if (actual.tag.equals(nombre)) {
                        break;
                    }
                }
            }

            @Override
            public void handleSimpleTag(
                    HTML.Tag etiqueta,
                    MutableAttributeSet atributos,
                    int posicion) {

                String nombre = etiqueta.toString().toLowerCase();

                HtmlNode nodo = new HtmlNode(nombre, "");

                agregarAtributos(nodo, atributos);

                pila.peek().children.add(nodo);
            }

            @Override
            public void handleText(char[] datos, int posicion) {

                String texto = new String(datos);

                HtmlNode nodo = new HtmlNode("#text", texto);

                pila.peek().children.add(nodo);
            }

            private void agregarAtributos(
                    HtmlNode nodo,
                    MutableAttributeSet atributos) {

                Enumeration<?> nombres = atributos.getAttributeNames();

                while (nombres.hasMoreElements()) {

                    Object nombre = nombres.nextElement();

                    Object valor = atributos.getAttribute(nombre);

                    if (valor != null) {
                        nodo.attributes.put(
                                nombre.toString().toLowerCase(),
                                valor.toString()
                        );
                    }
                }
            }
        };

        parser.parse(new StringReader(html), callback, true);

        return raiz;
    }
}
