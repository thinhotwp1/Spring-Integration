package flc.springintegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringIntegrationDemoApplication implements CommandLineRunner {

    @Autowired
    private IntegrationConfig.ProcessGateway processGateway;

    public static void main(String[] args) {
        SpringApplication.run(SpringIntegrationDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String input = "Spring Integration";
        
        // Gọi flow1
        String result1 = processGateway.processInFlow1(input);

        // Gọi flow2
        String result2 = processGateway.processInFlow2(result1);
    }
}
