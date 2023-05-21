package services;

import BD.ConnectionBD;

import encodage.Encode;
import exceptions.abonneNonTrouveException;

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

public class ServiceEmprunt extends ServiceCommun {

    private ConnectionBD dbConnect = new ConnectionBD();

    public ServiceEmprunt(Socket s) {
        super(s);
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

            sOut.println(Encode.encoder("Veuillez saisir le numéro d'un livre à emprunter. \n" + super.listeDesDocuments()));
            int numDvdChoisi = Integer.parseInt(sIn.readLine());

            try {
                this.emprunterDocument(numAbo, numDvdChoisi);
                sOut.println(Encode.encoder("Vous avez emprunté ce document."));
            } catch (documentNonTrouveException e) {
                sOut.println(Encode.encoder("Ce document n'existe pas."));
            } catch (SQLException e) {
                sOut.println(Encode.encoder("Erreur SQL."+e.getMessage()));
            } catch (documentNonLibreException e) {
                sOut.println(Encode.encoder("Ce document n'est pas libre."));
            } catch (documentDejaEmprunteException e) {
                sOut.println(Encode.encoder("Ce document est déjà emprunté."));
            } catch (documentDejaReserveException e) {
                sOut.println(Encode.encoder("Ce document est déjà réservé."));
            } catch (IOException e) {
                sOut.println(Encode.encoder("Erreur d'entrée/sortie."));
            } catch (InterruptedException e) {
                sOut.println(Encode.encoder("Erreur d'interruption."));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (abonneNonTrouveException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void emprunterDocument(int numAbo, int numDocument) throws abonneNonTrouveException, documentNonTrouveException, SQLException, documentNonLibreException, IOException, documentDejaEmprunteException, documentDejaReserveException, InterruptedException {
        synchronized (this) {
            Abonne ab = this.getAbonne(numAbo);
            IDocument d = super.getDocument(numDocument);
            if (d.empruntePar() == null &&
                    (d.reservePar() == null || d.reservePar().getNumeroAdhérent() == ab.getNumeroAdhérent())) {
                d.emprunt(ab);
                this.dbConnect.emprunterDocument(d, ab);
                System.out.println("Document emprunté.");
            } else {
                if (d.empruntePar() != null) {
                    throw new documentDejaEmprunteException();
                } else if (d.reservePar() != null || d.reservePar().getNumeroAdhérent() != ab.getNumeroAdhérent()) {
                    throw new documentDejaReserveException();
                }
                throw new documentNonLibreException();
            }
        }
    }
}
