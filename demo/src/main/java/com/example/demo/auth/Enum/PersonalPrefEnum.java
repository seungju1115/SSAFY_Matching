package com.example.demo.auth.Enum;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum PersonalPrefEnum {
    COMMUNICATE("지속적인 소통을 원해요"), CONCENTRATE("각자 업무에 집중하길 원해요");

    private final String pref;

    PersonalPrefEnum(String pref) {
        this.pref = pref;
    }

    public static Optional<PersonalPrefEnum> findByPref(String pref) {
        return Arrays.stream(values())
                .filter(prefEnum -> prefEnum.pref.equals(pref))
                .findFirst();
    }
}
