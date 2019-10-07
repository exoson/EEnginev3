package client.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.AuthenticationException;

public class Client implements Runnable{

    private final Socket client;
    private final BufferedReader in;
    private final PrintWriter out;
    private boolean running;

    public Client(String[] args) throws IOException, AuthenticationException {
        String ipAddress = args[0];
        String secret = args[1];
        client = new Socket(ipAddress, 8000);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream());
        out.println(secret);
        out.flush();
        String ret = in.readLine();
        if ("fail".equals(ret)) {
            client.close();
            out.close();
            in.close();
            throw new AuthenticationException();
        }
    }

    public void stop() {
        running = false;
        sendMsg("quit");
        try {
            client.close();
            out.close();
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMsg(String msg) {
        out.println(msg);
        out.flush();
    }

    public void sendMsg(boolean[] bools) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bools.length; i++) {
            if(bools[i]) {
                sb.append(i).append(",");
            }
        }
        sendMsg(sb.toString());
    }
    @Override
    public void run() {
        running = true;
        while(running && !client.isClosed()) {
            String input;
            try {
                input = in.readLine();
                //System.out.println(input);
                if(input.startsWith("in;")) {
                    Main.getGame().addObject(input);
                } else if(input.startsWith("up;")) {
                    Main.getGame().updateStates(input);
                } else if(input.startsWith("rm;")) {
                    System.out.println(input);
                    Main.getGame().removeObject(Integer.parseInt(input.substring(3)));
                } else if (input.equals("quit")) {
                    running = true;
                    Main.getGame().setFlag("running", false);
                }
                //Game.serverMessage(input);
            } catch (IOException ex) {

            }
        }
    }

}
