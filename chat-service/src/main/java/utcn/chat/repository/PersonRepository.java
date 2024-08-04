package utcn.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utcn.chat.dto.PersonDto;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<PersonDto, UUID> {

    Optional<PersonDto> findByUsername(String username);
}
