package com.lai.laiojbackendjudgeservice.judge.codesandbox;

import com.lai.laiojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.lai.laiojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;
//代理类，增强功能
@Slf4j

public class CodeSandboxProxy implements CodeSandbox{
    /**
     * 思路：
     * 调用一次输入日志，响应数据和请求数据
     * 调用本身一次。输出日志
     * 使用代理模式进行解耦
     * @param executeCodeRequest
     * @return
     */
    private final CodeSandbox codeSandbox;
    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息"+executeCodeRequest.toString());
        //调用
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱输出信息"+executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
