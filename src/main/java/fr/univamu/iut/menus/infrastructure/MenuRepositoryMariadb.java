package fr.univamu.iut.menus.infrastructure;

import fr.univamu.iut.menus.domain.Menu;
import fr.univamu.iut.menus.domain.MenuRepositoryInterface;
import fr.univamu.iut.menus.domain.Plat;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Accès aux menus stockés dans MariaDB.
 **/
public class MenuRepositoryMariadb implements MenuRepositoryInterface, Closeable
{
    protected Connection dbConnection;

    /**
     * Constructeur — ouvre la connexion
     * @param infoConnection URL JDBC
     * @param user identifiant
     * @param pwd mot de passe
     **/
    public MenuRepositoryMariadb(String infoConnection, String user, String pwd) throws Exception
    {
        Class.forName("org.mariadb.jdbc.Driver");
        dbConnection = DriverManager.getConnection(infoConnection, user, pwd);
    }

    @Override
    public void close()
    {
        try
        {
            dbConnection.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<Menu> getAllMenus()
    {
        List<Menu> menus = new ArrayList<>();

        String query = "SELECT m.id, m.nom, m.createurId, m.createurNom, " +
                "m.dateCreation, m.dateMiseAJour, m.prixTotal, " +
                "mp.platId, mp.platNom, mp.platPrix " +
                "FROM Menu m " +
                "LEFT JOIN Menu_Plat mp ON m.id = mp.menuId " +
                "ORDER BY m.id";

        try (PreparedStatement ps = dbConnection.prepareStatement(query))
        {
            ResultSet rs = ps.executeQuery();
            menus = construireMenus(rs);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return menus;
    }

    @Override
    public Menu getMenu(int id)
    {
        String query = "SELECT m.id, m.nom, m.createurId, m.createurNom, " +
                "m.dateCreation, m.dateMiseAJour, m.prixTotal, " +
                "mp.platId, mp.platNom, mp.platPrix " +
                "FROM Menu m " +
                "LEFT JOIN Menu_Plat mp ON m.id = mp.menuId " +
                "WHERE m.id = ?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query))
        {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            List<Menu> resultats = construireMenus(rs);

            return resultats.isEmpty() ? null : resultats.get(0);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addMenu(Menu menu)
    {
        String insertMenu = "INSERT INTO Menu (nom, createurId, createurNom, " +
                "dateCreation, dateMiseAJour, prixTotal) VALUES (?, ?, ?, ?, ?, ?)";
        String insertPlat = "INSERT INTO Menu_Plat (menuId, platId, platNom, platPrix) VALUES (?, ?, ?, ?)";

        try
        {
            dbConnection.setAutoCommit(false);

            try (PreparedStatement ps = dbConnection.prepareStatement(insertMenu, Statement.RETURN_GENERATED_KEYS))
            {
                ps.setString(1, menu.getNom());
                ps.setInt(2, menu.getCreateurId());
                ps.setString(3, menu.getCreateurNom());
                ps.setDate(4, Date.valueOf(menu.getDateCreation()));
                ps.setDate(5, Date.valueOf(menu.getDateMiseAJour()));
                ps.setDouble(6, menu.getPrixTotal());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();

                if (keys.next())
                {
                    menu.setId(keys.getInt(1));
                }
            }
            if (menu.getPlats() != null)
            {
                try (PreparedStatement ps = dbConnection.prepareStatement(insertPlat))
                {
                    for (Plat plat : menu.getPlats())
                    {
                        ps.setInt(1, menu.getId());
                        ps.setInt(2, plat.getId());
                        ps.setString(3, plat.getNom());
                        ps.setDouble(4, plat.getPrix());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            dbConnection.commit();

            return true;
        }
        catch (SQLException e)
        {
            try
            {
                dbConnection.rollback();
            }
            catch (SQLException ex)
            {
                System.err.println(ex.getMessage());
            }

            throw new RuntimeException(e);
        }
        finally
        {
            try
            {
                dbConnection.setAutoCommit(true);
            }
            catch (SQLException e)
            {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public boolean updateMenu(int id, Menu menu)
    {
        String updateMenu = "UPDATE Menu SET nom=?, createurId=?, createurNom=?, " + "dateMiseAJour=?, prixTotal=? WHERE id=?";
        String deletePlats = "DELETE FROM Menu_Plat WHERE menuId=?";
        String insertPlat  = "INSERT INTO Menu_Plat (menuId, platId, platNom, platPrix) VALUES (?, ?, ?, ?)";

        try
        {
            dbConnection.setAutoCommit(false);

            try (PreparedStatement ps = dbConnection.prepareStatement(updateMenu))
            {
                ps.setString(1, menu.getNom());
                ps.setInt(2, menu.getCreateurId());
                ps.setString(3, menu.getCreateurNom());
                ps.setDate(4, Date.valueOf(menu.getDateMiseAJour()));
                ps.setDouble(5, menu.getPrixTotal());
                ps.setInt(6, id);

                if (ps.executeUpdate() == 0)
                {
                    dbConnection.rollback();
                    return false;
                }
            }
            try (PreparedStatement ps = dbConnection.prepareStatement(deletePlats))
            {
                ps.setInt(1, id); ps.executeUpdate();
            }
            if (menu.getPlats() != null)
            {
                try (PreparedStatement ps = dbConnection.prepareStatement(insertPlat))
                {
                    for (Plat plat : menu.getPlats())
                    {
                        ps.setInt(1, id);
                        ps.setInt(2, plat.getId());
                        ps.setString(3, plat.getNom());
                        ps.setDouble(4, plat.getPrix());
                        ps.addBatch();
                    }

                    ps.executeBatch();
                }
            }
            dbConnection.commit();

            return true;

        }
        catch (SQLException e)
        {
            try
            {
                dbConnection.rollback();
            }
            catch (SQLException ex)
            {
                System.err.println(ex.getMessage());
            }

            throw new RuntimeException(e);
        }
        finally
        {
            try
            {
                dbConnection.setAutoCommit(true);
            }
            catch (SQLException e)
            {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public boolean deleteMenu(int id)
    {
        String query = "DELETE FROM Menu WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query))
        {
            ps.setInt(1, id);
            return ps.executeUpdate() != 0;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reconstruit les menus depuis un ResultSet
     **/
    private List<Menu> construireMenus(ResultSet rs) throws SQLException
    {
        List<Menu> menus = new ArrayList<>();
        Menu menuCourant = null;

        while (rs.next())
        {
            int idLu = rs.getInt("id");

            if (menuCourant == null || menuCourant.getId() != idLu)
            {
                menuCourant = new Menu();
                menuCourant.setId(idLu);
                menuCourant.setNom(rs.getString("nom"));
                menuCourant.setCreateurId(rs.getInt("createurId"));
                menuCourant.setCreateurNom(rs.getString("createurNom"));
                menuCourant.setDateCreation(rs.getDate("dateCreation").toLocalDate());
                menuCourant.setDateMiseAJour(rs.getDate("dateMiseAJour").toLocalDate());
                menuCourant.setPrixTotal(rs.getDouble("prixTotal"));
                menuCourant.setPlats(new ArrayList<>());
                menus.add(menuCourant);
            }
            if (rs.getString("platNom") != null)
            {
                Plat plat = new Plat();
                plat.setId(rs.getInt("platId"));
                plat.setNom(rs.getString("platNom"));
                plat.setPrix(rs.getDouble("platPrix"));
                menuCourant.getPlats().add(plat);
            }
        }
        return menus;
    }
}