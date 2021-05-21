package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.line.domain.Line;

public class SectionEdges {
    private final List<SectionEdge> sectionEdgeGroup;

    public SectionEdges(List<SectionEdge> sectionEdgeGroup) {
        this.sectionEdgeGroup = sectionEdgeGroup;
    }

    public int calculateDistance() {
        return sectionEdgeGroup.stream().mapToInt(it -> it.getSection().getDistance()).sum();
    }

    public int calculateMaxLineFare() {
        int maxLineFare = 0;
        for (final SectionEdge sectionEdge : sectionEdgeGroup) {
            final Line line = sectionEdge.getLine();
            maxLineFare = Math.max(maxLineFare, line.getFare());
        }
        return maxLineFare;
    }

    public List<SectionEdge> getSectionEdgeGroup() {
        return sectionEdgeGroup;
    }
}
