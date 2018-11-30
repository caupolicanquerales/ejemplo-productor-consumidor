package Paquete_productor_consumidor;

/*
 * Esta clase es una interface para permitir comunicar y sobreescribir
 * los metodos de captura y escritura del buffer.Herencia por interface.
 * +++++++ ESTA ES LA CLASE INTERFACE PARA CONECTAR LAS CLASSES POR HERENCIA MULTIPLE ++++++
 */

public interface Buffer_puerto 
{
    public void set(int value) throws InterruptedException;
    
    public int[] get() throws InterruptedException;
}
