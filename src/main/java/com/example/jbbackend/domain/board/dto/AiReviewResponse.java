package com.example.jbbackend.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AiReviewResponse(
    @JsonProperty("eval_score")
    Double evalScore,

    @JsonProperty("review_result")
    String reviewResult,

    List<String> checklist,

    @JsonProperty("conditional_checklist")
    List<String> conditionalChecklist,

    @JsonProperty("law_list")
    List<String> lawList,

    @JsonProperty("eval_feedback")
    String evalFeedback,

    @JsonProperty("review_status")
    String reviewStatus
) {
    public String toReviewComments() {
        return """
            review_result:
            %s

            checklist:
            %s

            conditional_checklist:
            %s

            law_list:
            %s

            eval_feedback:
            %s
            """.formatted(
            nullToBlank(reviewResult),
            formatList(checklist),
            formatList(conditionalChecklist),
            formatList(lawList),
            nullToBlank(evalFeedback)
        ).trim();
    }

    private String formatList(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        return String.join("\n", values.stream().map(value -> "- " + value).toList());
    }

    private String nullToBlank(String value) {
        return value == null ? "" : value;
    }
}
