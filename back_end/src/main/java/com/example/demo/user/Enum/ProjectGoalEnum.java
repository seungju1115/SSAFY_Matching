package com.example.demo.user.Enum;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum ProjectGoalEnum {
    JOB("취업우선"), AWARD("수상목표"), PORTFOLIO("포트폴리오중심"),
    STUDY("학습중심"), IDEA("아이디어실현"), PROFESSIONAL("실무경험"),
    QUICK("빠른개발"), QUALITY("완성도추구");

    private final String pref;

    ProjectGoalEnum(String pref) {
        this.pref = pref;
    }

    public static Optional<ProjectGoalEnum> findByPref(String pref) {
        return Arrays.stream(values())
                .filter(prefEnum -> prefEnum.pref.equals(pref))
                .findFirst();
    }
}
