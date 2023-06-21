
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.Scanner;

public class LancerService {
    public  static  final String COMMAND_HELP = "Error in command : Incorrect name or parameters number";
    public  static  final String COMMAND_INFO = "Please enter following command, choose parameters's value : 'run [width] [heigth] [number of part]'";
    public static void main(String[] args) {
        ServiceDistributeur dist = null;
        try {
            Registry reg = LocateRegistry.getRegistry(args[0], 4552);
            dist = (ServiceDistributeur) reg.lookup("distributeur");
        } catch (AccessException a) {
            System.out.println("Failed access ");
        } catch(RemoteException r) {
            System.out.println("Error in getting registry" + r.getMessage());
            System.exit(0);
        } catch (NotBoundException a) {
            System.out.println("service named "+ a.getMessage() + " not found");
            System.exit(0);
        }

        Scanner sc = new Scanner(System.in);

        String carac = "";
        while (carac != "q"){
            System.out.println(COMMAND_INFO);
            String command = sc.nextLine();

            String[] arguments = command.split(" ");

            for (String intem : arguments){
                System.out.println(intem);
            }
            System.out.println("length :" + arguments.length);

            if (Objects.equals(arguments[0], "run") && arguments.length == 4) {
                // Settings
                int width = Integer.parseInt(arguments[1]);
                int heigth = Integer.parseInt(arguments[2]);
                int nbPart = Integer.parseInt(arguments[3]);
                String scenePath = "./simple.txt";

                System.out.println("path");

                // Start image calcule
                Client.calculerImage(dist,scenePath,nbPart,width, heigth);
            }else {
                System.out.println(COMMAND_HELP);
            }
        }

        System.exit(0);


    }
}
