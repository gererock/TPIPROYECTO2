/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Equipos;
import Modelo.Jugadores;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Geremias Rocchietti
 */
public class Controlador {

    private final List<Jugadores> jugadores = new ArrayList<>();
    private final List<Equipos> equipos = new ArrayList<>();
    private final Vista vista = new Vista();

    public void cargarJugadores(String ruta) {
        try (FileReader fr = new FileReader(ruta); BufferedReader br = new BufferedReader(fr)) {
            String linea;

            // Saltar la primera línea (cabecera)
            br.readLine();

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                Jugadores jug = new Jugadores(
                        partes[0],
                        partes[1],
                        partes[2]);
                jugadores.add(jug);
                vista.mensaje(jug.toString());
            }
        } catch (IOException e) {
            vista.mensaje("Error leyendo archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            vista.mensaje("Error en formato de número (ID): " + e.getMessage());
        }
                
    }
    public void cargarEquipos(String ruta) {
        try (FileReader fr = new FileReader(ruta); BufferedReader br = new BufferedReader(fr)) {
            String linea;

            // Saltar la primera línea (cabecera)
            br.readLine();

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                Equipos equi = new Equipos(
                        partes[0],
                        partes[1]);
                equipos.add(equi);
                vista.mensaje(equi.toString());
            }
        } catch (IOException e) {
            vista.mensaje("Error leyendo archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            vista.mensaje("Error en formato de número (ID): " + e.getMessage());
        }

    }
}
