package com.example.demo.domain.mentoring.dto.response;

import com.example.demo.domain.mentoring.entity.Mentor;
import com.example.demo.domain.mentoring.entity.MentoringSession;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MentoringSessionDto {

    private String id;
    private SessionMentorDto mentor;
    private String status;
    private String topic;
    private String startDate;
    private Integer totalSessions;
    private Integer completedSessions;
    private NextSessionDto nextSession;
    private Integer progress;
    private List<String> goals;

    @Getter
    @Builder
    public static class SessionMentorDto {
        private String name;
        private String title;
        private String company;
        private List<String> expertise;
        private String profileImageUrl;
        private Double rating;

        public static SessionMentorDto from(Mentor mentor) {
            return SessionMentorDto.builder()
                    .name(mentor.getUser().getNickname() != null ? mentor.getUser().getNickname()
                            : mentor.getUser().getName())
                    .title(mentor.getTitle())
                    .company(mentor.getCompany())
                    .expertise(mentor.getSkills())
                    .profileImageUrl(
                            mentor.getImageUrl() != null ? mentor.getImageUrl() : mentor.getUser().getProfileImage())
                    .rating(mentor.getRating())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class NextSessionDto {
        private String date;
        private String time;
        private String type;
    }

    public static MentoringSessionDto from(MentoringSession session) {
        NextSessionDto nextSessionDto = null;
        if (session.getNextSessionDate() != null) {
            nextSessionDto = NextSessionDto.builder()
                    .date(session.getNextSessionDate().toString())
                    .time(session.getNextSessionTime())
                    .type(session.getNextSessionType() != null ? session.getNextSessionType().name().toLowerCase()
                            : null)
                    .build();
        }

        return MentoringSessionDto.builder()
                .id(session.getId())
                .mentor(SessionMentorDto.from(session.getMentor()))
                .status(session.getStatus().name().toLowerCase())
                .topic(session.getTopic())
                .startDate(session.getStartDate() != null ? session.getStartDate().toString() : null)
                .totalSessions(session.getTotalSessions())
                .completedSessions(session.getCompletedSessions())
                .nextSession(nextSessionDto)
                .progress(session.getProgress())
                .goals(session.getGoals())
                .build();
    }
}
