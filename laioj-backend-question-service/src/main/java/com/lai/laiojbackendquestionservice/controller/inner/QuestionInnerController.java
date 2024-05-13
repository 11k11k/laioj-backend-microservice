package com.lai.laiojbackendquestionservice.controller.inner;

import com.lai.laiojbackendmodel.model.entity.Question;
import com.lai.laiojbackendmodel.model.entity.QuestionSubmit;
import com.lai.laiojbackendquestionservice.service.QuestionService;
import com.lai.laiojbackendquestionservice.service.QuestionSubmitService;
import com.lai.laiojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionSubmitService questionSubmitService;

    /**
     * 根据id返回题目信息
     *
     * @param questionId
     * @return
     */

    @GetMapping("/get/id")
    @Override
    public Question getQuestionById(@RequestParam("questionId") long questionId) {
        return questionService.getById(questionId);
    }

    /**
     * 根据提交题目iD获取提交题目信息
     *
     * @param questionSubmitId
     * @return
     */

    @GetMapping("/question_submit/get/id")
    @Override
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);

    }


    @PostMapping("/question_submit/update")
    @Override
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmitUpdate) {
        return questionSubmitService.updateById(questionSubmitUpdate);
    }

}
