import java.rmi.RemoteException;
import java.util.ArrayList;

public class Distributeur implements ServiceDistributeur
{

	private ArrayList<ServiceTableauBlanc> clients;
	private ArrayList<Dessin> dessins;
	
	public Distributeur() throws RemoteException
	{
		this.clients = new ArrayList<ServiceTableauBlanc>();
		this.dessins = new ArrayList<Dessin>();
	}

	public void distribuerMessage(Dessin d) throws RemoteException
	{
		this.dessins.add(d);

		for (ServiceTableauBlanc s : this.clients)
		{
			try {
				s.afficherMessage(d);
			}
			catch (RemoteException e)
			{
				this.clients.remove(s);
			}
		}



		// catch clients.remove(s);
	}

	public void enregistrerClient(ServiceTableauBlanc serviceTableau) throws RemoteException
	{
		this.clients.add(serviceTableau);

		for (Dessin d : this.dessins)
			serviceTableau.afficherMessage(d);
	}
}
