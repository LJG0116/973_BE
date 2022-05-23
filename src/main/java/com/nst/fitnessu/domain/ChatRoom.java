package com.nst.fitnessu.domain;


import com.fasterxml.jackson.annotation.*;
import lombok.*;


import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @JsonManagedReference
    @OneToMany
    private List<ChatRoomJoin> chatRoomJoins;

    @JsonManagedReference
    @OneToMany(mappedBy = "chatRoom")
    private List<Message> message;


    public ChatRoom(){
        this.chatRoomJoins=new ArrayList<>();
        this.message=new ArrayList<>();
    }

}