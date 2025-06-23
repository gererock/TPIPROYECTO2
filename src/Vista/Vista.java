/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.Partidos;
import Modelo.Equipos;
import Modelo.Fase;
import Modelo.Grupos;
import Modelo.Jugadores;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

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
        System.out.println("3. VER GRUPOS");

        System.out.println("0. SALIR");
        System.out.print("Ingrese opción: ");
        return Integer.parseInt(scanner.nextLine());
    }

    public int menucampeonato() {
        System.out.println("\n----- MENÚ CAMPEONATO -----");
        System.out.println("1. Jugar un partido");
        System.out.println("2. Ver fixture");
        System.out.println("3. VER TABLA DE POSICIONES");
        System.out.println("0. Volver al menú principal");
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

    public void mostrarFixtureGrupos(Grupos grupos, List<Partidos> fixture) {
        System.out.println("\n=== FIXTURE – FASE DE GRUPOS ===");

        Map<Character, List<Equipos>> mapa = Map.of(
                'A', grupos.getGrupoA(),
                'B', grupos.getGrupoB(),
                'C', grupos.getGrupoC(),
                'D', grupos.getGrupoD()
        );

        char[] orden = {'A', 'B', 'C', 'D'};
        for (char letra : orden) {
            List<Partidos> lista = fixture.stream()
                    .filter(p -> p.getFase() == Fase.GRUPOS && !p.isJugado()
                    && mapa.get(letra).contains(p.getEquipoLocal())
                    && mapa.get(letra).contains(p.getEquipoVisitante()))
                    .toList();

            System.out.println("==== GRUPO " + letra + " ====");
            if (lista.isEmpty()) {
                System.out.println("   (Todos jugados)");
                System.out.println();
                continue;
            }   
            for (int i = 0; i < lista.size(); i++) {
                Partidos p = lista.get(i);
                System.out.printf("%d) %-15s vs %-15s - (%s)%n",
                        i + 1,
                        p.getEquipoLocal().getNombreEquipo(),
                        p.getEquipoVisitante().getNombreEquipo(),
                        p.getNombreEstadio());
            }
            System.out.println();
        }
    }
        /* elegir grupo A-D */
    public char menuSeleccionarGrupo() {
        System.out.print("Grupo (A-D, 0 para volver): ");
        while (true) {
            String in = scanner.nextLine().trim().toUpperCase();
            if (in.equals("0")) {
                return '0';
            }
            if (in.matches("[ABCD]")) {
                return in.charAt(0);
            }
            System.out.print("Grupo inválido. Intente otra vez: ");
        }
    }

    /* elegir número de partido dentro del grupo */
    public int menuSeleccionarPartidoGrupo(List<Partidos> lista) {
        System.out.print("N° de partido (0 para volver): ");
        int n = Integer.parseInt(scanner.nextLine().trim());
        return n;
    }

    public void mostrarTablaDePosiciones(List<Equipos> equipos) {

        Map<String, List<Equipos>> equiposPorGrupo = new TreeMap<>();

        for (Equipos e : equipos) {
            equiposPorGrupo.computeIfAbsent(e.getGrupo(), k -> new ArrayList<>()).add(e);
        }

        for (String grupo : equiposPorGrupo.keySet()) {
            System.out.println("\n=== Grupo " + grupo + " ===");

            List<Equipos> listaGrupo = equiposPorGrupo.get(grupo);
            listaGrupo.sort((e1, e2) -> {
                if (e2.getPuntuacionEquipo() != e1.getPuntuacionEquipo()) {
                    return Integer.compare(e2.getPuntuacionEquipo(), e1.getPuntuacionEquipo());
                } else {
                    return Integer.compare(e2.getDiferenciaGoles(), e1.getDiferenciaGoles());
                }
            });

            System.out.printf("%-20s %10s %15s %15s %20s\n", "Equipo", "Puntos", "Goles a Favor", "Goles en Contra", "Diferencia de Goles");
            System.out.println("-----------------------------------------------------------------------------------------------");

            for (Equipos e : listaGrupo) {
                System.out.printf("%-20s %10d %15d %15d %20d\n",
                        e.getNombreEquipo(),
                        e.getPuntuacionEquipo(),
                        e.getGolesAFavor(),
                        e.getGolesEnContra(),
                        e.getDiferenciaGoles());
            }
        }
    }

    public void mostrarGrupos(Grupos g) {
        System.out.println("\n=== Fase de grupos ===");
        imprimirGrupo("Grupo A", g.getGrupoA());
        imprimirGrupo("Grupo B", g.getGrupoB());
        imprimirGrupo("Grupo C", g.getGrupoC());
        imprimirGrupo("Grupo D", g.getGrupoD());
    }

    private void imprimirGrupo(String nombreGrupo, List<Equipos> equipos) {
        System.out.println("==== " + nombreGrupo.toUpperCase() + " ====");
        if (equipos.isEmpty()) {
            System.out.println("   (Sin equipos)");
            return;
        }
        for (Equipos e : equipos) {
            System.out.printf(" - %-20s Puntaje: %d%n", e.getNombreEquipo(), e.getPuntuacionEquipo());
        }
        System.out.println();
    }

    public void mostrarEquiposConJugadores(List<Equipos> equipos) {
        System.out.println("\n=== Equipos y Jugadores ===");
        for (Equipos e : equipos) {
            System.out.println("\n• " + e.getNombreEquipo() + " (Puntos: " + e.getPuntuacionEquipo() + ")");
            for (Jugadores j : e.getPlantel()) {
                System.out.println("   - " + j.getNombreJugador() + " (" + j.getEdadJugador() + " años)");
            }
        }
    }

    public Equipos[] pedirEquipos(List<Equipos> disponibles) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Seleccione el número del equipo LOCAL:");
        for (int i = 0; i < disponibles.size(); i++) {
            System.out.println(i + " - " + disponibles.get(i).getNombreEquipo());
        }

        int local = sc.nextInt();

        System.out.println("Seleccione el número del equipo VISITANTE:");
        int visitante = sc.nextInt();

        Equipos[] seleccionados = new Equipos[2];
        seleccionados[0] = disponibles.get(local);
        seleccionados[1] = disponibles.get(visitante);

        return seleccionados;
    }

    public String pedirEstadio() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese el nombre del estadio: ");
        return sc.nextLine();
    }

    private final Scanner sc = new Scanner(System.in);

    public int pedirGoles(String nombreEquipo) {
        System.out.print("Ingrese los goles del equipo " + nombreEquipo + ": ");
        while (true) {
            try {
                int goles = Integer.parseInt(sc.nextLine());
                if (goles < 0) {
                    System.out.print("Los goles no pueden ser negativos. Intente nuevamente: ");
                    continue;
                }
                return goles;
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Ingrese un número entero: ");
            }
        }
    }

}
