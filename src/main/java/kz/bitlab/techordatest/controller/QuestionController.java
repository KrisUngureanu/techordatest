package kz.bitlab.techordatest.controller;

import kz.bitlab.techordatest.dto.QuestionDto;
import kz.bitlab.techordatest.model.Question;
import kz.bitlab.techordatest.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService service;
    public QuestionController(QuestionService service) {
        this.service = service;
    }

    @GetMapping
    public List<QuestionDto> getAllQuestions(){
        return service.getAllQuesyions();
    }

    @PostMapping("/createQuestion")
    public Question createQuestion(@RequestBody QuestionDto questionDto){
        return service.createQuestion(questionDto);
    }

    @PostMapping("/createFewQuestion")
    public List<Question> createFewQuestions(@RequestBody List<QuestionDto> questionDtos){
        List<Question> questions = new ArrayList<>();
        for (QuestionDto questionDto: questionDtos){
            questions.add(service.createQuestion(questionDto));
        }
        return questions;
    }

    @DeleteMapping("{id}")
    public void deleteQuestion(@PathVariable("id") Long id){
        service.deleteQuestion(id);
    }

}
