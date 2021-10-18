package com.dailypit.dp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HowItWorkResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

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
