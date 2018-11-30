package Paquete_productor_consumidor;

/*
 *Clase que implemente la interface Buffer para sobreescribir en los metodos
 * set() y get() creados para almacenar los valores en el buffer array.
NOTA: A la clase se le debe suministrar la dimension de lo datos a capturar
por el buffer. "totalTamaño".
 * ++++++++++++ ESTA ES LA CLASE BUFFER A COMPARTIR ++++++++++++++++++++++
 */
import java.util.concurrent.ArrayBlockingQueue;

public class ClaseBlockingQueue_puerto implements Buffer_puerto
{
    private final ArrayBlockingQueue<Integer> buffer;
    private int totalTamaño; //variable para pasar la dimension de el arreglo de datos a capturar.
    public int[] lectura; //arreglo cantidad de valores capturados.
    
    
    public ClaseBlockingQueue_puerto(int totalPuntos)
    {
        totalTamaño = totalPuntos;
        lectura = new int[totalTamaño]; //adimensiona el arreglo dependiendo de la resolucion.
        buffer = new ArrayBlockingQueue<Integer>(totalTamaño); //esto dimensiona el arreglo para 181 valores enteros.
    }
    
    public void set(int value) throws InterruptedException // con este método se introduce valor por valor al buffer que es un arreglo.
    {
        buffer.put(value); //aqui se coloca el valor capturado en el arreglo buffer.
    }
    
    public int[] get() throws InterruptedException //método que devuelve el arreglo completo una vez lleno.
    {
        for(int i=0;i<totalTamaño;i++)
        {
            lectura[i] = buffer.take(); //adimensiona el arreglo para pasar los valores del buffer. (aqui se puede presentar problemas)
            
            System.out.println(" datos leidos "+lectura[i]);
        }
            return lectura;
    }
}
