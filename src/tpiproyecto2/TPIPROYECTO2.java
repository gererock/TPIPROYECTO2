/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpiproyecto2;

import Controlador.Controlador;

/**
 *
 * @author Geremias Rocchietti
 */
public class TPIPROYECTO2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    Controlador controlador = new Controlador();
    controlador.cargarEquipos("C:\\Users\\mateo\\OneDrive\\Desktop\\TPI\\TPIPROYECTO2\\src\\Archivos\\EquiposMundial.txt");     
    controlador.cargarJugadores("C:\\Users\\mateo\\OneDrive\\Desktop\\TPI\\TPIPROYECTO2\\src\\Archivos\\jugadoresMundial.txt"); 
    controlador.iniciar();
    }
}
