package utcn.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utcn.chat.entity.ChatRoom;

import java.util.Optional;
import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

    Optional<ChatRoom> findBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername);
}
