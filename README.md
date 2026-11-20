# HTML2Markdown

Aplicacion desarrollada en Java que permite descargar documentos HTML desde una URL HTTP o HTTPS, analizar su estructura y convertir su contenido a formato Markdown (`.md`).

## 1. Descripcion del proyecto

El objetivo de este proyecto es desarrollar una herramienta que facilite la conversion de paginas web HTML a archivos Markdown, conservando elementos importantes como encabezados, parrafos, enlaces, imagenes, listas y formatos de texto.

La aplicacion utiliza exclusivamente clases de la API estandar de Java, sin depender de bibliotecas externas para descargar o convertir documentos.

## 2. Tecnologias utilizadas

- **Java 17 o superior:** lenguaje de programacion.
- **URL y URLConnection:** conexion con paginas web.
- **HttpURLConnection:** comunicacion HTTP.
- **ParserDelegator:** analisis de documentos HTML.
- **HTMLEditorKit.ParserCallback:** procesamiento de los elementos HTML.
- **Files y Path:** creacion y escritura de archivos.
- **StandardCharsets.UTF_8:** codificacion de caracteres.
- **Visual Studio Code:** entorno de desarrollo.
- **Git y GitHub:** control de versiones y almacenamiento del proyecto.

## 3. Estructura del proyecto

```text
HTML2Markdown/
│
├── output/
│   └── documento_completo.md
│
├── src/
│   ├── HTML2Markdown.java
│   ├── WebClient.java
│   ├── HtmlParser.java
│   ├── MarkdownConverter.java
│   ├── MarkdownWriter.java
│   └── ServidorPrueba.java
│
├── tests/
│   └── documento_completo.html
│
├── .gitignore
└── README.md
```

### Responsabilidad de cada clase

| Clase | Responsabilidad |
|---|---|
| `HTML2Markdown` | Recibe los argumentos y coordina el proceso. |
| `WebClient` | Descarga el documento HTML mediante HTTP. |
| `HtmlParser` | Analiza la estructura del documento HTML. |
| `MarkdownConverter` | Convierte los elementos HTML a Markdown. |
| `MarkdownWriter` | Guarda el resultado en un archivo `.md`. |
| `ServidorPrueba` | Permite probar la aplicacion utilizando un servidor HTTP local. |

## 4. Requisitos funcionales

La aplicacion contempla los siguientes requisitos:

- Recibir una URL HTTP o HTTPS como argumento.
- Permitir especificar una ruta de salida opcional.
- Descargar el documento HTML.
- Verificar el codigo de respuesta HTTP.
- Comprobar que el contenido recibido sea HTML.
- Identificar la codificacion de caracteres.
- Analizar los elementos del documento.
- Convertir los elementos HTML a Markdown.
- Resolver referencias relativas de enlaces e imagenes.
- Excluir el contenido de las etiquetas `script` y `style`.
- Guardar el resultado utilizando codificacion UTF-8.
- Informar el resultado de la operacion y el tamaño del archivo generado.

## 5. Reglas de conversion

La aplicacion utiliza las siguientes reglas para transformar los elementos HTML:

| Elemento HTML | Resultado Markdown |
|---|---|
| `<h1>` | `# Encabezado` |
| `<h2>` | `## Encabezado` |
| `<h3>` | `### Encabezado` |
| `<h4>` | `#### Encabezado` |
| `<h5>` | `##### Encabezado` |
| `<h6>` | `###### Encabezado` |
| `<p>` | Parrafo separado |
| `<strong>` y `<b>` | `**texto**` |
| `<em>` y `<i>` | `*texto*` |
| `<a>` | `[texto](URL)` |
| `<img>` | `![alt](URL)` |
| `<ul>` y `<li>` | Lista con viñetas |
| `<ol>` y `<li>` | Lista numerada |
| `<br>` | Salto de linea |
| `<blockquote>` | Cita con `>` |
| `<code>` | Codigo en linea |
| `<pre>` | Bloque de codigo |
| `<hr>` | Separador `---` |
| `<script>` y `<style>` | Se omite su contenido |

## 6. Instalacion y configuracion

