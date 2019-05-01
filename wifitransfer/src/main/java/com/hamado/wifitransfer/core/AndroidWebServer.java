package com.hamado.wifitransfer.core;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.hamado.wifitransfer.R;
import com.hamado.wifitransfer.builder.HtmlDescription;
import com.hamado.wifitransfer.callback.OnFileCallback;
import com.hamado.wifitransfer.entities.ResponseDirectory;
import com.hamado.wifitransfer.entities.ResponseFile;
import com.hamado.wifitransfer.utils.FileHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class AndroidWebServer extends NanoHTTPD {

    private Context mContext;
    private static final String MIME_HTML = "text/html",
            MIME_JS = "text/javascript",
            MIME_CSS = "text/css",
            MIME_PNG = "image/png",
            MIME_XML = "text/xml",
            MIME_JSON = "application/json",
            MIME_DEFAULT_BINARY = "application/octet-stream";

    HtmlDescription htmlDescription;
    String storeFilePath;
    OnFileCallback onFileCallback;

    public AndroidWebServer(Context context, int port,
                            HtmlDescription htmlDescription,
                            String storeFilePath) {
        super(port);
        mContext = context;
        this.htmlDescription = htmlDescription;
        this.storeFilePath = storeFilePath;
    }

    public void setOnFileCallback(OnFileCallback onFileCallback) {
        this.onFileCallback = onFileCallback;
    }

    @Override
    public Response serve(IHTTPSession session) {
        if (session.getUri().startsWith("/api")) {
            return serveResponseApi(session);
        } else if (session.getUri().startsWith("/download")) {
            return downloadFile(session);
        } else {
            return serveFile(session.getUri());
        }
    }

    @NonNull
    private Response getHtmlResponse(InputStream in) throws IOException {
        int size = in.available();
        byte[] buffer = new byte[size];
        in.read(buffer);
        in.close();

        String strHtml = new String(buffer);
        strHtml = strHtml.replace("%device%", htmlDescription.getDeviceName())
                .replace("%title%", htmlDescription.getTitlePage())
                .replace("%header%", htmlDescription.getTitleHeader())
                .replace("%prologue%", mContext.getString(R.string.prologue_wifi_transfer))
                .replace("%epilogue%", htmlDescription.getEpilogue())
                .replace("%footer%", htmlDescription.getFooter());
        return newFixedLengthResponse(Response.Status.OK, MIME_HTML, strHtml);
    }

    private Response serveFile(String uri) {
        final String PATH = "transfer";
        String filename;

        filename = uri;
        if (filename.equals("/"))
            filename = "/index.html";

        InputStream in = null;

        try {
            in = mContext.getAssets().open(PATH + filename);

            if (filename.equals("/index.html")) {
                return getHtmlResponse(in);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String mime = MIME_CSS;

        if (uri.endsWith(".css"))
            mime = MIME_CSS;
        else if (uri.endsWith(".js"))
            mime = MIME_JS;
        else if (uri.endsWith(".png"))
            mime = MIME_PNG;
        else if (uri.endsWith(".xml"))
            mime = MIME_XML;
        else if (uri.endsWith(".json"))
            mime = MIME_JSON;
//        else
//            mime = MIME_DEFAULT_BINARY;

        return newFixedLengthResponse(Response.Status.OK, mime, in, 0);
    }

    private Response serveResponseApi(IHTTPSession session) {
        Map<String, String> files = new HashMap<>();

        if (session.getUri().equals("/api/upload")) {
            return uploadFile(session);
        }

        if (session.getMethod() == Method.POST) {
            try {
                session.parseBody(files);
            } catch (IOException | ResponseException e) {
                return newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "No parameters!");
            }
        }


        switch (session.getUri()) {
            case "/api/list":
                return listDirectory(session);
            case "/api/create":
                return createDirectory(files);
            case "/api/delete":
                return deleteItem(files);
            case "/api/move":
                return moveItem(files);
            default:
                String appName = mContext.getString(R.string.app_name);
                return newFixedLengthResponse(Response.Status.OK, MIME_JSON, appName);
        }
    }

    private Response downloadFile(IHTTPSession session) {
        try {
            String pathFile = storeFilePath + session.getParms().get("path");
            File file = new File(pathFile);
            FileInputStream inputStream = new FileInputStream(file);
            final Response resp = newFixedLengthResponse(
                    Response.Status.OK,
                    MIME_DEFAULT_BINARY,
                    inputStream,
                    file.length()
            );
            resp.addHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            return resp;
        } catch (final Exception ex) {
            ex.printStackTrace();
            return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, ex.getMessage());
        }
    }

    private Response uploadFile(IHTTPSession session) {
        Method method = session.getMethod();
        Map<String, String> files = new HashMap<>();
        Map<String, String> parms = session.getParms();

        if (Method.POST.equals(method)) {
            try {
                session.parseBody(files);
            } catch (IOException | ResponseException e) {
                return newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "No parameters!");
            }
        }
        String filename = parms.get("filename");
        String path = parms.get("path");
        String tmpFilePath = files.get("files[]");
        if (null == filename || null == tmpFilePath) {
            // Response for invalid parameters
            return newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "No parameters!");
        }
        filename = filename.replace("'", "").replace("\'", "");
        File dst = new File(storeFilePath + path, filename);
        File src = new File(tmpFilePath);
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);
            byte[] buf = new byte[65536];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            // Response for failed
            e.printStackTrace();
        }
        if (onFileCallback != null) {
            onFileCallback.onUploadFileSuccess(dst);
        }

        return newFixedLengthResponse(Response.Status.OK, MIME_JSON, "{}");
    }

    private Response moveItem(Map<String, String> files) {
        String oldPath;
        String newPath;
        try {
            oldPath = getPath(files, "oldPath");
            newPath = getPath(files, "newPath");
        } catch (JSONException e) {
            return newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, e.getMessage());
        }

        File srcFile = new File(oldPath);
        if (!srcFile.exists()) {
            return getMessageNotFound(oldPath);
        }

        //Move to a directory
        File destFile = new File(newPath);

        String directoryName = newPath.substring(newPath.lastIndexOf("/") + 1);
        if (directoryName.startsWith(".")) {
            return newFixedLengthResponse(Response.Status.FORBIDDEN, MIME_PLAINTEXT, directoryName + " is private!");
        }

        boolean moved;
        if (srcFile.isDirectory()) {
            moved = FileHelper.moveDir(srcFile, destFile);
        } else {
            moved = FileHelper.moveFile(srcFile, destFile);
        }
        if (moved) {
            return newFixedLengthResponse(Response.Status.OK, MIME_JSON, "{}");
        } else {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Failed moving " + srcFile.getName() + " to " + destFile.getName());
        }
    }

    private Response deleteItem(Map<String, String> files) {
        String path;
        try {
            path = getPath(files, "path");
        } catch (JSONException e) {
            return newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, e.getMessage());
        }
        File file = new File(path);
        if (!file.exists()) {
            return getMessageNotFound(path);
        }

        String fileName = path.substring(path.lastIndexOf("/") + 1);
        if (fileName.startsWith(".")) {
            return newFixedLengthResponse(Response.Status.FORBIDDEN, MIME_PLAINTEXT, fileName + " is private!");
        }

        boolean deleted;
        if (file.isDirectory()) {
            deleted = FileHelper.deleteDir(file);
        } else {
            deleted = FileHelper.deleteFile(file);
        }
        if (deleted) {
            if (onFileCallback != null) {
                onFileCallback.onFileDeleted(file);
            }
            return newFixedLengthResponse(Response.Status.OK, MIME_JSON, "{}");
        } else {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Failed deleting " + fileName);
        }
    }

    @NonNull
    private Response createDirectory(Map<String, String> files) {
        //check sandboxed absolute path
        String path;
        try {
            path = getPath(files, "path");
        } catch (JSONException e) {
            return newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, e.getMessage());
        }

        String absolutePath = path.substring(0, path.lastIndexOf("/"));
        if (!FileHelper.isDir(absolutePath)) {
            return newFixedLengthResponse(Response.Status.FORBIDDEN, MIME_PLAINTEXT, absolutePath + " does not exist or is not a directory!");
        }

        String directoryName = path.substring(path.lastIndexOf("/") + 1);
        if (directoryName.startsWith(".")) {
            return newFixedLengthResponse(Response.Status.FORBIDDEN, MIME_PLAINTEXT, directoryName + " is private!");
        }

        boolean created = FileHelper.createOrExistsDir(path);
        if (created) {
            return newFixedLengthResponse(Response.Status.OK, MIME_JSON, "{}");
        } else {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Failed creating directory " + directoryName);
        }

    }

    private String getPath(Map<String, String> files, String key) throws JSONException {
        JSONObject postData = new JSONObject(files.get("postData"));
        String path = storeFilePath + postData.get(key);
        if (path.endsWith("/")) path = path.substring(0, path.length() - 1);
        return path;
    }


    @NonNull
    private Response listDirectory(IHTTPSession session) {
        String path = session.getParms().get("path");

        path = storeFilePath + path;

        File dir = new File(path);
        if (!dir.exists()) {
            return getMessageNotFound(path);
        }

        if (!FileHelper.isDir(dir)) {
            return getMessageNotDirectory(path);
        }

        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        String directoryName = path.substring(path.lastIndexOf("/") + 1);
        if (directoryName.startsWith(".")) {
            return getMessageIsPrivate(directoryName);
        }

        ArrayList<ResponseDirectory> responses = new ArrayList<>();
        for (File file : dir.listFiles()) {
            ResponseDirectory fileResponse;
            String pathFile = file.getPath().replace(storeFilePath, "") + "/";
            if (file.isDirectory()) {
                fileResponse = new ResponseDirectory(file.getName(),
                        pathFile);
            } else {
                fileResponse = new ResponseFile(file.getName(),
                        pathFile, file.length());
            }
            responses.add(fileResponse);
        }

        return newFixedLengthResponse(Response.Status.OK, MIME_JSON, new Gson().toJson(responses));
    }


    @NonNull
    private Response getMessageIsPrivate(String directoryName) {
        return newFixedLengthResponse(Response.Status.FORBIDDEN, MIME_PLAINTEXT, directoryName + " is private!");
    }

    @NonNull
    private Response getMessageNotDirectory(String path) {
        return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, path + " is not a directory!");
    }

    @NonNull
    private Response getMessageNotFound(String path) {
        return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, path + " is not found!");
    }
}
