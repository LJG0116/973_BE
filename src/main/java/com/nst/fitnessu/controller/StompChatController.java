package com.nst.fitnessu.controller;

import com.nst.fitnessu.domain.Message;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.dto.chat.ChatRoomMessageDto;
import com.nst.fitnessu.dto.chat.MessageDto;
import com.nst.fitnessu.service.ChatService;
import com.nst.fitnessu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequiredArgsConstructor
public class StompChatController {
    @Autowired
    private SimpMessagingTemplate template;
    private final ChatService chatService;
    private final UserService userService;
    private final SimpMessageSendingOperations simpMessageSendingOperation;

    @Transactional
    @MessageMapping(value = "/chat/exit")
    public void exit(MessageDto message) {
    }

    @Transactional
    @MessageMapping(value = "/chat/message")
    public void sendMessage(MessageDto messageDto){
        User user=userService.findById(messageDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("없는 ID 입니다."));
        ChatRoomMessageDto chatRoomMessageDto=new ChatRoomMessageDto(user.getId(),user.getNickname(),user.getProfileImage() ,messageDto.getRoomId(), LocalDateTime.now(ZoneId.of("Asia/Seoul")).withNano(0),messageDto.getContent());
        System.out.println("sendMessage : " + chatRoomMessageDto.getContent());
        Message message=new Message( chatRoomMessageDto.getContent()
                ,chatRoomMessageDto.getMessageTime()
                ,chatService.findById(chatRoomMessageDto.getRoomId())
                ,userService.findById(chatRoomMessageDto.getUserId())
                .orElseThrow(()-> new IllegalArgumentException("없는 Id입니다.")));
        chatService.saveMessage(message);
        simpMessageSendingOperation.convertAndSend("/sub/chat/room/"+chatRoomMessageDto.getRoomId(),chatRoomMessageDto);
    }
}
