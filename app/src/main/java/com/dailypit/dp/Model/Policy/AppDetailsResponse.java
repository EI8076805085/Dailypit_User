package com.dailypit.dp.Model.Policy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppDetailsResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("term_condition")
    @Expose
    private String termCondition;
    @SerializedName("privacy_policy")
    @Expose
    private String privacyPolicy;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("helpline_number")
    @Expose
    private String helpline_number;
    @SerializedName("work_procedure")
    @Expose
    private String workProcedure;
    @SerializedName("work_procedure_video")
    @Expose
    private String workProcedureVideo;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTermCondition() {
        return termCondition;
    }

    public void setTermCondition(String termCondition) {
        this.termCondition = termCondition;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getHelpline_number() {
        return helpline_number;
    }

    public void setHelpline_number(String helpline_number) {
        this.helpline_number = helpline_number;
    }

    public String getWorkProcedure() {
        return workProcedure;
    }

    public void setWorkProcedure(String workProcedure) {
        this.workProcedure = workProcedure;
    }

    public String getWorkProcedureVideo() {
        return workProcedureVideo;
    }

    public void setWorkProcedureVideo(String workProcedureVideo) {
        this.workProcedureVideo = workProcedureVideo;
    }
}
