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
public class Grupos {
    private List<Equipos> GrupoA = new ArrayList<>();
    private List<Equipos> GrupoB = new ArrayList<>();
    private List<Equipos> GrupoC = new ArrayList<>();
    private List<Equipos> GrupoD = new ArrayList<>();

    public List<Equipos> getGrupoA() {
        return GrupoA;
    }

    public void setGrupoA(List<Equipos> GrupoA) {
        this.GrupoA = GrupoA;
    }

    public List<Equipos> getGrupoB() {
        return GrupoB;
    }

    public void setGrupoB(List<Equipos> GrupoB) {
        this.GrupoB = GrupoB;
    }

    public List<Equipos> getGrupoC() {
        return GrupoC;
    }

    public void setGrupoC(List<Equipos> GrupoC) {
        this.GrupoC = GrupoC;
    }

    public List<Equipos> getGrupoD() {
        return GrupoD;
    }

    public void setGrupoD(List<Equipos> GrupoD) {
        this.GrupoD = GrupoD;
    }

    @Override
    public String toString() {
        return "=============Grupos=============" + "\nGrupoA=" + GrupoA + "\n GrupoB=" + GrupoB + "\n GrupoC=" + GrupoC + "\n GrupoD=" + GrupoD + '}';
    }
    
        public void asignarAGrupo(Equipos e, int indice) {
        if      (indice < 4)  GrupoA.add(e);
        else if (indice < 8)  GrupoB.add(e);
        else if (indice < 12) GrupoC.add(e);
        else                  GrupoD.add(e);
    }

    
}
