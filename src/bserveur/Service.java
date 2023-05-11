package bserveur;

import java.net.Socket;

public abstract class Service implements Runnable {
    protected final Socket s;

    public Service(Socket s) {
        this.s = s;
    }

    public abstract void run();
}