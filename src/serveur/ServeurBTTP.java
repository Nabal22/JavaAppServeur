package serveur;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import services.Service;

public class ServeurBTTP implements Runnable {
    private int port; // Port sur lequel le serveur écoute les connexions entrantes
    private Class<? extends Service> c; // Classe de service à utiliser pour traiter les requêtes

    /**
     * Constructeur de la classe ServeurBTTP.
     * @param c la classe de service à utiliser.
     * @param port le port sur lequel le serveur écoute les connexions entrantes.
     */
    public ServeurBTTP(Class<? extends Service> c, int port) {
        this.port = port;
        this.c = c;
    }

    /**
     * Méthode exécutée par le thread du serveur.
     */
    public void run() {
        try {
            ServerSocket monServeur = new ServerSocket(this.port); // Crée un socket serveur lié au port spécifié
            System.out.println("Serveur lancé sur le port " + this.port + ".");

            while(true) {
                Socket socketCoteServeur = monServeur.accept(); // Attend une connexion entrante et accepte la connexion
                // Crée un nouveau thread pour gérer la connexion client en utilisant la classe de service spécifiée
                (new Thread((Runnable)this.c.getConstructor(Socket.class).newInstance(socketCoteServeur))).start();
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | IOException var3) {
            var3.printStackTrace();
        }
    }
}
