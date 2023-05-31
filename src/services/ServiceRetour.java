package services;

import model.IDocument;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class ServiceRetour extends ServiceCommun {

    public ServiceRetour(Socket s) {
        super(s);
    }

    @Override
    public void run() {
        try {
            BufferedReader sIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter sOut = new PrintWriter(s.getOutputStream(), true);

            // Demande au client de saisir le numéro du document à retourner
            sOut.println("Veuillez saisir le numéro du document à retourner.");
            int numDocumentChoisi = Integer.parseInt(sIn.readLine());

            try {
                // Tente de retourner le document
                this.retournerDocument(getDocument(numDocumentChoisi));
                sOut.println("Le document a bien été retourné.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Effectue le retour d'un document.
     * @param document le document à retourner.
     * @throws SQLException en cas d'erreur SQL lors du retour du document.
     */
    public void retournerDocument(IDocument document) throws SQLException {
        synchronized (document) {
            document.retour();
            super.dbConnect.retournerDocument(document);
        }
    }
}
