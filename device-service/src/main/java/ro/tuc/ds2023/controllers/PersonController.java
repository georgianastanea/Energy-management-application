package ro.tuc.ds2023.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2023.dtos.PersonDto;
import ro.tuc.ds2023.entities.Device;
import ro.tuc.ds2023.entities.Person;
import ro.tuc.ds2023.repositories.DeviceRepository;
import ro.tuc.ds2023.services.PersonService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/person")
public class PersonController {

    private final PersonService personService;
    private final DeviceRepository deviceRepository;

    public PersonController(PersonService personService, DeviceRepository deviceRepository) {
        this.personService = personService;
        this.deviceRepository = deviceRepository;
    }

    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody PersonDto personDto){
        return ResponseEntity.ok(personService.addPerson(personDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID id){
        List<Device> devices = deviceRepository.findByPerson_Id(id);
        if(devices.size() == 0)
            personService.deletePerson(id);
        else
            deviceRepository.deleteAll(devices);
        return ResponseEntity.ok().build();
    }
}
