package services;
import BD.ConnectionBD;
import bserveur.Service;
import encodage.Encode;
import exceptions.*;
import model.Abonne;
import model.IDocument;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceReservation extends ServiceCommun {
    private static ArrayList<Abonne> abonnes;
    private static ArrayList<IDocument> dvds;

    private ConnectionBD dbConnect = new ConnectionBD();



    public ServiceReservation(Socket s) {
        super(s);
    }

    public static void setAbonnes(ArrayList<Abonne> abonnes) {
        ServiceReservation.abonnes = abonnes;
    }
    public static void setDvds(ArrayList<IDocument> dvds) {
        ServiceReservation.dvds = dvds;
    }

    @Override
    public void run() {
        try {
            this.dbConnect.connectToBD();
            BufferedReader sIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter sOut = new PrintWriter(s.getOutputStream(), true);

            sOut.println(Encode.encoder("Veuillez saisir votre numéro abonné."));
            int numAbo = Integer.parseInt(sIn.readLine());
            while (!this.isAbonne(numAbo)) {
                sOut.println(Encode.encoder("Votre numéro abonné n'est pas reconnu. Veuillez réessayer."));
                numAbo = Integer.parseInt(sIn.readLine());
            }

            sOut.println(Encode.encoder("Veuillez saisir le numéro d'un document à réserver. \n" + super.listeDesDocuments()));
            int numDocumentChoisi = Integer.parseInt(sIn.readLine());



            try {
                this.réserverDocument(numAbo, numDocumentChoisi);
                sOut.println("Réservation effectuée.");
            } catch (documentNonTrouveException e) {
                sOut.println("Ce document n'éxiste pas.");
            } catch (abonneNonTrouveException e) {
                sOut.println("Votre numéro d'abonné n'est pas enregistré.");
            } catch (SQLException e ) {
                sOut.println("sql : " + e.getMessage());
            } catch (documentDejaReserveException e) {
                sOut.println("Ce document est déjà réservé");
            } catch (documentDejaEmprunteException e) {
                sOut.println("Ce document est déjà emprunté");
            } catch (documentNonLibreException e) {
                sOut.println("?");
            } catch (documentPourAdulteException e) {
                sOut.println("Vous n'avez pas l'âge requis pour réserver ce document");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }



    private void réserverDocument(int numAbo, int numDocument) throws SQLException, abonneNonTrouveException, documentNonTrouveException, documentDejaReserveException, documentDejaEmprunteException, documentNonLibreException, documentPourAdulteException {
            Abonne ab = this.getAbonne(numAbo);
            IDocument d = this.getDocument(numDocument);
            d.reservation(ab);
            this.dbConnect.reserverDocumentBD(ab, d);
    }


}
