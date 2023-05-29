package services;

import BD.ConnectionBD;
import bserveur.Service;

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

    public ServiceRetour(Socket s) {
        super(s);
    }

    @Override
    public void run() {
        try {
            BufferedReader sIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter sOut = new PrintWriter(s.getOutputStream(), true);

            sOut.println("Veuillez saisir le numéro du document à retourner.");
            int numDocumentChoisi = Integer.parseInt(sIn.readLine());

            try {
                this.retournerDocument(getDocument(numDocumentChoisi));
                sOut.println("Le document a bien été retourné.");
            }
             catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void retournerDocument(IDocument document) throws SQLException {
        synchronized (document) {
            document.retour();
            super.dbConnect.retournerDocument(document);
        }
    }
}
