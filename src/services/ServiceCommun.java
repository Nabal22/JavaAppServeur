package services;

import BD.ConnectionBD;
import bserveur.Service;
import exceptions.RestrictionException;
import model.Abonne;
import model.IDocument;

import java.net.Socket;
import java.util.ArrayList;

public class ServiceCommun extends Service {

    private static ArrayList<Abonne> abonnes;
    private static ArrayList<IDocument> documents;

    protected ConnectionBD dbConnect;

    public static void setConnectionDB(ConnectionBD dbConnect) {
        dbConnect = dbConnect;
    }

    public static void setAbonnes(ArrayList<Abonne> abonnes) {
        ServiceCommun.abonnes = abonnes;
    }
    public static void setDocuments(ArrayList<IDocument> documents) {
        ServiceCommun.documents = documents;
    }

    public ConnectionBD getDbConnect() {
        return dbConnect;
    }

    public ServiceCommun(Socket s) {
        super(s);
    }

    @Override
    public void run() {}

    protected IDocument getDocument(int numDvd) throws RestrictionException {
        for (int i = 0; i < this.documents.size(); i++) {
            if(this.documents.get(i).numero() == numDvd) {
                return this.documents.get(i);
            }
        }
        throw new RestrictionException("Document non trouvé.");
    }

    protected boolean isAbonne(int numAbo) {
        for (int i = 0; i < this.abonnes.size(); i++) {
            if(this.abonnes.get(i).getNumeroAdhérent() == numAbo) {
                return true;
            }
        }
        return false;
    }

    protected Abonne getAbonne(int numAbo) throws RestrictionException {
        for (int i = 0; i < this.abonnes.size(); i++) {
            if(this.abonnes.get(i).getNumeroAdhérent() == numAbo) {
                return this.abonnes.get(i);
            }
        }
        throw new RestrictionException("Abonne non trouvé");
    }

    protected String listeDesDocuments() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.documents.size(); i++) {
            s.append(this.documents.get(i).numero() + "-" + this.documents.get(i).toString() + "\n");
        }
        return s.toString();
    }
}
