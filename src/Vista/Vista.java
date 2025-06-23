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

public class Vista {

    private final Scanner scanner = new Scanner(System.in);

    public int menu() {
        System.out.println("MENU");
        System.out.println("1. COMENZAR CAMPEONATO");
        System.out.println("2. VER EQUIPOS");
        System.out.println("3. VER GRUPOS");
        System.out.println("0. SALIR");
        System.out.print("INGRESE OPCION: ");
        return Integer.parseInt(scanner.nextLine());
    }

    public int menucampeonato() {
        System.out.println("\n----- MENU CAMPEONATO -----");
        System.out.println("1. JUGAR UN PARTIDO");
        System.out.println("2. VER FIXTURE");
        System.out.println("3. VER TABLA DE POSICIONES");
        System.out.println("0. VOLVER AL MENU PRINCIPAL");
        System.out.print("INGRESE OPCION: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private int leerEntero(String label) {
        int valor;
        while (true) {
            try {
                System.out.print(label.toUpperCase());
                valor = Integer.parseInt(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("INGRESE UN NUMERO VALIDO.");
            }
        }
    }

    public int menuSeleccionarPartido(List<Partidos> partidos) {
        System.out.println("\n=== PARTIDOS PENDIENTES ===");
        for (int i = 0; i < partidos.size(); i++) {
            Partidos p = partidos.get(i);
            String texto = (i + 1) + ". " + p.getEquipoLocal().getNombreEquipo() + " VS " +
                           p.getEquipoVisitante().getNombreEquipo() + " (ESTADIO: " + p.getNombreEstadio() + ")";
            System.out.println(texto.toUpperCase());
        }
        System.out.println("0. VOLVER");
        return leerEntero("SELECCIONE PARTIDO: ");
    }

    public void mensaje(String mensaje) {
        System.out.println(mensaje.toUpperCase());
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

            System.out.println(("==== GRUPO " + letra + " ====").toUpperCase());
            if (lista.isEmpty()) {
                System.out.println("   (TODOS JUGADOS)\n");
                continue;
            }
            for (int i = 0; i < lista.size(); i++) {
                Partidos p = lista.get(i);
                String linea = (i + 1) + ") " + p.getEquipoLocal().getNombreEquipo() + " VS " +
                               p.getEquipoVisitante().getNombreEquipo() + " - (ESTADIO: " + p.getNombreEstadio() + ")";
                System.out.println(linea.toUpperCase());
            }
            System.out.println();
        }
    }

    public char menuSeleccionarGrupo() {
        System.out.print("GRUPO (A-B-C-D ====== PRESIONE 0 PARA VOLVER): ");
        while (true) {
            String in = scanner.nextLine().trim().toUpperCase();
            if (in.equals("0")) {
                return '0';
            }
            if (in.matches("[ABCD]")) {
                return in.charAt(0);
            }
            System.out.print("GRUPO INVALIDO. INTENTE OTRA VEZ: ");
        }
    }

   public void mostrarTablaDePosiciones(List<Equipos> equipos) {
    Map<String, List<Equipos>> equiposPorGrupo = new TreeMap<>();

    for (Equipos e : equipos) {
        equiposPorGrupo.computeIfAbsent(e.getGrupo(), k -> new ArrayList<>()).add(e);
    }

    for (String grupo : equiposPorGrupo.keySet()) {
        System.out.println(("\n=== GRUPO " + grupo + " ===").toUpperCase());

        List<Equipos> listaGrupo = equiposPorGrupo.get(grupo);
        listaGrupo.sort((e1, e2) -> {
            if (e2.getPuntuacionEquipo() != e1.getPuntuacionEquipo()) {
                return Integer.compare(e2.getPuntuacionEquipo(), e1.getPuntuacionEquipo());
            } else {
                return Integer.compare(e2.getDiferenciaGoles(), e1.getDiferenciaGoles());
            }
        });

        System.out.println("EQUIPO              PUNTOS     GOLES A FAVOR   GOLES EN CONTRA   DIFERENCIA DE GOLES");
        System.out.println("--------------------------------------------------------------------------------------");

        for (Equipos e : listaGrupo) {
            String nombre = e.getNombreEquipo().toUpperCase();
            int espaciosNombre = 20 - nombre.length();
            if (espaciosNombre < 1) espaciosNombre = 1;

            String linea = nombre
                    + " ".repeat(espaciosNombre)
                    + "   " + e.getPuntuacionEquipo()
                    + "          " + e.getGolesAFavor()
                    + "              " + e.getGolesEnContra()
                    + "                " + e.getDiferenciaGoles();

            System.out.println(linea);
        }
    }
}


    public void mostrarGrupos(Grupos g) {
        System.out.println("\n=== FASE DE GRUPOS ===");
        imprimirGrupo("GRUPO A", g.getGrupoA());
        imprimirGrupo("GRUPO B", g.getGrupoB());
        imprimirGrupo("GRUPO C", g.getGrupoC());
        imprimirGrupo("GRUPO D", g.getGrupoD());
    }

    private void imprimirGrupo(String nombreGrupo, List<Equipos> equipos) {
        System.out.println(("==== " + nombreGrupo + " ====").toUpperCase());
        if (equipos.isEmpty()) {
            System.out.println("   (SIN EQUIPOS)");
            return;
        }
        for (Equipos e : equipos) {
            String linea = " - " + e.getNombreEquipo() + " (PUNTAJE: " + e.getPuntuacionEquipo() + ")";
            System.out.println(linea.toUpperCase());
        }
        System.out.println();
    }

    public void mostrarEquiposConJugadores(List<Equipos> equipos) {
        System.out.println("\n=== EQUIPOS Y JUGADORES ===");
        for (Equipos e : equipos) {
            System.out.println(("\n• " + e.getNombreEquipo() + " (PUNTOS: " + e.getPuntuacionEquipo() + ")").toUpperCase());
            for (Jugadores j : e.getPlantel()) {
                String linea = "   - " + j.getNombreJugador() + " (" + j.getEdadJugador() + " AÑOS)";
                System.out.println(linea.toUpperCase());
            }
        }
    }

    public String pedirEstadio() {
        System.out.print("INGRESE EL NOMBRE DEL ESTADIO: ");
        return scanner.nextLine();
    }

    public int pedirGoles(String nombreEquipo) {
        System.out.print(("INGRESE LOS GOLES DEL EQUIPO " + nombreEquipo + ": ").toUpperCase());
        while (true) {
            try {
                int goles = Integer.parseInt(scanner.nextLine());
                if (goles < 0) {
                    System.out.print("LOS GOLES NO PUEDEN SER NEGATIVOS. INTENTE NUEVAMENTE: ");
                    continue;
                }
                return goles;
            } catch (NumberFormatException e) {
                System.out.print("ENTRADA INVALIDA. INGRESE UN NUMERO ENTERO: ");
            }
        }
    }
}
