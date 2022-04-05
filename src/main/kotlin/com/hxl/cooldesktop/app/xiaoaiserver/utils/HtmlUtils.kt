package com.hxl.cooldesktop.app.xiaoaiserver.utils

import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

object HtmlUtils {
    fun createIndex(context:Context): String {
        val  templateResolver = ClassLoaderTemplateResolver(HtmlUtils::class.java.classLoader)
        val templateEngine = TemplateEngine()
        templateEngine.setTemplateResolver(templateResolver)
        return templateEngine.process("/templates/index.html", context)
    }

}