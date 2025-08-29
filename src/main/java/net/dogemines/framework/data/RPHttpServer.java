package net.dogemines.framework.data;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;

//hosts a resource pack file with a http server
public final class RPHttpServer {
    private final JavaPlugin plugin;
    private final File resourcePackFile;
    private HttpServer server;

    private final int PORT = 8000;
    private final String RESOURCES_PATH = "/resources.zip";

    public RPHttpServer(JavaPlugin plugin, File resourcePackFile) {
        this.plugin = plugin;
        this.resourcePackFile = resourcePackFile;
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext(RESOURCES_PATH, new FileHandler());
            server.setExecutor(null);

            // Run server in a separate thread
            Thread serverThread = new Thread(() -> {
               server.start();
            });

            serverThread.start();

            plugin.getLogger().info("Started HTTP server");
        } catch(IOException e) {
            plugin.getLogger().info("Error starting the server: " + e.getMessage());
        }
    }

    public void stop() {
        server.stop(0);
    }

    public String getResourceURL() {
        return "http://" + Bukkit.getIp() + ":" + PORT + RESOURCES_PATH;
    }

    private class FileHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException
        {
            if (!resourcePackFile.exists()) {
                String response = "File not found";
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }

            byte[] bytes = Files.readAllBytes(resourcePackFile.toPath());
            exchange.getResponseHeaders().add("Content-Type", "application/octet-stream");
            exchange.sendResponseHeaders(200, bytes.length);

            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }
}
