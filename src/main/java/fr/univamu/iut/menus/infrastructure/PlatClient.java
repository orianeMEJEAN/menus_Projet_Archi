package fr.univamu.iut.menus.infrastructure;

import fr.univamu.iut.menus.domain.Plat;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * Client HTTP vers l'API Plats et Utilisateurs (json-server port 3003).
 **/
public class PlatClient
{
    private final String apiUrl;

    /**
     * Constructeur
     * @param apiUrl URL de base
     **/
    public PlatClient(String apiUrl)
    {
        this.apiUrl = apiUrl;
    }

    /**
     * Récupère un plat par son id
     * @param id identifiant du plat
     * @return le plat ou null si non trouvé
     **/
    public Plat getPlat(int id)
    {
        Client client = ClientBuilder.newClient();

        try
        {
            WebTarget target = client.target(apiUrl).path("plats/" + id);
            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == 200)
            {
                return response.readEntity(Plat.class);
            }
        }
        finally
        {
            client.close();
        }

        return null;
    }

    /**
     * Récupère tous les plats
     * @return liste de tous les plats
     **/
    public List<Plat> getAllPlats()
    {
        Client client = ClientBuilder.newClient();

        try
        {
            WebTarget target = client.target(apiUrl).path("plats");
            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == 200)
            {
                return response.readEntity(new GenericType<List<Plat>>() {});
            }
        }
        finally
        {
            client.close();
        }

        return null;
    }
}