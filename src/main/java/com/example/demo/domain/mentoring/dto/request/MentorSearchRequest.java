package com.example.demo.domain.mentoring.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MentorSearchRequest {

    private List<String> skills;

    private String location;
}
