package com.lai.laiojbackendmodel.model.vo;


import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lai.laiojbackendmodel.model.entity.QuestionSubmit;
import com.lai.laiojbackendmodel.model.codesandbox.JudgeInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交
 *
 * @TableName question
 */
@TableName(value = "question")
@Data
public class QuestionSubmitVO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题信息（json 对象）
     */
    private JudgeInfo judgeInfo;

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 包装类转对象
     *
     * @param questionSubmitVO
     * @return
     */
    public static QuestionSubmit voToObj(QuestionSubmitVO questionSubmitVO) {
        if (questionSubmitVO == null) {
            return null;
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitVO, questionSubmit);
        JudgeInfo judgeInfoObj = questionSubmitVO.getJudgeInfo();
        if (judgeInfoObj != null) {
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfoObj));
        }
        return questionSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param questionSubmit
     * @return
     */
    public static QuestionSubmitVO objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVO);
        String judgeInfoStr = questionSubmit.getJudgeInfo();
//        questionSubmitVO.setJudgeInfo(JSONUtil.toBean(judgeInfoStr,JudgeInfo.class));
        if (StringUtils.isNotBlank(judgeInfoStr)) { // 使用 StringUtils.isNotBlank() 方法检查字符串是否非空非null
            try {
                JudgeInfo judgeInfo = JSONUtil.toBean(judgeInfoStr, JudgeInfo.class);
                questionSubmitVO.setJudgeInfo(judgeInfo);
            } catch (JSONException e) {
                // JSON 解析异常处理逻辑，可以记录日志或者返回默认值
                System.out.printf("Failed to parse judgeInfo JSON: " + judgeInfoStr, e);
                // 设置默认的 JudgeInfo 对象或者返回 null，视情况而定
                questionSubmitVO.setJudgeInfo(new JudgeInfo());
            }
        } else {
            // 如果 judgeInfoStr 为空，可以根据需求进行相应处理，比如设置默认值或者返回 null
            questionSubmitVO.setJudgeInfo(new JudgeInfo());
        }

        return questionSubmitVO;
    }
}