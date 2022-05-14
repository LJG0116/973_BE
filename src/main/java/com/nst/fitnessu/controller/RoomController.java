package com.nst.fitnessu.controller;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nst.fitnessu.domain.ChatRoom;
import com.nst.fitnessu.domain.ChatRoomJoin;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.dto.ChatRoomDto;
import com.nst.fitnessu.dto.LoginResponseDto;
import com.nst.fitnessu.dto.ResultResponse;
import com.nst.fitnessu.dto.RoomEnterDTO;
import com.nst.fitnessu.service.ChatService;
import com.nst.fitnessu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<ChatRoomDto> getUserChatRoom(String email){
        User user=userService.findOne(email).orElseThrow(() ->new IllegalArgumentException("없는 email입니다."));
        List<ChatRoomJoin> chatRooms=chatService.findByUserId(user.getId());
        List<ChatRoomDto> chatRoomList=new ArrayList<>();
        for(ChatRoomJoin cj:chatRooms){
            ChatRoom chatRoom=chatService.findById(cj.getChatRoom().getId());
            chatRoomList.add(new ChatRoomDto(chatRoom.getId(),chatRoom.getMessage()));
        }
        return chatRoomList;
    }
    /*
    @GetMapping("/chat/room")
    public ChatRoomDto getRoomById(Long id){
        ChatRoom chatRoom=chatService.findById(id);
        ChatRoomDto chatRoomDto=new ChatRoomDto(chatRoom.getId(),chatRoom.getMessage());
        return chatRoomDto;
    }*/
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
    public ResponseEntity<ResultResponse> getRoomByEmail(RoomEnterDTO roomEnterDTO){
        //findbynickname
        User sender=userService.findOne(roomEnterDTO.getSender())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        User receiver=userService.findOne(roomEnterDTO.getReceiver())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        //And로 처리..
        Long senderId=sender.getId();
        Long receiverId=receiver.getId();
        List<ChatRoomJoin> sendChatRoom=chatService.findByUserId(senderId);
        List<ChatRoomJoin> receiverChatRoom=chatService.findByUserId(receiverId);
        List<Long> senderRoomIdList=new ArrayList<>();
        List<Long> receiverRoomIdList=new ArrayList<>();
        ChatRoom chatRoom;
        //roomid뽑는 과정
        for(ChatRoomJoin cj: sendChatRoom ){
            senderRoomIdList.add(cj.getChatRoom().getId());
        }
        for(ChatRoomJoin cj: receiverChatRoom){
            receiverRoomIdList.add(cj.getChatRoom().getId());
        }
        //교집합
        senderRoomIdList.retainAll(receiverRoomIdList);
        ResultResponse<ChatRoomDto> resultResponse=new ResultResponse<>();
        if(senderRoomIdList.size()==0){
            chatRoom=chatService.createRoom(sender,receiver);
        }else{
            chatRoom=chatService.findById(senderRoomIdList.get(0));
        }
        ChatRoomDto chatRoomDto=new ChatRoomDto(chatRoom.getId(),chatRoom.getMessage());
        resultResponse.successResponse("채팅방 불러오기 성공",chatRoomDto);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
