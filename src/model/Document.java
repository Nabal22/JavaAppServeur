package model;

import exceptions.RestrictionException;

import java.util.GregorianCalendar;

public abstract class Document implements IDocument {

    protected int idDocument; // Identifiant du document
    protected String titre; // Titre du document
    protected Enum etat; // État du document (réservé, emprunté, libre)
    protected Abonne abonne; // Abonné qui a emprunté ou réservé le document
    protected GregorianCalendar dateReservation; // Date de réservation du document

    /**
     * Constructeur de la classe Document.
     * @param idDocument l'identifiant du document.
     * @param titre le titre du document.
     * @param etat l'état du document.
     */
    public Document(int idDocument, String titre, Etat etat) {
        this.idDocument = idDocument;
        this.titre = titre;
        this.etat = etat;
    }

    /**
     * Constructeur de la classe Document avec abonné.
     * @param idDocument l'identifiant du document.
     * @param titre le titre du document.
     * @param etat l'état du document.
     * @param abonne l'abonné qui a emprunté ou réservé le document.
     */
    public Document(int idDocument, String titre, Etat etat, Abonne abonne) {
        this.idDocument = idDocument;
        this.titre = titre;
        this.etat = etat;
        this.abonne = abonne;
    }

    /**
     * Constructeur de la classe Document avec abonné et date de réservation.
     * @param idDocument l'identifiant du document.
     * @param titre le titre du document.
     * @param etat l'état du document.
     * @param abonne l'abonné qui a emprunté ou réservé le document.
     * @param dateReservation la date de réservation du document.
     */
    public Document(int idDocument, String titre, Etat etat, Abonne abonne, GregorianCalendar dateReservation) {
        this.idDocument = idDocument;
        this.titre = titre;
        this.etat = etat;
        this.abonne = abonne;
        this.dateReservation = dateReservation;
    }


    /**
     * Retourne l'abonné qui a emprunté le document.
     * @return l'abonné qui a emprunté le document, ou null si le document n'est pas emprunté.
     */
    public Abonne empruntePar() {
        if (this.etat.equals(Etat.EMPRUNTE)) {
            return this.abonne; // Retourne l'abonné qui a emprunté ce document
        } else {
            return null;
        }
    }

    /**
     * Retourne l'abonné qui a réservé le document.
     * @return l'abonné qui a réservé le document, ou null s'il n'y a pas de réservation.
     */
    public Abonne reservePar() {
        if (this.etat.equals(Etat.RESERVE)) {
            return this.abonne;
        } else {
            return null;
        }
    }

    /**
     * Réserve le document pour un abonné et enregistre la date de réservation.
     * @param ab l'abonné qui souhaite réserver le document.
     * @throws RestrictionException si l'abonné ne remplit pas les conditions pour emprunter le document.
     */
    public void reservation(Abonne ab) {
        assert (this.etat.equals(Etat.LIBRE) && this.estAutorise(ab));
        if (this.estAutorise(ab)) {
            this.etat = Etat.RESERVE;
            this.abonne = ab;
            this.dateReservation = new GregorianCalendar();
        } else {
            throw new RestrictionException("Vous n'avez pas l'âge requis pour emprunter ce document.");
        }
    }

    /**
     * Emprunte le document par un abonné s'il est autorisé.
     * @param ab l'abonné qui souhaite emprunter le document.
     * @throws RestrictionException si l'abonné ne remplit pas les conditions pour emprunter le document.
     */
    public void emprunt(Abonne ab) {
        assert ((this.etat.equals(Etat.RESERVE) && this.abonne == ab) ||
                (this.etat.equals(Etat.LIBRE) && this.abonne == null));
        if (this.estAutorise(ab)) {
            this.etat = Etat.EMPRUNTE;
            this.abonne = ab;
        } else {
            throw new RestrictionException("Vous n'avez pas l'âge requis pour emprunter ce document.");
        }
    }

    /**
     * Retourne le document et annule la réservation.
     */
    public void retour() {
        assert (this.etat.equals(Etat.EMPRUNTE));
        this.etat = Etat.LIBRE;
        this.abonne = null;
    }

    /**
     * Méthode abstraite pour vérifier si un abonné est autorisé à emprunter le document.
     * @param ab l'abonné à vérifier.
     * @return true si l'abonné est autorisé à emprunter le document, false sinon.
     */
    public abstract boolean estAutorise(Abonne ab);

    /**
     * Renvoie une représentation sous forme de chaîne de caractères de l'objet Document.
     * @return une chaîne de caractères représentant l'objet Document.
     */
    public String toString() {
        String etatString = this.etat.equals(Etat.EMPRUNTE) ? "emprunté" : this.etat.equals(Etat.LIBRE) ? "libre" : "réservé";
        String abonneString = this.abonne == null ? "aucun" : this.abonne.toString();

        return "Document [idDocument=" + idDocument + ", titre=" + titre + ", état=" + etatString + ", abonné=" + abonneString + ", date de réservation=" + dateReservation + "]";
    }
}
