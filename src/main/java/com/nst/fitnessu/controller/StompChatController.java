package com.nst.fitnessu.controller;

import com.nst.fitnessu.domain.ChatRoom;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.dto.MessageDto;
import com.nst.fitnessu.service.ChatService;
import com.nst.fitnessu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StompChatController {
    @Autowired
    private SimpMessagingTemplate template;
    private final UserService userService;
    private final ChatService chatService;



    @MessageMapping(value ="/chat/enter")
    public ChatRoom enter(MessageDto messageDTO){
        ChatRoom chatRoom;
        Long senderId=messageDTO.getSend();
        User senderUser=userService.findById(senderId);
        Long receiverId=messageDTO.getReceive();
        User receiverUser=userService.findById(receiverId);
        String room_id=messageDTO.getRoomId();
        if(room_id==""){
            List<ChatRoom> sendChatRoom=chatService.findByUser(senderUser);
            List<ChatRoom> receiverChatRoom=chatService.findByUser(receiverUser);
            sendChatRoom.retainAll(receiverChatRoom);
            if(sendChatRoom.size()==0){
                chatRoom=chatService.createRoom(senderUser,receiverUser);
                template.convertAndSendToUser("/sub/chat/room/",chatRoom.getId().toString(),messageDTO.getMessage());
            }else{
                chatRoom=sendChatRoom.get(0);
                template.convertAndSendToUser("/sub/chat/room/",sendChatRoom.get(0).getId().toString(),messageDTO.getMessage());
            }
        }else{
            chatRoom=chatService.findById(Long.parseLong(room_id));
            template.convertAndSendToUser("/sub/chat/room/",room_id,messageDTO.getMessage());
        }
        return chatRoom;
    }
    @Transactional
    @MessageMapping(value = "/chat/exit")
    public void exit(MessageDto message) {
    }

    @MessageMapping(value = "/chat/message")
    public void message(MessageDto message){
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
