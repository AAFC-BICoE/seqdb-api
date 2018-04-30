package ca.gc.aafc.seqdb.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Launches the application.
 */
@SpringBootApplication
@EntityScan("ca.gc.aafc.seqdb.entities")
public class SeqdbApiLauncher {

  public static void main(String[] args) {
    SpringApplication.run(SeqdbApiLauncher.class, args);
  }

}
