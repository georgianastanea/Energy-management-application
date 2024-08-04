package ro.tuc.ds2023.services;

import org.springframework.stereotype.Service;
import ro.tuc.ds2023.dtos.PersonDto;
import ro.tuc.ds2023.dtos.builder.PersonBuilder;
import ro.tuc.ds2023.entities.Device;
import ro.tuc.ds2023.entities.Person;
import ro.tuc.ds2023.repositories.DeviceRepository;
import ro.tuc.ds2023.repositories.PersonRepository;

import java.util.List;
import java.util.UUID;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final DeviceRepository deviceRepository;

    public PersonService(PersonRepository personRepository, DeviceRepository deviceRepository) {
        this.personRepository = personRepository;
        this.deviceRepository = deviceRepository;
    }

    public Person addPerson(PersonDto personDto) {
        if(personRepository.findById(personDto.getPersonId()).isPresent()) {
            throw new RuntimeException("Person already exists!");
        }
        return personRepository.save(PersonBuilder.toPerson(personDto));
    }

    public void deletePerson(UUID personId) {
        personRepository.findById(personId).orElseThrow(() -> new RuntimeException("Person not found!"));
        List<Device> devices =  deviceRepository.findByPerson_Id(personId);
        if(devices != null) {
            devices.forEach(device -> deviceRepository.deleteById(device.getId()));
        }
        personRepository.deleteById(personId);
    }
}
