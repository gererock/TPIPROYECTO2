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

public class Controlador {

    private List<Jugadores> jugadores = new ArrayList<>();
    private List<Equipos> equipos = new ArrayList<>();
    private Grupos grupos = new Grupos();
    private List<Partidos> fixture = new ArrayList<>();
    private Fase faseActual = Fase.GRUPOS;
    private Vista vista = new Vista();

    private List<Equipos> equiposVivos() {
        List<Equipos> vivos = new ArrayList<>();
        for (int i = 0; i < equipos.size(); i++) {
            if (equipos.get(i).getPuntuacionEquipo() >= 0) {
                vivos.add(equipos.get(i));
            }
        }
        return vivos;
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            int opcion = vista.menu();
            if (opcion == 1) {
                generarFixtureGrupos();
                faseActual = Fase.GRUPOS;
                vista.mensaje("FIXTURE GENERADO Y FASE DE GRUPOS INICIADA!");
                vista.mostrarFixtureGrupos(grupos, fixture);
                ejecutarFases();
            } else if (opcion == 2) {
                vista.mostrarEquiposConJugadores(equipos);
            } else if (opcion == 3) {
                vista.mostrarGrupos(grupos);
            } else if (opcion == 0) {
                salir = true;
            } else {
                vista.mensaje("OPCION INVALIDA");
            }
        }
    }

    private void ejecutarFases() {
        boolean volver = false;
        while (!volver) {
            int opcion = vista.menucampeonato();
            if (opcion == 1) {
                jugarPartido();
            } else if (opcion == 2) {
                vista.mostrarFixtureGrupos(grupos, fixture);
            } else if (opcion == 3) {
                vista.mostrarTablaDePosiciones(equipos);
            } else if (opcion == 0) {
                volver = true;
            } else {
                vista.mensaje("OPCION INVALIDA.");
            }
        }
    }

    private void generarFixtureGrupos() {
        fixture.clear();

        List<Equipos> grupoA = grupos.getGrupoA();
        List<Equipos> grupoB = grupos.getGrupoB();
        List<Equipos> grupoC = grupos.getGrupoC();
        List<Equipos> grupoD = grupos.getGrupoD();

        List<List<Equipos>> todosGrupos = new ArrayList<>();
        todosGrupos.add(grupoA);
        todosGrupos.add(grupoB);
        todosGrupos.add(grupoC);
        todosGrupos.add(grupoD);

        for (int g = 0; g < todosGrupos.size(); g++) {
            List<Equipos> grupo = todosGrupos.get(g);
            for (int i = 0; i < grupo.size() - 1; i++) {
                for (int j = i + 1; j < grupo.size(); j++) {
                    Partidos p = new Partidos();
                    p.setEquipoLocal(grupo.get(i));
                    p.setEquipoVisitante(grupo.get(j));
                    p.setFase(Fase.GRUPOS);
                    p.setNombreEstadio("POR DEFINIR");
                    fixture.add(p);
                }
            }
        }
    }

    public void cargarJugadores(String ruta) {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(ruta), StandardCharsets.UTF_8));

            br.readLine();
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(",");
                if (p.length < 3) {
                    continue;
                }
                String nombreJugador = p[0].trim();
                int edad = 0;
                try {
                    edad = Integer.parseInt(p[1].trim());
                } catch (Exception e) {
                    edad = 0;
                }
                String nombreEquipo = p[2].trim();

                Jugadores jug = new Jugadores(nombreJugador, edad);
                Equipos eq = buscarEquipoPorNombre(nombreEquipo);

                if (eq != null) {
                    jug.setEquipo(eq);
                    eq.addJugador(jug);
                    jugadores.add(jug);
                }
            }
            br.close();
            vista.mensaje("JUGADORES CARGADOS: " + jugadores.size());
        } catch (IOException ex) {
            vista.mensaje("ERROR LEYENDO JUGADORES: " + ex.getMessage());
        }
    }

    private String normalizar(String texto) {
        String textoNorm = Normalizer.normalize(texto, Normalizer.Form.NFD);
        textoNorm = textoNorm.replaceAll("\\p{M}", "");
        textoNorm = textoNorm.toLowerCase(Locale.ROOT);
        textoNorm = textoNorm.trim();
        return textoNorm;
    }

    private Equipos buscarEquipoPorNombre(String nombre) {
        String buscado = normalizar(nombre);
        for (int i = 0; i < equipos.size(); i++) {
            if (normalizar(equipos.get(i).getNombreEquipo()).equals(buscado)) {
                return equipos.get(i);
            }
        }
        return null;
    }

    public void cargarEquipos(String ruta) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(ruta));
            br.readLine();
            String linea;
            int indice = 0;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(",");
                String nombre = p[0].trim();
                int puntaje = 0;
                try {
                    puntaje = Integer.parseInt(p[1].trim());
                } catch (Exception e) {
                    puntaje = 0;
                }
                Equipos e = new Equipos(nombre, puntaje);
                char letraGrupo = (char) ('A' + (indice / 4));
                e.setGrupo("" + letraGrupo);
                equipos.add(e);
                grupos.asignarAGrupo(e, indice);
                indice++;
            }
            br.close();
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
            List<Equipos> listaEquipos = null;
            if (letra == 'A') {
                listaEquipos = grupos.getGrupoA();
            } else if (letra == 'B') {
                listaEquipos = grupos.getGrupoB();
            } else if (letra == 'C') {
                listaEquipos = grupos.getGrupoC();
            } else {
                listaEquipos = grupos.getGrupoD();
            }

            List<Partidos> pendientes = new ArrayList<>();
            for (int i = 0; i < fixture.size(); i++) {
                Partidos p = fixture.get(i);
                if (!p.isJugado() && p.getFase() == Fase.GRUPOS && listaEquipos.contains(p.getEquipoLocal())) {
                    pendientes.add(p);
                }
            }
            if (pendientes.size() == 0) {
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

        List<Partidos> pendientes = new ArrayList<>();
        for (int i = 0; i < fixture.size(); i++) {
            Partidos p = fixture.get(i);
            if (!p.isJugado() && p.getFase() == faseActual) {
                pendientes.add(p);
            }
        }
        if (pendientes.size() == 0) {
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
            if (gL == gV) {
                vista.mensaje("EMPATE! SE DEFINE POR PENALES.");
                int penalesLocal, penalesVisita;
                do {
                    penalesLocal = vista.pedirGoles("GOLES POR PENALES de " + local.getNombreEquipo());
                    penalesVisita = vista.pedirGoles("GOLES POR PENALES de " + visita.getNombreEquipo());
                    if (penalesLocal == penalesVisita) {
                        vista.mensaje("NO PUEDE HABER EMPATE EN PENALES. INGRESE NUEVAMENTE.");
                    }
                } while (penalesLocal == penalesVisita);

                if (penalesLocal > penalesVisita) {
                    visita.setPuntuacionEquipo(-1);
                    vista.mensaje("GANADOR POR PENALES: " + local.getNombreEquipo());
                } else {
                    local.setPuntuacionEquipo(-1);
                    vista.mensaje("GANADOR POR PENALES: " + visita.getNombreEquipo());
                }
            } else {
                Equipos perdedor = gL > gV ? visita : local;
                perdedor.setPuntuacionEquipo(-1);
            }
        }

        vista.mensaje("RESULTADO REGISTRADO.");
    }

    private void finalizarFaseGrupos() {
        if (faseActual != Fase.GRUPOS) {
            return;
        }
        boolean quedanPartidos = false;
        for (int i = 0; i < fixture.size(); i++) {
            Partidos p = fixture.get(i);
            if (p.getFase() == Fase.GRUPOS && !p.isJugado()) {
                quedanPartidos = true;
                break;
            }
        }
        if (quedanPartidos) {
            return;
        }

        List<Equipos> clasificados = new ArrayList<>();
        List<List<Equipos>> todosGrupos = new ArrayList<>();
        todosGrupos.add(grupos.getGrupoA());
        todosGrupos.add(grupos.getGrupoB());
        todosGrupos.add(grupos.getGrupoC());
        todosGrupos.add(grupos.getGrupoD());

        for (int g = 0; g < todosGrupos.size(); g++) {
            List<Equipos> grupo = todosGrupos.get(g);
            for (int i = 0; i < grupo.size() - 1; i++) {
                for (int j = 0; j < grupo.size() - i - 1; j++) {
                    if (grupo.get(j).getPuntuacionEquipo() < grupo.get(j + 1).getPuntuacionEquipo()) {
                        Equipos temp = grupo.get(j);
                        grupo.set(j, grupo.get(j + 1));
                        grupo.set(j + 1, temp);
                    }
                }
            }

            Equipos segundo = grupo.get(1);
            Equipos tercero = grupo.get(2);

            if (segundo.getPuntuacionEquipo() == tercero.getPuntuacionEquipo()) {
                Partidos desempate = new Partidos();
                desempate.setEquipoLocal(segundo);
                desempate.setEquipoVisitante(tercero);
                desempate.setFase(Fase.GRUPOS);
                desempate.setNombreEstadio("DESEMPATE");
                fixture.add(desempate);
                vista.mensaje("SE AGENDO DESEMPATE " + segundo.getNombreEquipo() + " VS " + tercero.getNombreEquipo());
                return;
            }

            clasificados.add(grupo.get(0));
            clasificados.add(segundo);
        }
        crearCuartos(clasificados);
        vista.mensaje("FASE DE GRUPOS FINALIZADA! PASANDO A CUARTOS DE FINAL...");
        faseActual = Fase.CUARTOS;
    }

    private void crearCuartos(List<Equipos> c) {
        int[][] cruces = {{0, 3}, {2, 1}, {4, 7}, {6, 5}};
        for (int i = 0; i < cruces.length; i++) {
            Partidos p = new Partidos();
            p.setEquipoLocal(c.get(cruces[i][0]));
            p.setEquipoVisitante(c.get(cruces[i][1]));
            p.setFase(Fase.CUARTOS);
            p.setNombreEstadio("POR DEFINIR");
            fixture.add(p);
        }
        vista.mensaje("CUARTOS DE FINAL GENERADOS.");
    }

    private void crearSemis(List<Equipos> c) {
        int[][] cruces = {{0, 1}, {2, 3}};
        for (int i = 0; i < cruces.length; i++) {
            Partidos p = new Partidos();
            p.setEquipoLocal(c.get(cruces[i][0]));
            p.setEquipoVisitante(c.get(cruces[i][1]));
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
        List<Partidos> pendientes = new ArrayList<>();
        for (int i = 0; i < fixture.size(); i++) {
            Partidos p = fixture.get(i);
            if (p.getFase() == faseActual && !p.isJugado()) {
                pendientes.add(p);
            }
        }

        if (pendientes.size() > 0) {
            return;
        }

        List<Partidos> partidosJugados = new ArrayList<>();
        for (int i = 0; i < fixture.size(); i++) {
            Partidos p = fixture.get(i);
            if (p.getFase() == faseActual && p.isJugado()) {
                partidosJugados.add(p);
            }
        }

        List<Equipos> ganadores = new ArrayList<>();
        for (Partidos p : partidosJugados) {
            Equipos local = p.getEquipoLocal();
            Equipos visitante = p.getEquipoVisitante();

            if (local.getPuntuacionEquipo() == -1) {
                ganadores.add(visitante);
            } else if (visitante.getPuntuacionEquipo() == -1) {
                ganadores.add(local);
            } else {
                if (p.getGolesLocal() > p.getGolesVisitante()) {
                    ganadores.add(local);
                } else {
                    ganadores.add(visitante);
                }
            }
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
            vista.mensaje("\nEL GANADOR DEL MUNDIAL ES: " + ganadorFinal.getNombreEquipo() + "!");
            System.exit(0);
        }
    }
}