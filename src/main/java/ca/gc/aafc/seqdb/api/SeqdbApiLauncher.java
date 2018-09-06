package ca.gc.aafc.seqdb.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Launches the application.
 * @author poffm
 */
@SpringBootApplication
@EnableJpaRepositories(considerNestedRepositories = true)
public class SeqdbApiLauncher {

  public static void main(String[] args) {
    SpringApplication.run(SeqdbApiLauncher.class, args);
  }

}
