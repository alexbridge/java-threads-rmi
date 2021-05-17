package messages;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Interlocutor extends Remote {
    Message acceptMessage(Message message) throws RemoteException;
}
