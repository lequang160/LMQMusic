package com.hamado.wifitransfer.callback;

import java.io.File;

/**
 * Created by Thang Tran on 12/25/18.
 * Copyright © 2018 Hamado. All rights reserved.
 */
public interface OnFileCallback {

    void onUploadFileSuccess(File file);

    void onFileDeleted(File file);
}
