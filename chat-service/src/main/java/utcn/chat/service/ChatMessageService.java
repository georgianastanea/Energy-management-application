package utcn.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utcn.chat.entity.ChatMessage;
import utcn.chat.repository.ChatMessageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage){
        var chatId = chatRoomService.getChatRoomId(chatMessage.getSenderUsername(), chatMessage.getReceiverUsername(), true).orElseThrow();
        chatMessage.setChatId(chatId);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(String senderUsername, String receiverUsername){
        var chatId = chatRoomService.getChatRoomId(senderUsername, receiverUsername, false);
        return chatId.map(chatMessageRepository::findByChatId).orElse(new ArrayList<>());
    }
}
