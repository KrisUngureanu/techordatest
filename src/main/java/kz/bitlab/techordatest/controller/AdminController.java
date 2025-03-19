package kz.bitlab.techordatest.controller;

import kz.bitlab.techordatest.model.Answer;
import kz.bitlab.techordatest.model.Question;
import kz.bitlab.techordatest.repository.AnswerRepository;
import kz.bitlab.techordatest.repository.QuestionRepository;
import kz.bitlab.techordatest.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final QuestionService service;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    public AdminController(QuestionService service, AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.service = service;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    @GetMapping
    public String admin(){
        return "admin";
    }

    @GetMapping("/createQuestion/{type}")
    public String createQuestion(@PathVariable("type") Long type, Model model){
        model.addAttribute("type", type);
        List<Question> questions = service.getQuestions(type);
        model.addAttribute("questions", questions);
        return "createQuestion";
    }

    @GetMapping("/question/details/{id}")
    public String getQuestionDetails(@PathVariable("id") Long id, Model model) {
        Question question = service.getQuestionById(id); // Предполагаем, что сервис уже имеет этот метод
        if (question != null) {
            model.addAttribute("question", question);
        } else {
            model.addAttribute("error", "Вопрос с ID " + id + " не найден.");
        }
        return "questionDetails"; // Шаблон страницы для детального описания вопроса
    }

    @GetMapping("/question/new/{type}")
    public String newQuestion(@PathVariable("type") Long type, Model model) {
        model.addAttribute("type", type); // Передаем тип вопроса в модель
        model.addAttribute("question", new Question()); // Создаем пустой объект вопроса для формы
        return "newQuestion"; // Отображаем страницу с формой
    }

    @PostMapping("/question/new/{type}")
    public String saveQuestion(
            @ModelAttribute Question question,
            @RequestParam("correct") int correctIndex,
            @PathVariable("type") Long type,
            RedirectAttributes redirectAttributes) {
        // Установка типа вопроса
        question.setType(type);

        List<Answer> answers = question.getAnswers();
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            answer.setCorrect(i == correctIndex); // Пометка правильного ответа
            answer.setQuestionID(question.getId()); // Назначение ID вопроса (сначала будет null)
            answers.set(i, answerRepository.save(answer)); // Сохраняем каждый ответ вручную
        }

        question.setAnswers(answers); // Устанавливаем сохраненные ответы
        questionRepository.save(question); // Сохраняем вопрос

        redirectAttributes.addFlashAttribute("success", "Вопрос успешно создан!");
        return "redirect:/admin/createQuestion/" + type;

    }

}
