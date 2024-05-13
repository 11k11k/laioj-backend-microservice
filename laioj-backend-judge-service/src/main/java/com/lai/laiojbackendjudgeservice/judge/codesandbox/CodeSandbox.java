package com.lai.laiojbackendjudgeservice.judge.codesandbox;

import com.lai.laiojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.lai.laiojbackendmodel.model.codesandbox.ExecuteCodeResponse;

public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
