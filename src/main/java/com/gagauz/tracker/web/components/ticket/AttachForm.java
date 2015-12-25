package com.gagauz.tracker.web.components.ticket;

import com.gagauz.tracker.web.components.Layout;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.util.TextStreamResponse;

import java.net.URLEncoder;

public class AttachForm {

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private ComponentResources resources;

    @Cached
    public String getLink() {
        return URLEncoder.encode(resources.createEventLink("FileUploaded").toAbsoluteURI());
    }

    public Object onFileUploaded(@RequestParameter(allowBlank = true, value = "result") String result,
                                 @RequestParameter(allowBlank = true, value = "filePath") String filePath,
                                 @RequestParameter(allowBlank = true, value = "contentType") String contentType) {

        String text = "alert('Ошибка при заливке файла!');";

        if (null != filePath) {
            text = "$('#Div', window.parent.document).empty().html('<div class=\"alert alert-info\">Файл загружен успешно. " +
                    "Изображение Вашего профиля будет обновлено после одобрения администрацией сайта.</div>');";

            text = "$('#" + Layout.MODAL_ID + "').modal('close')";
        }

        TextStreamResponse response = new TextStreamResponse("text/plain", text);
        return response;
    }
}
