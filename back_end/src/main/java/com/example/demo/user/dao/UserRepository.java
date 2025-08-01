package com.example.demo.user.dao;

import com.example.demo.dashboard.dto.UserCountDto;
import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.chatRoomMembers m " +
            "LEFT JOIN FETCH m.chatRoom " +
            "WHERE u.id = :id")
    Optional<com.example.demo.user.entity.User> findByIdWithChatRoomMembers(@Param("id") Long id);

    @Query(value = "select new com.example.demo.dashboard.dto.UserCountDto(u.team, u.major, u.wantedPosition) from User u")
    List<UserCountDto> CountUsers();
}
