package com.lai.laiojbackendjudgeservice.judge;

import com.lai.laiojbackendmodel.model.entity.QuestionSubmit;

/**
 * 判题服务
 */
public interface JudgeService {
    /**
     * 判题
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
