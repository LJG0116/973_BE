package com.nst.fitnessu.service;


import com.nst.fitnessu.domain.ChatRoom;
import com.nst.fitnessu.domain.ChatRoomJoin;
import com.nst.fitnessu.domain.Message;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.dto.chat.ChatRoomJoinDto;
import com.nst.fitnessu.repository.ChatRoomJoinRepository;
import com.nst.fitnessu.repository.ChatRoomRepository;
import com.nst.fitnessu.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomJoinRepository chatRoomJoinRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;




    public List<ChatRoomJoinDto> findByUserId(Long userId){
        List<ChatRoomJoin> chatRoomJoinList=chatRoomJoinRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 User는 chatRoom을가지고 있지 않습니다."));
        List<ChatRoomJoinDto> chatRoomJoinDtoList=new ArrayList<>();
        for(ChatRoomJoin cj: chatRoomJoinList){
            chatRoomJoinDtoList.add(new ChatRoomJoinDto(cj.getId(),cj.getUser(),cj.getChatRoom().getId()));
        }
        return chatRoomJoinDtoList;
    }

    @Transactional
    public ChatRoom createRoom(Long senderId,Long receiverId){
        User sender=userService.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        User receiver=userService.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        ChatRoom chatRoom=new ChatRoom();
        ChatRoomJoin chatRoomJoinSender=new ChatRoomJoin(sender,chatRoom);
        ChatRoomJoin chatRoomJoinReceiver=new ChatRoomJoin(receiver,chatRoom);
        chatRoomRepository.save(chatRoom);
        chatRoomJoinRepository.save(chatRoomJoinSender);
        chatRoomJoinRepository.save(chatRoomJoinReceiver);
        return chatRoom;
    }

    /*
    @Transactional
    public ChatRoom createRoom(User sender,User receiver){
        ChatRoom chatRoom=new ChatRoom();
        ChatRoomJoin chatRoomJoinSender=new ChatRoomJoin(sender,chatRoom);
        ChatRoomJoin chatRoomJoinReceiver=new ChatRoomJoin(receiver,chatRoom);
        chatRoomRepository.save(chatRoom);
        chatRoomJoinRepository.save(chatRoomJoinSender);
        chatRoomJoinRepository.save(chatRoomJoinReceiver);
        //System.out.println(chatRoom.getChatRoomJoins().size());
        System.out.println(chatRoom.getMessage().size());
        return chatRoom;
    }


     */



    public ChatRoom findById(Long id){ return chatRoomRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("없는 RoomId입니다."));}
    public void saveMessage(Message message){
        messageRepository.save(message);
    }


}
