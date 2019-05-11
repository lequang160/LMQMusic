package com.hamado.wifitransfer.callback;

import java.io.File;


public interface OnFileCallback {

    void onUploadFileSuccess(File file);

    void onFileDeleted(File file);
}
