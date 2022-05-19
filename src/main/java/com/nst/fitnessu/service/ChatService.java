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
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    @PersistenceContext
    EntityManager em;

    public List<ChatRoomJoinDto> findByUserId(Long userId){
        String queryString="select chat_room_id,user_id,nickname from "
                + "user natural join chat_room_join "
                +"WHERE chat_room_id in (select T.chat_room_id from chat_room_join as T WHERE user_id="+userId+")"
                +" AND user_id !="+userId;
        JpaResultMapper resultMapper=new JpaResultMapper();
        Query query=em.createNativeQuery(queryString);
        List<ChatRoomJoinDto> chatRoomList=resultMapper.list(query,ChatRoomJoinDto.class);
        return chatRoomList;
    }

    @Transactional
    public ChatRoom createRoom(Long senderId,Long receiverId){
        User sender=userService.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 Id 입니다."));
        User receiver=userService.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 Id 입니다."));
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
    public List<Message> getMessages(Long chatRoomId){
        return messageRepository.findByChatRoomIdOrderByMessageTimeDesc(chatRoomId)
                .orElseThrow(()-> new IllegalArgumentException("없는 채팅방 입니다."));
    }


    public ChatRoom findById(Long id){ return chatRoomRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("없는 RoomId입니다."));}
    public void saveMessage(Message message){
        messageRepository.save(message);
    }


}
