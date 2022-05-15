package com.nst.fitnessu.controller;


import com.nst.fitnessu.domain.*;
import com.nst.fitnessu.dto.chat.*;
import com.nst.fitnessu.dto.ResultResponse;
import com.nst.fitnessu.dto.post.ViewPostRequestDto;
import com.nst.fitnessu.service.ChatService;
import com.nst.fitnessu.service.PostService;
import com.nst.fitnessu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    private final PostService postService;

    @GetMapping("/chat/rooms")
    public ResponseEntity<ResultResponse> getUserChatRoom(Long userId){
        List<ChatRoomJoinDto> chatRooms=chatService.findByUserId(userId);
        List<ChatRoomDto> chatRoomList=new ArrayList<>();
        ResultResponse<List<ChatRoomDto>> resultResponse=new ResultResponse<>();
        for(ChatRoomJoinDto cj:chatRooms){
            ChatRoom chatRoom=chatService.findById(cj.getChatRoomId());
            List<ChatRoomMessageDto> messageDtoList=new ArrayList<>();
            for(Message msg:chatRoom.getMessage()){
                messageDtoList.add(new ChatRoomMessageDto(msg.getUser().getId(),msg.getChatRoom().getId(),msg.getMessageTime(),msg.getContent()));
            }
            chatRoomList.add(new ChatRoomDto(chatRoom.getId(),messageDtoList));
        }
        resultResponse.successResponse("채팅방 불러오기 성공",chatRoomList);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }


/*
    @GetMapping("/chat/rooms")
    public ResponseEntity<ResultResponse> getUserChatRoom(String email){
        User user=userService.findByEmail(email).orElseThrow(() ->new IllegalArgumentException("없는 email입니다."));
        List<ChatRoomJoin> chatRooms=chatService.findByUserId(user.getId());
        List<ChatRoomDto> chatRoomList=new ArrayList<>();
        ResultResponse<List<ChatRoomDto>> resultResponse=new ResultResponse<>();
        for(ChatRoomJoin cj:chatRooms){
            ChatRoom chatRoom=chatService.findById(cj.getChatRoom().getId());
            chatRoomList.add(new ChatRoomDto(chatRoom.getId(),chatRoom.getMessage()));
        }
        resultResponse.successResponse("채팅방 불러오기 성공",chatRoomList);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
    */

    /*
    @GetMapping("/chat/room")
    public ResponseEntity<ResultResponse> getRoomById(Long id){
        ChatRoom chatRoom=chatService.findById(id);
        ChatRoomDto chatRoomDto=new ChatRoomDto(chatRoom.getId(),chatRoom.getMessage());
        ResultResponse<ChatRoomDto> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("채팅방 불러오기 성공",chatRoomDto);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);;
    }*/

    //sockejs 테스트
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

    @Transactional
    @GetMapping("/chat/enter")
    public ResponseEntity<ResultResponse> getRoomBySenderIdAndPostId(RoomEnterRequestDto roomEnterDTO){

        //사용자 정보를 받아오는 과정
        List<ChatRoomJoinDto> sendChatRoom=chatService.findByUserId(roomEnterDTO.getSenderId());
        User receiver=postService.findPostInChat(roomEnterDTO);
        List<ChatRoomJoinDto> receiverChatRoom=chatService.findByUserId(receiver.getId());

        List<Long> senderRoomIdList=new ArrayList<>();
        List<Long> receiverRoomIdList=new ArrayList<>();
        ChatRoom chatRoom;
        //roomid뽑는 과정
        for(ChatRoomJoinDto cj: sendChatRoom ){
            senderRoomIdList.add(cj.getChatRoomId());
        }
        for(ChatRoomJoinDto cj: receiverChatRoom){
            receiverRoomIdList.add(cj.getChatRoomId());
        }
        //교집합
        senderRoomIdList.retainAll(receiverRoomIdList);
        ResultResponse<ChatRoomDto> resultResponse=new ResultResponse<>();
        if(senderRoomIdList.size()==0){
            chatRoom=chatService.createRoom(roomEnterDTO.getSenderId(), receiver.getId());
        }else{
            chatRoom=chatService.findById(senderRoomIdList.get(0));
        }

        List<ChatRoomMessageDto> messageDtoList=new ArrayList<>();
        for(Message msg:chatRoom.getMessage()){
            messageDtoList.add(new ChatRoomMessageDto(msg.getUser().getId(),msg.getChatRoom().getId(),msg.getMessageTime(),msg.getContent()));
        }

        ChatRoomDto chatRoomDto=new ChatRoomDto(chatRoom.getId(),messageDtoList);
        resultResponse.successResponse("채팅방 불러오기 성공",chatRoomDto);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
/*
    @GetMapping("/chat/enter")
    public ResponseEntity<ResultResponse> getRoomBySenderIdAndReceiverId(RoomEnterDTO roomEnterDTO){
        List<ChatRoomJoin> sendChatRoom=chatService.findByUserId(roomEnterDTO.getSenderId());
        List<ChatRoomJoin> receiverChatRoom=chatService.findByUserId(roomEnterDTO.getReceiverId());
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
            chatRoom=chatService.createRoom(roomEnterDTO.getSenderId(),roomEnterDTO.getReceiverId());
        }else{
            chatRoom=chatService.findById(senderRoomIdList.get(0));
        }
        ChatRoomDto chatRoomDto=new ChatRoomDto(chatRoom.getId(),chatRoom.getMessage());
        resultResponse.successResponse("채팅방 불러오기 성공",chatRoomDto);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
 */