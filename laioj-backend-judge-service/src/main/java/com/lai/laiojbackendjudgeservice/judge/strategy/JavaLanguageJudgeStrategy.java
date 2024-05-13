package com.lai.laiojbackendjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.lai.laiojbackendmodel.model.codesandbox.JudgeInfo;
import com.lai.laiojbackendmodel.model.dto.question.JudgeCase;
import com.lai.laiojbackendmodel.model.dto.question.JudgeConfig;
import com.lai.laiojbackendmodel.model.entity.Question;
import com.lai.laiojbackendmodel.model.enums.JudgeInfoMessageEnum;


import java.util.List;
import java.util.Optional;

/**
 * Java 程序的判题策略
 */
public class JavaLanguageJudgeStrategy implements JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     * judgeContext:{
     *      private JudgeInfo judgeInfo;
     *
     *     private List<String> inputList;
     *
     *     private List<String> outputList;
     *
     *     private List<JudgeCase> judgeCaseList;
     *
     *     private Question question;
     *
     *     private QuestionSubmit questionSubmit;
     * }
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        //通过上下文判题类获取到判题信息
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        //判断是否为空
        Long memory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
        Long time = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
        //获取输入输出信息
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        //获取题目信息
        Question question = judgeContext.getQuestion();
        //获取到判题用例
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        //定义成功信息
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        //new一个返回值，也就是响应值
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        //存入判题信息数据
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);
        // 先判断沙箱执行的结果输出数量是否和预期输出数量相等
        if (outputList.size() != inputList.size()) {
            //将消息更改为错误并存入到返回值中
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        // 依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        // 判断题目限制
        //通过题目信息获取到判题配置
        String judgeConfigStr = question.getJudgeConfig();
        //JSON字符串转换成JudgeConfig类
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        //获取到配置中的信息
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        Long needTimeLimit = judgeConfig.getTimeLimit();
        //将提交的判题时间和内存信息与题目的配置的判题时间和内存信息对比，然后进行判断返回
        if (memory > needMemoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        // Java 程序本身需要额外执行 10 秒钟
        long JAVA_PROGRAM_TIME_COST = 10000L;
        if ((time - JAVA_PROGRAM_TIME_COST) > needTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }
}
