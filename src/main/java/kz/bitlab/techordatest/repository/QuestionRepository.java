package kz.bitlab.techordatest.repository;

import kz.bitlab.techordatest.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
@Transactional
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByType(Long type);

    List<Question> findByType(Long type);
}
