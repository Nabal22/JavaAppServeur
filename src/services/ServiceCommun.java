package services;

import BD.ConnectionBD;
import exceptions.RestrictionException;
import model.Abonne;
import model.IDocument;

import java.net.Socket;
import java.util.ArrayList;

public class ServiceCommun extends Service {

    private static ArrayList<Abonne> abonnes;
    private static ArrayList<IDocument> documents;

    protected static ConnectionBD dbConnect;

    /**
     * Définit la connexion à la base de données.
     * @param dbConnect l'objet ConnectionBD pour la connexion à la base de données.
     */
    public static void setConnectionDB(ConnectionBD dbConnect) {
        ServiceCommun.dbConnect = dbConnect;
    }

    /**
     * Définit la liste des abonnés.
     * @param abonnes la liste des abonnés.
     */
    public static void setAbonnes(ArrayList<Abonne> abonnes) {
        ServiceCommun.abonnes = abonnes;
    }

    /**
     * Définit la liste des documents.
     * @param documents la liste des documents.
     */
    public static void setDocuments(ArrayList<IDocument> documents) {
        ServiceCommun.documents = documents;
    }

    /**
     * Constructeur de ServiceCommun.
     * @param s le Socket utilisé pour la communication avec le client.
     */
    public ServiceCommun(Socket s) {
        super(s);
    }

    @Override
    public void run() {}

    /**
     * Récupère le document correspondant au numéro spécifié.
     * @param numDvd le numéro du document recherché.
     * @return le document correspondant au numéro spécifié.
     * @throws RestrictionException si le document n'est pas trouvé.
     */
    protected IDocument getDocument(int numDvd) throws RestrictionException {
        for (int i = 0; i < this.documents.size(); i++) {
            if(this.documents.get(i).numero() == numDvd) {
                return this.documents.get(i);
            }
        }
        throw new RestrictionException("Document non trouvé.");
    }

    /**
     * Vérifie si l'abonné avec le numéro spécifié existe.
     * @param numAbo le numéro de l'abonné à vérifier.
     * @return true si l'abonné existe, sinon false.
     */
    protected boolean isAbonne(int numAbo) {
        for (int i = 0; i < this.abonnes.size(); i++) {
            if(this.abonnes.get(i).getNumeroAdhérent() == numAbo) {
                return true;
            }
        }
        return false;
    }

    /**
     * Récupère l'abonné correspondant au numéro spécifié.
     * @param numAbo le numéro de l'abonné recherché.
     * @return l'abonné correspondant au numéro spécifié.
     * @throws RestrictionException si l'abonné n'est pas trouvé.
     */
    protected Abonne getAbonne(int numAbo) throws RestrictionException {
        for (int i = 0; i < this.abonnes.size(); i++) {
            if(this.abonnes.get(i).getNumeroAdhérent() == numAbo) {
                return this.abonnes.get(i);
            }
        }
        throw new RestrictionException("Abonne non trouvé");
    }

    /**
     * Retourne une chaîne de caractères représentant la liste des documents.
     * @return la liste des documents sous forme de chaîne de caractères.
     */
    protected String listeDesDocuments() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.documents.size(); i++) {
            s.append(this.documents.get(i).numero() + "-" + this.documents.get(i).toString() + "\n");
        }
        return s.toString();
    }
}
