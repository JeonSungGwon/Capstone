package com.example.Capstone.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TodoDTO {
    private Long id;
    private String title;
    private boolean completed;
    private Date dueDate;
    private Long memberId;
    private String memberNickname;

    // 생성자, getter, setter
}
