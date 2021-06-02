package wooteco.subway.path.domain;

import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.section.domain.Section;
import wooteco.subway.section.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayGraph extends WeightedMultigraph<Station, SectionEdge> {
    public SubwayGraph(Class edgeClass) {
        super(edgeClass);
    }

    public void addVertexWith(Sections sections) {
        sections.sortedStations()
            .stream()
            .distinct()
            .collect(Collectors.toList())
            .forEach(this::addVertex);
    }

    public void addEdge(Sections sections) {
        for (Section section : sections.getSections()) {
            SectionEdge sectionEdge = new SectionEdge(section, section.getLine());
            addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
            setEdgeWeight(sectionEdge, section.getDistanceValue());
        }
    }
}
