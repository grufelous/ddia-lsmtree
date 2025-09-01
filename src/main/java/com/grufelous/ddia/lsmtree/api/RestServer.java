package com.grufelous.ddia.lsmtree.api;

import com.grufelous.ddia.lsmtree.constants.Ports;
import com.grufelous.ddia.lsmtree.constants.Status;
import com.grufelous.ddia.lsmtree.services.LSMService;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

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
            newServer.createContext("/write", new WriteHandler());
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
            OutputStream os = exchange.getResponseBody();
            List<String> keys = exchange.getRequestHeaders().get("key");
            if(keys.size() == 1) {
                String key = keys.getFirst();
                System.out.println("Received read key: " + key);
                try {
                    String value = lsmService.get(key);
                    if(value != null) {
                        System.out.println("Evaluated value: " + value);
                        exchange.sendResponseHeaders(200, value.getBytes().length);
                        os.write(value.getBytes());
                    } else {
                        exchange.sendResponseHeaders(404, Status.NO_RECORD_FOUND.getBytes().length);
                        os.write(Status.NO_RECORD_FOUND.getBytes());
                    }
                    os.close();
                } catch (Exception e) {
                    System.err.println("Error processing read" + e);
                    String error = e.toString();
                    exchange.sendResponseHeaders(500, error.getBytes().length);
                    os.write(error.getBytes());
                    os.close();
                }
            } else {
                exchange.sendResponseHeaders(400, Status.INVALID_REQUEST_FORMAT.getBytes().length);
                os.write(Status.INVALID_REQUEST_FORMAT.getBytes());
                os.close();
            }
        }
    }

    private class WriteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            OutputStream os = exchange.getResponseBody();
            List<String> keys = exchange.getRequestHeaders().get("key");
            List<String> values = exchange.getRequestHeaders().get("value");
            if(keys.size() == 1 && values.size() == 1) {
                String key = keys.getFirst(), value = values.getFirst();
                System.out.println("Received write key: " + key + ", value: " + value);
                try {
                    lsmService.put(key, value);
                    exchange.sendResponseHeaders(200, Status.RECORD_INSERTED.getBytes().length);
                    os.write(Status.RECORD_INSERTED.getBytes());
                    os.close();
                } catch (Exception e) {
                    System.err.println("Error putting: " + e);
                    String error = e.toString();
                    exchange.sendResponseHeaders(500, error.getBytes().length);
                    os.write(error.getBytes());
                    os.close();
                }
            } else {
                exchange.sendResponseHeaders(400, Status.INVALID_REQUEST_FORMAT.getBytes().length);
                os.write(Status.INVALID_REQUEST_FORMAT.getBytes());
                os.close();
            }
        }
    }
}
