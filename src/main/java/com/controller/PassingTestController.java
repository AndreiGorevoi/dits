/*
@author Andrei Gorevoi
*/
package com.controller;

import com.dto.QuestionDto;
import com.model.Question;
import com.model.Statistic;
import com.model.Test;
import com.service.answer.AnswerService;
import com.service.statistic.StatisticService;
import com.service.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/passing")
public class PassingTestController {

    private final TestService testService;
    private final AnswerService answerService;
    private final StatisticService statisticService;

    @Autowired
    public PassingTestController(TestService testService, AnswerService answerService, StatisticService statisticService) {
        this.testService = testService;
        this.answerService = answerService;
        this.statisticService = statisticService;
    }

    /**
     *
     * @param id
     * @param model
     * @param session
     * @return
     *
     * Данный метод принимает id теста, который выбрал пользователь для прохождения, при первом обращении достает
     * список вопросов и ответов к ним из базы данных и сохраняет все в сессию. Находит в текущем списке вопросов
     * вопрос, который еще не ответил пользователь и передает его на представление.
     */
    @GetMapping("{id}")
    public String passingTest(@PathVariable int id, Model model, HttpSession session){
        if(session.getAttribute("questions")==null){
            questionsToSession(id, session);
        }
        List<QuestionDto> questionDtoList = (List<QuestionDto>) session.getAttribute("questions");
//        Finding no answered question;
            for (QuestionDto questionDto : questionDtoList) {
                if (questionDto.isAnswered()){
                    continue;
                }
                model.addAttribute("question",questionDto);
                break;
            }
       if(model.getAttribute("question") == null){
//        Saving all statistics from session to DB;
          List<Statistic> statisticList = (List<Statistic>) session.getAttribute("statistics");
           for (Statistic statistic : statisticList) {
               statisticService.addStatistic(statistic);
           }
//           Cleaning attributes from session
           session.removeAttribute("questions");
           session.removeAttribute("passingTest");
           session.removeAttribute("answersMap");
//           session.removeAttribute("startTime");
//           session.removeAttribute("statistics");

           return "redirect:/statistic/test/final";
       }
        return "/user/passingTest/runningTest";
    }

    /**
     *
     * @param id
     * @param session
     * Данный метод вытягивает из базы данных тест, его вопросы и помещяет их в сессию.
     * P.S. Используется при первом попадании в метод passingTest.
     */
    public void questionsToSession(int id,HttpSession session){
        Test test = testService.getOne(id);
        Map<Integer,List<String>> answersMap = new HashMap<>();
        List<Question> questions = test.getQuestions();
        List<QuestionDto> questionDtoList = new ArrayList<>();
//        Convert questions to dtoQuestion and adding to dtoList;
        for (Question question : questions) {
            QuestionDto questionDto = new QuestionDto();
            questionDto = questionDto.convertToDto(question,answerService.getAllAnswersByQuestionId(question.getQuestionId()));
            questionDtoList.add(questionDto);
        }
        session.setAttribute("questions",questionDtoList);
        session.setAttribute("passingTest",test);
        session.setAttribute("answersMap",answersMap);
        session.setAttribute("userId", 21);
        session.setAttribute("startTime",new Date(System.currentTimeMillis()));
        session.setAttribute("statistics", new ArrayList<Statistic>());
    }
}