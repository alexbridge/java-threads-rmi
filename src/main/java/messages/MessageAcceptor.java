package messages;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageAcceptor extends Remote {
    Message acceptMessage(Message message) throws RemoteException;
}
