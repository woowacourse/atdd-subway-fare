package wooteco.subway.line.ui.dto;

import static java.util.stream.Collectors.toList;

import java.io.Serializable;
import java.util.List;
import wooteco.subway.line.domain.Sections;

public class SectionsResponse implements Serializable {

    private final List<SectionResponse> sectionResponses;

    public SectionsResponse(Sections sections) {
        this.sectionResponses = sections.getSections().stream()
            .map(SectionResponse::new)
            .collect(toList());
    }

    public SectionsResponse(List<SectionResponse> sectionResponses) {
        this.sectionResponses = sectionResponses;
    }

    public List<SectionResponse> getSectionResponses() {
        return sectionResponses;
    }

}
