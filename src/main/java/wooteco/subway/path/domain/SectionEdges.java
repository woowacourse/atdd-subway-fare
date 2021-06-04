package wooteco.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.line.domain.Line;
import wooteco.subway.section.domain.Section;

public class SectionEdges {

    private final List<SectionEdge> sectionEdges;

    public SectionEdges(List<SectionEdge> sectionEdges) {
        this.sectionEdges = sectionEdges;
    }

    public int calculateDistance() {
        return sectionEdges.stream()
            .map(SectionEdge::getSection)
            .mapToInt(Section::getDistanceValue)
            .sum();
    }

    public List<Line> getLines() {
        return sectionEdges.stream()
            .map(SectionEdge::getLine)
            .distinct()
            .collect(Collectors.toList());
    }
}
