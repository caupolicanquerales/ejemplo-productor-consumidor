/*
    Clase encergada de ejecutar el "executor" requerido para inicial el
    POOL THREAD de los hilos que llevan a cabo la accion del productor consumidor.
 */

package Paquete_productor_consumidor;

import Paquete_almacena_informacion.Almacena_cadena_string;
import Paquete_almacena_informacion.Clase_para_guardar_ArrayList;
import Paquete_almacena_informacion.Clase_para_guardar_datos_numericos;
import Paquete_coordinar_acciones.Clase_para_coordinar_acciones;
import Paquete_de_comunicacion_serial.Clase_para_coordinar_configuracion_puerto_serial;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class ClaseEjecutoraSensorPuerto 
{
    public JButton boton;
    public int totalPuntos;// longitud de linea de codigo de sensor de posicion
    public int longitudCodigoBlocking=1;
    public String[] informacion;// Arreglo para los valores guardados en la clase encargada "ALMACENA NUMEROS".
    public int[] cabezal_principal;
    public int[] valores_guardados;//arreglo que contiene los datos de los JTEXTFIELD
    public JTextArea jtext;
    
    
    Clase_para_coordinar_configuracion_puerto_serial configuracion_puerto;
    Almacena_cadena_string almacena_string;
    Clase_para_guardar_datos_numericos almacena_numeros;
    Clase_para_guardar_ArrayList guardar_ArrayList;
    Clase_para_coordinar_acciones coordinar_acciones;
    
    ExecutorService aplicacionUbicacion;// = Executors.newCachedThreadPool();
    
    public ClaseEjecutoraSensorPuerto()
    {
    }
    
    public void metodo_para_pasar_objetos(Clase_para_coordinar_configuracion_puerto_serial configuracion_puerto,
            Almacena_cadena_string almacena_string,
            Clase_para_guardar_datos_numericos almacena_numeros,
            Clase_para_guardar_ArrayList guardar_ArrayList,
            Clase_para_coordinar_acciones coordinar_acciones)
    {
        this.configuracion_puerto=configuracion_puerto;
        this.almacena_string=almacena_string;
        this.almacena_numeros=almacena_numeros;
        this.guardar_ArrayList=guardar_ArrayList;
        this.coordinar_acciones=coordinar_acciones;
    }
    
    
    public void metodo_para_pasar_referencia_boton(JButton boton)
    {
        this.boton= boton;
    }
    
    public void metodo_para_ordenar_productor_consumidor()
    {
            ////////////////////////////////////////////////////////////////////////
        aplicacionUbicacion = Executors.newCachedThreadPool();
        Buffer_puerto datosCompartidos2 = new ClaseBlockingQueue_puerto(longitudCodigoBlocking);
        aplicacionUbicacion.execute(new DataCaptura_puerto(configuracion_puerto,datosCompartidos2,
        almacena_numeros,longitudCodigoBlocking));
        aplicacionUbicacion.execute(new AnalisisDatos_puerto(datosCompartidos2,
        configuracion_puerto,guardar_ArrayList,coordinar_acciones));
    }
    
     public void ejecutarStop() throws InterruptedException
    {
        try{
            aplicacionUbicacion.shutdownNow();
        }finally{
           // sPort2.close();
        }
    }
    
}//fin de l clase
