package utcn.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import utcn.chat.dto.ReadReceiptDto;
import utcn.chat.dto.TypingNotificationDto;
import utcn.chat.entity.ChatMessage;
import utcn.chat.dto.ChatNotificationDto;
import utcn.chat.service.ChatMessageService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage){
        ChatMessage savedMassage = chatMessageService.save(chatMessage);
        simpMessagingTemplate.convertAndSendToUser(
                chatMessage.getReceiverUsername(),
                "/queue/messages",
                ChatNotificationDto.builder()
                        .id(savedMassage.getId())
                        .senderUsername(savedMassage.getSenderUsername())
                        .receiverUsername(savedMassage.getReceiverUsername())
                        .content(savedMassage.getContent())
                        .build()
        );
    }

    @MessageMapping("/readReceipt")
    public void sendReadReceipt(@Payload ReadReceiptDto readReceiptDto) {
        if(readReceiptDto.getReceiverUsername() != null){
            simpMessagingTemplate.convertAndSendToUser(
                    readReceiptDto.getReceiverUsername(),
                    "/queue/readReceipts",
                    readReceiptDto
            );
        }
    }

    @MessageMapping("/typing")
    public void processMessage(@Payload TypingNotificationDto typingNotificationDto){
        if(typingNotificationDto.getReceiverUsername() != null){
            simpMessagingTemplate.convertAndSendToUser(
                    typingNotificationDto.getReceiverUsername(),
                    "/queue/typing",
                    typingNotificationDto
            );
        }
    }


    @GetMapping("/messages/{senderUsername}/{receiverUsername}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
            @PathVariable("senderUsername") String senderUsername,
            @PathVariable("receiverUsername") String receiverUsername
            ){
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderUsername, receiverUsername));
    }
}
