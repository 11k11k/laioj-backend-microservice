package com.lai.laiojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;

import com.lai.laiojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.lai.laiojbackendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.lai.laiojbackendjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.lai.laiojbackendjudgeservice.judge.strategy.JudgeContext;
import com.lai.laiojbackendserviceclient.service.QuestionFeignClient;
import com.lai.laiojbackendcommon.common.ErrorCode;
import com.lai.laiojbackendcommon.exception.BusinessException;
import com.lai.laiojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.lai.laiojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.lai.laiojbackendmodel.model.codesandbox.JudgeInfo;
import com.lai.laiojbackendmodel.model.dto.question.JudgeCase;
import com.lai.laiojbackendmodel.model.entity.Question;
import com.lai.laiojbackendmodel.model.entity.QuestionSubmit;
import com.lai.laiojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionFeignClient questionFeignClient;
    //注入方法同名使用@Resource进行注入

    @Value("${codesandbox.type:example}")
    private String type;

    @Resource
    private JudgeManager judgeManager;
    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        /**
         * 1.传入题目的提交id,获取到对应的题目，提交信息
         * 2.调用沙箱，获取到执行结果
         * 3.根据沙箱的执行结果，设置题目的判题状态和信息
         */

        //根据提交问题的id查询到对应的提交记录
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        //判断是否为空
        if (questionSubmit == null) {
            //抛出异常
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        //获取提交的题目Id
        Long questionId = questionSubmit.getQuestionId();
        //通过获取到的题目id找到对应的题目类
        Question question = questionFeignClient.getQuestionById(questionId);
        //如果题目为空，抛出异常
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        }
        //判断状态是否为等待中
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            //如果不是等待中就抛出异常
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        // 3）更改判题（题目提交）的状态为 “判题中”，防止重复执行
        //创建新的提交题目类用于修改提交题目
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        //存入题目提交的id和状态
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        //判断是否更新成功
        //通过id修改，所以对象要存在id
        boolean b = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新异常");
        }
        // 4）调用沙箱，获取到执行结果
        //选择调用哪一种沙箱，type是配置中定义的，这里使用了工厂类
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
//        CodeSandbox codeSandbox = new RemoteCodeSandbox();
        //使用代理模式，增强了接口功能，打印输出输入日志
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        //从提交的题目中获取到语言和代码
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        // 获取题目的输入用例
        String judgeCaseStr = question.getJudgeCase();
        //将json字符串转换成list数组，
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        //将list数组使用stream方法进行处理，集合出input属性的数组
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        //将数据存入到封装好的前端接收类
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        //将数据传入到代码沙箱进行处理，返回响应值
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        //获取响应里的输出结果
        List<String> outputList = executeCodeResponse.getOutputList();
        // 5）根据沙箱的执行结果，设置题目的判题状态和信息
        //设置上下文类,就是把所有要用到的东西塞进去，然后在策略里面进行调用时候用到，judgeContext
        JudgeContext judgeContext = new JudgeContext();
        //获取响应结果中的judgeInfo
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        //将存入数据的context类发送给JudgeManager方法，这里使用到了策略模式
        //使用manager.doJudge进行选择不同的处理方法，也就是选择不同策略
        //每个策略会进行不同判断，然后返回结果都是judgeInfo也就是判题结束后返回的详细和信息
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        // 6）修改数据库中的判题结果
        //新new一个题目提交类进行题目提交后的修改
        questionSubmitUpdate = new QuestionSubmit();
        //存入修改信息：修改题目的id用于查询，修改题目状态为成功 SUCCEED("成功", 2),
        //存入返回的判题信息里面包含判题的消息以及时间和内存
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        //返回boolean类型的数据进行判断
        b = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!b) {
            //抛出自定义异常,继承运行中异常类，注意只有继承运行中异常类才能正常抛出
            // SYSTEM_ERROR(50000, "系统内部异常"),

            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        //根据提交题目的id获取到对应的题目提交信息进行返回
        QuestionSubmit questionSubmitResult = questionFeignClient.getQuestionSubmitById(questionId);
        //获取到最新的状态返回给前端
        return questionSubmitResult;
    }
}
