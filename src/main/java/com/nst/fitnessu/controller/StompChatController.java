package com.nst.fitnessu.controller;

import com.nst.fitnessu.domain.ChatRoom;
import com.nst.fitnessu.domain.Message;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.dto.MessageDto;
import com.nst.fitnessu.service.ChatService;
import com.nst.fitnessu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public void sendMessage(@RequestBody MessageDto messageDto){
        System.out.println("sendMessage");
        messageDto.setContent("adsfadsf");
        Message message=new Message( messageDto.getContent()
                ,LocalDateTime.now()
                ,chatService.findById(messageDto.getRoomId())
                ,userService.findById(messageDto.getUserId()));
        chatService.saveMessage(message);
        simpMessageSendingOperation.convertAndSend("/sub/chat/room/"+messageDto.getRoomId(),messageDto);
    }

}
