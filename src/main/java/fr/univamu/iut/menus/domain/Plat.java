package fr.univamu.iut.menus.domain;

/**
 * Classe représentant un plat proposé par l'entreprise.
 * Les données proviennent de l'API Plats et Utilisateurs (port 3003).
 *
 * Proviens de Lou (adapter légèrement)
 */
public class Plat
{
    protected int id;
    protected String nom;
    protected String description;
    protected double prix;

    /** Constructeur par défaut requis pour JSON-B */
    public Plat() {}

    /**
     * Constructeur d'un plat
     * @param nom         nom du plat
     * @param description description du plat
     * @param prix        prix du plat
     */
    public Plat(String nom, String description, double prix)
    {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    @Override
    public String toString()
    {
        return "Plat{id=" + id + ", nom='" + nom + "', prix=" + prix + '}';
    }
}