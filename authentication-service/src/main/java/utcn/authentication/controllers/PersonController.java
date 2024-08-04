package utcn.authentication.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import utcn.authentication.dtos.PersonDto;
import utcn.authentication.services.CommunicationService;

import java.util.List;
import java.util.UUID;

import static utcn.authentication.services.CommunicationService.USER_SERVICE_URL;

@RestController
@CrossOrigin
@RequestMapping(value = "/person")
public class PersonController {

    private final PasswordEncoder passwordEncoder;

    public PersonController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{username}")
    public ResponseEntity<PersonDto> getPersonByUsername(@PathVariable String username){
        return ResponseEntity.ok(CommunicationService.sendGetByCriteriaRequest(PersonDto.class, USER_SERVICE_URL + "person/" + username));
    }

    @GetMapping("/id/{username}")
    public ResponseEntity<UUID> getIdByUsername(@PathVariable String username){
        return ResponseEntity.ok(CommunicationService.sendGetByCriteriaRequest(UUID.class, USER_SERVICE_URL + "person/id/" + username));
    }

    @GetMapping
    public ResponseEntity<List<PersonDto>> getALlPersons(){
        return ResponseEntity.ok(CommunicationService.sendGetAllRequest(USER_SERVICE_URL + "person", PersonDto[].class));
    }

    @PostMapping
    public ResponseEntity<Object> addPerson(@RequestBody PersonDto personDto){
        personDto.setPassword(passwordEncoder.encode(personDto.getPassword()));
        Object response = CommunicationService.sendPostRequest(personDto, USER_SERVICE_URL + "person");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePerson(@PathVariable UUID id, @RequestBody PersonDto personDto){
        personDto.setPassword(passwordEncoder.encode(personDto.getPassword()));
        Object response = CommunicationService.sendPutRequest(personDto, USER_SERVICE_URL + "person/" + id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID id){
        CommunicationService.sendDeleteRequest(USER_SERVICE_URL + "person/" + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
