package utcn.authentication.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utcn.authentication.dtos.*;
import utcn.authentication.entities.Person;
import utcn.authentication.entities.Role;

import static utcn.authentication.services.CommunicationService.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse login(LoginRequestDto loginRequestDto) {
        if(!loginRequestDto.getUsername().equals(ADMIN_USERNAME) || !loginRequestDto.getPassword().equals(ADMIN_PASSWORD))
        {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword()
                    )
            );

            PersonDto personDto = CommunicationService.sendGetByCriteriaRequest(PersonDto.class, USER_SERVICE_URL + "person/" + loginRequestDto.getUsername());
            if(personDto == null){
                throw new RuntimeException("User not found");
            }
            var person = Person.builder()
                    .id(personDto.getId())
                    .username(personDto.getUsername())
                    .password(personDto.getPassword())
                    .role(Role.valueOf(personDto.getRole()))
                    .build();

            var jwtToken = jwtService.generateToken(person);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
        else
        {

            PersonDto personDto = CommunicationService.sendGetByCriteriaRequest(PersonDto.class, USER_SERVICE_URL + "person/" + loginRequestDto.getUsername());
            Person person;
            if(personDto == null){
                person = Person.builder()
                        .username(loginRequestDto.getUsername())
                        .password(passwordEncoder.encode(loginRequestDto.getPassword()))
                        .role(Role.ADMIN)
                        .build();

                CommunicationService.sendPostRequest(person, USER_SERVICE_URL + "person");
                CommunicationService.sendPostRequest(new PersonIdDto(person.getId()), DEVICE_SERVICE_URL + "person");
            }
            else{
                person = Person.builder()
                        .id(personDto.getId())
                        .username(personDto.getUsername())
                        .password(personDto.getPassword())
                        .role(Role.valueOf(personDto.getRole()))
                        .build();
            }

            var jwtToken = jwtService.generateToken(person);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
    }

    public AuthenticationResponse register(PersonDto personDto) {
        var person = Person.builder()
                .username(personDto.getUsername())
                .password(passwordEncoder.encode(personDto.getPassword()))
                .role(Role.valueOf(personDto.getRole()))
                .build();

        PersonDto existingPerson = CommunicationService.sendGetByCriteriaRequest(PersonDto.class, USER_SERVICE_URL + "person/" + personDto.getUsername());

        if(existingPerson != null){
            throw new RuntimeException("Person already exists");
        }
        CommunicationService.sendPostRequest(person, USER_SERVICE_URL + "person");

        var jwtToken = jwtService.generateToken(person);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse logout(String username) {
        CommunicationService.sendDeleteRequest(CHAT_SERVICE_URL + "person/" + username);
        return AuthenticationResponse.builder()
                .token("")
                .build();
    }
}