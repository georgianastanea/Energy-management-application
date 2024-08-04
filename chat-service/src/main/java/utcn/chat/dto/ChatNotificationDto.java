package utcn.chat.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotificationDto {

    private UUID id;
    private String senderUsername;
    private String receiverUsername;
    private String content;
}
