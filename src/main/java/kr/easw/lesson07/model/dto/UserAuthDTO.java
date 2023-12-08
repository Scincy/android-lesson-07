package kr.easw.lesson07.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserAuthDTO {
    private final String token;
}