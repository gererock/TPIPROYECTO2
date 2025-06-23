/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Equipos;
import Modelo.Fase;
import Modelo.Grupos;
import Modelo.Jugadores;
import Modelo.Partidos;
import Vista.Vista;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Locale;

/**
 *
 * @author Geremias Rocchietti
 */
public class Controlador {

    private final List<Jugadores> jugadores = new ArrayList<>();
    private final List<Equipos> equipos = new ArrayList<>();

    private final Grupos grupos = new Grupos();
    private final List<Partidos> fixture = new ArrayList<>();
    private Fase faseActual = Fase.GRUPOS;
    private final Vista vista = new Vista();


    /* Listado rápido de equipos “vivos” */
    private List<Equipos> equiposVivos() {
        return equipos.stream()
                .filter(e -> e.getPuntuacionEquipo() >= 0)
                .collect(Collectors.toList());

    }

    public void mostrarEquipos() {
        vista.mostrarEquiposConJugadores(equipos);
    }

    public void iniciar() {
        boolean salir = false;

        while (!salir) {
            switch (vista.menu()) {
                case 1 -> {
                    comenzarCampeonato();
                    ejecutarCampeonato();
                }
                case 2 -> {
                    mostrarEquipos();
                }
                case 3 -> {
                    verGrupos();
                }
                case 0 ->
                    salir = true;
                default ->
                    vista.mensaje("Opción inválida");
            }
        }
    }

    private void comenzarCampeonato() {
        if (equipos.size() != 16) {
            vista.mensaje("Para comenzar se necesitan exactamente 16 equipos (4 por grupo).");
            return;
        }

        generarFixtureGrupos();                       // ➊  AGREGA ESTA LÍNEA

        faseActual = Fase.GRUPOS;
        vista.mensaje("Fixture generado y fase de grupos iniciada!"); // quité el «¡»
        vista.mostrarFixtureGrupos(grupos, fixture);  // mantiene el orden correcto ahora
    }

    private void generarFixtureGrupos() {
        fixture.clear();
        List<List<Equipos>> gs = List.of(
                grupos.getGrupoA(),
                grupos.getGrupoB(),
                grupos.getGrupoC(),
                grupos.getGrupoD()
        );
        for (List<Equipos> g : gs) {
            for (int i = 0; i < g.size() - 1; i++) {
                for (int j = i + 1; j < g.size(); j++) {
                    Partidos p = new Partidos();
                    p.setEquipoLocal(g.get(i));
                    p.setEquipoVisitante(g.get(j));
                    p.setFase(Fase.GRUPOS);
                    p.setNombreEstadio("Por definir");
                    fixture.add(p);
                }
            }
        }
    }

