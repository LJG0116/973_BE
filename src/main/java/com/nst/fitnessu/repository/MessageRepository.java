package com.nst.fitnessu.repository;

import com.nst.fitnessu.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    public Optional<List<Message>> findByChatRoomIdOrderByMessageTimeAsc(Long chatRoomId);
}
