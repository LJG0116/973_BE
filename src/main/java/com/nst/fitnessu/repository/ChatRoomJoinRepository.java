package com.nst.fitnessu.repository;


import com.nst.fitnessu.domain.ChatRoomJoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long> {
    Optional<List<ChatRoomJoin>> findByUserId(Long userId);
}
