/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Geremias Rocchietti
 */
public class Estadios {

    private String nombreEstadio;
    private String ubicacionEstadio;

    public Estadios(String nombreEstadio, String ubicacionEstadio) {
        this.nombreEstadio = nombreEstadio;
        this.ubicacionEstadio = ubicacionEstadio;
    }

    public String getNombreEstadio() {
        return nombreEstadio;
    }

    public void setNombreEstadio(String nombreEstadio) {
        this.nombreEstadio = nombreEstadio;
    }

    public String getUbicacionEstadio() {
        return ubicacionEstadio;
    }

    public void setUbicacionEstadio(String ubicacionEstadio) {
        this.ubicacionEstadio = ubicacionEstadio;
    }

    @Override
    public String toString() {
        return "Estadios{" + "nombreEstadio=" + nombreEstadio + ", ubicacionEstadio=" + ubicacionEstadio + '}';
    }

}
