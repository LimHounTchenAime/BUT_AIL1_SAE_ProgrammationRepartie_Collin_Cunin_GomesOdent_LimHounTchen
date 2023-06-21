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

//        try {
//            Distributeur distributeur = new Distributeur();
//            ServiceDistributeur rd = (ServiceDistributeur) UnicastRemoteObject.exportObject(distributeur, portService);
//            Registry reg = LocateRegistry.getRegistry(portAnnuaire);
//            reg.rebind("LeDistributeur", rd);
//            System.out.println("Service accessible depuis l'annuaire");
//        }
//		catch (java.rmi.server.ExportException e)
//		{
//			System.err.println("Port " + portService + " déjà utilisé : impossible d'y affecter le service");
//			System.exit(-1);
//		}
//		catch (java.rmi.ConnectException e)
//		{
//			System.err.println("Impossible de récupérer l'annuaire sur le port " + portAnnuaire);
//			System.exit(-1); //sinon le programme ne s'arrête pas après l'exception
//		}

        try {
            // 1. ATTRAPER L'ANNUAIRE DU SERVICE PROXY
            Registry regCentral = LocateRegistry.getRegistry(args[0]);
            System.out.println("Annuaire attrapé");

            // 2. RECUPERER SERVICE CENTRAL
            ServiceDistributeur serviceDistrib =
                    (ServiceDistributeur)regCentral.lookup("LeDistributeur");
            System.out.println("Service récupéré");

            // 3. CREER SERVICE CLIENT
            //ExportException
            TableauBlanc tableauBlanc = new TableauBlanc(serviceDistrib);
            ServiceTableauBlanc rd = (ServiceTableauBlanc) UnicastRemoteObject.exportObject(tableauBlanc, 0);


            // 4. ENREGISTRER SERVICE CLIENT AVEC SERVICE CENTRAL
            serviceDistrib.enregistrerClient(rd);

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
        }
        catch (java.rmi.NotBoundException e)
        {
            System.err.println("Service absent de l'annuaire");
        }
	
	}
}
