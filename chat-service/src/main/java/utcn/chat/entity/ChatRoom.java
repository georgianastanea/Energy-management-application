package utcn.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ChatRoom {

    @Id
    private UUID id;
    private String chatId;
    private String senderUsername;
    private String receiverUsername;
}
