package fr.univamu.iut.menus.domain;

import java.util.List;

/**
 * Interface définissant les opérations d'accès aux données des menus.
 **/
public interface MenuRepositoryInterface
{
    /** Ferme la connexion au dépôt de données **/
    void close();

    /**
     * Retourne tous les menus
     * @return liste de tous les menus
     **/
    List<Menu> getAllMenus();

    /**
     * Retourne un menu par son id
     * @param id identifiant du menu
     * @return le menu trouvé, ou null si inexistant
     **/
    Menu getMenu(int id);

    /**
     * Crée un nouveau menu
     * @param menu le menu à créer
     * @return true si succès
     **/
    boolean addMenu(Menu menu);

    /**
     * Met à jour un menu existant
     * @param id identifiant du menu
     * @param menu nouvelles données
     * @return true si succès
     **/
    boolean updateMenu(int id, Menu menu);

    /**
     * Supprime un menu
     * @param id identifiant du menu
     * @return true si succès
     **/
    boolean deleteMenu(int id);
}