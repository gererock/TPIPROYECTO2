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

    public void iniciar() {
        boolean salir = false;

        while (!salir) {
            switch (vista.menu()) {
                case 1 ->{
                    comenzarCampeonato();
                    ejecutarCampeonato();
                }
                case 2 ->{
                    vista.mostrarEquiposConJugadores(equipos);
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
        generarFixtureGrupos();
        faseActual = Fase.GRUPOS;
        vista.mensaje("¡Fixture generado y fase de grupos iniciada!");
        vista.mostrarFixture(fixture);
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
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            br.readLine();                 // cabecera

            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(",");
                String nombreJugador = p[0].trim();
                int edad = Integer.parseInt(p[1].trim());
                String nombreEquipo = p[2].trim();

                Jugadores j = new Jugadores(nombreJugador, edad);
                Equipos eq = buscarEquipoPorNombre(nombreEquipo);
                if (eq != null) {
                    j.setEquipo(eq);
                    eq.addJugador(j);                    // ← queda en el plantel del equipo
                }
                jugadores.add(j);
            }
            vista.mensaje("Jugadores cargados: " + jugadores.size());
        } catch (IOException | NumberFormatException ex) {
            vista.mensaje("Error leyendo jugadores: " + ex.getMessage());
        }
    }

    private Equipos buscarEquipoPorNombre(String nombre) {
        return equipos.stream()
                .filter(e -> e.getNombreEquipo().equalsIgnoreCase(nombre))
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
                equipos.add(e);
                grupos.asignarAGrupo(e, indice);         // ← lo mete en A, B, C o D
                indice++;
            }
            vista.mensaje("Equipos cargados: " + equipos.size());
        } catch (IOException ex) {
            vista.mensaje("Error leyendo equipos: " + ex.getMessage());
        }
    }

    public void jugarPartido() {
        if (faseActual == null) {
            vista.mensaje("No hay fase activa.");
            return;
        }

        Equipos[] par = vista.pedirEquipos(equiposVivos());
        Equipos local = par[0], visit = par[1];
        if (local == null || visit == null || local == visit) {
            vista.mensaje("Equipos inválidos.");
            return;
        }

        // ¿Existe un partido disponible entre esos equipos en esta fase?
        Partidos partido = fixture.stream()
                .filter(p -> !p.isJugado()
                && p.getFase() == faseActual
                && ((p.getEquipoLocal() == local && p.getEquipoVisitante() == visit)
                || (p.getEquipoLocal() == visit && p.getEquipoVisitante() == local)))
                .findFirst().orElse(null);

        if (partido == null) {
            vista.mensaje("Ese encuentro no está disponible en esta fase.");
            return;
        }

        // Pedir estadio si aún no estaba fijado
        if (partido.getNombreEstadio().equals("Por definir")) {
            partido.setNombreEstadio(vista.pedirEstadio());
        }

        int gL = vista.pedirGoles(local.getNombreEquipo());
        int gV = vista.pedirGoles(visit.getNombreEquipo());

        partido.setGolesLocal(gL);
        partido.setGolesVisitante(gV);
        partido.setJugado(true);

        // Sumar puntos por gol SOLO en fase de grupos
        if (faseActual == Fase.GRUPOS) {
            local.setPuntuacionEquipo(local.getPuntuacionEquipo() + gL);
            visit.setPuntuacionEquipo(visit.getPuntuacionEquipo() + gV);
        }

        // Si es eliminatorio, avanza el ganador y elimina el perdedor
        if (partido.isEliminatorio()) {
            Equipos ganador = gL > gV ? local : visit;
            Equipos perdedor = gL > gV ? visit : local;
            perdedor.setPuntuacionEquipo(-1);             // marcador “eliminado”
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
            // ordenar por puntaje descendente
            g.sort((a, b) -> Integer.compare(b.getPuntuacionEquipo(), a.getPuntuacionEquipo()));

            Equipos segundo = g.get(1);
            Equipos tercero = g.get(2);

            if (segundo.getPuntuacionEquipo() == tercero.getPuntuacionEquipo()) {
                // desempate directo - partido extra
                Partidos tie = new Partidos();
                tie.setEquipoLocal(segundo);
                tie.setEquipoVisitante(tercero);
                tie.setFase(Fase.GRUPOS);           // sigue siendo grupos
                tie.setNombreEstadio("Desempate");
                fixture.add(tie);
                vista.mensaje("Se agendó desempate " + segundo.getNombreEquipo()
                        + " vs " + tercero.getNombreEquipo());
                return; // se esperará a que se juegue el desempate
            }
            clasificados.add(g.get(0));
            clasificados.add(segundo);
        }

        // Crear cuartos de final (A1-B2, B1-A2, C1-D2, D1-C2)
        crearCuartos(clasificados);
        faseActual = Fase.CUARTOS;
    }

    private void crearCuartos(List<Equipos> c) {
        // c viene en orden [A1,A2,B1,B2,C1,C2,D1,D2]
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

    public void verEquipos() {
        vista.mostrarGrupos(grupos);
    }
    
    private void ejecutarCampeonato() {
    boolean volver = false;
    while (!volver) {
        switch (vista.menucampeonato()) {
            case 1 -> jugarPartido();
            case 2 -> vista.mostrarFixture(fixture);
            case 0 -> volver = true;
            default -> vista.mensaje("Opción inválida.");
        }
    }
    
    }


}
