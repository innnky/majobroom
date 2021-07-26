package net.fabricmc.majobroom.jsonbean;


import com.google.gson.annotations.SerializedName;

public class ModelBean {
    private String format_version;
    @SerializedName("geometry.model")
    private GeomtryBean model;

    public ModelBean() {
    }

    public String getFormat_version() {
        return this.format_version;
    }

    public void setFormat_version(String format_version) {
        this.format_version = format_version;
    }

    public GeomtryBean getModel() {
        return this.model;
    }

    public void setModel(GeomtryBean model) {
        this.model = model;
    }
}
