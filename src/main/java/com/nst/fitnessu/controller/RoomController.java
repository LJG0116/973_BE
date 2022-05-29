package com.nst.fitnessu.controller;


import com.nst.fitnessu.domain.*;
import com.nst.fitnessu.dto.chat.*;
import com.nst.fitnessu.dto.ResultResponse;
import com.nst.fitnessu.service.ChatService;
import com.nst.fitnessu.service.PostService;
import com.nst.fitnessu.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
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
    @ApiOperation(value = "채팅방 목록 조회")
    public ResponseEntity<ResultResponse> getUserChatRoom(Long userId){
        List<ChatRoomJoinDto> chatRooms=chatService.findByUserId(userId);
        User user=userService.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("없는 사용자 입니다."));
        ResultResponse<List<ChatRoomJoinResponseDto>> resultResponse=new ResultResponse<>();
        List<ChatRoomJoinResponseDto> chatRoomJoinResponseDtos=new ArrayList<>();
        for(ChatRoomJoinDto chatRoomJoinDto: chatRooms){
            User receiver=userService.findById(chatRoomJoinDto.getUserId().longValue())
                    .orElseThrow(()->new IllegalArgumentException("없는 사용자 입니다."));
            chatRoomJoinResponseDtos.add(new ChatRoomJoinResponseDto(user.getId(),
                    user.getNickname(),
                    chatRoomJoinDto.getChatRoomId().longValue(),
                    chatRoomJoinDto.getUserId().longValue(),
                    chatRoomJoinDto.getNickname(),
                    receiver.getProfileImage()
            ));
        }
        resultResponse.successResponse("채팅방 목록 불러오기 성공",chatRoomJoinResponseDtos);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }


    @GetMapping("/chat/room")
    @ApiOperation(value = "채팅 목록에서 채팅방 선택")
    public ResponseEntity<ResultResponse> getRoomById(RoomSelectDto roomSelectDto){
        User user=userService.findById(roomSelectDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("없는 Id 입니다."));
        List<ChatRoomMessageDto> chatRoomMessageDtoList=new ArrayList<>();
        List<Message> messages=chatService.getMessages(roomSelectDto.getRoomId());
        for(Message msg: messages){
            chatRoomMessageDtoList.add(new ChatRoomMessageDto(
                             msg.getUser().getId()
                            ,msg.getUser().getNickname()
                            ,msg.getUser().getProfileImage()
                            ,roomSelectDto.getRoomId()
                            ,msg.getMessageTime()
                            ,msg.getContent())
            );
        }
        ChatRoomDto chatRoomDto=new ChatRoomDto(roomSelectDto.getRoomId(),user.getId(),user.getNickname(), chatRoomMessageDtoList);
        ResultResponse<ChatRoomDto> resultResponse=new ResultResponse<>();
        resultResponse.successResponse("채팅방 불러오기 성공",chatRoomDto);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
    

    //sockejs 테스트
    @GetMapping("/chat/test")
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
    @ApiOperation(value = "게시글에서 채팅방 불러올 때")
    public ResponseEntity<ResultResponse> getRoomBySenderIdAndPostId(RoomEnterRequestDto roomEnterDTO){
        Long roomId;
        //사용자 정보를 받아오는 과정
        List<ChatRoomJoinDto> sendChatRoom=chatService.findByUserId(roomEnterDTO.getSenderId());
        List<BigInteger> senderRoomIdList=new ArrayList<>();
        List<BigInteger> receiverRoomIdList=new ArrayList<>();
        User sender=userService.findById(roomEnterDTO.getSenderId()).
                orElseThrow(()-> new IllegalArgumentException("없는 유저ID입니다."));
        User receiver=userService.findById(roomEnterDTO.getReceiverId()).
                orElseThrow(()-> new IllegalArgumentException("없는 유저ID입니다."));
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
            chatRoom=chatService.createRoom(roomEnterDTO.getSenderId(), receiver.getId());
            roomId= chatRoom.getId();
        }else{
            roomId= senderRoomIdList.get(0).longValue();
            List<Message> messages=chatService.getMessages(roomId);
            for(Message message: messages){
                messageDtoList.add(new ChatRoomMessageDto(message.getUser().getId()
                        ,message.getUser().getNickname()
                        ,message.getUser().getProfileImage()
                        ,roomId
                        ,message.getMessageTime()
                        ,message.getContent()));
            }
        }
        ChatRoomDto chatRoomDto=new ChatRoomDto(roomId, sender.getId(), sender.getNickname(), messageDtoList);
        resultResponse.successResponse("채팅방 불러오기 성공",chatRoomDto);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
