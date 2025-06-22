/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Geremias Rocchietti
 */
public class Partidos {

    private Equipos EquipoLocal;
    private Equipos EquipoVisitante;
    private int golesLocal;
    private int golesVisitante;
    private String fasePartido;
    private String nombreEstadio;
    private Boolean jugado = false;
    private Fase fase;

    public Partidos() {
    }

    public Partidos(Equipos EquipoLocal, Equipos EquipoVisitante, int golesLocal, int golesVisitante, String fasePartido, String nombreEstadio) {
        this.EquipoLocal = EquipoLocal;
        this.EquipoVisitante = EquipoVisitante;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.fasePartido = fasePartido;
        this.nombreEstadio = nombreEstadio;
    }

    public Equipos getEquipoLocal() {
        return EquipoLocal;
    }

    public void setEquipoLocal(Equipos EquipoLocal) {
        this.EquipoLocal = EquipoLocal;
    }

    public Equipos getEquipoVisitante() {
        return EquipoVisitante;
    }

    public void setEquipoVisitante(Equipos EquipoVisitante) {
        this.EquipoVisitante = EquipoVisitante;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public int getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(int golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public String getFasePartido() {
        return fasePartido;
    }

    public void setFasePartido(String fasePartido) {
        this.fasePartido = fasePartido;
    }

    public String getNombreEstadio() {
        return nombreEstadio;
    }

    public void setNombreEstadio(String nombreEstadio) {
        this.nombreEstadio = nombreEstadio;
    }

    public Boolean getJugado() {
        return jugado;
    }

    public void setJugado(Boolean jugado) {
        this.jugado = jugado;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }

    public boolean isEliminatorio() {
        return fase != Fase.GRUPOS;  
    }

    @Override
    public String toString() {
        return "Partidos{" + "EquipoLocal=" + EquipoLocal + ", EquipoVisitante=" + EquipoVisitante + ", golesLocal=" + golesLocal + ", golesVisitante=" + golesVisitante + ", fasePartido=" + fasePartido + ", nombreEstadio=" + nombreEstadio + ", jugado=" + jugado + '}';
    }

    public boolean isJugado() {
        return jugado;
    }

    public void setJugado(boolean jugado) {
        this.jugado = jugado;
    }

}
