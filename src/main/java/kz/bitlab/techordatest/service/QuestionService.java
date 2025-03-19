package kz.bitlab.techordatest.service;

import kz.bitlab.techordatest.dto.AnswerDto;
import kz.bitlab.techordatest.dto.QuestionDto;
import kz.bitlab.techordatest.model.Answer;
import kz.bitlab.techordatest.model.Question;
import kz.bitlab.techordatest.repository.AnswerRepository;
import kz.bitlab.techordatest.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository repository;
    private final AnswerService answerService;
    private final AnswerRepository answerRepository;
    public QuestionService(QuestionRepository repository, AnswerService answerService, AnswerRepository answerRepository) {
        this.repository = repository;
        this.answerService = answerService;

        this.answerRepository = answerRepository;
    }

    public List<Question> getAll(){
        return repository.findAll();
    }

    public List<Question> getAllByType(Long typeQuestion){
        return repository.findAllByType(typeQuestion);
    }

    public QuestionDto toDto(Question question){
        QuestionDto questionDto = new QuestionDto();
        questionDto.setText(question.getText());
        questionDto.setType(question.getType());
        List<AnswerDto> answerDtos = answerService.getAnswersDtoByQuestion(question);
        questionDto.setAnswers(answerDtos);
        return questionDto;
    }

    public List<QuestionDto> getAllQuesyions(){
        List<Question> questions = getAll();
        List<QuestionDto> questionDtos = new ArrayList<>();
        for (Question question: questions){
           questionDtos.add(toDto(question));

        }
        return questionDtos;
    }


    public Question createQuestion(QuestionDto questionDto){
        Question question = new Question();
        question.setText(questionDto.getText());
        question.setType(questionDto.getType());
        List<AnswerDto> answerDtos = questionDto.getAnswers();
        Question question1 = repository.save(question);
        List<Answer> answers = answerService.createListAnswers(answerDtos, question1);
        question.setAnswers(answers);

        repository.save(question);
        return question;
    }

    public void deleteQuestion(Long id){
        Question question = repository.findById(id).orElse(null);
        if (question != null){
            question.setAnswers(null);
            repository.save(question);
            answerService.deleteAnswers(question);
            repository.delete(question);
        }
    }

    public List<Question> getQuestions(Long typeId){

        List<Question> questions = repository.findByType(typeId); // Загружаем все вопросы
        Collections.shuffle(questions); // Перемешиваем
        return questions.stream().limit(30).toList(); // Берём 30 случайных
    }


    public Question getQuestionById(Long id) {
        return repository.findById(id).orElse(null);
    }


    public void saveQuestion(Question question) {
        repository.save(question);
    }

    public void saveQuestionWithAnswers(Question question) {
        // Сохранение вопроса
        Question savedQuestion = repository.save(question);

        // Установка ID вопроса для каждого ответа
        for (Answer answer : question.getAnswers()) {
            answer.setQuestionID(savedQuestion.getId());
            answerRepository.save(answer);
        }
    }

}
