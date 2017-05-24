/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package conecta4;

/**
 *
 * @author sistemas inteligentes
 */
public class JugadorMaquina extends Jugador{

    //Profundidad hasta la que se va a desarrollar el árbol de juego
    public final static int NIVEL_DEFECTO = 2;

    //Constructor
    public JugadorMaquina(int jugador)
    {
        super(jugador);
    }

    // Función que se ejecuta en el thread
    public void run()
    {
        //Llama a la función Minimax que implementa el algoritmo para calcular la jugada
        minimax();
        
        //No borrar esta línea!!
        isDone(true);
    }


    /**
     * Se debe determinar la mejor jugada mediante Minimax. El tablero de juego se
     * encuentra en la variable m_tablero.
     * Al final de la función de la variable m_columna debe contener la tirada.
     * @return
     */
    public void minimax()
    {

        //El siguiente código genera una jugada aleatoria
        //SE DEBE SUSTITUIR ESTE CÓDIGO POR EL DEL ALGORITMO Minimax
        boolean buenaTirada = false;
        int columna = -1;

        columna = (int) (Math.random()*m_tablero.numColumnas());

        while(!buenaTirada)
        {
            if(m_tablero.comprobarColumna(columna)!=-1)
            {
                buenaTirada = true;
                m_columna = columna;
            }
            else
                columna = (int) (Math.random()*m_tablero.numColumnas());
    
        }
     }
}

