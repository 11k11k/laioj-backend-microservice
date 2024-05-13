package com.lai.laiojbackendjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.lai.laiojbackendmodel.model.codesandbox.JudgeInfo;
import com.lai.laiojbackendmodel.model.dto.question.JudgeCase;
import com.lai.laiojbackendmodel.model.dto.question.JudgeConfig;
import com.lai.laiojbackendmodel.model.entity.Question;
import com.lai.laiojbackendmodel.model.enums.JudgeInfoMessageEnum;


import java.util.List;

/**
 * 默认判题策略
 */
//继承判题用例的接口
public class DefaultJudgeStrategy implements JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    //实现doJudge方法
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        //获取到存入的judgeInfo
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        //通过judgeInfo获取到对应的内存限制，时间限制
        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();
        //获取到输入输出信息
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        //获取到题目信息
        Question question = judgeContext.getQuestion();
        //获取到判题用例
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        //获取枚举  ACCEPTED("成功", "Accepted"),
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        //new一个新的判题信息限制
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        //存入信息
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);
        // 先判断沙箱执行的结果输出数量是否和预期输出数量相等
        if (outputList.size() != inputList.size()) {
            //如果不相等则修改枚举    WRONG_ANSWER("答案错误", "Wrong Answer"),
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            //存入到判题信息中去
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            //返回判题信息
            return judgeInfoResponse;
        }
        // 依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            //获取当前的判题用例
            JudgeCase judgeCase = judgeCaseList.get(i);
            //判断是否相等
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                //如果不相等则返回答案错误自定义枚举信息
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                //存入枚举信息
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        // 判断题目限制
        //获取题目判题配置
        String judgeConfigStr = question.getJudgeConfig();
        //是JSON字符串类型，转换成JudgeConfig类型
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        //获取判题配置信息
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        Long needTimeLimit = judgeConfig.getTimeLimit();
        //判断内存
        if (memory > needMemoryLimit) {
            //MEMORY_LIMIT_EXCEEDED("", "内存溢出"),
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        if (time > needTimeLimit) {
            //TIME_LIMIT_EXCEEDED("Time Limit Exceeded", "超时"),
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        //枚举 -> 成功JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        //返回判题信息响应
        return judgeInfoResponse;
    }
}
