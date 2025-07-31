package com.example.demo.user.dao;

import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.chatRoomMembers m " +
            "LEFT JOIN FETCH m.chatRoom " +
            "WHERE u.id = :id")
    Optional<User> findByIdWithChatRoomMembers(@Param("id") Long id);

}