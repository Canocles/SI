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
    public static int NIVEL_DEFECTO = 7;
    
    public final static int MIN = 1;
    public final static int MAX = 2;
    
    public static int nodos;
    public static int mejorMaxValue;

    //Constructor
    public JugadorMaquina(int jugador) {
        super(jugador);
        nodos = 0;
        mejorMaxValue = NIVEL_DEFECTO;
    }

    // Función que se ejecuta en el thread
    public void run() {
        NIVEL_DEFECTO = Interfaz.nivel+1;
        System.out.println(NIVEL_DEFECTO);
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
        boolean jugadaLibro = false;
        int max = Integer.MIN_VALUE;
        int mejorColumna = -1;
        int hijoActual = 0;
        int profundidad = NIVEL_DEFECTO;
         /* 
        Necesitamos generar la jugada raíz para poder hacer
        el cálculo de los sucesores.
        */
        
        for (int i = 0; i < m_tablero.numColumnas() && !jugadaLibro; i++) {
            Tablero hijo = new Tablero(m_tablero);
            if (hijo.ponerFicha(i, MAX) == 0) {
                if (cuentaFichas(hijo) <= 3 && i == 3) {
                    mejorColumna = jugadaDeLibroInicial(hijo);
                    jugadaLibro = true;
                }
                else {
                    if (Interfaz.seleccion)
                        hijoActual = alfaBeta(hijo, MIN, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    else           
                        hijoActual = minimaxRecursivo(hijo, MIN, 1);
                    if (hijoActual == Integer.MAX_VALUE && mejorMaxValue < profundidad) {
                        mejorColumna = i;
                        max = hijoActual;
                        profundidad = mejorMaxValue;
                    }
                    else if (hijoActual > max) {
                        mejorColumna = i;
                        max = hijoActual;
                    }
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
        System.out.println("Se han generado " + nodos + " nodos" );
     }
    
    private int alfaBeta(Tablero tablero, int jugador, int profundidad, int alfa, int beta) {
        nodos++;
        int puntuacionActual = 0;
        if (!tablero.tableroLleno()) {
            if (tablero.cuatroEnRaya() == MAX) {
                if (profundidad == 1)
                    mejorMaxValue = profundidad;  
                return Integer.MAX_VALUE;
            }
            else if (tablero.cuatroEnRaya() == MIN)
                return Integer.MIN_VALUE;
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
                        if (jugador == MAX) {
                            alfa = Math.max(alfa, alfaBeta(hijo, MIN, profundidad + 1, alfa, beta));
                            if (alfa >= beta)
                                return beta;
                            puntuacionActual = alfa;
                        }
                        else {
                            beta = Math.min(beta, alfaBeta(hijo, MAX, profundidad + 1, alfa, beta));
                            if (alfa >= beta)
                                return alfa;
                            puntuacionActual = beta;
                        }
                    }
                }
                return puntuacionActual;
            }
        }
        else {
            return 0;
        }
    }
    
    private int minimaxRecursivo(Tablero tablero, int jugador, int profundidad) {
        nodos++;
        int puntuacionActual = 0;
        if (!tablero.tableroLleno()) {
            if (tablero.cuatroEnRaya() == MAX) {
                if (profundidad == 1)
                    mejorMaxValue = profundidad;  
                return Integer.MAX_VALUE;
            }
            else if (tablero.cuatroEnRaya() == MIN)
                return Integer.MIN_VALUE;
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
        
        for (int i = 0; i < tablero.numFilas(); i++) {
            for (int j = 0; j < tablero.numColumnas(); j++) {
                int puntos = 0;
                jugador = tablero.obtenerCasilla(i, j);
                if (jugador != 0) {
                    puntos += comprobarHorizontalDerecha(i, j, jugador, tablero);
                    //puntos += comprobarHorizontalIzquierda(i, j, jugador, tablero);
                    puntos += comprobarVerticalArriba(i, j, jugador, tablero);
                    //puntos += comprobarVerticalAbajo(i, j, jugador, tablero);
                    puntos += comprobarDiagonalDerechaArriba(i, j, jugador, tablero);
                    puntos += comprobarDiagonalIzquierdaArriba(i, j, jugador, tablero);
                    puntos += comprobarDiagonalDerechaAbajo(i, j, jugador, tablero);
                    puntos += comprobarDiagonalIzquierdaAbajo(i, j, jugador, tablero);
                }
                if (jugador == MIN)
                    puntos = -puntos;
                total = total + puntos;
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
                contador += 20;
            }
            if (tablero.obtenerCasilla(i, j+1) == jugador &&
                tablero.obtenerCasilla(i, j+2) == 0 &&
                tablero.obtenerCasilla(i, j+3) == jugador) {
                contador += 20;
            }
            if (tablero.obtenerCasilla(i, j+1) == 0 &&
                tablero.obtenerCasilla(i, j+2) == jugador &&
                tablero.obtenerCasilla(i, j+3) == jugador) {
                contador += 20;
            }
            // Comprobamos todas las posibilidades de 2 en raya.
            if (tablero.obtenerCasilla(i, j+1) == jugador &&
                tablero.obtenerCasilla(i, j+2) == 0 &&
                tablero.obtenerCasilla(i, j+3) == 0) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i, j+1) == 0 &&
                tablero.obtenerCasilla(i, j+2) == 0 &&
                tablero.obtenerCasilla(i, j+3) == jugador) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i, j+1) == 0 &&
                tablero.obtenerCasilla(i, j+2) == jugador &&
                tablero.obtenerCasilla(i, j+3) == 0) {
                contador += 10;
            }
            // Comprobamos la posibilidad del 1 en raya
            if (tablero.obtenerCasilla(i, j+1) == 0 &&
                tablero.obtenerCasilla(i, j+2) == 0 &&
                tablero.obtenerCasilla(i, j+3) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 5;
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
                contador += 20;
            }
            if (tablero.obtenerCasilla(i, j-1) == jugador &&
                tablero.obtenerCasilla(i, j-2) == 0 &&
                tablero.obtenerCasilla(i, j-3) == jugador) {
                contador += 20;
            }
            if (tablero.obtenerCasilla(i, j-1) == 0 &&
                tablero.obtenerCasilla(i, j-2) == jugador &&
                tablero.obtenerCasilla(i, j-3) == jugador) {
                contador += 20;
            }
            // Comprobamos todas las posibilidades de 2 en raya.
            if (tablero.obtenerCasilla(i, j-1) == jugador &&
                tablero.obtenerCasilla(i, j-2) == 0 &&
                tablero.obtenerCasilla(i, j-3) == 0) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i, j-1) == 0 &&
                tablero.obtenerCasilla(i, j-2) == 0 &&
                tablero.obtenerCasilla(i, j-3) == jugador) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i, j-1) == 0 &&
                tablero.obtenerCasilla(i, j-2) == jugador &&
                tablero.obtenerCasilla(i, j-3) == 0) {
                contador += 10;
            }
            // Comprobamos la posibilidad del 1 en raya
            if (tablero.obtenerCasilla(i, j-1) == 0 &&
                tablero.obtenerCasilla(i, j-2) == 0 &&
                tablero.obtenerCasilla(i, j-3) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 5;
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
                contador += 20;
            }
            if (tablero.obtenerCasilla(i+1, j) == jugador &&
                tablero.obtenerCasilla(i+2, j) == 0 &&
                tablero.obtenerCasilla(i+3, j) == jugador) {
                contador += 20;
            }
            if (tablero.obtenerCasilla(i+1, j) == 0 &&
                tablero.obtenerCasilla(i+2, j) == jugador &&
                tablero.obtenerCasilla(i+3, j) == jugador) {
                contador += 20;
            }
            // Comprobamos todas las posibilidades de 2 en raya.
            if (tablero.obtenerCasilla(i+1, j) == 0 &&
                tablero.obtenerCasilla(i+2, j) == jugador &&
                tablero.obtenerCasilla(i+3, j) == 0) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i+1, j) == 0 &&
                tablero.obtenerCasilla(i+2, j) == 0 &&
                tablero.obtenerCasilla(i+3, j) == jugador) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i+1, j) == jugador &&
                tablero.obtenerCasilla(i+2, j) == 0 &&
                tablero.obtenerCasilla(i+3, j) == 0) {
                contador += 10;
            }
            // Comprobamos la posibilidad del 1 en raya
            if (tablero.obtenerCasilla(i+1, j) == 0 &&
                tablero.obtenerCasilla(i+2, j) == 0 &&
                tablero.obtenerCasilla(i+3, j) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 5;
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
                contador += 20;
            }
            if (tablero.obtenerCasilla(i-1, j) == jugador &&
                tablero.obtenerCasilla(i-2, j) == 0 &&
                tablero.obtenerCasilla(i-3, j) == jugador) {
                contador += 20;
            }
            if (tablero.obtenerCasilla(i-1, j) == 0 &&
                tablero.obtenerCasilla(i-2, j) == jugador &&
                tablero.obtenerCasilla(i-3, j) == jugador) {
                contador += 20;
            }
            // Comprobamos todas las posibilidades de 2 en raya.
            if (tablero.obtenerCasilla(i-1, j) == 0 &&
                tablero.obtenerCasilla(i-2, j) == jugador &&
                tablero.obtenerCasilla(i-3, j) == 0) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i-1, j) == 0 &&
                tablero.obtenerCasilla(i-2, j) == 0 &&
                tablero.obtenerCasilla(i-3, j) == jugador) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i-1, j) == jugador &&
                tablero.obtenerCasilla(i-2, j) == 0 &&
                tablero.obtenerCasilla(i-3, j) == 0) {
                contador += 10;
            }
            // Comprobamos la posibilidad del 1 en raya
            if (tablero.obtenerCasilla(i-1, j) == 0 &&
                tablero.obtenerCasilla(i-2, j) == 0 &&
                tablero.obtenerCasilla(i-3, j) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 5;
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
                contador += 20;
            }
            if (tablero.obtenerCasilla(i+1, j+1) == jugador &&
                tablero.obtenerCasilla(i+2, j+2) == 0 &&
                tablero.obtenerCasilla(i+3, j+3) == jugador) {
                contador += 20;
            }
            if (tablero.obtenerCasilla(i+1, j+1) == 0 &&
                tablero.obtenerCasilla(i+2, j+2) == jugador &&
                tablero.obtenerCasilla(i+3, j+3) == jugador) {
                contador += 20;
            }
            
            if (tablero.obtenerCasilla(i+1, j+1) == 0 &&
                tablero.obtenerCasilla(i+2, j+2) == 0 &&
                tablero.obtenerCasilla(i+3, j+3) == jugador) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i+1, j+1) == 0 &&
                tablero.obtenerCasilla(i+2, j+2) == jugador &&
                tablero.obtenerCasilla(i+3, j+3) == 0) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i+1, j+1) == jugador &&
                tablero.obtenerCasilla(i+2, j+2) == 0 &&
                tablero.obtenerCasilla(i+3, j+3) == 0) {
                contador += 10;
            }
            
            if (tablero.obtenerCasilla(i+1, j+1) == 0 &&
                tablero.obtenerCasilla(i+2, j+2) == 0 &&
                tablero.obtenerCasilla(i+3, j+3) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 5;
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
                contador += 20;
            }
            if (tablero.obtenerCasilla(i+1, j-1) == jugador &&
                tablero.obtenerCasilla(i+2, j-2) == 0 &&
                tablero.obtenerCasilla(i+3, j-3) == jugador) {
                contador += 20;
            }
            if (tablero.obtenerCasilla(i+1, j-1) == 0 &&
                tablero.obtenerCasilla(i+2, j-2) == jugador &&
                tablero.obtenerCasilla(i+3, j-3) == jugador) {
                contador += 20;
            }
            
            if (tablero.obtenerCasilla(i+1, j-1) == 0 &&
                tablero.obtenerCasilla(i+2, j-2) == 0 &&
                tablero.obtenerCasilla(i+3, j-3) == jugador) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i+1, j-1) == 0 &&
                tablero.obtenerCasilla(i+2, j-2) == jugador &&
                tablero.obtenerCasilla(i+3, j-3) == 0) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i+1, j-1) == jugador &&
                tablero.obtenerCasilla(i+2, j-2) == 0 &&
                tablero.obtenerCasilla(i+3, j-3) == 0) {
                contador += 10;
            }
            
            if (tablero.obtenerCasilla(i+1, j-1) == 0 &&
                tablero.obtenerCasilla(i+2, j-2) == 0 &&
                tablero.obtenerCasilla(i+3, j-3) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 5;
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
                contador += 20;
            }
            if (tablero.obtenerCasilla(i-1, j+1) == jugador &&
                tablero.obtenerCasilla(i-2, j+2) == 0 &&
                tablero.obtenerCasilla(i-3, j+3) == jugador) {
                contador += 20;
            }
            if (tablero.obtenerCasilla(i-1, j+1) == 0 &&
                tablero.obtenerCasilla(i-2, j+2) == jugador &&
                tablero.obtenerCasilla(i-3, j+3) == jugador) {
                contador += 20;
            }
            
            if (tablero.obtenerCasilla(i-1, j+1) == 0 &&
                tablero.obtenerCasilla(i-2, j+2) == 0 &&
                tablero.obtenerCasilla(i-3, j+3) == jugador) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i-1, j+1) == 0 &&
                tablero.obtenerCasilla(i-2, j+2) == jugador &&
                tablero.obtenerCasilla(i-3, j+3) == 0) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i-1, j+1) == jugador &&
                tablero.obtenerCasilla(i-2, j+2) == 0 &&
                tablero.obtenerCasilla(i-3, j+3) == 0) {
                contador += 10;
            }
            
            if (tablero.obtenerCasilla(i-1, j+1) == 0 &&
                tablero.obtenerCasilla(i-2, j+2) == 0 &&
                tablero.obtenerCasilla(i-3, j+3) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 5;
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
                contador += 20;
            }
            if (tablero.obtenerCasilla(i-1, j-1) == jugador &&
                tablero.obtenerCasilla(i-2, j-2) == 0 &&
                tablero.obtenerCasilla(i-3, j-3) == jugador) {
                contador += 20;
            }
            if (tablero.obtenerCasilla(i-1, j-1) == 0 &&
                tablero.obtenerCasilla(i-2, j-2) == jugador &&
                tablero.obtenerCasilla(i-3, j-3) == jugador) {
                contador += 20;
            }
            
            if (tablero.obtenerCasilla(i-1, j-1) == 0 &&
                tablero.obtenerCasilla(i-2, j-2) == 0 &&
                tablero.obtenerCasilla(i-3, j-3) == jugador) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i-1, j-1) == 0 &&
                tablero.obtenerCasilla(i-2, j-2) == jugador &&
                tablero.obtenerCasilla(i-3, j-3) == 0) {
                contador += 10;
            }
            if (tablero.obtenerCasilla(i-1, j-1) == jugador &&
                tablero.obtenerCasilla(i-2, j-2) == 0 &&
                tablero.obtenerCasilla(i-3, j-3) == 0) {
                contador += 10;
            }
            
            if (tablero.obtenerCasilla(i-1, j-1) == 0 &&
                tablero.obtenerCasilla(i-2, j-2) == 0 &&
                tablero.obtenerCasilla(i-3, j-3) == 0 &&
                tablero.obtenerCasilla(i, j) != 0) {
                contador += 5;
            }
        }
        return contador;
    }
    
    private int jugadaDeLibroInicial(Tablero tablero) {
        if (tablero.obtenerCasilla(0, 3) == MAX && tablero.obtenerCasilla(1, 3) != MIN)
            return 3;
        else {
            if (tablero.obtenerCasilla(0, 3) == MIN)
                return 3;
            return 2;
        }
    }
    
    private int cuentaFichas(Tablero tablero) {
        int contador = 0;
        for (int i = 0; i < tablero.numFilas(); i++) {
            for (int j = 0; j < tablero.numColumnas(); j++) {
                if (tablero.existeFicha(i, j))
                    contador++;      
            }
        }
        return contador;
    }
}