    public void cargarJugadores(String ruta) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ruta), StandardCharsets.UTF_8))) {

            String linea;
            int nroLinea = 0;
            br.readLine();                       // salta cabecera
            nroLinea++;

            while ((linea = br.readLine()) != null) {
                nroLinea++;
                String[] p = linea.split(",");
                if (p.length < 3) {
                    vista.mensaje("Línea " + nroLinea + ": formato incorrecto → " + linea);
                    continue;
                }

                String nombreJugador = p[0].trim();
                int edad = parseEdad(p[1]);
                String nombreEquipo = p[2].trim();

                Jugadores jug = new Jugadores(nombreJugador, edad);
                Equipos eq = buscarEquipoPorNombre(nombreEquipo);

                if (eq != null) {
                    jug.setEquipo(eq);
                    eq.addJugador(jug);
                } else {
                    vista.mensaje("Línea " + nroLinea + ": equipo NO encontrado → " + nombreEquipo);
                }
                jugadores.add(jug);
            }
            vista.mensaje("Jugadores cargados: " + jugadores.size());

        } catch (IOException ex) {
            vista.mensaje("Error leyendo jugadores (I/O): " + ex.getMessage());
        } catch (Exception ex) {
            vista.mensaje("Error inesperado en cargarJugadores: " + ex.getMessage());
            ex.printStackTrace();   // opcional: ver detalle en consola
        }
    }

    private String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // quita tildes
                .toLowerCase(Locale.ROOT)
                .trim();
    }

    private int parseEdad(String campo) {
        try {
            return Integer.parseInt(campo.trim());
        } catch (NumberFormatException ex) {
            return 0;
        }   // 0 = edad desconocida
    }

    private Equipos buscarEquipoPorNombre(String nombre) {
        String buscado = normalizar(nombre);
        return equipos.stream()
                .filter(e -> normalizar(e.getNombreEquipo()).equals(buscado))
                .findFirst()
                .orElse(null);
    }

    public void cargarEquipos(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            br.readLine();                 // cabecera

            int indice = 0;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(",");
                String nombre = p[0].trim();
                int puntaje = Integer.parseInt(p[1].trim());

                Equipos e = new Equipos(nombre, puntaje);
                e.setGrupo(obtenerGrupoPorIndice(indice));
                equipos.add(e);
                grupos.asignarAGrupo(e, indice);
                indice++;
            }
            vista.mensaje("Equipos cargados: " + equipos.size());
        } catch (IOException ex) {
            vista.mensaje("Error leyendo equipos: " + ex.getMessage());
        }
    }

    private String obtenerGrupoPorIndice(int indice) {
        char grupo = (char) ('A' + (indice / 4)); // 4 equipos por grupo: A, B, C, D...
        return String.valueOf(grupo);
    }

    public void jugarPartido() {

        if (faseActual == null) {
            vista.mensaje("No hay fase activa.");
            return;
        }

        /* ---------------------------- FASE DE GRUPOS ---------------------------- */
        if (faseActual == Fase.GRUPOS) {

            // 1) Mostrar fixture agrupado para que el usuario vea qué queda.
            vista.mostrarFixtureGrupos(grupos, fixture);

            // 2) Elegir grupo
            char letra = vista.menuSeleccionarGrupo();
            if (letra == '0') {
                return;
            }

            List<Equipos> listaEquipos = switch (letra) {
                case 'A' ->
                    grupos.getGrupoA();
                case 'B' ->
                    grupos.getGrupoB();
                case 'C' ->
                    grupos.getGrupoC();
                default ->
                    grupos.getGrupoD();
            };

            // 3) Partidos pendientes de ese grupo
            List<Partidos> pendientes = fixture.stream()
                    .filter(p -> !p.isJugado()
                    && p.getFase() == Fase.GRUPOS
                    && listaEquipos.contains(p.getEquipoLocal()))
                    .toList();

            if (pendientes.isEmpty()) {
                vista.mensaje("No quedan partidos pendientes en el grupo " + letra + ".");
                return;
            }

            int n = vista.menuSeleccionarPartido(pendientes);
            if (n <= 0 || n > pendientes.size()) {
                return;
            }

            procesarResultado(pendientes.get(n - 1)); 
            finalizarFaseGrupos();
            return;                                    // fin para fase de grupos
        }

        /* ----------------------- CUARTOS / SEMI / FINAL ------------------------- */
        List<Partidos> pendientes = fixture.stream()
                .filter(p -> !p.isJugado() && p.getFase() == faseActual)
                .toList();

        if (pendientes.isEmpty()) {
            vista.mensaje("No hay partidos pendientes en " + faseActual + ".");
            return;
        }

        vista.mensaje("\n=== PARTIDOS PENDIENTES – " + faseActual + " ===");
        int n = vista.menuSeleccionarPartido(pendientes);
        if (n <= 0 || n > pendientes.size()) {
            return;
        }

        boolean quedan = fixture.stream()
                .anyMatch(p -> p.getFase() == Fase.GRUPOS && !p.isJugado());

        if (!quedan) {
            finalizarFaseGrupos();               // genera Cuartos y cambia fase
        }
        return;
    }

    private void procesarResultado(Partidos partido) {
        Equipos local = partido.getEquipoLocal();
        Equipos visita = partido.getEquipoVisitante();

        if (partido.getNombreEstadio().equals("Por definir")) {
            partido.setNombreEstadio(vista.pedirEstadio());
        }

        int gL = vista.pedirGoles(local.getNombreEquipo());
        int gV = vista.pedirGoles(visita.getNombreEquipo());

        partido.setGolesLocal(gL);
        partido.setGolesVisitante(gV);
        partido.setJugado(true);

        if (partido.getFase() == Fase.GRUPOS) {
            // puntos
            if (gL > gV) {
                local.sumarPuntos(3);
            } else if (gV > gL) {
                visita.sumarPuntos(3);
            } else {
                local.sumarPuntos(1);
                visita.sumarPuntos(1);
            }

            // estadísticas
            local.sumarGolesAFavor(gL);
            local.sumarGolesEnContra(gV);
            visita.sumarGolesAFavor(gV);
            visita.sumarGolesEnContra(gL);
            local.actualizarDiferenciaGoles();
            visita.actualizarDiferenciaGoles();
        } else {
            Equipos perd = (gL > gV) ? visita : local;
            perd.setPuntuacionEquipo(-1);   // eliminado
        }

        vista.mensaje("Resultado registrado.");
    }

    private void finalizarFaseGrupos() {

        if (faseActual != Fase.GRUPOS) {
            return;
        }

        // ¿faltan partidos?
        if (fixture.stream().anyMatch(p -> p.getFase() == Fase.GRUPOS && !p.isJugado())) {
            vista.mensaje("Aún quedan partidos de grupos.");
            return;
        }

        // Clasificar los dos con más puntos de cada grupo
        List<Equipos> clasificados = new ArrayList<>();
        for (List<Equipos> g : List.of(grupos.getGrupoA(), grupos.getGrupoB(),
                grupos.getGrupoC(), grupos.getGrupoD())) {

            g.sort((a, b) -> Integer.compare(b.getPuntuacionEquipo(), a.getPuntuacionEquipo()));

            Equipos segundo = g.get(1);
            Equipos tercero = g.get(2);

            if (segundo.getPuntuacionEquipo() == tercero.getPuntuacionEquipo()) {

                Partidos tie = new Partidos();
                tie.setEquipoLocal(segundo);
                tie.setEquipoVisitante(tercero);
                tie.setFase(Fase.GRUPOS);
                tie.setNombreEstadio("Desempate");
                fixture.add(tie);
                vista.mensaje("Se agendó desempate " + segundo.getNombreEquipo()
                        + " vs " + tercero.getNombreEquipo());
                return;
            }
            clasificados.add(g.get(0));
            clasificados.add(segundo);
        }

        crearCuartos(clasificados);
        vista.mensaje("¡Fase de grupos finalizada! Pasando a cuartos de final…");
        faseActual = Fase.CUARTOS;
    }

    private void crearCuartos(List<Equipos> c) {

        int[][] cruces = {{0, 3}, {2, 1}, {4, 7}, {6, 5}};
        for (int[] par : cruces) {
            Partidos p = new Partidos();
            p.setEquipoLocal(c.get(par[0]));
            p.setEquipoVisitante(c.get(par[1]));
            p.setFase(Fase.CUARTOS);
            p.setNombreEstadio("Por definir");
            fixture.add(p);
        }
        vista.mensaje("Cuartos de final generados.");
    }

    public void verGrupos() {
        vista.mostrarGrupos(grupos);
    }

    private void ejecutarCampeonato() {
        boolean volver = false;
        while (!volver) {
            switch (vista.menucampeonato()) {
                case 1 ->
                    jugarPartido();
                case 2 ->
                    vista.mostrarFixtureGrupos(grupos, fixture);
                case 3 ->
                    vista.mostrarTablaDePosiciones(equipos);
                case 0 ->
                    volver = true;
                default ->
                    vista.mensaje("Opción inválida.");
            }
        }

    }

}
