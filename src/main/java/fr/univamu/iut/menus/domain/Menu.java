package fr.univamu.iut.menus.domain;

import java.time.LocalDate;
import java.util.List;

/**
 * Entité métier représentant un menu
 **/
public class Menu
{
    /** Identifiant unique du menu **/
    protected int id;
    /** Nom du menu **/
    protected String nom;
    /** Identifiant du créateur **/
    protected int createurId;
    /** Nom du créateur **/
    protected String createurNom;
    /** Date de création **/
    protected LocalDate dateCreation;
    /** Date de dernière mise à jour **/
    protected LocalDate dateMiseAJour;
    /** Liste des plats du menu **/
    protected List<Plat> plats;
    /** Prix total **/
    protected double prixTotal;

    /** Constructeur par défaut requis pour JSON-B **/
    public Menu()
    { }

    /**
     * Constructeur principal
     * @param nom nom du menu
     * @param createurId id du créateur
     * @param createurNom nom du créateur
     * @param plats liste des plats
     **/
    public Menu(String nom, int createurId, String createurNom, List<Plat> plats)
    {
        this.nom = nom;
        this.createurId = createurId;
        this.createurNom = createurNom;
        this.plats = plats;
        this.dateCreation = LocalDate.now();
        this.dateMiseAJour = LocalDate.now();
        this.prixTotal = plats.stream().mapToDouble(Plat::getPrix).sum();
    }

    /** Getter et Setter **/
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getCreateurId() { return createurId; }
    public void setCreateurId(int createurId) { this.createurId = createurId; }

    public String getCreateurNom() { return createurNom; }
    public void setCreateurNom(String createurNom) { this.createurNom = createurNom; }

    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }

    public LocalDate getDateMiseAJour() { return dateMiseAJour; }
    public void setDateMiseAJour(LocalDate dateMiseAJour) { this.dateMiseAJour = dateMiseAJour; }

    public List<Plat> getPlats() { return plats; }
    public void setPlats(List<Plat> plats) { this.plats = plats; }

    public double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(double prixTotal) { this.prixTotal = prixTotal; }

    @Override
    public String toString()
    {
        return "Menu{id= " + id +
                ", nom= " + nom +
                ", createurNom= " + createurNom +
                ", prixTotal= " + prixTotal +
                "}";
    }
}