package spacerace;

import org.springframework.web.client.RestTemplate;

import spacerace.server.response.ServerResponse;

public class SpaceRaceTest {

    private static final String SERVER_ADDRESS = "127.0.0.1:8080"; // If you run locally
    //    private static final String SERVER_ADDRESS = "10.46.1.193:8080"; // Game server

    public static void main(final String[] args) throws InterruptedException {

        final RestTemplate restTemplate = new RestTemplate();

        final String url = "http://" + SERVER_ADDRESS + "/test";

        for (int i = 0; i < 20; i++) {
            final long           before    = System.currentTimeMillis();
            final ServerResponse response  = restTemplate.getForObject(url, ServerResponse.class);
            final long           totalTime = System.currentTimeMillis() - before;
            System.out.println(response + "    " + totalTime);

            Thread.sleep(500);
        }
    }
}
