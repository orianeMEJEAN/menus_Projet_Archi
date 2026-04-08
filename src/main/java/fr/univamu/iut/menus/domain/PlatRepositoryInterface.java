package fr.univamu.iut.menus.domain;

import java.util.ArrayList;

/**
 * Interface définissant les opérations d'accès aux données des plats.
 * Permet de découpler la logique métier du mécanisme de stockage.
 *
 * @author Lou Decamps
 */
public interface PlatRepositoryInterface
{
    void close();

    Plat getPlat(int id);

    ArrayList<Plat> getAllPlats();

    boolean addPlat(String nom, String description, double prix);

    boolean updatePlat(int id, String nom, String description, double prix);

    boolean deletePlat(int id);
}