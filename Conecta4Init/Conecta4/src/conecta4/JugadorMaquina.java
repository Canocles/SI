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
    
    public final static int MIN = 1;
    public final static int MAX = 2;

    //Constructor
    public JugadorMaquina(int jugador) {
        super(jugador);
    }

    // Función que se ejecuta en el thread
    public void run() {
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
    public void minimax() {
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
                    mejorColumna = i;
                    max = hijoActual;
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
                columna =  (int) (Math.random()*m_tablero.numColumnas());
        }
     }
    
    private int minimaxRecursivo(Tablero tablero, int jugador, int profundidad) {
        int puntuacionActual = 0;
        if (!tablero.tableroLleno()) {
            if (tablero.cuatroEnRaya() == MAX) 
                return Integer.MAX_VALUE;
            else if (tablero.cuatroEnRaya() == MIN)
                return Integer.MIN_VALUE+1;
            else if (profundidad == NIVEL_DEFECTO)
                return heuristica(tablero);
            else {
                if (jugador == MAX)
                    puntuacionActual = Integer.MIN_VALUE;
                else
                    puntuacionActual = Integer.MAX_VALUE;
                for (int i = 0; i < tablero.numColumnas(); i++) {
                    Tablero hijo = new Tablero(tablero);
                    if (hijo.ponerFicha(i, jugador) == 0) {
                        if (jugador == MAX) 
                            puntuacionActual = Math.max(puntuacionActual, minimaxRecursivo(hijo, MIN, profundidad + 1));
                        else 
                            puntuacionActual = Math.min(puntuacionActual, minimaxRecursivo(hijo, MAX, profundidad + 1));
                    }
                }
                return puntuacionActual;
            }
        }
        else {
            return 0;
        }
    }
 
    private int heuristica(Tablero tablero) {
        int jugador, total = 0;
        if (jugadaDeLibroInicial(tablero)) {
            if (tablero.obtenerCasilla(0, 3) == MAX)
                total += 10; 
            else {
                 if (tablero.obtenerCasilla(0, 2) == MAX ||
                     tablero.obtenerCasilla(1, 3) == MIN ||
                     tablero.obtenerCasilla(0, 4) == MAX)
                     total += 7;
            }
        }
        else {
            for (int i = 0; i < tablero.numFilas(); i++) {
                for (int j = 0; j < tablero.numColumnas(); j++) {
                    int puntos = 0;
                    jugador = tablero.obtenerCasilla(i, j);
                    if (jugador == 0)
                        jugador = MAX;
                    puntos += comprobarHorizontalDerecha(i, j, jugador, tablero);
                    puntos += comprobarHorizontalIzquierda(i, j, jugador, tablero);
                    puntos += comprobarVerticalArriba(i, j, jugador, tablero);
                    puntos += comprobarVerticalAbajo(i, j, jugador, tablero);
                    puntos += comprobarDiagonalDerechaArriba(i, j, jugador, tablero);
                    puntos += comprobarDiagonalIzquierdaArriba(i, j, jugador, tablero);
                    puntos += comprobarDiagonalDerechaAbajo(i, j, jugador, tablero);
                    puntos += comprobarDiagonalIzquierdaAbajo(i, j, jugador, tablero);
                    if (tablero.obtenerCasilla(i, j) == 0)
                        puntos /= 4;
                    if (jugador == MIN)
                        puntos = -puntos;
                    total += puntos;
                }
            }
        }
        return total;
    }
    
    private int comprobarHorizontalDerecha(int i, int j, int jugador, Tablero tablero) {
        int contador = 0;
        if (j + 3 < tablero.numColumnas()) {
            // Comprobamos todas las posibilidades de 3 en raya.
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
            // Comprobamos todas las posibilidades de 2 en raya.
            if (tablero.obtenerCasilla(i, j+1) == jugador &&
                tablero.obtenerCasilla(i, j+2) == 0 &&
                tablero.obtenerCasilla(i, j+3) == 0) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i, j+1) == 0 &&
                tablero.obtenerCasilla(i, j+2) == 0 &&
                tablero.obtenerCasilla(i, j+3) == jugador) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i, j+1) == 0 &&
                tablero.obtenerCasilla(i, j+2) == jugador &&
                tablero.obtenerCasilla(i, j+3) == 0) {
                contador += 2;
            }
            // Comprobamos la posibilidad del 1 en raya
            if (tablero.obtenerCasilla(i, j+1) == 0 &&
                tablero.obtenerCasilla(i, j+2) == 0 &&
                tablero.obtenerCasilla(i, j+3) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 1;
            }
        }
        return contador;
    }
    
    private int comprobarHorizontalIzquierda(int i, int j, int jugador, Tablero tablero) {
        int contador = 0;
        if (j - 3 >= 0) {
            // Comprobamos todas las posibilidades de 3 en raya.
            if (tablero.obtenerCasilla(i, j-1) == jugador &&
                tablero.obtenerCasilla(i, j-2) == jugador &&
                tablero.obtenerCasilla(i, j-3) == 0) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i, j-1) == jugador &&
                tablero.obtenerCasilla(i, j-2) == 0 &&
                tablero.obtenerCasilla(i, j-3) == jugador) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i, j-1) == 0 &&
                tablero.obtenerCasilla(i, j-2) == jugador &&
                tablero.obtenerCasilla(i, j-3) == jugador) {
                contador += 3;
            }
            // Comprobamos todas las posibilidades de 2 en raya.
            if (tablero.obtenerCasilla(i, j-1) == jugador &&
                tablero.obtenerCasilla(i, j-2) == 0 &&
                tablero.obtenerCasilla(i, j-3) == 0) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i, j-1) == 0 &&
                tablero.obtenerCasilla(i, j-2) == 0 &&
                tablero.obtenerCasilla(i, j-3) == jugador) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i, j-1) == 0 &&
                tablero.obtenerCasilla(i, j-2) == jugador &&
                tablero.obtenerCasilla(i, j-3) == 0) {
                contador += 2;
            }
            // Comprobamos la posibilidad del 1 en raya
            if (tablero.obtenerCasilla(i, j-1) == 0 &&
                tablero.obtenerCasilla(i, j-2) == 0 &&
                tablero.obtenerCasilla(i, j-3) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 1;
            }
        }
        return contador;
    }
    
    private int comprobarVerticalArriba(int i, int j, int jugador, Tablero tablero) {
        int contador = 0;
        if (i + 3 < tablero.numFilas()) {
            // Comprobamos todas las posibilidades de 3 en raya.
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
            // Comprobamos todas las posibilidades de 2 en raya.
            if (tablero.obtenerCasilla(i+1, j) == 0 &&
                tablero.obtenerCasilla(i+2, j) == jugador &&
                tablero.obtenerCasilla(i+3, j) == 0) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i+1, j) == 0 &&
                tablero.obtenerCasilla(i+2, j) == 0 &&
                tablero.obtenerCasilla(i+3, j) == jugador) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i+1, j) == jugador &&
                tablero.obtenerCasilla(i+2, j) == 0 &&
                tablero.obtenerCasilla(i+3, j) == 0) {
                contador += 2;
            }
            // Comprobamos la posibilidad del 1 en raya
            if (tablero.obtenerCasilla(i+1, j) == 0 &&
                tablero.obtenerCasilla(i+2, j) == 0 &&
                tablero.obtenerCasilla(i+3, j) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 1;
            }
        }
        return contador;
    }
    
    private int comprobarVerticalAbajo(int i, int j, int jugador, Tablero tablero) {
        int contador = 0;
        if (i - 3 >= 0) {
            // Comprobamos todas las posibilidades de 3 en raya.
            if (tablero.obtenerCasilla(i-1, j) == jugador &&
                tablero.obtenerCasilla(i-2, j) == jugador &&
                tablero.obtenerCasilla(i-3, j) == 0) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i-1, j) == jugador &&
                tablero.obtenerCasilla(i-2, j) == 0 &&
                tablero.obtenerCasilla(i-3, j) == jugador) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i-1, j) == 0 &&
                tablero.obtenerCasilla(i-2, j) == jugador &&
                tablero.obtenerCasilla(i-3, j) == jugador) {
                contador += 3;
            }
            // Comprobamos todas las posibilidades de 2 en raya.
            if (tablero.obtenerCasilla(i-1, j) == 0 &&
                tablero.obtenerCasilla(i-2, j) == jugador &&
                tablero.obtenerCasilla(i-3, j) == 0) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i-1, j) == 0 &&
                tablero.obtenerCasilla(i-2, j) == 0 &&
                tablero.obtenerCasilla(i-3, j) == jugador) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i-1, j) == jugador &&
                tablero.obtenerCasilla(i-2, j) == 0 &&
                tablero.obtenerCasilla(i-3, j) == 0) {
                contador += 2;
            }
            // Comprobamos la posibilidad del 1 en raya
            if (tablero.obtenerCasilla(i-1, j) == 0 &&
                tablero.obtenerCasilla(i-2, j) == 0 &&
                tablero.obtenerCasilla(i-3, j) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 1;
            }
        }
        return contador;
    }
    
    private int comprobarDiagonalDerechaArriba(int i, int j, int jugador, Tablero tablero) {
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
            
            if (tablero.obtenerCasilla(i+1, j+1) == 0 &&
                tablero.obtenerCasilla(i+2, j+2) == 0 &&
                tablero.obtenerCasilla(i+3, j+3) == jugador) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i+1, j+1) == 0 &&
                tablero.obtenerCasilla(i+2, j+2) == jugador &&
                tablero.obtenerCasilla(i+3, j+3) == 0) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i+1, j+1) == jugador &&
                tablero.obtenerCasilla(i+2, j+2) == 0 &&
                tablero.obtenerCasilla(i+3, j+3) == 0) {
                contador += 2;
            }
            
            if (tablero.obtenerCasilla(i+1, j+1) == 0 &&
                tablero.obtenerCasilla(i+2, j+2) == 0 &&
                tablero.obtenerCasilla(i+3, j+3) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 1;
            }
        }
        return contador;
    }
    
    private int comprobarDiagonalIzquierdaArriba(int i, int j, int jugador, Tablero tablero) {
        int contador = 0;
        if (i + 3 < tablero.numFilas() && j - 3 >= 0) {
            if (tablero.obtenerCasilla(i+1, j-1) == jugador &&
                tablero.obtenerCasilla(i+2, j-2) == jugador &&
                tablero.obtenerCasilla(i+3, j-3) == 0) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i+1, j-1) == jugador &&
                tablero.obtenerCasilla(i+2, j-2) == 0 &&
                tablero.obtenerCasilla(i+3, j-3) == jugador) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i+1, j-1) == 0 &&
                tablero.obtenerCasilla(i+2, j-2) == jugador &&
                tablero.obtenerCasilla(i+3, j-3) == jugador) {
                contador += 3;
            }
            
            if (tablero.obtenerCasilla(i+1, j-1) == 0 &&
                tablero.obtenerCasilla(i+2, j-2) == 0 &&
                tablero.obtenerCasilla(i+3, j-3) == jugador) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i+1, j-1) == 0 &&
                tablero.obtenerCasilla(i+2, j-2) == jugador &&
                tablero.obtenerCasilla(i+3, j-3) == 0) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i+1, j-1) == jugador &&
                tablero.obtenerCasilla(i+2, j-2) == 0 &&
                tablero.obtenerCasilla(i+3, j-3) == 0) {
                contador += 2;
            }
            
            if (tablero.obtenerCasilla(i+1, j-1) == 0 &&
                tablero.obtenerCasilla(i+2, j-2) == 0 &&
                tablero.obtenerCasilla(i+3, j-3) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 1;
            }
        }
        return contador;
    }
    
    private int comprobarDiagonalDerechaAbajo(int i, int j, int jugador, Tablero tablero) {
        int contador = 0;
        if (i - 3 >= 0 && j + 3 < tablero.numColumnas()) {
            if (tablero.obtenerCasilla(i-1, j+1) == jugador &&
                tablero.obtenerCasilla(i-2, j+2) == jugador &&
                tablero.obtenerCasilla(i-3, j+3) == 0) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i-1, j+1) == jugador &&
                tablero.obtenerCasilla(i-2, j+2) == 0 &&
                tablero.obtenerCasilla(i-3, j+3) == jugador) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i-1, j+1) == 0 &&
                tablero.obtenerCasilla(i-2, j+2) == jugador &&
                tablero.obtenerCasilla(i-3, j+3) == jugador) {
                contador += 3;
            }
            
            if (tablero.obtenerCasilla(i-1, j+1) == 0 &&
                tablero.obtenerCasilla(i-2, j+2) == 0 &&
                tablero.obtenerCasilla(i-3, j+3) == jugador) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i-1, j+1) == 0 &&
                tablero.obtenerCasilla(i-2, j+2) == jugador &&
                tablero.obtenerCasilla(i-3, j+3) == 0) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i-1, j+1) == jugador &&
                tablero.obtenerCasilla(i-2, j+2) == 0 &&
                tablero.obtenerCasilla(i-3, j+3) == 0) {
                contador += 2;
            }
            
            if (tablero.obtenerCasilla(i-1, j+1) == 0 &&
                tablero.obtenerCasilla(i-2, j+2) == 0 &&
                tablero.obtenerCasilla(i-3, j+3) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 1;
            }
        }
        return contador;
    }
    
    private int comprobarDiagonalIzquierdaAbajo(int i, int j, int jugador, Tablero tablero) {
        int contador = 0;
        if (i - 3 >= 0 && j - 3 >= 0) {
            if (tablero.obtenerCasilla(i-1, j-1) == jugador &&
                tablero.obtenerCasilla(i-2, j-2) == jugador &&
                tablero.obtenerCasilla(i-3, j-3) == 0) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i-1, j-1) == jugador &&
                tablero.obtenerCasilla(i-2, j-2) == 0 &&
                tablero.obtenerCasilla(i-3, j-3) == jugador) {
                contador += 3;
            }
            if (tablero.obtenerCasilla(i-1, j-1) == 0 &&
                tablero.obtenerCasilla(i-2, j-2) == jugador &&
                tablero.obtenerCasilla(i-3, j-3) == jugador) {
                contador += 3;
            }
            
            if (tablero.obtenerCasilla(i-1, j-1) == 0 &&
                tablero.obtenerCasilla(i-2, j-2) == 0 &&
                tablero.obtenerCasilla(i-3, j-3) == jugador) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i-1, j-1) == 0 &&
                tablero.obtenerCasilla(i-2, j-2) == jugador &&
                tablero.obtenerCasilla(i-3, j-3) == 0) {
                contador += 2;
            }
            if (tablero.obtenerCasilla(i-1, j-1) == jugador &&
                tablero.obtenerCasilla(i-2, j-2) == 0 &&
                tablero.obtenerCasilla(i-3, j-3) == 0) {
                contador += 2;
            }
            
            if (tablero.obtenerCasilla(i-1, j-1) == 0 &&
                tablero.obtenerCasilla(i-2, j-2) == 0 &&
                tablero.obtenerCasilla(i-3, j-3) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 1;
            }
        }
        return contador;
    }
    
    private boolean jugadaDeLibroInicial(Tablero tablero) {
        int contador = 0;
        for (int i = 0; i < tablero.numFilas(); i++) {
            for (int j = 0; j < tablero.numColumnas(); j++) {
                if (tablero.obtenerCasilla(i, j) != 0)
                    contador++;
            }
        }
        if (contador <= 10)
            return true;
        return false;
    }
}

