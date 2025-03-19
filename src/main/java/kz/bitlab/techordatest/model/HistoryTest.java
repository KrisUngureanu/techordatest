package kz.bitlab.techordatest.model;

import kz.bitlab.techordatest.repository.QuestionRepository;
import lombok.Data;

import java.util.Collections;
import java.util.List;
@Data
public class HistoryTest {
   List<UserTry> userTries;
   Long count;
   Long answOk;
   Long answNo;
   List<Question> questions;

}
