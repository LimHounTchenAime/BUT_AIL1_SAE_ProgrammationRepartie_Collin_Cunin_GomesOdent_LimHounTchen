import java.rmi.Naming;
import java.rmi.RemoteException;

public class DataRetriever {
    public static void main(String[] args) {
        try {
            DataService dataService = new DataServiceImpl();

            String result = dataService.fetchData();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
