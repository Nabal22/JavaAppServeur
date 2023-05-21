package services;

import BD.ConnectionBD;
import bserveur.Service;

import exceptions.documentNonEmpruntéException;

import exceptions.documentNonTrouveException;
import model.Abonne;
import model.IDocument;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceRetour extends ServiceCommun {
    private static ArrayList<Abonne> abonnes;
    private static ArrayList<IDocument> dvds;

    private ConnectionBD dbConnect = new ConnectionBD();



    public ServiceRetour(Socket s) {
        super(s);
    }

    public static void setAbonnes(ArrayList<Abonne> abonnes) {
        ServiceRetour.abonnes = abonnes;
    }
    public static void setDvds(ArrayList<IDocument> dvds) {
        ServiceRetour.dvds = dvds;
    }

    @Override
    public void run() {

        try {
            this.dbConnect.connectToBD();

            BufferedReader sIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter sOut = new PrintWriter(s.getOutputStream(), true);

            sOut.println("Veuillez saisir le numéro du document à retourner.");
            int numDocumentChoisi = Integer.parseInt(sIn.readLine());

            System.out.println("test");



            try {
                this.retournerDocument(getDocument(numDocumentChoisi));
                sOut.println("Le document a bien été retourné.");
            } catch (documentNonEmpruntéException e) {
                sOut.println("Le document n'est pas emprunté.");
            } catch (documentNonTrouveException e) {
                sOut.println("Le document n'existe pas dans notre base de données.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void retournerDocument(IDocument document) throws documentNonEmpruntéException, SQLException {
        document.retour();
        this.dbConnect.retournerDocument(document);
    }
}
