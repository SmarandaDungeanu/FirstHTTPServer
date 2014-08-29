package firsthttpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class FirstHttpServer {

    static int port = 8080;
    static String ip = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        if (args.length >= 2) {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }
        InetSocketAddress i = new InetSocketAddress("127.0.0.1", 8080);
        HttpServer server = HttpServer.create(i, 0);
        server.createContext("/welcome", new WelcomeHandler());
        server.createContext("/headers", new HeadersHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Started the server, listening on: " + port);
        System.out.println("Bound to: " + ip);
    }

    static class HeadersHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            Map headers = he.getRequestHeaders();
            String response;
            Iterator entries = headers.entrySet().iterator();
            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html>\n");
            sb.append("<html>\n");
            sb.append("<head>\n");
            sb.append("<title>My fancy Web Site</title>\n");
            sb.append("<meta charset='UTF-8'>\n");
            sb.append("</head>\n");
            sb.append("<body>\n");
            sb.append("<table border=1>\n");
            sb.append("<tr>\n");
            sb.append("<th>Header</th>\n");
            sb.append("<th>Value</th>\n");
            sb.append("</tr>\n");
            while (entries.hasNext()) {
                Entry thisEntry = (Entry) entries.next();
                Object key = thisEntry.getKey();
                Object value = thisEntry.getValue();
                sb.append("<tr>\n");
                sb.append("<td>" + key.toString() + "</td>\n");
                sb.append("<td>" + value.toString() + "</td>\n");
                sb.append("</tr>\n");
            }
            sb.append("</table>");
            sb.append("</body>\n");
            sb.append("</html>\n");
            response = sb.toString();
            Headers h = he.getResponseHeaders();
            h.add("Content-Type", "text/html");

            he.sendResponseHeaders(200, response.length());
            try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
                pw.print(response);
            };
        }

    }

    static class WelcomeHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            String response = "Welcome to my first fantastic server! :D";
            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html>\n");
            sb.append("<html>\n");
            sb.append("<head>\n");
            sb.append("<title>My fancy Web Site</title>\n");
            sb.append("<meta charset='UTF-8'>\n");
            sb.append("</head>\n");
            sb.append("<body>\n");
            sb.append("<h2>Welcome to my very first home made Web Server :-)</h2>\n");
            sb.append("</body>\n");
            sb.append("</html>\n");
            response = sb.toString();
            Headers h = he.getResponseHeaders();
            h.add("Content-Type", "text/html");

            he.sendResponseHeaders(200, response.length());
            try (PrintWriter pw = new PrintWriter(he.getResponseBody())) {
                pw.print(response);
            };
        }

    }

}
