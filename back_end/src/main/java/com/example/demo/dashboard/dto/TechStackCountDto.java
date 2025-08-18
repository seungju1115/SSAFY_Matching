package com.example.demo.dashboard.dto;

import com.example.demo.user.Enum.TechEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "기술 스택 별 사용자 수 집계용 dto")
public class TechStackCountDto {
    private TechEnum techStack;
    private Long count;
}
