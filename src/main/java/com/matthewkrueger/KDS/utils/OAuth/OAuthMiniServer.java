package com.matthewkrueger.KDS.utils.OAuth;

import com.matthewkrueger.KDS.utils.StackTraceUtils;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

public class OAuthMiniServer {

    private static String authorize;

    public static class MyHTTPSHandler implements HttpHandler {
        private HttpsServer httpsServer;
        public MyHTTPSHandler(HttpsServer httpsServer) {
            this.httpsServer = httpsServer;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {

            HttpsExchange httpsExchange = (HttpsExchange) t;
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            String codeFromAuthorize = httpsExchange.getRequestURI().getQuery();
            authorize = codeFromAuthorize.substring(codeFromAuthorize.indexOf('=')+1);
            authorize = authorize.substring(0, authorize.indexOf('&'));

            String response = "<!DOCTYPE html><html><head><title>OAuth Complete</title><SCRIPT>\r\n" +
                    "setTimeout(\"self.close()\", 5000 ) // after 5 seconds\r\n" +
                    "</SCRIPT></head><body><h1>Authentication Complete. Close this window, and return to the App</h1></body></html>";

            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

            OAuthSubroutines.LATCH.countDown();

            httpsServer.stop(2);
            //Thread.currentThread().stop();
        }
    }

    static class RecieveHookHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response;
            System.out.println("Recieved Callback");

            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            try {
                String codeFromAuthorize = t.getRequestURI().getQuery();
                if(codeFromAuthorize.indexOf('=') == -1)
                    throw new Exception("Authorize not found");
                authorize = codeFromAuthorize.substring(codeFromAuthorize.indexOf('=') + 1);
                authorize = authorize.substring(0, authorize.indexOf('&'));

                response = "<!DOCTYPE html><html><head><title>OAuth Complete</title><SCRIPT>\r\n" +
                        "setTimeout(\"self.close()\", 5000 ) // after 5 seconds\r\n" +
                        "</SCRIPT></head><body><h1>Authentication Complete. Close this window, and return to the App</h1></body></html>";
                //response = authorize;
            }catch (Exception e){
                response = "<h1>Error:</h1>\n" + StackTraceUtils.asString(e.getStackTrace());
            }

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

            OAuthSubroutines.LATCH.countDown();

        }
    }

    /**
     * The runnable to start the OAuth Mini Server
     */
    /*private static final Runnable startServer = ()->{
        System.out.println("Starting mini-server to listen for reply.");
        try {
            // setup the socket address
            InetSocketAddress address = new InetSocketAddress(8000);

            // initialise the HTTPS server
            HttpsServer httpsServer = HttpsServer.create(address, 0);
            SSLContext sslContext = SSLContext.getInstance("TLS");

            // initialise the keystore
            char[] password = "d5336010".toCharArray();

            KeyStore ks = KeyStore.getInstance("JKS");
            //FileInputStream fis = new FileInputStream("testkey.jks");

            InputStream in = Settings.class.getResourceAsStream("/certs/identity.jks");
            ks.load(in, password);

            // setup the key manager factory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);

            // setup the trust manager factory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            // setup the HTTPS context and parameters
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
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
            httpsServer.createContext("/recieveHook", new MyHandler(httpsServer));
            httpsServer.setExecutor(null); // creates a default executor
            httpsServer.start();

        } catch (Exception exception) {

            System.err.println("Fatal error in creating an HTTPS server on port 8000 of localhost");

        }
    };*/

    private static final Runnable startServer = () -> {
        System.out.println("Starting mini-server to listen for reply on thread " + Thread.currentThread().getId() + " : " + Thread.currentThread().getName());
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/recieveHook", new RecieveHookHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
            Thread.sleep(10*60*1000); // keep thread alive for 10 minutes
            server.stop(0);
            System.out.println("Http Server Stopped on thread " + Thread.currentThread().getId() + " : " + Thread.currentThread().getName());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    };

    /**
     * Starts the miniHTTPS Server and waits for user response. Once user response is gotten in browser, it sends the code to exchange
     * for an oauth token.
     * @return 0 is oauth token, 1 is expires, 2 is refresh
     */
    public static String[] startMiniHTTPSServer() {


        OAuthSubroutines.LATCH = new CountDownLatch(1);
        Thread serverThread = new Thread(startServer, "HTTP Server Thread");
        serverThread.start();

        // opent the server
        OAuthSubroutines.openDefaultBrowserOAuthAuthorize();

        try {
            OAuthSubroutines.LATCH.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String result[] = OAuthSubroutines.getAccessTokenFromAuthorize(authorize);

        return result;

    }

    public static void startMiniServerForTesting(){
        Thread serverThread = new Thread(startServer, "HTTP Server Thread");
        serverThread.start();
    }


}
