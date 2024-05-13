package com.lai.laiojbackendjudgeservice.judge.strategy;



import com.lai.laiojbackendmodel.model.codesandbox.JudgeInfo;
import com.lai.laiojbackendmodel.model.dto.question.JudgeCase;
import com.lai.laiojbackendmodel.model.entity.Question;
import com.lai.laiojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 * 如果不确定要传递什么参数就使用这个
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;


}
