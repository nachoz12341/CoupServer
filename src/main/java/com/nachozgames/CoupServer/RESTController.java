package com.nachozgames.CoupServer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.rmi.server.ObjID;
import java.util.Map;

@RestController
public class RESTController {

    CoupGame game = new CoupGame();

	@PostMapping(value="/client-connect/uuid/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> clientConnect(@PathVariable String uuid) {

        if(uuid.length()!=0 && !game.gameStarted())
        {
            game.addPlayer(new Player(uuid));
            System.out.println("Added player to game "+uuid);
            return new ResponseEntity<>("{}",HttpStatus.OK);
        }

        System.out.println("Unable to add player, issue with uuid");
		return new ResponseEntity<>("{}",HttpStatus.BAD_REQUEST);
	}

    @PostMapping(value="/client-set-name/uuid/{uuid}/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> clientSetName(@PathVariable String uuid, @PathVariable String name) {

        Player p = game.getPlayer(uuid);
        if(p==null)
        {
            System.out.println("Error finding player "+uuid);
		    return new ResponseEntity<>("{}",HttpStatus.BAD_REQUEST);
        }

        p.setName(name);
        System.out.println("Set player "+uuid+" name to "+name);
		return new ResponseEntity<>("{}",HttpStatus.OK);
	}
    
    @PostMapping(value="/client-disconnect/uuid/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> clientDisconnect(@PathVariable String uuid){
        if(game.removePlayer(uuid))
            return new ResponseEntity<>("{}",HttpStatus.OK);
        return new ResponseEntity<>("{}",HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value="/client-get-game-state/uuid/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> clientGetGameState(@PathVariable String uuid){
        Player p = game.getPlayer(uuid); 

        if(p==null)
            return new ResponseEntity<>("{}",HttpStatus.BAD_REQUEST);
        
        Gson gson = new Gson();
        String json = gson.toJson(p);

        return new ResponseEntity<>(json,HttpStatus.OK);
    }

    @GetMapping(value="/server-get-game-state", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> serverGetGameState(){
        Gson gson = new Gson();
        String json = gson.toJson(game);

        return new ResponseEntity<>(json,HttpStatus.OK);
    }

    @PostMapping(value="client-post-game-update/uuid/{uuid}", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> clientPostGameUpdate(@RequestBody String payload,@PathVariable String uuid){
        Player p = game.getPlayer(uuid); 

        if(p==null)
            return new ResponseEntity<>("{}",HttpStatus.BAD_REQUEST);

        ObjectMapper mapper = new ObjectMapper();
        try{
            Map<String,String> map = mapper.readValue(payload, Map.class);
            game.gameUpdate(p,map);
        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>("{}",HttpStatus.OK);
    }
    
    @GetMapping(value="client-player-list",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> clientPlayerList(){
        Gson gson = new Gson();
        String playerJson = "{\"players\":"+gson.toJson(game.players)+"}";
        return new ResponseEntity<>(playerJson,HttpStatus.OK);
    }

    @PostMapping(value="/server-game-start", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> serverGameStart() {
        if(!game.gameStarted()){
            game.gameStart();
            System.out.println("Game started!");
            return new ResponseEntity<>("{}",HttpStatus.OK);
        }
        System.out.println("Game was already started!");
        return new ResponseEntity<>("{}",HttpStatus.BAD_REQUEST);
    }

    //Used mainly for debug will reset game
    @PostMapping(value="/clear-game", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> clearGame() {
        game = new CoupGame();
        System.out.println("Game was reset!");
        return new ResponseEntity<>("{}",HttpStatus.OK);
    }

}