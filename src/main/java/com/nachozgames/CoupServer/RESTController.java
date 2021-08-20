package com.nachozgames.CoupServer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

@RestController
public class RESTController {

    CoupGame game = new CoupGame();

	@PostMapping(value="/client-connect/uuid/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> ClientConnect(@PathVariable String uuid) {

        if(uuid.length()!=0)
        {
            game.addPlayer(new Player(uuid));
            System.out.println("Added player to game"+uuid);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        System.out.println("Unable to add player, issue with uuid");
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}