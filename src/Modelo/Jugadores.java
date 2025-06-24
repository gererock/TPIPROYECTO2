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
    private int edadJugador;
    private Equipos equipo;

    public Jugadores(String nombreJugador, int edadJugador) {
        this.nombreJugador = nombreJugador;
        this.edadJugador = edadJugador;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public int getEdadJugador() {
        return edadJugador;
    }

    public void setEdadJugador(int edadJugador) {
        this.edadJugador = edadJugador;
    }

    public Equipos getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipos equipo) {
        this.equipo = equipo;
    }

    @Override
    public String toString() {
        return "%s (%d años) - %s".formatted(nombreJugador, edadJugador,
                equipo != null ? equipo.getNombreEquipo() : "Sin equipo");
    }

}
