package utcn.authentication.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utcn.authentication.dtos.AuthenticationResponse;
import utcn.authentication.dtos.LoginRequestDto;
import utcn.authentication.dtos.PersonDto;
import utcn.authentication.services.AuthenticationService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequestDto loginRequestDto)  {
        return ResponseEntity.ok(authenticationService.login(loginRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody PersonDto personDto){
        return ResponseEntity.ok(authenticationService.register(personDto));
    }
}