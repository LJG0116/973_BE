package com.nst.fitnessu.domain;

import lombok.*;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;
    private String content;
    private LocalDateTime messageTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    public Message (String content,LocalDateTime messageTime,ChatRoom chatRoom,User user){
        this.content=content;
        this.messageTime=messageTime;
        this.chatRoom=chatRoom;
        this.user=user;
    }

}
