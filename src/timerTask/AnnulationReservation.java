package timerTask;

import model.IDocument;

import java.sql.SQLException;
import java.util.TimerTask;

public class AnnulationReservation extends TimerTask {

    private IDocument document; // Le document associé à la tâche d'annulation de réservation
    private BD.ConnectionBD db; // Objet de connexion à la base de données

    public AnnulationReservation(IDocument document, BD.ConnectionBD db) {
        this.document = document;
        this.db = db;
    }

    @Override
    public void run() {
        try {
            // Vérifie si le document est réservé et non emprunté
            if (this.document.reservePar() != null && this.document.empruntePar() == null) {
                // Annule la réservation du document dans la base de données
                this.db.annulerReservation(this.document);
                // Effectue le retour du document
                document.retour();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
