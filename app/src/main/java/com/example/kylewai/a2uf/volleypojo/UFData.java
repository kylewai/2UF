package com.example.kylewai.a2uf.volleypojo;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UFData {

    @SerializedName("COURSES")
    @Expose
    private List<UFCourse> cOURSES = null;
    @SerializedName("LASTCONTROLNUMBER")
    @Expose
    private Integer lASTCONTROLNUMBER;
    @SerializedName("RETRIEVEDROWS")
    @Expose
    private Integer rETRIEVEDROWS;
    @SerializedName("TOTALROWS")
    @Expose
    private Integer tOTALROWS;

    public List<UFCourse> getCOURSES() {
        return cOURSES;
    }

    public void setCOURSES(List<UFCourse> cOURSES) {
        this.cOURSES = cOURSES;
    }

    public Integer getLASTCONTROLNUMBER() {
        return lASTCONTROLNUMBER;
    }

    public void setLASTCONTROLNUMBER(Integer lASTCONTROLNUMBER) {
        this.lASTCONTROLNUMBER = lASTCONTROLNUMBER;
    }

    public Integer getRETRIEVEDROWS() {
        return rETRIEVEDROWS;
    }

    public void setRETRIEVEDROWS(Integer rETRIEVEDROWS) {
        this.rETRIEVEDROWS = rETRIEVEDROWS;
    }

    public Integer getTOTALROWS() {
        return tOTALROWS;
    }

    public void setTOTALROWS(Integer tOTALROWS) {
        this.tOTALROWS = tOTALROWS;
    }

}
