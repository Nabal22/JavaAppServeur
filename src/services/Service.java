package services;

import java.net.Socket;

public abstract class Service implements Runnable {
    protected final Socket s; // Socket représentant la connexion client

    /**
     * Constructeur de la classe Service.
     * @param s le socket représentant la connexion client.
     */
    public Service(Socket s) {
        this.s = s;
    }

    /**
     * Méthode abstraite à implémenter par les classes filles.
     * Contient le code à exécuter lors de l'exécution du service.
     */
    public abstract void run();
}
