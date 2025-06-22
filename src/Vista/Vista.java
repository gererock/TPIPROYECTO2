/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.Partidos;
import Modelo.Equipos;
import Modelo.Grupos;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Geremias Rocchietti
 */
public class Vista {

    Scanner scanner = new Scanner(System.in);

    public int menu() {
        System.out.println("Menu");
        System.out.println("1. COMENZAR CAMPEONATO");
        System.out.println("2. VER EQUIPOS");
        System.out.println("2. AGREGAR EQUIPOS");

        System.out.println("0. SALIR");
        System.out.print("Ingrese opción: ");
        return Integer.parseInt(scanner.nextLine());
    }

    public int menucampeonato() {
        System.out.println("Menu");
        System.out.println("1. JUGAR PARTIDO");
        System.out.println("2. VER PARTIDOS");
        System.out.println("0. SALIR");
        System.out.print("Ingrese opción: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private int leerEntero(String label) {
        int valor;
        while (true) {
            try {
                System.out.print(label);
                valor = Integer.parseInt(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }

    public int menuSeleccionarPartido(List<Partidos> partidos) {
        System.out.println("\n=== PARTIDOS PENDIENTES ===");
        for (int i = 0; i < partidos.size(); i++) {
            System.out.printf("%d. %s vs %s (%s)%n", i + 1,
                    partidos.get(i).getEquipoLocal().getNombreEquipo(),
                    partidos.get(i).getEquipoVisitante().getNombreEquipo(),
                    partidos.get(i).getNombreEstadio());
        }
        System.out.println("0. Volver");
        return leerEntero("Seleccione partido: ");
    }

    public void mensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarFixture(List<Partidos> fixture) {
        System.out.println("\n=== FIXTURE ===");
        fixture.forEach(p -> {
            String marcador = p.isJugado()
                    ? String.format("%d-%d", p.getGolesLocal(), p.getGolesVisitante())
                    : "-";
            System.out.printf("t%s vs %s\t%s (%s)%n",
                    p.getEquipoLocal().getNombreEquipo(),
                    p.getEquipoVisitante().getNombreEquipo(), marcador,
                    p.getNombreEstadio());
        });
    }
    
        public void mostrarGrupos(Grupos g) {
        System.out.println("\n=== Fase de grupos ===");
        imprimirGrupo("Grupo A", g.getGrupoA());
        imprimirGrupo("Grupo B", g.getGrupoB());
        imprimirGrupo("Grupo C", g.getGrupoC());
        imprimirGrupo("Grupo D", g.getGrupoD());
    }
    private void imprimirGrupo(String titulo, List<Equipos> lista) {
        System.out.println("\n" + titulo);
        lista.forEach(e -> System.out.println("  • " + e));
    }

}
