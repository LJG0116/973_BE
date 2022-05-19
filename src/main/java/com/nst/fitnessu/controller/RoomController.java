package com.nst.fitnessu.controller;


import com.nst.fitnessu.domain.*;
import com.nst.fitnessu.dto.chat.*;
import com.nst.fitnessu.dto.ResultResponse;
import com.nst.fitnessu.service.ChatService;
import com.nst.fitnessu.service.PostService;
import com.nst.fitnessu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final ChatService chatService;
    private final UserService userService;
    private final PostService postService;

    @GetMapping("/chat/rooms")
    public ResponseEntity<ResultResponse> getUserChatRoom(Long userId){
        List<ChatRoomJoinDto> chatRooms=chatService.findByUserId(userId);
        ResultResponse<List<ChatRoomJoinDto>> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("채팅방 목록 불러오기 성공",chatRooms);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }


    @GetMapping("/chat/room")
    public ResponseEntity<ResultResponse> getRoomById(RoomSelectDto roomSelectDto){
        User user=userService.findById(roomSelectDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("없는 Id 입니다."));
        List<ChatRoomMessageDto> chatRoomMessageDtoList=new ArrayList<>();
        List<Message> messages=chatService.getMessages(roomSelectDto.getRoomId());
        for(Message msg: messages){
            chatRoomMessageDtoList.add(new ChatRoomMessageDto(
                             msg.getUser().getId()
                            ,msg.getUser().getNickname()
                            ,roomSelectDto.getRoomId()
                            ,msg.getMessageTime()
                            ,msg.getContent())
            );
        }
        ChatRoomDto chatRoomDto=new ChatRoomDto(roomSelectDto.getRoomId(), chatRoomMessageDtoList);
        ResultResponse<ChatRoomDto> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("채팅방 불러오기 성공",chatRoomDto);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
    
    /*
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
    */

    @Transactional
    @GetMapping("/chat/enter")
    public ResponseEntity<ResultResponse> getRoomBySenderIdAndPostId(RoomEnterRequestDto roomEnterDTO){
        Long roomId;
        //사용자 정보를 받아오는 과정
        List<ChatRoomJoinDto> sendChatRoom=chatService.findByUserId(roomEnterDTO.getUserId());
        List<BigInteger> senderRoomIdList=new ArrayList<>();
        List<BigInteger> receiverRoomIdList=new ArrayList<>();
        User receiver=postService.findPostInChat(roomEnterDTO);
        List<ChatRoomJoinDto> receiverChatRoom=chatService.findByUserId(receiver.getId());
        List<ChatRoomMessageDto> messageDtoList=new ArrayList<>();
        ChatRoom chatRoom;
        for(ChatRoomJoinDto chatRoomJoinDto: sendChatRoom){
            senderRoomIdList.add(chatRoomJoinDto.getChatRoomId());
        }
        for(ChatRoomJoinDto chatRoomJoinDto: receiverChatRoom){
            receiverRoomIdList.add(chatRoomJoinDto.getChatRoomId());
        }

        //교집합
        senderRoomIdList.retainAll(receiverRoomIdList);
        ResultResponse<ChatRoomDto> resultResponse=new ResultResponse<>();
        if(senderRoomIdList.size()==0){
            chatRoom=chatService.createRoom(roomEnterDTO.getUserId(), receiver.getId());
            roomId= chatRoom.getId();
        }else{
            roomId= senderRoomIdList.get(0).longValue();
            List<Message> messages=chatService.getMessages(roomId);
            for(Message message: messages){
                messageDtoList.add(new ChatRoomMessageDto(message.getUser().getId()
                        ,message.getUser().getNickname()
                        ,roomId
                        ,message.getMessageTime()
                        ,message.getContent()));
            }
        }
        ChatRoomDto chatRoomDto=new ChatRoomDto(roomId,messageDtoList);
        resultResponse.successResponse("채팅방 불러오기 성공",chatRoomDto);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
