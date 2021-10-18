package com.dailypit.dp.Model.HowItWorks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HowitWorksResponseData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("work_procedure")
    @Expose
    private String workProcedure;
    @SerializedName("work_procedure_video")
    @Expose
    private String workProcedureVideo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
