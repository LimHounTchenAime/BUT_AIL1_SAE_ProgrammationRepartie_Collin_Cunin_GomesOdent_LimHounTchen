import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface ServiceDistributeur extends Remote{
	public int DonnerTicket() throws RemoteException, ServerNotActiveException;
}
