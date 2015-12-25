package com.gagauz.tracker.web.pages;

import com.gagauz.tracker.utils.RequestSender;
import com.gagauz.tracker.web.components.Layout;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.upload.services.UploadedFile;

import java.io.File;

public class UploadForm {

    @Component
    private Form uploadForm;

    @ActivationRequestParameter
    private String callback;

    @ActivationRequestParameter
    @Property
    private String label;

    @Property
    @Persist("flash")
    private String script;

    @Property
    private UploadedFile uploadFile;

    @Inject
    private Request request;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    void onActivate() {
        if (null == label) {
            label = "";
        }
    }

    void setupRender() {
        if (null != script) {
            javaScriptSupport.require("upload").invoke("closeModal").with(Layout.MODAL_ID);
        }
    }

    void onSubmitFromUploadForm() {

        String filePath = null;
        String result = "ERROR";

        if (uploadForm.isValid()) {
            File file = new File(System.getProperty("java.io.tmpdir"), uploadFile.getFileName());
            uploadFile.write(file);
            filePath = file.getAbsolutePath();
            result = "OK";
        }

        if (null != callback) {
            RequestSender get = RequestSender.get(callback);
            for (String h : request.getHeaderNames()) {
                System.out.println(h);
                get.setHeader(h, request.getHeader(h));
            }
            StringBuilder sb = new StringBuilder();
            get.setParam("result", result)
                    .setParam("filePath", filePath)
                    .setParam("contentType", uploadFile.getContentType())
                    .execute(sb);

            script = sb.toString();

        }
    }
}
