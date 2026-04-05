package fr.univamu.iut.menus.application;

import fr.univamu.iut.menus.domain.Menu;
import fr.univamu.iut.menus.domain.MenuRepositoryInterface;
import fr.univamu.iut.menus.domain.Plat;
import fr.univamu.iut.menus.infrastructure.PlatClient;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Cas d'utilisation liés aux menus.
 **/
public class MenuService
{
    protected MenuRepositoryInterface menuRepo;
    protected PlatClient platClient;

    /**
     * Constructeur
     * @param menuRepo accès aux données des menus
     * @param platClient client vers l'API des plats
     **/
    public MenuService(MenuRepositoryInterface menuRepo, PlatClient platClient)
    {
        this.menuRepo = menuRepo;
        this.platClient = platClient;
    }

    /**
     * Retourne tous les menus au format JSON
     * @return chaîne JSON
     **/
    public String getAllMenusJSON()
    {
        List<Menu> menus = menuRepo.getAllMenus();
        String result = null;

        try (Jsonb jsonb = JsonbBuilder.create())
        {
            result = jsonb.toJson(menus);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }

        return result;
    }

    /**
     * Retourne un menu au format JSON
     * @param id identifiant du menu
     * @return chaîne JSON ou null si non trouvé
     **/
    public String getMenuJSON(int id)
    {
        Menu menu = menuRepo.getMenu(id);

        if (menu == null)
        {
            return null;
        }

        String result = null;

        try (Jsonb jsonb = JsonbBuilder.create())
        {
            result = jsonb.toJson(menu);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }

        return result;
    }

    /**
     * Crée un menu après vérification des plats dans l'API externe
     * @param menu le menu à créer
     * @return true si succès
     **/
    public boolean addMenu(Menu menu)
    {
        List<Plat> platsVerifies = new ArrayList<>();

        for (Plat plat : menu.getPlats())
        {
            Plat platExterne = platClient.getPlat(plat.getId());

            if (platExterne == null)
            {
                System.err.println("Plat introuvable : id=" + plat.getId());
                return false;
            }

            platsVerifies.add(platExterne);
        }

        menu.setPlats(platsVerifies);
        menu.setPrixTotal(platsVerifies.stream().mapToDouble(Plat::getPrix).sum());
        menu.setDateCreation(LocalDate.now());
        menu.setDateMiseAJour(LocalDate.now());

        return menuRepo.addMenu(menu);
    }

    /**
     * Met à jour un menu existant
     * @param id identifiant du menu
     * @param menu nouvelles données
     * @return true si succès
     **/
    public boolean updateMenu(int id, Menu menu)
    {
        if (menuRepo.getMenu(id) == null)
        {
            return false;
        }

        List<Plat> platsVerifies = new ArrayList<>();

        for (Plat plat : menu.getPlats())
        {
            Plat platExterne = platClient.getPlat(plat.getId());

            if (platExterne == null)
            {
                System.err.println("Plat introuvable : id=" + plat.getId());
                return false;
            }

            platsVerifies.add(platExterne);
        }

        menu.setPlats(platsVerifies);
        menu.setPrixTotal(platsVerifies.stream().mapToDouble(Plat::getPrix).sum());
        menu.setDateMiseAJour(LocalDate.now());

        return menuRepo.updateMenu(id, menu);
    }

    /**
     * Supprime un menu
     * @param id identifiant du menu
     * @return true si succès
     **/
    public boolean deleteMenu(int id)
    {
        return menuRepo.deleteMenu(id);
    }
}