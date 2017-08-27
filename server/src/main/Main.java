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
            @Override
            public boolean init(ClientServer cs) {
                int playerId = game.addObject("in;Transform:pos:10,10,0:rot:0,0,0:size:64,64,0;Animator;TankMovement:speed:10.0:rotSpeed:0.05:client:" + cs.toString() + ";CannonBehavior");
                game.setFlag(cs.toString() + "-player", playerId);
                return false;
            }

            @Override
            public boolean update(ClientServer cs) {
                String cName = cs.toString();
                int KEY_0 = 0x30;
                int KEY_A = 0x41, KEY_S = 0x53, KEY_W = 0x57, KEY_D = 0x44;
                int playerId = (int)game.getFlag(cName + "-player");
                Gameobject player = Main.getGame().getObject(playerId);
                //System.out.println(cs.toString() + "-player, " + playerId);
                if(player == null) return false;
                float speed = 0.01f;
                /*if(Main.getGame().getClientKey(cName, KEY_W)) {
                    ((Transform)player.getBehavior("Transform")).move(new Vector3f(0, -speed, 0));
                }
                if(Main.getGame().getClientKey(cName, KEY_A)) {
                    ((Transform)player.getBehavior("Transform")).move(new Vector3f(-speed, 0, 0));
                }
                if(Main.getGame().getClientKey(cName, KEY_S)) {
                    ((Transform)player.getBehavior("Transform")).move(new Vector3f(0, speed, 0));
                }
                if(Main.getGame().getClientKey(cName, KEY_D)) {
                    ((Transform)player.getBehavior("Transform")).move(new Vector3f(speed, 0, 0));
                }*/
                return false;
            }

            @Override
            public void quit(ClientServer cs) {
                String cName = cs.toString();
                System.out.println("Quitting");
                Main.getGame().removeObject((int)game.getFlag(cName + "-player"));
                
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
