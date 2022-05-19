package com.nst.fitnessu.repository;


import com.nst.fitnessu.domain.ChatRoomJoin;
import com.nst.fitnessu.dto.chat.ChatRoomJoinDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.NamedNativeQuery;
import java.util.List;
import java.util.Optional;

public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long> {
    Optional<List<ChatRoomJoin>> findByUserId(Long userId);
}
