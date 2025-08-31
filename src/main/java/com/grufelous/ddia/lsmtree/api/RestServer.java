package com.grufelous.ddia.lsmtree.api;

import com.grufelous.ddia.lsmtree.constants.Ports;
import com.grufelous.ddia.lsmtree.services.LSMService;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class RestServer {
    private HttpServer server;
    private LSMService lsmService;

    public RestServer(LSMService lsmService) {
        this.lsmService = lsmService;
    }

    public void startServer() {
        int port = Ports.REST_SERVER_PORT;
        this.startServer(port);
    }

    public void startServer(int port) {
        try {
            HttpServer newServer = HttpServer.create(new InetSocketAddress(port), 0);
            newServer.createContext("/read", new ReadHandler());
            newServer.setExecutor(null);
            newServer.start();
            this.server = newServer;

            System.out.println("Launched API Rest Server at http://localhost:" + port + "/read");
        } catch (IOException e) {
            System.err.println("Error launching server!");
            System.err.println(e.toString());
        }
    }

    private class ReadHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String key = String.valueOf(exchange.getRequestHeaders().get("key"));
            lsmService.get(key);
            exchange.sendResponseHeaders(200, key.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(key.getBytes());
            os.close();
        }
    }
}
