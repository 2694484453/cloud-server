package com.ruoyi.common.base;

/**
 * @author gaopuguang_zz
 * @version 1.0
 * @description: TODO
 * @date 2024/8/19 11:30
 */
public class ExecResponse {

    private String successMsg;

    private String failMsg;

    private Integer existCode;

    public ExecResponse() {
    }

    public ExecResponse(String successMsg, String failMsg, Integer existCode) {
        this.successMsg = successMsg;
        this.failMsg = failMsg;
        this.existCode = existCode;
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg;
    }

    public Integer getExistCode() {
        return existCode;
    }

    public void setExistCode(Integer existCode) {
        this.existCode = existCode;
    }

    @Override
    public String toString() {
        return "ExecResponse{" +
                "successMsg='" + successMsg + '\'' +
                ", failMsg='" + failMsg + '\'' +
                ", existCode=" + existCode +
                '}';
    }
}
