package utcn.authentication.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utcn.authentication.dtos.ChatMessageDto;
import utcn.authentication.dtos.PersonUsernameDto;
import utcn.authentication.services.CommunicationService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/chat")
public class ChatController {

    @GetMapping
    public ResponseEntity<List<PersonUsernameDto>> getConnectedPersons(){
        return ResponseEntity.ok(CommunicationService
                .sendGetAllRequest(CommunicationService.CHAT_SERVICE_URL + "persons" , PersonUsernameDto[].class));
    }

    @GetMapping("/messages/{senderUsername}/{receiverUsername}")
    public ResponseEntity<List<ChatMessageDto>> getChatMessages(
            @PathVariable("senderUsername") String senderUsername,
            @PathVariable("receiverUsername") String receiverUsername
    ){
        return ResponseEntity.ok(CommunicationService
                .sendGetAllRequest(CommunicationService.CHAT_SERVICE_URL + "messages/" + senderUsername + "/" + receiverUsername, ChatMessageDto[].class));
    }
}
