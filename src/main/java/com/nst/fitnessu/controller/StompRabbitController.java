package com.nst.fitnessu.controller;

import com.nst.fitnessu.domain.Message;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.dto.chat.ChatRoomMessageDto;
import com.nst.fitnessu.dto.chat.MessageDto;
import com.nst.fitnessu.dto.chat.RabbitChatDto;
import com.nst.fitnessu.service.ChatService;
import com.nst.fitnessu.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.ZoneId;


@Controller
@RequiredArgsConstructor
@Log4j2
public class StompRabbitController {

    private final RabbitTemplate template;

    private final ChatService chatService;
    private final UserService userService;

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final static String CHAT_QUEUE_NAME = "chat.queue";


    @MessageMapping("chat.enter.{chatRoomId}")
    public void enter(MessageDto chat, @DestinationVariable String chatRoomId){


        template.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chat);
    }


    @MessageMapping("chat.message")
    @Transactional(readOnly = false)


    public void send(MessageDto messageDto){
/*
        User user=userService.findByNickname(messageDto.getNickname())
                .orElseThrow(() -> new IllegalArgumentException("없는 ID 입니다."));


 */
        User user=userService.findById(messageDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("없는 ID 입니다."));

        LocalDateTime localDateTime=LocalDateTime.now(ZoneId.of("Asia/Seoul")).withNano(0);
        ChatRoomMessageDto chatRoomMessageDto=new ChatRoomMessageDto(user.getId(),user.getNickname(),user.getProfileImage() ,messageDto.getRoomId(), localDateTime,messageDto.getContent());
        RabbitChatDto rabbitChatDto=new RabbitChatDto(user.getId(),user.getNickname(),user.getProfileImage() ,messageDto.getRoomId(), localDateTime.toString(),messageDto.getContent());
        System.out.println("sendMessage : " + chatRoomMessageDto.getContent());
        Message message=new Message( chatRoomMessageDto.getContent()
                ,localDateTime
                ,chatService.findById(chatRoomMessageDto.getRoomId())
                ,userService.findById(chatRoomMessageDto.getUserId())
                .orElseThrow(()-> new IllegalArgumentException("없는 Id입니다.")));
        template.convertAndSend(CHAT_EXCHANGE_NAME,"room." + chatRoomMessageDto.getRoomId(), rabbitChatDto);
        //simpMessageSendingOperation.convertAndSend("chat.exchange/room." + chatRoomMessageDto.getRoomId(), chatRoomMessageDto);
        chatService.saveMessage(message);
    }

}
