package wooteco.subway.docs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocsController {

    @GetMapping("/api-docs")
    public String getApiDocs() {
        return "/api-docs.html";
    }
}
