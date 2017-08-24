
package Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable{
    
    private final Socket client;
    private final BufferedReader in;
    private final PrintWriter out;
    private boolean running;
    
    public Client() throws IOException {
        client = new Socket("localhost",8000);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream()); 
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
        for(boolean b : bools) {
            sb.append(b ? '1' : '0');
        }
        sendMsg(sb.toString());
    }
    @Override
    public void run() {
        running = true;
        while(running && !client.isClosed())
        {
            String input;
            try {
                input = in.readLine();
                if(input.startsWith("in:")) {
                    Main.getGame().addObject(Gameobject.fromString(input));
                }
                //Game.serverMessage(input);
            } catch (IOException ex) {
                
            }
        }
    }
    
}
