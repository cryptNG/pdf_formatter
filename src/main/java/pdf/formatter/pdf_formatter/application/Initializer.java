package pdf.formatter.pdf_formatter.application;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class Initializer {

  @Autowired
  private ApplicationContext applicationContext;

  Logger Log = LoggerFactory.getLogger(Initializer.class);

  @Bean
  CommandLineRunner startingMessages() {

    // void would work but leads to the code being executed parallel to spring boot
    // initialization
    // commandlinerunner is executed at the end of the context initialization

    return args -> {
      try {
        String ip = InetAddress.getLocalHost().getHostAddress();
        String ipCorrected = null;
        if (ip.equals("127.0.0.1") || ip.equals("127.0.1.1")) {
          ipCorrected = "localhost";
        }
        int port = applicationContext.getBean(Environment.class).getProperty("server.port", Integer.class, 8080);

        Log.info("OpenAPI-Specification @ http://" + ipCorrected + ":" + port + "/v3/api-docs/");
        Log.info("Swagger-UI @ http://" + ipCorrected + ":" + port + "/swagger-ui.html");

      } catch (UnknownHostException e) {
        e.printStackTrace();
      }

    };

  }

  // @Bean
  // CommandLineRunner initOrderDatabase(OrderRepository repository) {
  //
  // return args -> {
  // Log.Info("Preloading " + repository.save(new Order("xmlpathtest",
  // "xslpathtest", ResultType.PDF, "someId")));
  // Log.Info("Preloading " + repository.save(new Order("xmlpathtest2",
  // "xslpathtest2", ResultType.PDF, "someId2")));
  // };
  // }
}