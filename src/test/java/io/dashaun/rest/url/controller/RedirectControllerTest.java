package io.dashaun.rest.url.controller;

import io.dashaun.rest.url.domain.Miss;
import io.dashaun.rest.url.domain.Redirect;
import io.dashaun.rest.url.dto.Target;
import io.dashaun.rest.url.repository.MissRepository;
import io.dashaun.rest.url.repository.RedirectRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class RedirectControllerTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Autowired
    private MissRepository missRepository;
    @Autowired
    private RedirectRepository redirectRepository;

    @Before
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Value("${url-service.shortDomain:}")
    String domain = "";

    @Value("${url-service.baseUrl:}")
    String baseUrl = "";

    @Autowired
    private RedirectController redirectController;

    @Test
    public void testSaveLongUrl() throws Exception {
        Target target = new Target();
        target.setLongUrl("somecrazylongurilwith?queryparams=12323");
        final Redirect redirect = redirectController.save(target);
        assertNotNull(redirect);
        assertEquals(redirect.getLongUrl(), baseUrl + target.getLongUrl());
        assertNotNull(redirect.getId());
        assertEquals(redirect.getDomain(), domain);

        redirectController.redirect(redirect.getId(), response);
        Optional<Redirect> checkCount = redirectRepository.findById(redirect.getId());
        assertTrue(checkCount.isPresent());
        assertEquals(checkCount.get().getAccessCount(), Long.valueOf(1));
    }

    @Test
    public void testRedirectNegative() throws Exception {
        redirectController.redirect("1", response);
        assertEquals(response.getStatus(), HttpServletResponse.SC_NOT_FOUND);

        Optional<Miss> miss = missRepository.findById("1");
        assertTrue(miss.isPresent());
        assertEquals(domain, miss.get().getDomain());
    }

}