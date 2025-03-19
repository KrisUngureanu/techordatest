package kz.bitlab.techordatest.dto;

import kz.bitlab.techordatest.model.Answer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class QuestionDto {
    private String text;
    private Long type;
    List<AnswerDto> answers;
}
