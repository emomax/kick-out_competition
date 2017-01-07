package commons.network.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import spacerace.server.SpaceRaceRestController;

@SpringBootApplication
@ComponentScan(basePackageClasses = SpaceRaceRestController.class)
public class ServerApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
