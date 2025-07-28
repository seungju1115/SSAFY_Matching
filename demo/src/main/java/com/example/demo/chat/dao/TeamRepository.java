package com.example.demo.chat.dao;

import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.chat.entity.RoomType;
import com.example.demo.chat.entity.Team;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);

}
