package server.game;

/**
 *
 * @author Lime
 */
public class DeathMatchClientHandler implements ClientHandler{

            @Override
            public boolean init(ClientServer cs) {
                int playerId = Main.getGame().addObject("in;client:" + cs.toString() + ";file:tank;");
                Main.getGame().setFlag(cs.toString() + "-player", playerId);
                return false;
            }

            @Override
            public boolean update(ClientServer cs) {
//                String cName = cs.toString();
//                int playerId = (int)Main.getGame().getFlag(cName + "-player");
//                Gameobject player = Main.getGame().getObject(playerId);
                return false;
            }

            @Override
            public void quit(ClientServer cs) {
                String cName = cs.toString();
                Main.getGame().removeObject((int)Main.getGame().getFlag(cName + "-player"));
            }

}
