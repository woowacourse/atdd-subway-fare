package wooteco.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.line.domain.Line;

public class SectionEdges {

    private final List<SectionEdge> sectionEdgeGroup;

    public SectionEdges(List<SectionEdge> sectionEdgeGroup) {
        this.sectionEdgeGroup = sectionEdgeGroup;
    }

    public int calculateDistance() {
        return sectionEdgeGroup.stream()
            .mapToInt(SectionEdge::getSectionDistance)
            .sum();
    }

    public List<Line> getLines() {
        return sectionEdgeGroup.stream()
            .map(SectionEdge::getLine)
            .distinct()
            .collect(Collectors.toList());
    }
}
