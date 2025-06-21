/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Geremias Rocchietti
 */
public class Equipos {
    private String nombreEquipo;
    private String puntuacionEquipo;

    public Equipos(String nombreEquipo, String puntuacionEquipo) {
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

    @Override
    public String toString() {
        return "Equipos{" + "nombreEquipo=" + nombreEquipo + ", puntuacionEquipo=" + puntuacionEquipo + '}';
    }
    
    
}