### Requisitos previos

1. Tener instalado Java JDK 17 o superior.
2. Tener instalado Visual Studio Code.
3. Abrir la carpeta del proyecto en Visual Studio Code.

Para comprobar la instalacion de Java, ejecutar en la terminal:

```bash
java -version
javac -version
```

### Compilacion

Desde la carpeta principal del proyecto, ejecutar:

```bash
javac -encoding UTF-8 -d bin src/*.java
```

Este comando compila los archivos Java y coloca los archivos `.class` dentro de la carpeta `bin`.

## 7. Ejecucion

La aplicacion recibe una URL como argumento obligatorio y permite indicar una ruta de salida opcional.

### Ejemplo con una pagina web

```bash
java -cp bin HTML2Markdown https://ejemplo.org/articulo.html
```

### Ejemplo indicando el archivo de salida

```bash
java -cp bin HTML2Markdown https://ejemplo.org/articulo.html salida.md
```

### Ejemplo utilizando el servidor local de pruebas

Primero, compilar el servidor:

```bash
javac --add-modules jdk.httpserver -encoding UTF-8 -d bin src/ServidorPrueba.java
```

Despues, iniciarlo:

```bash
java --add-modules jdk.httpserver -cp bin ServidorPrueba
```

Con el servidor iniciado, abrir otra terminal y ejecutar:

```bash
java -cp bin HTML2Markdown http://localhost:8000/documento_completo.html
```

El archivo Markdown generado se encontrara dentro de `output/`.

## 8. Ejemplo de conversion

### HTML de entrada

```html
<h1>Introduccion a Java</h1>

<p>Java es un lenguaje
<strong>orientado a objetos</strong>.</p>

<h2>Caracteristicas</h2>

<ul>
    <li>Portabilidad</li>
    <li>Seguridad</li>
</ul>

<p>Visita la
<a href="https://dev.java/">documentacion</a>.</p>
```

### Markdown esperado

```markdown
# Introduccion a Java

Java es un lenguaje **orientado a objetos**.

## Caracteristicas

- Portabilidad
- Seguridad

Visita la [documentacion](https://dev.java/).
```

## 9. Pruebas de funcionamiento

Se utiliza el archivo `tests/documento_completo.html` para comprobar la conversion de diferentes elementos HTML mediante un servidor local.

Los casos de prueba definidos para el proyecto son:

## Pruebas realizadas

Todas las pruebas se ejecutaron correctamente.

| Prueba | Descripcion | Resultado |
|---|---|---|
| P01 | Ejecucion sin argumentos | Correcta |
| P02 | URL invalida | Correcta |
| P03 | Pagina no encontrada (HTTP 404) | Correcta |
| P04 | Conversion de encabezados | Correcta |
| P05 | Negritas y cursivas | Correcta |
| P06 | Conversion de enlaces | Correcta |
| P07 | Conversion de imagenes | Correcta |
| P08 | Listas ordenadas y no ordenadas | Correcta |
| P09 | Listas anidadas | Correcta |
| P10 | Ignorar scripts y estilos | Correcta |
| P11 | Caracteres especiales y acentos | Correcta |
| P12 | Documento HTML vacio | Correcta |


**Evidencia de funcionamiento:** la conversion del documento `documento_completo.html` mediante el servidor local fue ejecutada correctamente y genero un archivo Markdown.

Se recomienda agregar capturas de pantalla de la ejecucion y del contenido generado para documentar las pruebas.

## 10. Manejo de errores y restricciones

La aplicacion contempla situaciones como:

- URL invalida.
- Error de conexion con el servidor.
- Respuesta HTTP no exitosa.
- Contenido que no corresponde a HTML.
- Problemas durante la lectura del documento.
- Errores al guardar el archivo.

Tambien se establecen tiempos maximos de conexion y lectura, asi como un limite de tamaño para el documento descargado.

### Restricciones tecnicas

- Se utilizan exclusivamente clases estandar de Java.
- No se utilizan bibliotecas externas como Jsoup, flexmark o Apache HttpClient.
- No se ejecuta JavaScript.
- No se descargan imagenes ni recursos adicionales.
- No se recorren enlaces hacia otras paginas.
- La aplicacion trabaja con un subconjunto definido de elementos HTML.

