package com.nst.fitnessu.service;


import com.nst.fitnessu.domain.ChatRoom;
import com.nst.fitnessu.domain.ChatRoomJoin;
import com.nst.fitnessu.domain.Message;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.repository.ChatRoomJoinRepository;
import com.nst.fitnessu.repository.ChatRoomRepository;
import com.nst.fitnessu.repository.MessageRepository;
import com.nst.fitnessu.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomJoinRepository chatRoomJoinRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    public List<ChatRoomJoin> findByUser(User user){
        return (chatRoomJoinRepository.findByUser(user).get());
    }

    public ChatRoom createRoom(User sender,User receiver){
        Random random=new Random();
        ChatRoom chatRoom=new ChatRoom();
        ChatRoomJoin chatRoomJoinSender=new ChatRoomJoin(sender,chatRoom);
        ChatRoomJoin chatRoomJoinReceiver=new ChatRoomJoin(receiver,chatRoom);
        chatRoomRepository.save(chatRoom);
        chatRoomJoinRepository.save(chatRoomJoinSender);
        chatRoomJoinRepository.save(chatRoomJoinReceiver);
        return chatRoom;
    }
    public ChatRoom findById(Long id){ return chatRoomRepository.findById(id).get();}
    public void saveMessage(Message message){
        messageRepository.save(message);
    }


}
