package commons.network.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import spacerace.server.SpaceRaceRestController;
import spacerace.server.socket.SpaceRaceSocketServer;

@SpringBootApplication
@ComponentScan(basePackageClasses = SpaceRaceRestController.class)
@ComponentScan(basePackageClasses = SpaceRaceSocketServer.class)
public class ServerApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
