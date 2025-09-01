package com.grufelous.ddia.lsmtree;

import com.grufelous.ddia.lsmtree.api.RestServer;
import com.grufelous.ddia.lsmtree.services.LSMService;

public class LSMTreeApplication {
    public static void main(String[] args) {
        LSMService lsmService = new LSMService();
        RestServer server = new RestServer(lsmService);
        server.startServer();
    }
}
