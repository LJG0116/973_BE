package com.nst.fitnessu.service;


import com.nst.fitnessu.domain.ChatRoom;
import com.nst.fitnessu.domain.ChatRoomJoin;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.repository.ChatRoomJoinRepository;
import com.nst.fitnessu.repository.ChatRoomRepository;
import com.nst.fitnessu.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomJoinRepository chatRoomJoinRepository;
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoom> findByUser(User user){
        return (chatRoomJoinRepository.findByUser(user).get());
    }

    public ChatRoom createRoom(User sender,User receiver){
        ChatRoom chatRoom=new ChatRoom();
        ChatRoomJoin chatRoomJoinSender=new ChatRoomJoin(sender,chatRoom);
        ChatRoomJoin chatRoomJoinReceiver=new ChatRoomJoin(receiver,chatRoom);
        chatRoomJoinRepository.save(chatRoomJoinSender);
        chatRoomJoinRepository.save(chatRoomJoinReceiver);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }
    public ChatRoom findById(Long id){ return chatRoomRepository.findById(id).get();}


}
