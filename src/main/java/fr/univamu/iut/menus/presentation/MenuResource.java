package fr.univamu.iut.menus.presentation;

import fr.univamu.iut.menus.application.MenuService;
import fr.univamu.iut.menus.domain.Menu;
import fr.univamu.iut.menus.domain.MenuRepositoryInterface;
import fr.univamu.iut.menus.infrastructure.PlatClient;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

/**
 * Ressource REST exposant les endpoints de l'API Menus.
 **/
@Path("/menus")
@ApplicationScoped
public class MenuResource
{
    @Inject
    private MenuRepositoryInterface menuRepo;

    private MenuService service;

    public MenuResource()
    { }

    @PostConstruct
    public void init()
    {
        this.service = new MenuService(menuRepo, new PlatClient("http://localhost:3003/"));
    }

    /**
     * GET /menus — liste tous les menus
     * @return JSON avec tous les menus
     **/
    @GET
    @Produces("application/json")
    public String getAllMenus()
    {
        return service.getAllMenusJSON();
    }

    /**
     * GET /menus/{id} — retourne un menu par son id
     * @param id identifiant du menu
     * @return JSON du menu ou 404
     **/
    @GET
    @Path("{id}")
    @Produces("application/json")
    public String getMenu(@PathParam("id") int id)
    {
        String result = service.getMenuJSON(id);

        if (result == null)
        {
            throw new NotFoundException();
        }

        return result;
    }

    /**
     * POST /menus — crée un nouveau menu
     * @param menu le menu à créer
     * @return 201 si succès, 400 si plat invalide
     **/
    @POST
    @Consumes("application/json")
    public Response addMenu(Menu menu)
    {
        boolean created = service.addMenu(menu);

        if (!created)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Impossible de créer le menu : vérifiez les ids des plats.").build();
        }
        return Response.status(Response.Status.CREATED).entity("created").build();
    }

    /**
     * PUT /menus/{id} — met à jour un menu
     * @param id identifiant du menu
     * @param menu nouvelles données
     * @return 200 si succès, 404 si inexistant
     **/
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public Response updateMenu(@PathParam("id") int id, Menu menu)
    {
        if (!service.updateMenu(id, menu))
        {
            throw new NotFoundException();
        }

        return Response.ok("updated").build();
    }

    /**
     * DELETE /menus/{id} — supprime un menu
     * @param id identifiant du menu
     * @return 200 si succès, 404 si inexistant
     **/
    @DELETE
    @Path("{id}")
    public Response deleteMenu(@PathParam("id") int id)
    {
        if (!service.deleteMenu(id))
        {
            throw new NotFoundException();
        }

        return Response.ok("deleted").build();
    }
}