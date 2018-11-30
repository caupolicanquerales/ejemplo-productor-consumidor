/*
 * Clase encargada de activar un TIMER contador de tiempo para desparar un evento
    una vez transcurrido el tiempo del TIMER.

NOTA:
    En esta clase solo se modificara el valor de la VARIABLE encargada para
    el chequeo de la ejecucion del TIMER.
 */

package Paquete_para_TIMER;

import Paquete_almacena_informacion.Clase_para_guardar_ArrayList;
import Paquete_coordinar_acciones.Clase_para_coordinar_acciones;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

public class Clase_para_TIMER extends TimerTask
{
    private int paso=0;
    public Timer tiempo;
    public ArrayList<Integer> lineaEnteros;
    
    Clase_para_guardar_ArrayList guardar_ArrayList;
    Clase_para_coordinar_acciones coordinar_acciones;
    
    public Clase_para_TIMER(Timer tiempo,ArrayList<Integer> lineaEnteros,
            Clase_para_guardar_ArrayList guardar_ArrayList,
            Clase_para_coordinar_acciones coordinar_acciones)
    {
        this.tiempo=tiempo;
        this.lineaEnteros=lineaEnteros;
        this.guardar_ArrayList=guardar_ArrayList;
        this.coordinar_acciones=coordinar_acciones;
    }
    
    public void run()
    {
        if(paso==0)
        {
            paso++;
            guardar_ArrayList.metodo_para_pasar_ArrayList_INTEGER(lineaEnteros);
            
        }else{
            guardar_ArrayList.metodo_para_pasar_ArrayList_INTEGER(lineaEnteros);
            
            coordinar_acciones.metodo_para_ejecutar_acciones();
            tiempo.cancel();
            tiempo.purge();
        }
    }
    
}//fin de la clase
