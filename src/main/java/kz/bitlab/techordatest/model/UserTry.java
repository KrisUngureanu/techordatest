package kz.bitlab.techordatest.model;

import lombok.Data;

@Data
public class UserTry {
    Question question;
    Answer answer;
    Answer correctAnswer;
    Boolean resultOk;
}
