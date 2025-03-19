package kz.bitlab.techordatest.controller;

import kz.bitlab.techordatest.model.Answer;
import kz.bitlab.techordatest.model.Question;
import kz.bitlab.techordatest.repository.AnswerRepository;
import kz.bitlab.techordatest.repository.QuestionRepository;
import kz.bitlab.techordatest.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestingController {
    private final QuestionService service;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    public TestingController(QuestionService service, AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.service = service;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    @GetMapping("/devops")
    public String getTestDevops(Model model){
        List<Question> questions = service.getQuestions(1L);
        model.addAttribute("questions", questions);
        return "testing";
    }

    @GetMapping("/qa")
    public String getTestQa(Model model){
        List<Question> questions = service.getQuestions(2L);
        model.addAttribute("questions", questions);
        return "testing";
    }

    @GetMapping("/java")
    public String getTestJava(Model model){
        List<Question> questions = service.getQuestions(3L);
        model.addAttribute("questions", questions);
        return "testing";
    }
    @PostMapping("/submit")
    public String submitTest(@RequestParam Map<String, String> answers, Model model) {
        int correctCount = 0;
        List<Map<String, Object>> results = new ArrayList<>();

        for (Map.Entry<String, String> entry : answers.entrySet()) {
            Long answerId = Long.parseLong(entry.getValue());
            Answer selectedAnswer = answerRepository.findById(answerId).orElse(null);

            if (selectedAnswer != null) {
                Long questionId = selectedAnswer.getQuestionID();
                Answer correctAnswer = null;

                // Находим правильный ответ
                List<Answer> answerList = answerRepository.findAllByQuestionID(questionId);
                if (!answerList.isEmpty()) {
                    for (Answer answer : answerList) {
                        if (answer.isCorrect()) {
                            correctAnswer = answer;
                            break;
                        }
                    }
                }

                boolean isCorrect = selectedAnswer.isCorrect();
                if (isCorrect) {
                    correctCount++;
                }

                // Безопасное формирование результата для вопроса
                Map<String, Object> questionResult = new HashMap<>();
                questionResult.put("question", questionRepository.findById(questionId).map(Question::getText).orElse("Не найдено"));
                questionResult.put("selectedAnswer", selectedAnswer.getText());
                questionResult.put("isCorrect", isCorrect);

                // Проверяем, есть ли correctAnswer
                questionResult.put("correctAnswer", correctAnswer != null ? correctAnswer.getText() : "Правильный ответ отсутствует");

                results.add(questionResult);
            }
        }

        model.addAttribute("score", correctCount);
        model.addAttribute("results", results);
        return "result";
    }
}
