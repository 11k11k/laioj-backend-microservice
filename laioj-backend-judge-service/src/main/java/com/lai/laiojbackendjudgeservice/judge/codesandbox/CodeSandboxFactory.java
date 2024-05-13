package com.lai.laiojbackendjudgeservice.judge.codesandbox;


import com.lai.laiojbackendjudgeservice.judge.codesandbox.Impl.ExampleCodeSandbox;
import com.lai.laiojbackendjudgeservice.judge.codesandbox.Impl.RemoteCodeSandbox;
import com.lai.laiojbackendjudgeservice.judge.codesandbox.Impl.ThirdPartyCodeSandbox;

//代码沙箱工厂
public class CodeSandboxFactory {
    /**
     * 创建代码沙箱示例
     *
     * @param type
     * @return
     */
    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}
