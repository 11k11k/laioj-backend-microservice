package com.lai.laiojbackendjudgeservice.judge.codesandbox.Impl;


import com.lai.laiojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.lai.laiojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.lai.laiojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.lai.laiojbackendmodel.model.codesandbox.JudgeInfo;
import com.lai.laiojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.lai.laiojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

//示例沙箱
@Slf4j
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
