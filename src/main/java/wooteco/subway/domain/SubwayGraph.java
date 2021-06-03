package wooteco.subway.domain;

import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayGraph extends WeightedMultigraph<Station, SectionEdge> {
    public SubwayGraph(Class edgeClass) {
        super(edgeClass);
    }

    public void addVertexWith(List<Line> lines) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(this::addVertex);
    }

    public void addEdge(List<Line> lines) {
        for (Line line : lines) {
            line.getSections()
                    .getSections()
                    .forEach(it -> addEdge(it, line));
        }
    }

    private void addEdge(Section section, Line line) {
        SectionEdge sectionEdge = new SectionEdge(section, line);
        addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        setEdgeWeight(sectionEdge, section.getDistance());
    }
}