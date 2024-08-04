package ro.tuc.userService.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.userService.dtos.PersonDto;
import ro.tuc.userService.services.PersonService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<PersonDto> getPersonByUsername(@PathVariable String username){
        return ResponseEntity.ok(personService.getPersonByUsername(username));
    }

    @GetMapping("/id/{username}")
    public ResponseEntity<UUID> getIdByUsername(@PathVariable String username){
        return ResponseEntity.ok(personService.getIdByUsername(username));
    }

    @GetMapping
    public ResponseEntity<List<PersonDto>> getALlPersons(){
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @PostMapping
    public ResponseEntity<PersonDto> addPerson(@RequestBody PersonDto personDto){
        return ResponseEntity.ok(personService.addPerson(personDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> updatePerson(@PathVariable UUID id, @RequestBody PersonDto deviceDto){
        return ResponseEntity.ok(personService.updatePerson(id, deviceDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID id){
        personService.deletePerson(id);
        return ResponseEntity.ok().build();
    }
}
