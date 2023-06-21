import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class LancerTableau
{
	public static void main(String[]args) throws RemoteException
	{

		try {



			// 2. ATTRAPER L'ANNUAIRE DU SERVICE CENTRAL
			Registry regCentral = LocateRegistry.getRegistry(args[0]);
			System.out.println("Annuaire attrapé");

			// 3. RECUPERER SERVICE CENTRAL
			ServiceDistributeur serviceDistrib =
					(ServiceDistributeur)regCentral.lookup("distributeur");
			System.out.println("Service récupéré");

			// 1. CREER SERVICE CLIENT
			//ExportException
			TableauBlanc tableauBlanc = new TableauBlanc(serviceDistrib);
			ServiceTableauBlanc rd = (ServiceTableauBlanc) UnicastRemoteObject.exportObject(tableauBlanc, 0);


			// 4. ENREGISTRER SERVICE CLIENT AVEC SERVICE CENTRAL
			serviceDistrib.enregistrerClient(rd);




//			//ConnectException
//			Registry reg = LocateRegistry.getRegistry(args[0]);
//
////			reg.rebind("tableauBlanc", tableauBlanc);
//			System.out.println("Service accessible depuis l'annuaire");


		}
		catch (java.rmi.server.ExportException e)
		{
			System.err.println("Port xyz déjà utilisé : impossible d'y affecter le service");
			System.exit(-1);
		}
		catch (java.lang.ArrayIndexOutOfBoundsException e)
		{
			System.err.println("Paramètre manquant : adresse du service");
		}
		catch (java.rmi.ConnectException e)
		{
			System.err.println("Connexion à la machine distante impossible");
//			System.exit(-1); //sinon le programme ne s'arrête pas après l'exception
		}
		catch (java.rmi.NotBoundException e)
		{
			System.err.println("Service absent de l'annuaire");
		}




//		catch (NoRouteToHostException e)
//		{
//			System.err.println("Pas de route jusqu'à l'hôte");
//		}
	}
}
