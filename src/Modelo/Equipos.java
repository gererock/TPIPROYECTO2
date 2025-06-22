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
    private int puntuacionEquipo;
    private final List<Jugadores> plantel = new ArrayList<>();
    private String grupo;

    public Equipos(String nombreEquipo, int puntuacionEquipo) {
        this.nombreEquipo = nombreEquipo;
        this.puntuacionEquipo = puntuacionEquipo;
        this.grupo = "";
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
    
    

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public int getPuntuacionEquipo() {
        return puntuacionEquipo;
    }

    public void setPuntuacionEquipo(int puntuacionEquipo) {
        this.puntuacionEquipo = puntuacionEquipo;
    }

    public List<Jugadores> getPlantel() {
        return plantel;
    }

    public void addJugador(Jugadores j) {
        plantel.add(j);
    }

    private int golesAFavor = 0;
    private int golesEnContra = 0;

    public int getGolesAFavor() {
        return golesAFavor;
    }

    public void addGolesAFavor(int goles) {
        this.golesAFavor += goles;
    }

    public int getGolesEnContra() {
        return golesEnContra;
    }

    public void addGolesEnContra(int goles) {
        this.golesEnContra += goles;
    }

    public int getDiferenciaGoles() {
        return golesAFavor - golesEnContra;
    }

    public void sumarPuntos(int puntos) {
        this.puntuacionEquipo += puntos;
    }

    @Override
    public String toString() {
        return "Equipos{" + "nombreEquipo=" + nombreEquipo + ", puntuacionEquipo=" + puntuacionEquipo + '}';
    }
}
