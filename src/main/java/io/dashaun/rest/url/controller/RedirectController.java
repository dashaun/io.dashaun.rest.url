package io.dashaun.rest.url.controller;

import io.dashaun.rest.url.domain.Redirect;
import io.dashaun.rest.url.dto.Target;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface RedirectController {
    void redirect(String id, HttpServletResponse resp) throws IOException;

    Redirect save(Target target);
}
