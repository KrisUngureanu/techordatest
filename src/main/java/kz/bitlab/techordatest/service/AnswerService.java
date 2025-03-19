package kz.bitlab.techordatest.service;

import kz.bitlab.techordatest.dto.AnswerDto;
import kz.bitlab.techordatest.model.Answer;
import kz.bitlab.techordatest.model.Question;
import kz.bitlab.techordatest.repository.AnswerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerService {
    private final AnswerRepository repository;


    public AnswerService(AnswerRepository repository) {
        this.repository = repository;
    }

    public List<Answer> getAnswersByQuestion(Question question){
        Long idQuestion = question.getId();
        return repository.findAllByQuestionID(idQuestion);
    }

    public AnswerDto toDto(Answer answer){
        AnswerDto answerDto = new AnswerDto();
        answerDto.setText(answer.getText());
        answerDto.setCorrect(answer.isCorrect());
        return answerDto;
    }

    public List<AnswerDto> getAnswersDtoByQuestion(Question question){
        List<Answer> answers = getAnswersByQuestion(question);
        List<AnswerDto> answerDtos = new ArrayList<>();
        for (Answer answer: answers){
            AnswerDto answerDto = toDto(answer);
            answerDtos.add(answerDto);
        }
        return answerDtos;
    }

    public Answer createAnswer(AnswerDto answerDto){
        Answer answer = new Answer();
        answer.setText(answerDto.getText());
        answer.setCorrect(answerDto.isCorrect());
        repository.save(answer);
        return answer;
    }

    public List<Answer> createListAnswers(List<AnswerDto> list, Question question){
        List<Answer> answers = new ArrayList<>();
        for (AnswerDto answerDto: list){
            Answer answer = createAnswer(answerDto);
            Long id = question.getId();
            answer.setQuestionID(id);
            answers.add(answer);
        }
        return answers;
    }

    public void deleteAnswers(Question question){
        List<Answer> answers = getAnswersByQuestion(question);
        if (answers != null){
            repository.deleteAll(answers);
        }
    }
}
