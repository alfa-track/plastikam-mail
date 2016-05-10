package ru.plastikam.mail.sys;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Сокращения FacesContext
 */
public class FC implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(FC.class);

    public static void js(String js) {
        RequestContext.getCurrentInstance().execute(js);
    }

    public static void update(String id) {
        RequestContext.getCurrentInstance().update(id);
    }

    public static void warn(String title, String message) {
        logger.debug("warn " + title + " " + message);
        if (FacesContext.getCurrentInstance() == null)
            return;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, title, br(message)));
    }

    public static void info(String title, String message) {
        logger.debug("info " + title + " " + message);
        if (FacesContext.getCurrentInstance() == null)
            return;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, title, br(message)));
    }

    public static void error(String title, String message) {
        error(title, message, null);
    }

    public static void error(String title, Exception ex) {
        error(title, null, ex);
    }

    public static void error(String title, String message, Exception ex) {
        title = title == null ? "" : title;
        message = message == null ? "" : message;
        if (StringUtils.isBlank(message) && ex != null) {
            message = ex.getMessage();
        }
        if (ex != null) {
            // ex.printStackTrace();
        }
        logger.warn("error " + title + " " + message, ex);
        if (FacesContext.getCurrentInstance() == null)
            return;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, br(message)));
    }

    public static String br(String s) {
        return s == null ? "" : s.replaceAll("\n", "<br/>");
    }

}
