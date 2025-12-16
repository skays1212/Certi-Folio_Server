package com.example.demo.domain.mentoring.dto.response;

import com.example.demo.domain.mentoring.entity.Mentor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MentorDto {

    private Long id;
    private String name;
    private String title;
    private String company;
    private String experience;
    private Double rating;
    private Integer reviewsCount;
    private List<String> skills;
    private String location;
    private String price;
    private String imageUrl;
    private Boolean isVerified;
    private String description;

    public static MentorDto from(Mentor mentor) {
        return MentorDto.builder()
                .id(mentor.getId())
                .name(mentor.getUser().getNickname() != null ? mentor.getUser().getNickname()
                        : mentor.getUser().getName())
                .title(mentor.getTitle())
                .company(mentor.getCompany())
                .experience(mentor.getExperience())
                .rating(mentor.getRating())
                .reviewsCount(mentor.getReviewsCount())
                .skills(mentor.getSkills())
                .location(mentor.getLocation())
                .price(mentor.getPrice())
                .imageUrl(mentor.getImageUrl() != null ? mentor.getImageUrl() : mentor.getUser().getProfileImage())
                .isVerified(mentor.getIsVerified())
                .description(mentor.getDescription())
                .build();
    }
}
