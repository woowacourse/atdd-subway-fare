package wooteco.subway.path.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

public class SubwayGraph extends WeightedMultigraph<Station, SectionEdge> {
    public SubwayGraph(Class edgeClass, List<Line> lines) {
        super(edgeClass);
        createGraph(lines);
    }

    private void createGraph(List<Line> lines) {
        addVertexesWith(lines);
        addEdgesWith(lines);
    }

    private void addVertexesWith(List<Line> lines) {
        lines.stream()
            .flatMap(it -> it.getStations().stream())
            .distinct()
            .collect(Collectors.toList())
            .forEach(this::addVertex);
    }

    private void addEdgesWith(List<Line> lines) {
        lines.forEach(line -> line.getSections()
            .forEach(section -> addEdgeWith(section, line)));
    }

    private void addEdgeWith(Section section, Line line) {
        SectionEdge sectionEdge = new SectionEdge(section, line);
        addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        setEdgeWeight(sectionEdge, section.getDistance());
    }
}
