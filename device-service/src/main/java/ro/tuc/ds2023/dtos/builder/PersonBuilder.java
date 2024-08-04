package ro.tuc.ds2023.dtos.builder;

import ro.tuc.ds2023.dtos.PersonDto;
import ro.tuc.ds2023.entities.Person;

public class PersonBuilder {

    public static Person toPerson(PersonDto personDto) {
        return Person.builder()
                .id(personDto.getPersonId())
                .build();
    }
}
