package com.nst.fitnessu.controller;


import com.nst.fitnessu.domain.ChatRoom;
import com.nst.fitnessu.domain.ChatRoomJoin;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.dto.RoomEnterDTO;
import com.nst.fitnessu.service.ChatService;
import com.nst.fitnessu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final ChatService chatService;
    private final UserService userService;

    @GetMapping("/chat/rooms")
    public List<ChatRoom> getUserChatRoom(String email){
        List<ChatRoomJoin> chatRooms=chatService.findByUser(userService.findOne(email).get());
        List<ChatRoom> chatRoomList=new ArrayList<>();
        for(ChatRoomJoin cj:chatRooms){
            chatRoomList.add(chatService.findById(cj.getChatRoom().getId()));
        }
        return chatRoomList;
    }
    @GetMapping("/chat/room")
    public ModelAndView getRoomById(Long id){
        //return chatService.findById(id);
        ChatRoom chatRoom=chatService.findById(id);
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("room");
        Map<String,Object> map=new HashMap<>();
        map.put("chatRoom",chatRoom);
        return modelAndView;
    }

    @GetMapping("/chat/enter")
    public ChatRoom getRoomByEmail(RoomEnterDTO roomEnterDTO){
        User sender=userService.findOne(roomEnterDTO.getSender()).get();
        User receiver=userService.findOne(roomEnterDTO.getReceiver()).get();
        List<ChatRoomJoin> sendChatRoom=chatService.findByUser(sender);
        List<ChatRoom> sendChatRoomList=new ArrayList<>();
        for(ChatRoomJoin cj:sendChatRoom)
            sendChatRoomList.add(chatService.findById(cj.getChatRoom().getId()));
        List<ChatRoomJoin> receiverChatRoom=chatService.findByUser(receiver);
        List<ChatRoom> receiverChatList= new ArrayList<>();
        for(ChatRoomJoin cj:receiverChatRoom)
            receiverChatList.add(chatService.findById(cj.getChatRoom().getId()));
        sendChatRoomList.retainAll(receiverChatList);
        if(sendChatRoomList.size()==0){
            ChatRoom chatRoom=chatService.createRoom(sender,receiver);
            return chatRoom;
        }else{
            return sendChatRoomList.get(0);
        }
    }
}
