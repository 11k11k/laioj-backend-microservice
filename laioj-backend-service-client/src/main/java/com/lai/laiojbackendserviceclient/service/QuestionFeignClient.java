package com.lai.laiojbackendserviceclient.service;

import com.lai.laiojbackendmodel.model.entity.Question;
import com.lai.laiojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @author Laiyijun
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-03-24 21:31:19
*/
@FeignClient(name="laioj-backend-question-service",path = "/api/question/inner")
public interface QuestionFeignClient {


    /**
     * 根据id返回题目信息
     * @param questionId
     * @return
     */
    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);

    /**
     * 根据提交题目iD获取提交题目信息
     * @param questionSubmitId
     * @return
     */
    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId);

    /**
     * 根据提交题目的修改信息类修改信息，返回布尔类型
     * @param questionSubmitUpdate
     * @return
     */
    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmitUpdate);

}
