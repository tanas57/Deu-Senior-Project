package net.muslu.seniorproject;

import net.muslu.seniorproject.Algorithm.Chromosome;

import java.util.ArrayList;

public class Functions {

    private static ArrayList<Chromosome> routes = new ArrayList<>();

    public static ArrayList<Chromosome> getRoutes() {
        return routes;
    }

    public static void setRoutes(ArrayList<Chromosome> routes) {
        Functions.routes = routes;
    }

    public static void addRoute(Chromosome chromosome) {
        routes.add(chromosome);
    }
}
