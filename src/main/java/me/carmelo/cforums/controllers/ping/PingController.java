package me.carmelo.cforums.controllers.ping;

import lombok.SneakyThrows;
import me.carmelo.cforums.database.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    private final DatabaseService databaseService;

    @Autowired
    public PingController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong!";
    }

    @SneakyThrows
    @GetMapping("/pingdb")
    public String pingDb() {
        return databaseService.checkDatabaseConnection();
    }
}
