package timerTask;

import model.IDocument;

import java.sql.SQLException;
import java.util.TimerTask;

public class AnnulationReservation extends TimerTask {

    private IDocument document;
    private BD.ConnectionBD db;

    public AnnulationReservation(IDocument document, BD.ConnectionBD db) {
        this.document = document;
        this.db = db;
    }

    @Override
    public void run() {
        try {
            if (this.document.reservePar() != null){
                this.db.annulerReservation(this.document);
                document.retour();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
