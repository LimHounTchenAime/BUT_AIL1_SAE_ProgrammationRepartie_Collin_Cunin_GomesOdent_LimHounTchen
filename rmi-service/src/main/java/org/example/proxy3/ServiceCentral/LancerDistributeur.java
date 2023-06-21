import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class LancerDistributeur
{
	public static void main(String[]args) throws RemoteException
	{
		//Affectation des numéros de port
		int portService = 0, portAnnuaire = 1099;
		try {
			if (args.length > 0) portService = Integer.valueOf(args[0]);
		}
		catch (java.lang.NumberFormatException e)
		{
			System.err.println("Format incorrect du port de service"); //: port automatiquement attribué
			System.exit(-1);
		}
		try {
			if (args.length > 1) portAnnuaire = Integer.valueOf(args[1]);
		}
		catch (java.lang.NumberFormatException e)
		{
			System.err.println("Format incorrect du port de l'annuaire"); // : port par défaut utilisé
			System.exit(-1);
		}

		String name = "distributeur";
		try {
			Distributeur distributeur = new Distributeur();
			//ExportException
			ServiceDistributeur rd = (ServiceDistributeur) UnicastRemoteObject.exportObject(distributeur, portService);
			//ConnectException
			Registry reg = LocateRegistry.getRegistry(portAnnuaire);
			reg.rebind(name, rd);
			System.out.println("Service " + name + " accessible depuis l'annuaire");
		}
		catch (java.rmi.server.ExportException e)
		{
			System.err.println("Port " + portService + " déjà utilisé : impossible d'y affecter le service");
			System.exit(-1);
		}
		catch (java.rmi.ConnectException e)
		{
			System.err.println("Impossible de récupérer l'annuaire sur le port " + portAnnuaire);
			System.exit(-1); //sinon le programme ne s'arrête pas après l'exception
		}
	
	}
}
