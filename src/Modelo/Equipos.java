/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Geremias Rocchietti
 */
public class Equipos {
    private String nombreEquipo;
    private String puntuacionEquipo;
    private final List<Jugadores> plantel = new ArrayList<>();

    public Equipos(String nombreEquipo, int puntuacionEquipo) {
        this.nombreEquipo = nombreEquipo;
        this.puntuacionEquipo = puntuacionEquipo;
    }
    

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getPuntuacionEquipo() {
        return puntuacionEquipo;
    }

    public void setPuntuacionEquipo(String puntuacionEquipo) {
        this.puntuacionEquipo = puntuacionEquipo;
    }
    public List<Jugadores> getPlantel()           { return plantel; }
    public void addJugador(Jugadores j)           { plantel.add(j); }

    @Override
    public String toString() {
        return "Equipos{" + "nombreEquipo=" + nombreEquipo + ", puntuacionEquipo=" + puntuacionEquipo + '}';
    }
    
    
}
