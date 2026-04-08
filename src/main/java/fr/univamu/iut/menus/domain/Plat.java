package fr.univamu.iut.menus.domain;

/**
 * Classe représentant un plat proposé par l'entreprise.
 * Les données proviennent de l'API Plats et Utilisateurs (port 3003).
 *
 * @author Lou Decamps
 */
public class Plat {

    /** Identifiant unique du plat en base de données */
    protected int id;

    /** Nom du plat (ex: "Salade niçoise") */
    protected String nom;

    /** Description détaillée du plat */
    protected String description;

    /** Prix du plat en euros */
    protected double prix;

    /**
     * Constructeur par défaut requis pour la désérialisation JSON-B.
     */
    public Plat(){
    }

    /**
     * Constructeur permettant de créer un plat avec ses informations.
     * @param nom         nom du plat
     * @param description description du plat
     * @param prix        prix du plat en euros
     */
    public Plat(String nom, String description, double prix) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
    }

    /**
     * Retourne l'identifiant du plat.
     * @return l'identifiant unique
     */
    public int getId() { return id; }

    /**
     * Retourne le nom du plat.
     * @return le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne la description du plat.
     * @return la description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retourne le prix du plat.
     * @return le prix en euros
     */
    public double getPrix() {
        return prix;
    }

    /**
     * Définit l'identifiant du plat.
     * @param id identifiant à affecter
     */
    public void setId(int id) {
        this.id = id;
    }


    public void setNom(String nom) {
        this.nom = nom;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Plat{" + "id='" + id + ", nom='" + nom + ", description='" + description + ", prix=" + prix + '}';
    }
}