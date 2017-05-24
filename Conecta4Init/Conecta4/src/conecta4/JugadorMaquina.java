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
    public final static int NIVEL_DEFECTO = 8;
    
    public final static int MIN = 1;
    public final static int MAX = 2;

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
        int max = Integer.MIN_VALUE;
        int mejorColumna = -1;
        /* 
        Necesitamos generar la jugada raíz para poder hacer
        el cálculo de los sucesores.
        */
        for (int i = 0; i < m_tablero.numColumnas(); i++) {
            Tablero hijo = new Tablero(m_tablero);
            if (hijo.ponerFicha(i, MAX) == 0) {
                int hijoActual = minimaxRecursivo(hijo, MIN, 1);
                if (hijoActual > max) {
                    max = hijoActual;
                    mejorColumna = i;
                }
            }
        }
        
        int columna = mejorColumna;
        boolean buenaTirada = false;

        while(!buenaTirada)
        {
            if(m_tablero.comprobarColumna(columna)!=-1)
            {
                buenaTirada = true;
                m_columna = columna;
            }
            else
                columna = 6;
        }
     }
    
    private int minimaxRecursivo(Tablero tablero, int jugador, int profundidad) {
        if (!tablero.tableroLleno()) {
            if (tablero.cuatroEnRaya() == MAX)
                return Integer.MAX_VALUE;
            else if (tablero.cuatroEnRaya() == MIN)
                return Integer.MIN_VALUE;
            else if (profundidad == NIVEL_DEFECTO)
                return heuristica(tablero);
            else {
                for (int i = 0; i < tablero.numColumnas(); i++) {
                    Tablero hijo = new Tablero(tablero);
                    if (hijo.ponerFicha(i, jugador) == 0) {
                        if (jugador == MAX)
                            return Math.max(Integer.MIN_VALUE, minimaxRecursivo(hijo, MIN, profundidad + 1));
                        else
                            return Math.min(Integer.MAX_VALUE, minimaxRecursivo(hijo, MAX, profundidad + 1));
                    }
                }
                if (jugador == MAX)
                    return Integer.MIN_VALUE;
                else
                    return Integer.MAX_VALUE;
            }
        }
        else {
            return 0;
        }
    }

    
    private int heuristica(Tablero tablero) {
        int total = 0, jugador = 0;
        
        if (tablero.obtenerCasilla(0, 3) == MIN) {
            if (tablero.obtenerCasilla(1, 3) == MAX)
                total = 2;
            if (tablero.obtenerCasilla(3, 3) == MAX)
                if (tablero.obtenerCasilla(0, 2) == MAX)
                    total = 5;
                else
                    total = 4;
        }
        else if (tablero.obtenerCasilla(0, 3) == MAX) {
            total = 3;
        }
        
        for (int i = 0; i < tablero.numFilas(); i++) {
            for (int j = 0; j < tablero.numColumnas(); j++) {
                int puntos = 0;
                if (tablero.obtenerCasilla(i, j) == MAX) {
                    puntos += comprobarHorizontal(i, j, jugador, tablero);
                    puntos += comprobarVertical(i, j, jugador, tablero);
                    puntos += comprobarDiagonal(i, j, jugador, tablero);
                    total += puntos;
                }
            }
        }
        return total;
    }
    
    private int comprobarHorizontal(int i, int j, int jugador, Tablero tablero) {
        int contador = 0;
        if (j + 3 < tablero.numColumnas()) {
            if (tablero.obtenerCasilla(i, j+1) == jugador &&
                tablero.obtenerCasilla(i, j+2) == jugador &&
                tablero.obtenerCasilla(i, j+3) == 0) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i, j+1) == jugador &&
                tablero.obtenerCasilla(i, j+2) == 0 &&
                tablero.obtenerCasilla(i, j+3) == jugador) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i, j+1) == 0 &&
                tablero.obtenerCasilla(i, j+2) == jugador &&
                tablero.obtenerCasilla(i, j+3) == jugador) {
                contador += 3;
            }
        }
        if (j + 2 < tablero.numColumnas()) {
            if (tablero.obtenerCasilla(i, j+1) == jugador &&
                tablero.obtenerCasilla(i, j+2) == jugador) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i, j+1) == jugador &&
                tablero.obtenerCasilla(i, j+2) == 0) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i, j+1) == 0 &&
                tablero.obtenerCasilla(i, j+2) == jugador) {
                contador += 2;
            }
        }
        
        if (j + 1 < tablero.numColumnas()) {
            if (tablero.obtenerCasilla(i, j+1) == 0) {
                contador += 1;
            }
            if (tablero.obtenerCasilla(i, j+1) == jugador) {
                contador += 1;
            }
        }
        return 0;
    }
    
    private int comprobarVertical(int i, int j, int jugador, Tablero tablero) {
        int contador = 0;
        if (i + 3 < tablero.numFilas()) {
            if (tablero.obtenerCasilla(i+1, j) == jugador &&
                tablero.obtenerCasilla(i+2, j) == jugador &&
                tablero.obtenerCasilla(i+3, j) == 0) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i+1, j) == jugador &&
                tablero.obtenerCasilla(i+2, j) == 0 &&
                tablero.obtenerCasilla(i+3, j) == jugador) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i+1, j) == 0 &&
                tablero.obtenerCasilla(i+2, j) == jugador &&
                tablero.obtenerCasilla(i+3, j) == jugador) {
                contador += 3;
            }
        }
        if (i + 2 < tablero.numFilas()) {
            if (tablero.obtenerCasilla(i+1, j) == jugador &&
                tablero.obtenerCasilla(i+2, j) == jugador) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i+1, j) == jugador &&
                tablero.obtenerCasilla(i+2, j) == 0) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i+1, j) == 0 &&
                tablero.obtenerCasilla(i+2, j) == jugador) {
                contador += 2;
            }
        }
        
        if (i + 1 < tablero.numFilas()) {
            if (tablero.obtenerCasilla(i+1, j) == 0) {
                contador += 1;
            }
            if (tablero.obtenerCasilla(i+1, j) == jugador) {
                contador += 1;
            }
        }
        return 0;
    }
    
    private int comprobarDiagonal(int i, int j, int jugador, Tablero tablero) {
        int contador = 0;
        if (i + 3 < tablero.numFilas() && j + 3 < tablero.numColumnas()) {
            if (tablero.obtenerCasilla(i+1, j+1) == jugador &&
                tablero.obtenerCasilla(i+2, j+2) == jugador &&
                tablero.obtenerCasilla(i+3, j+3) == 0) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i+1, j+1) == jugador &&
                tablero.obtenerCasilla(i+2, j+2) == 0 &&
                tablero.obtenerCasilla(i+3, j+3) == jugador) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i+1, j+1) == 0 &&
                tablero.obtenerCasilla(i+2, j+2) == jugador &&
                tablero.obtenerCasilla(i+3, j+3) == jugador) {
                contador += 3;
            }
        }
        if (i + 2 < tablero.numFilas() && j + 2 < tablero.numColumnas()) {
            if (tablero.obtenerCasilla(i+1, j+1) == jugador &&
                tablero.obtenerCasilla(i+2, j+2) == jugador) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i+1, j+1) == jugador &&
                tablero.obtenerCasilla(i+2, j+2) == 0) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i+1, j+1) == 0 &&
                tablero.obtenerCasilla(i+2, j+2) == jugador) {
                contador += 2;
            }
        }
        
        if (i + 1 < tablero.numFilas() && j + 1 < tablero.numColumnas()) {
            if (tablero.obtenerCasilla(i+1, j+1) == 0) {
                contador += 1;
            }
            if (tablero.obtenerCasilla(i+1, j+1) == jugador) {
                contador += 1;
            }
        }
        return 0;
    }
    
}

