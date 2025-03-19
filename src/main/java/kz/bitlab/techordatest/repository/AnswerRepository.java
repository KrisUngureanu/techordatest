package kz.bitlab.techordatest.repository;

import kz.bitlab.techordatest.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionID(Long questionID);


    Answer findByQuestionIDAndCorrectIsTrue(Long questionID);
}
