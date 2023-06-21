package org.example.proxy;

import com.sun.net.httpserver.*;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.security.cert.CertificateException;

public class Proxy {
    private int PROXY_PORT = 8000;
    private int RMI_PORT = 1099;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        int portProxy = 8000;
        String ipRMI = args[0];
        int portRMI = 1098;

        HttpsServer server = HttpsServer.create(new InetSocketAddress(portProxy), 0);
        SSLContext sslContext = SSLContext.getInstance("TLS");

        // initialise the keystore
        char[] password = "password".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream("ressources/testkey.jks");
        ks.load(fis, password);

        // setup the key manager factory
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password);

        // setup the trust manager factory
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        // setup the HTTPS context and parameters
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            public void configure(HttpsParameters params) {
                try {
                    // initialise the SSL context
                    SSLContext context = getSSLContext();
                    SSLEngine engine = context.createSSLEngine();
                    params.setNeedClientAuth(false);
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());

                    // Set the SSL parameters
                    SSLParameters sslParameters = context.getSupportedSSLParameters();
                    params.setSSLParameters(sslParameters);

                } catch (Exception ex) {
                    System.out.println("Failed to create HTTPS port");
                }
            }
        });
        ServiceProxy sP = null;
        InterfaceServiceProxy intSP = null;
        // création du service proxy
        try {
            sP = new ServiceProxy();
            Registry registryProxy = LocateRegistry.createRegistry(portRMI);
            intSP = (InterfaceServiceProxy) UnicastRemoteObject.exportObject(sP, 0);
            registryProxy.rebind("ServiceProxy", intSP);

            System.out.println("Le service proxy est enregistré sur le port " + portRMI);
        } catch (RemoteException e) {
            System.out.println("Erreur lors de l'enregistrement du service RMI : " + e.getMessage());
        }
        if(sP != null && intSP != null) {
            server.createContext("/data", new ProxyHandler(sP));
            server.setExecutor(null);
            server.start();
            System.out.println("Server started on port 8000");
        }
        else{
            System.out.println("problème service proxy");
        }
    }
}
