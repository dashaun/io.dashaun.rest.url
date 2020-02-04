package io.dashaun.rest.url.controller;

import io.dashaun.rest.url.domain.Miss;
import io.dashaun.rest.url.domain.Redirect;
import io.dashaun.rest.url.dto.Target;
import io.dashaun.rest.url.repository.MissRepository;
import io.dashaun.rest.url.repository.RedirectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.google.common.hash.Hashing.murmur3_32;

@RestController
public class RedirectControllerImpl implements RedirectController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedirectControllerImpl.class);
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private String baseUrl;
    private String domain;
    private MissRepository missRepository;
    private RedirectRepository redirectRepository;

    @Autowired
    public RedirectControllerImpl(@Value("${url-service.baseUrl:}") String baseUrl,
                                  @Value("${url-service.shortDomain:}") String domain,
                                  MissRepository missRepository,
                                  RedirectRepository redirectRepository) {
        this.baseUrl = baseUrl;
        this.domain = domain;
        this.missRepository = missRepository;
        this.redirectRepository = redirectRepository;

    }

    @Override
    @RequestMapping(value = Mappings.ID, method = RequestMethod.GET)
    public void redirect(@PathVariable String id, HttpServletResponse resp) throws IOException {
        Assert.hasText(id, "ID is required");
        Assert.notNull(resp, "HttpServletResponse is required");
        final Optional<Redirect> result = redirectRepository.findById(id);
        if (result.isPresent()) {
            Redirect redirect = result.get();
            resp.sendRedirect(redirect.getLongUrl());
            if (redirect.getAccessCount() != null) {
                redirect.setAccessCount(redirect.getAccessCount() + 1);
            } else {
                redirect.setAccessCount(1L);
            }
            redirectRepository.save(redirect);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            Miss miss = new Miss();
            miss.setId(id);
            miss.setDomain(domain);
            missRepository.save(miss);
        }
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public Redirect save(@RequestBody Target target) {
        Assert.hasText(target.getLongUrl(), "Long URL is required");
        String theFullUrl = baseUrl + target.getLongUrl();
        try{
            URL url = new URL(theFullUrl);
            LOGGER.debug(url.toString());
            final String id = murmur3_32().hashString(theFullUrl, StandardCharsets.UTF_8).toString();
            Redirect redirect = new Redirect();
            redirect.setId(id);
            redirect.setLongUrl(theFullUrl);
            redirect.setDomain(domain);
            redirect = redirectRepository.save(redirect);
            LOGGER.debug(redirect.toString());
            return redirect;
        } catch(MalformedURLException e) {
            LOGGER.error(e.getMessage(), theFullUrl);
            return null;
        }
    }
}