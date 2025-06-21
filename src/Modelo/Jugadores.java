/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Geremias Rocchietti
 */
public class Jugadores {
    private String nombreJugador;
    private String edadJugador;
    private String equipo;

    public Jugadores(String nombreJugador, String edadJugador, String equipo) {
        this.nombreJugador = nombreJugador;
        this.edadJugador = edadJugador;

        this.equipo = equipo;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public String getEdadJugador() {
        return edadJugador;
    }

    public void setEdadJugador(String edadJugador) {
        this.edadJugador = edadJugador;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    @Override
    public String toString() {
        return "Jugadores{" + "nombreJugador=" + nombreJugador + ", edadJugador=" + edadJugador + ", equipo=" + equipo + '}';
    }

 
    
    
}
