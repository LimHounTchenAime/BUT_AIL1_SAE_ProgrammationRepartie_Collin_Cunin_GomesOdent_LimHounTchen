import raytracer.Disp;
import raytracer.Scene;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static List<Coordonnees> splitImage(int nbPart, int width, int heigth){
        int nbLongPart = (int) (Math.sqrt(nbPart));
        int partwidth = width/nbLongPart;
        int partHeigth = heigth/nbLongPart;
        int x;
        int y = 0;
        List<Coordonnees> res = new ArrayList<>();
        for (int i=0; i<nbLongPart; i++){
            x=0;
            for (int j=0; j<nbLongPart; j++){
                res.add(new Coordonnees(x,y,partwidth,partHeigth));
                x+= partwidth;

            }
            y+= partHeigth;
        }
        return res;
    }

    public static void calculerImage(ServiceDistributeur distributeur , String scenePath, int nbpart, int width, int heigth){

        // Getting coordinates list
        List<Coordonnees> list = Client.splitImage(nbpart, width, heigth);

        // Getting scene
        Scene scene = new Scene(scenePath, width, heigth);

        // Creation of one window
        Disp disp = new Disp("Raytracer", width, heigth);

        // for each coordinates
        for (Coordonnees coor : list){
            // in Thread

            Thread t = new Thread() {
                public void run() {
                    boolean calc = false;

                    // Whyle not calculate
                    while (!calc){
                        // Getting one 'calcule' instance
                        ServiceInterface calcService = null;

                        try {
                            calcService = distributeur.demanderService();
                        } catch (RemoteException e) {
                            System.out.println("Server Not enable");
                            e.printStackTrace();
                            break;
                        }

                        if (calcService != null){
                            // Calcule image
                            try {
                                raytracer.Image image = calcService.calculer(scene, coor);
                                System.out.printf(coor.x +" " + coor.y);
                                System.out.printf(image.toString() + "\n");
                                disp.setImage(image, coor.x, coor.y);
                                calc = true;
                            }catch (RemoteException r){
                                System.out.println("Failed to make calcul");
                                r.printStackTrace();
                                try {
                                    distributeur.deleteCalcule(calcService);
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println("Sevice deleted");
                            } catch (ServerNotActiveException e) {
                                try {
                                    distributeur.deleteCalcule(calcService);
                                } catch (RemoteException ex) {
                                    throw new RuntimeException(ex);
                                }
                                System.out.println("Sevice deleted");
                            }
                        }else {
                            System.out.println("No Services actually, please wait...");
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            };

            // Starting thread
            t.start();
        }

    }
}
