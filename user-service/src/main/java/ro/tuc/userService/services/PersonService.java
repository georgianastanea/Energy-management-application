package ro.tuc.userService.services;

import org.springframework.stereotype.Service;
import ro.tuc.userService.dtos.PersonDto;
import ro.tuc.userService.dtos.PersonIdDto;
import ro.tuc.userService.dtos.builder.PersonBuilder;
import ro.tuc.userService.entities.Person;
import ro.tuc.userService.repositories.PersonRepository;

import java.util.List;
import java.util.UUID;

import static ro.tuc.userService.services.CommunicationService.DEVICE_SERVICE_URL;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonDto getPersonByUsername(String username){
        Person person = personRepository.findByUsername(username).orElse(null);
        return person == null ? null : PersonBuilder.toPersonDto(person);
    }

    public UUID getIdByUsername(String username){
        Person person = personRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Person not found"));
        return person.getId();
    }

    public List<PersonDto> getAllPersons(){
        List<Person> persons = personRepository.findAll();
        return persons.stream().map(PersonBuilder::toPersonDto).toList();
    }

    public PersonDto addPerson(PersonDto personDto){
        Person person = PersonBuilder.toPerson(personDto);
        person.setPassword(personDto.getPassword());
        personRepository.save(person);

        UUID personId = personRepository.findByUsername(person.getUsername()).orElseThrow(() -> new RuntimeException("Person not found")).getId();
        CommunicationService.sendPostRequest(new PersonIdDto(personId), DEVICE_SERVICE_URL + "person");

        return PersonBuilder.toPersonDto(person);
    }

    public PersonDto updatePerson(UUID id, PersonDto newPerson){
        if(!personRepository.existsById(id)){
            throw new RuntimeException("Person does not exist");
        }

        Person person = PersonBuilder.toPerson(newPerson);
        person.setId(id);
        person.setPassword(person.getPassword());
        personRepository.save(person);
        return PersonBuilder.toPersonDto(person);
    }

    public void deletePerson(UUID id){
        if(!personRepository.existsById(id)){
            throw new RuntimeException("Person does not exist");
        }
        personRepository.deleteById(id);
        CommunicationService.sendDeleteRequest(DEVICE_SERVICE_URL + "person/" + id);
    }
}
