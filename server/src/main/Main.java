package main;

import behaviors.Transform;
import math.Vector3f;

/**
 *
 * @author Lime
 */
public class Main {
    
    private static ServerGame game;
    
    public static void main(String args[]) {
        game = new ServerGame(new GameMode() {

            @Override
            public void start() {
                
            }

            @Override
            public boolean update() {
                return false;
            }

            @Override
            public void render() {
                
            }

            @Override
            public void reset() {
                
            }
        }, new ClientHandler() {
            int playerId;
            @Override
            public boolean init(ClientServer cs) {
                playerId = game.addObject("in;pos,10,10,0;Transform;Animator");
                return false;
            }

            @Override
            public boolean update(ClientServer cs) {
                String cName = cs.toString();
                int KEY_0 = 0x30;
                int KEY_A = 0x41, KEY_S = 0x53, KEY_W = 0x57, KEY_D = 0x44;
                Gameobject player = Main.getGame().getObject(playerId);
                if(player == null) return false;
                float speed = 0.01f;
                if(Main.getGame().getClientKey(cName, KEY_W)) {
                    ((Transform)player.getBehavior("SpecifiedTransform")).move(new Vector3f(0, -speed, 0));
                }
                if(Main.getGame().getClientKey(cName, KEY_A)) {
                    ((Transform)player.getBehavior("SpecifiedTransform")).move(new Vector3f(-speed, 0, 0));
                }
                if(Main.getGame().getClientKey(cName, KEY_S)) {
                    ((Transform)player.getBehavior("SpecifiedTransform")).move(new Vector3f(0, speed, 0));
                }
                if(Main.getGame().getClientKey(cName, KEY_D)) {
                    ((Transform)player.getBehavior("SpecifiedTransform")).move(new Vector3f(speed, 0, 0));
                }
                return false;
            }

            @Override
            public void quit(ClientServer cs) {
                
            }
        });
        game.run();
    }

    /**
     * @return the game
     */
    public static ServerGame getGame() {
        return game;
    }
}
