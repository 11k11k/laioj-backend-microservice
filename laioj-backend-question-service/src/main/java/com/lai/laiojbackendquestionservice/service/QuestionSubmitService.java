package com.lai.laiojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lai.laiojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.lai.laiojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.lai.laiojbackendmodel.model.entity.QuestionSubmit;
import com.lai.laiojbackendmodel.model.entity.User;
import com.lai.laiojbackendmodel.model.vo.QuestionSubmitVO;


/**
* @author Laiyijun
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-03-24 21:32:40
*/
public interface QuestionSubmitService  extends IService<QuestionSubmit> {
    /**
     * 点赞
     *
     * @param questionSubmitAddRequest 题目提交信息
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 题目点赞（内部服务）
     *
     * @param QuestionSubmitQueryRequest
     *
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest QuestionSubmitQueryRequest);


    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO>  getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);



}
