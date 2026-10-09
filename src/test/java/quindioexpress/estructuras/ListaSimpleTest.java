package quindioexpress.estructuras;
import org.junit.jupiter.api.Test; import static org.junit.jupiter.api.Assertions.*;
class ListaSimpleTest { @Test void listaNuevaDebeEstarVacia(){ ListaSimple<String> lista=new ListaSimple<>(); assertTrue(lista.estaVacia()); assertEquals(0,lista.tamano()); } }
