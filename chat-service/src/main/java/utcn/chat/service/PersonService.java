package utcn.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utcn.chat.dto.PersonDto;
import utcn.chat.repository.PersonRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public void savePerson(PersonDto personDto){
        personRepository.save(personDto);
    }

    public void disconnect(PersonDto personDto){
        var storedPerson = personRepository.findByUsername(personDto.getUsername()).orElse(null);
        if(storedPerson != null){
            personRepository.delete(storedPerson);
        }
    }

    public List<PersonDto> findConnectedPersons(){
        return personRepository.findAll();
    }

}
