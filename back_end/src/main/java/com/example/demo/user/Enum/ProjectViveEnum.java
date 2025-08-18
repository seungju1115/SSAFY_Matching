package com.example.demo.user.Enum;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum ProjectViveEnum {
    CASUAL("반말 지향"), FORMAL("존대 지향"), COMFY("편한 분위기"),
    RULE("규칙적인 분위기"), LEADER("리더 중심"), DEMOCRACY("합의 중심"),
    BRANDNEW("새로운 주제"), STABLE("안정적인 주제"), AGILE("애자일 방식"),
    WATERFALL("워터폴 방식");


    private final String pref;

    ProjectViveEnum(String pref) {
        this.pref = pref;
    }

    public static Optional<ProjectViveEnum> findByPref(String pref) {
        return Arrays.stream(values())
                .filter(prefEnum -> prefEnum.pref.equals(pref))
                .findFirst();
    }
}
