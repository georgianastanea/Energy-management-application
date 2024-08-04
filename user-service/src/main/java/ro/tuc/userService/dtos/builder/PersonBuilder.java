package ro.tuc.userService.dtos.builder;

import ro.tuc.userService.dtos.PersonDto;
import ro.tuc.userService.entities.Person;
import ro.tuc.userService.entities.Role;

public class PersonBuilder {

    public static PersonDto toPersonDto(Person person) {
        return PersonDto.builder()
                .id(person.getId())
                .username(person.getUsername())
                .password(person.getPassword())
                .role(person.getRole().toString())
                .build();
    }

    public static Person toPerson(PersonDto personDto) {
        return Person.builder()
                .id(personDto.getId())
                .username(personDto.getUsername())
                .password(personDto.getPassword())
                .role(Role.valueOf(personDto.getRole().toUpperCase()))
                .build();
    }
}