## 11. Preguntas de reflexion

### 1. ¿Que papel desempeña la aplicacion dentro de una arquitectura cliente-servidor?

La aplicacion funciona como un cliente HTTP. Su responsabilidad es solicitar un documento HTML a un servidor web, recibir su contenido y procesarlo localmente para generar un archivo Markdown.

### 2. ¿Que responsabilidades corresponden al servidor y cuales al cliente?

El servidor se encarga de almacenar y proporcionar el documento HTML mediante HTTP. El cliente solicita ese documento, verifica la respuesta, analiza su contenido, realiza la conversion y guarda el resultado en el equipo local.

### 3. ¿Que diferencia existe entre recuperar HTML e interpretar su estructura?

Recuperar HTML consiste en descargar el contenido del documento como texto. Interpretar su estructura implica identificar sus etiquetas, atributos, elementos y relaciones para comprender como esta organizado.

### 4. ¿Por que no basta reemplazar etiquetas HTML con expresiones regulares?

Porque HTML permite estructuras anidadas y elementos que pueden aparecer en diferentes combinaciones. Reemplazar etiquetas directamente puede provocar errores y perder la organizacion del documento. Por eso se utiliza un analizador HTML.

### 5. ¿Que dificultades presentan los elementos anidados?

Los elementos anidados requieren conservar correctamente el orden y la relacion entre sus etiquetas. Por ejemplo, un enlace puede contener texto en negritas, por lo que es necesario mantener ambos formatos durante la conversion.

### 6. ¿Que informacion puede perderse al convertir HTML a Markdown?

Pueden perderse estilos visuales, colores, tamaños de fuente, posiciones, animaciones, interacciones y otros elementos propios del diseño web. Markdown se enfoca principalmente en conservar la estructura y el contenido textual.

### 7. ¿Por que deben resolverse las URL relativas?

Porque una URL relativa depende de la ubicacion del documento HTML original. Al convertirla en un archivo Markdown independiente, es necesario transformarla en una referencia valida para que los enlaces y las imagenes sigan apuntando al recurso correcto.

### 8. ¿Que ventajas ofrece separar descarga, analisis y conversion en clases?

Permite organizar el codigo y asignar responsabilidades especificas a cada componente. Esto facilita encontrar errores, realizar modificaciones, reutilizar funcionalidades y mantener el proyecto.

### 9. ¿Que limitaciones presenta ParserDelegator con HTML moderno?

ParserDelegator es un analizador heredado que se basa principalmente en HTML 3.2. Por esta razon, no garantiza interpretar correctamente todos los elementos y caracteristicas de HTML5 moderno.

### 10. ¿Que modificaciones serian necesarias para convertir tablas HTML a Markdown?

Seria necesario identificar las etiquetas `table`, `tr`, `th` y `td`, organizar sus datos en filas y columnas y generar la sintaxis de tablas Markdown. Tambien habria que considerar las celdas combinadas y las tablas con estructuras complejas.

## 12. Conclusion

El desarrollo de HTML2Markdown permitio aplicar conocimientos de programacion cliente-servidor, comunicacion HTTP, analisis de documentos, procesamiento de texto y manejo de archivos en Java.

La separacion del programa en diferentes clases permite mantener una estructura organizada y facilita futuras modificaciones.

Aunque ParserDelegator presenta limitaciones al trabajar con documentos HTML5 modernos, permite implementar un conversor funcional utilizando unicamente herramientas incluidas en Java.

## 13. Control de versiones y entrega

El proyecto se administra mediante Git y se almacena en GitHub.

El repositorio debe incluir:

- Codigo fuente completo.
- Documentos HTML de prueba.
- Ejemplos representativos de salida Markdown.
- Instrucciones de compilacion y ejecucion.
- Este archivo README.
- Evidencias del funcionamiento.
- Historial de commits que muestre el desarrollo incremental.

**Repositorio GitHub:** [Agregar aqui el enlace del repositorio].
