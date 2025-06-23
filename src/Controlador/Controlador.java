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
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Controlador {

    private final List<Jugadores> jugadores = new ArrayList<>();
    private final List<Equipos> equipos = new ArrayList<>();
    private final Grupos grupos = new Grupos();
    private final List<Partidos> fixture = new ArrayList<>();
    private Fase faseActual = Fase.GRUPOS;
    private final Vista vista = new Vista();

    private List<Equipos> equiposVivos() {
        return equipos.stream()
                .filter(e -> e.getPuntuacionEquipo() >= 0)
                .collect(Collectors.toList());
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            switch (vista.menu()) {
                case 1 -> {
                    generarFixtureGrupos();
                    faseActual = Fase.GRUPOS;
                    vista.mensaje("FIXTURE GENERADO Y FASE DE GRUPOS INICIADA!");
                    vista.mostrarFixtureGrupos(grupos, fixture);
                    ejecutarFases();
                }
                case 2 ->
                    vista.mostrarEquiposConJugadores(equipos);
                case 3 ->
                    vista.mostrarGrupos(grupos);
                case 0 ->
                    salir = true;
                default ->
                    vista.mensaje("OPCION INVALIDA");
            }
        }
    }

    private void ejecutarFases() {
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
                    vista.mensaje("OPCION INVALIDA.");
            }
        }
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
                    p.setNombreEstadio("POR DEFINIR");
                    fixture.add(p);
                }
            }
        }
    }

    public void cargarJugadores(String ruta) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ruta), StandardCharsets.UTF_8))) {

            String linea;
            br.readLine();
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(",");
                if (p.length < 3) {
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
                    jugadores.add(jug);
                }
            }
            vista.mensaje("JUGADORES CARGADOS: " + jugadores.size());
        } catch (IOException ex) {
            vista.mensaje("ERROR LEYENDO JUGADORES: " + ex.getMessage());
        }
    }

    private int parseEdad(String campo) {
        try {
            return Integer.parseInt(campo.trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .trim();
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
            br.readLine();
            int indice = 0;
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(",");
                String nombre = p[0].trim();
                int puntaje = Integer.parseInt(p[1].trim());

                Equipos e = new Equipos(nombre, puntaje);
                e.setGrupo(String.valueOf((char) ('A' + (indice / 4))));
                equipos.add(e);
                grupos.asignarAGrupo(e, indice);
                indice++;
            }
            vista.mensaje("EQUIPOS CARGADOS: " + equipos.size());
        } catch (IOException ex) {
            vista.mensaje("ERROR LEYENDO EQUIPOS: " + ex.getMessage());
        }
    }

    public void jugarPartido() {
        if (faseActual == null) {
            return;
        }

        if (faseActual == Fase.GRUPOS) {
            vista.mostrarFixtureGrupos(grupos, fixture);
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

            List<Partidos> pendientes = fixture.stream()
                    .filter(p -> !p.isJugado() && p.getFase() == Fase.GRUPOS && listaEquipos.contains(p.getEquipoLocal()))
                    .toList();

            if (pendientes.isEmpty()) {
                vista.mensaje("NO QUEDAN PARTIDOS PENDIENTES EN EL GRUPO " + letra + ".");
                return;
            }

            int n = vista.menuSeleccionarPartido(pendientes);
            if (n <= 0 || n > pendientes.size()) {
                return;
            }

            procesarResultado(pendientes.get(n - 1));
            finalizarFaseGrupos();
            return;
        }

        List<Partidos> pendientes = fixture.stream()
                .filter(p -> !p.isJugado() && p.getFase() == faseActual)
                .toList();

        if (pendientes.isEmpty()) {
            vista.mensaje("NO HAY PARTIDOS PENDIENTES EN " + faseActual + ".");
            return;
        }

        vista.mensaje("\n=== PARTIDOS PENDIENTES – " + faseActual + " ===");
        int n = vista.menuSeleccionarPartido(pendientes);
        if (n <= 0 || n > pendientes.size()) {
            return;
        }

        procesarResultado(pendientes.get(n - 1));
        avanzarFaseSiCorresponde();
    }

    private void procesarResultado(Partidos partido) {
        Equipos local = partido.getEquipoLocal();
        Equipos visita = partido.getEquipoVisitante();

        if (partido.getNombreEstadio().equals("POR DEFINIR")) {
            partido.setNombreEstadio(vista.pedirEstadio());
        }

        int gL = vista.pedirGoles(local.getNombreEquipo());
        int gV = vista.pedirGoles(visita.getNombreEquipo());

        partido.setGolesLocal(gL);
        partido.setGolesVisitante(gV);
        partido.setJugado(true);

        if (partido.getFase() == Fase.GRUPOS) {
            if (gL > gV) {
                local.sumarPuntos(3);
            } else if (gV > gL) {
                visita.sumarPuntos(3);
            } else {
                local.sumarPuntos(1);
                visita.sumarPuntos(1);
            }
            local.sumarGolesAFavor(gL);
            local.sumarGolesEnContra(gV);
            visita.sumarGolesAFavor(gV);
            visita.sumarGolesEnContra(gL);
            local.actualizarDiferenciaGoles();
            visita.actualizarDiferenciaGoles();
        } else {
            Equipos perd = (gL > gV) ? visita : local;
            perd.setPuntuacionEquipo(-1);
        }

        vista.mensaje("RESULTADO REGISTRADO.");
    }

    private void finalizarFaseGrupos() {
        if (faseActual != Fase.GRUPOS) {
            return;
        }

        if (fixture.stream().anyMatch(p -> p.getFase() == Fase.GRUPOS && !p.isJugado())) {
            return;
        }

        List<Equipos> clasificados = new ArrayList<>();
        for (List<Equipos> g : List.of(grupos.getGrupoA(), grupos.getGrupoB(), grupos.getGrupoC(), grupos.getGrupoD())) {
            g.sort((a, b) -> Integer.compare(b.getPuntuacionEquipo(), a.getPuntuacionEquipo()));
            Equipos segundo = g.get(1);
            Equipos tercero = g.get(2);

            if (segundo.getPuntuacionEquipo() == tercero.getPuntuacionEquipo()) {
                Partidos tie = new Partidos();
                tie.setEquipoLocal(segundo);
                tie.setEquipoVisitante(tercero);
                tie.setFase(Fase.GRUPOS);
                tie.setNombreEstadio("DESEMPATE");
                fixture.add(tie);
                vista.mensaje("SE AGENDO DESEMPATE " + segundo.getNombreEquipo() + " VS " + tercero.getNombreEquipo());
                return;
            }
            clasificados.add(g.get(0));
            clasificados.add(segundo);
        }
        crearCuartos(clasificados);
        vista.mensaje("FASE DE GRUPOS FINALIZADA! PASANDO A CUARTOS DE FINAL...");
        faseActual = Fase.CUARTOS;
    }

    private void crearCuartos(List<Equipos> c) {
        int[][] cruces = {{0, 3}, {2, 1}, {4, 7}, {6, 5}};
        for (int[] par : cruces) {
            Partidos p = new Partidos();
            p.setEquipoLocal(c.get(par[0]));
            p.setEquipoVisitante(c.get(par[1]));
            p.setFase(Fase.CUARTOS);
            p.setNombreEstadio("POR DEFINIR");
            fixture.add(p);
        }
        vista.mensaje("CUARTOS DE FINAL GENERADOS.");
    }

    private void crearSemis(List<Equipos> c) {
        int[][] cruces = {{0, 1}, {2, 3}};
        for (int[] par : cruces) {
            Partidos p = new Partidos();
            p.setEquipoLocal(c.get(par[0]));
            p.setEquipoVisitante(c.get(par[1]));
            p.setFase(Fase.SEMIS);
            p.setNombreEstadio("POR DEFINIR");
            fixture.add(p);
        }
        vista.mensaje("SEMIFINALES GENERADAS.");
    }

    private void crearFinal(List<Equipos> c) {
        Partidos finalP = new Partidos();
        finalP.setEquipoLocal(c.get(0));
        finalP.setEquipoVisitante(c.get(1));
        finalP.setFase(Fase.FINAL);
        finalP.setNombreEstadio("POR DEFINIR");
        fixture.add(finalP);
        vista.mensaje("FINAL GENERADA.");
    }

    private void avanzarFaseSiCorresponde() {
        List<Partidos> pendientes = fixture.stream()
                .filter(p -> p.getFase() == faseActual && !p.isJugado())
                .toList();

        if (!pendientes.isEmpty()) {
            return;
        }

        List<Partidos> partidosJugados = fixture.stream()
                .filter(p -> p.getFase() == faseActual && p.isJugado())
                .toList();

        List<Equipos> ganadores = new ArrayList<>();
        for (Partidos p : partidosJugados) {
            int gL = p.getGolesLocal();
            int gV = p.getGolesVisitante();
            Equipos ganador = (gL > gV) ? p.getEquipoLocal() : p.getEquipoVisitante();
            ganadores.add(ganador);
        }

        if (faseActual == Fase.CUARTOS) {
            crearSemis(ganadores);
            faseActual = Fase.SEMIS;
            vista.mensaje("SEMIFINALES GENERADAS!");
        } else if (faseActual == Fase.SEMIS) {
            crearFinal(ganadores);
            faseActual = Fase.FINAL;
            vista.mensaje("FINAL GENERADA!");
        } else if (faseActual == Fase.FINAL) {
            Equipos ganadorFinal = ganadores.get(0);
            vista.mensaje("\n¡EL GANADOR DEL MUNDIAL ES: " + ganadorFinal.getNombreEquipo() + "!");
            System.exit(0);
        }
    }
}
