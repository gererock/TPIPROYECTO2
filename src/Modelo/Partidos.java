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

    private String EquipoLocal;
    private String EquipoVisitante;
    private int golesLocal;
    private int golesVisitante;
    private String fasePartido;
    private Boolean jugado = false;

    public Partidos(String EquipoLocal, String EquipoVisitante, int golesLocal, int golesVisitante, String fasePartido) {
        this.EquipoLocal = EquipoLocal;
        this.EquipoVisitante = EquipoVisitante;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.fasePartido = fasePartido;
    }

    public String getEquipoLocal() {
        return EquipoLocal;
    }

    public void setEquipoLocal(String EquipoLocal) {
        this.EquipoLocal = EquipoLocal;
    }

    public String getEquipoVisitante() {
        return EquipoVisitante;
    }

    public void setEquipoVisitante(String EquipoVisitante) {
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

    public Boolean getJugado() {
        return jugado;
    }

    public void setJugado(Boolean jugado) {
        this.jugado = jugado;
    }

    @Override
    public String toString() {
        return "Partidos{" + "EquipoLocal=" + EquipoLocal + ", EquipoVisitante=" + EquipoVisitante + ", golesLocal=" + golesLocal + ", golesVisitante=" + golesVisitante + ", fasePartido=" + fasePartido + ", jugado=" + jugado + '}';
    }

    public boolean isJugado() {
        return jugado;
    }

    public void setJugado(boolean jugado) {
        this.jugado = jugado;
    }

}
