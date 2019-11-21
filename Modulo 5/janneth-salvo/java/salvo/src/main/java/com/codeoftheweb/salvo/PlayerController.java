package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class PlayerController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PlayerRepository playerRepository;

    //players
    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String username, @RequestParam String password) {

        if (username.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data,dato perdido", HttpStatus.FORBIDDEN);
        }
        if (playerRepository.findByUserName(username).orElse(null) != null) {
            return new ResponseEntity<>("Name already in use, nombre en uso", HttpStatus.FORBIDDEN);
        }
        playerRepository.save(new Player(username, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
