package utcn.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utcn.chat.entity.ChatRoom;
import utcn.chat.repository.ChatRoomRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatRoomId(String senderUsername, String receiverUsername, boolean createRoomIfNotExists){
        Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderUsernameAndReceiverUsername(senderUsername, receiverUsername);
        if(chatRoom.isPresent()){
            return chatRoom.map(ChatRoom::getChatId);
        }
        if(createRoomIfNotExists){
            var chatId = createChatId(senderUsername, receiverUsername);
            return Optional.of(chatId);
        }
        return Optional.empty();
    }

    private String createChatId(String senderUsername, String receiverUsername){
        var chatId = String.format("%s_%s", senderUsername, receiverUsername);

        ChatRoom senderRecipient = ChatRoom.builder()
                .id(UUID.randomUUID())
                .chatId(chatId)
                .senderUsername(senderUsername)
                .receiverUsername(receiverUsername)
                .build();

        ChatRoom recipientSender = ChatRoom.builder()
                .id(UUID.randomUUID())
                .chatId(chatId)
                .senderUsername(receiverUsername)
                .receiverUsername(senderUsername)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);
        return chatId;
    }
}
