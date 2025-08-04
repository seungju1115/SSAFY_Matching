package com.example.demo.team.dto;


import com.example.demo.team.entity.RequestType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamOffer {
    @NotNull(message = "요청 타입은 필수입니다.")
    private RequestType requestType;

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    @NotBlank(message = "메시지는 필수입니다.")
    private String message;

    @NotNull(message = "팀 ID는 필수입니다.")
    private Long teamId;
}
