package com.lai.laiojbackendjudgeservice.judge;




import com.lai.laiojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.lai.laiojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.lai.laiojbackendjudgeservice.judge.strategy.JudgeContext;
import com.lai.laiojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.lai.laiojbackendmodel.model.codesandbox.JudgeInfo;
import com.lai.laiojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    //创建doJudge方法，传入上下文数据配置
    JudgeInfo doJudge(JudgeContext judgeContext) {
        //获取题目提交数据
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        //获取提交的语言
        String language = questionSubmit.getLanguage();
        //new一个默认的策略
        //DefaultJudgeStrategy.doJudge返回的是处理完后的判题信息响应judgeInfo
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        //判断语言来使用不同种判断逻辑
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        //doJudge返回的JudgeInfo类
        return judgeStrategy.doJudge(judgeContext);
    }

}
