package com.hamado.wifitransfer.builder;

import android.support.annotation.NonNull;


public class HtmlDescription {

    private String deviceName;
    private String titlePage;
    private String titleHeader;
    private String epilogue = "";
    private String footer = "";

    public HtmlDescription(@NonNull String deviceName,
                           @NonNull String titlePage,
                           @NonNull String titleHeader,
                           @NonNull String epilogue,
                           @NonNull String footer) {
        this.deviceName = deviceName;
        this.titlePage = titlePage;
        this.titleHeader = titleHeader;
        this.epilogue = epilogue;
        this.footer = footer;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getTitlePage() {
        return titlePage;
    }

    public void setTitlePage(String titlePage) {
        this.titlePage = titlePage;
    }

    public String getTitleHeader() {
        return titleHeader;
    }

    public void setTitleHeader(String titleHeader) {
        this.titleHeader = titleHeader;
    }


    public String getEpilogue() {
        return epilogue;
    }

    public void setEpilogue(String epilogue) {
        this.epilogue = epilogue;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }
}
