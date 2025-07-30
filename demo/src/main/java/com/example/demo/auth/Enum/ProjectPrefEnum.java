package com.example.demo.auth.Enum;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum ProjectPrefEnum {
    STABLE("안정적인 프로젝트 진행을 원해요"), CHALLENGE("도전적인 경험을 원해요");

    private final String pref;

    ProjectPrefEnum(String pref) {
        this.pref = pref;
    }

    public static Optional<ProjectPrefEnum> findByPref(String pref) {
        return Arrays.stream(values())
                .filter(prefEnum -> prefEnum.pref.equals(pref))
                .findFirst();
    }
}
