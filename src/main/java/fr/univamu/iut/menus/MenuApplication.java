package fr.univamu.iut.menus;

import fr.univamu.iut.menus.domain.MenuRepositoryInterface;
import fr.univamu.iut.menus.infrastructure.MenuRepositoryMariadb;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Point d'entrée de l'application Menus.
 * Gère la connexion à MariaDB via CDI.
 **/
@ApplicationPath("/api")
@ApplicationScoped
public class MenuApplication extends Application
{
    static Dotenv dotenv = Dotenv.load();

    private static final String DB_URL = dotenv.get("DB_URL");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PWD = dotenv.get("DB_PASSWORD");

    /**
     * Ouvre la connexion à MariaDB — appelé par CDI au démarrage
     * @return instance connectée de MenuRepositoryMariadb
     **/
    @Produces
    public MenuRepositoryInterface openDbConnection()
    {
        MenuRepositoryMariadb db = null;

        try
        {
            db = new MenuRepositoryMariadb(DB_URL, DB_USER, DB_PWD);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        return db;
    }

    /**
     * Ferme la connexion à MariaDB
     * @param menuRepo la connexion à fermer
     **/
    private void closeDbConnection(@Disposes MenuRepositoryInterface menuRepo)
    {
        menuRepo.close();
    }

    /** Pour l'API de Lou sur les plats **/
//    @Produces
//    private PlatRepositoryInterface connectUserApi()
//    {
//        return new PlatRepositoryAPI("http://localhost:8080/projet-1.0-SNAPSHOT/api/");
//    }
}