package server.game;

/**
 *
 * @author Lime
 */
public interface ClientHandler {

    public abstract boolean init(ClientServer cs);

    public abstract boolean update(ClientServer cs);

    public abstract void quit(ClientServer cs);
}
