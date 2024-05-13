package com.lai.laiojbackendjudgeservice.judge.codesandbox.Impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.util.StringUtils;

import com.lai.laiojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.lai.laiojbackendcommon.common.ErrorCode;
import com.lai.laiojbackendcommon.exception.BusinessException;
import com.lai.laiojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.lai.laiojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;
@Slf4j
//远程代码沙箱
public class RemoteCodeSandbox implements CodeSandbox {
    // 定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("这是远程代码沙箱调用前的前端接收参数"+executeCodeRequest);
        String url = "http://localhost:8099/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr =   HttpUtil.createPost(url).header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);

    }
}
