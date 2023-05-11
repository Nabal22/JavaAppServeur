package serveur;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import bserveur.Service;

public class ServeurBTTP implements Runnable {
    private int port;
    private Class<? extends Service> c;

    public ServeurBTTP(Class<? extends Service> c, int port) {
        this.port = port;
        this.c = c;
    }

    public void run() {
        try {
            ServerSocket monServeur = new ServerSocket(this.port);
            System.out.println("Serveur lancé !!!!");

            while(true) {
                Socket socketCoteServeur = monServeur.accept();
                (new Thread((Runnable)this.c.getConstructor(Socket.class).newInstance(socketCoteServeur))).start();
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | IOException var3) {
            var3.printStackTrace();
        }
    }
}
