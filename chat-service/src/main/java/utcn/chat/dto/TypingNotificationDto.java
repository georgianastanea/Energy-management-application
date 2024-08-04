package utcn.chat.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TypingNotificationDto {

    private String senderUsername;
    private String receiverUsername;
    private boolean typing;
}
