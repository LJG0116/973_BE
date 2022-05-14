package com.nst.fitnessu.service;


import com.nst.fitnessu.domain.ChatRoom;
import com.nst.fitnessu.domain.ChatRoomJoin;
import com.nst.fitnessu.domain.Message;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.repository.ChatRoomJoinRepository;
import com.nst.fitnessu.repository.ChatRoomRepository;
import com.nst.fitnessu.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomJoinRepository chatRoomJoinRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    public List<ChatRoomJoin> findByUserId(Long userId){
        return (chatRoomJoinRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 User는 chatRoom을가지고 있지 않습니다.")));
    }

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

    public ChatRoom findById(Long id){ return chatRoomRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("없는 RoomId입니다."));}
    public void saveMessage(Message message){
        messageRepository.save(message);
    }


}
