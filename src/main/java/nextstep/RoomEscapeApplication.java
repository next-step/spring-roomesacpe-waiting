package nextstep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"nextstep","auth"})
public class RoomEscapeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomEscapeApplication.class, args);
    }
}
