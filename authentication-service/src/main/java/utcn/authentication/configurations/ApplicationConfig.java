package utcn.authentication.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import utcn.authentication.dtos.PersonDto;
import utcn.authentication.entities.Person;
import utcn.authentication.entities.Role;
import utcn.authentication.services.CommunicationService;

import static utcn.authentication.services.CommunicationService.USER_SERVICE_URL;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            PersonDto personDto = CommunicationService.sendGetByCriteriaRequest(PersonDto.class, USER_SERVICE_URL + "person/" + username);
            if(personDto == null){
                throw new RuntimeException("User not found");
            }
            return Person.builder()
                    .id(personDto.getId())
                    .username(personDto.getUsername())
                    .password(personDto.getPassword())
                    .role(Role.valueOf(personDto.getRole().toUpperCase()))
                    .build();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}