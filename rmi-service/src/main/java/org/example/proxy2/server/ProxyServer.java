package org.example.proxy2.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ProxyServer {
    public static void main(String[] args) throws IOException {
        int port = 8000;
        String registryHost = args[0];
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/proxy", new ProxyHandler(registryHost));
        server.setExecutor(null);
        server.start();
    }
}

