package utcn.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import utcn.chat.dto.PersonDto;
import utcn.chat.service.PersonService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @MessageMapping("/person.addPerson")
    @SendTo("person/topic")
    public PersonDto addPerson(@Payload PersonDto personDto){
        personService.savePerson(personDto);
        return personDto;
    }

    @MessageMapping("/person.disconnectPerson")
    @SendTo("person/topic")
    public PersonDto disconnect(@Payload PersonDto personDto){
        personService.disconnect(personDto);
        return personDto;
    }

    @GetMapping("/persons")
    public ResponseEntity<List<PersonDto>> findConnectedPersons(){
        return ResponseEntity.ok(personService.findConnectedPersons());
    }
}
