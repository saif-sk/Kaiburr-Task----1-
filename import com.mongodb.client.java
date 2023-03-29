import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ServerController {
    private final ServerRepository repository;

    public ServerController(ServerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/servers")
    public List<Server> getServers(@RequestParam(required = false) String id) {
        if (id == null) {
            return repository.findAll();
        } else {
            return repository.findById(id).map(List::of).orElseThrow(() -> new NotFoundException("Server not found."));
        }
    }

    @PutMapping("/servers")
    public void addServer(@RequestBody Server server) {
        repository.insert(server);
    }

    @DeleteMapping("/servers/{id}")
    public void deleteServer(@PathVariable String id) {
        repository.deleteById(id);
    }

    @GetMapping("/servers/search")
    public List<Server> searchServersByName(@RequestParam String name) {
        List<Server> servers = new ArrayList<>();
        for (Server server : repository.findAll()) {
            if (server.getName().contains(name)) {
                servers.add(server);
            }
        }
        if (servers.isEmpty()) {
            throw new NotFoundException("No servers found.");
        }
        return servers;
    }
}