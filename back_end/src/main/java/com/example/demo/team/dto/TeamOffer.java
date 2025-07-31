package com.example.demo.team.dto;


import com.example.demo.team.entity.RequestType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamOffer {
    public RequestType requestType;
    public Long userId;
    public String message;
    public Long teamId;
}
