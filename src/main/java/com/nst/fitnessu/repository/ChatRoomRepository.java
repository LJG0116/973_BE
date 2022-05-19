package com.nst.fitnessu.repository;

import com.nst.fitnessu.domain.ChatRoom;
import com.nst.fitnessu.domain.ChatRoomJoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findById(Long id);
}
