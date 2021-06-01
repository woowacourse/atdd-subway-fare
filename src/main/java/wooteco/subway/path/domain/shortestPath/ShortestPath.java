package wooteco.subway.path.domain.shortestPath;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;

import wooteco.subway.exception.invalid.InvalidPathException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayGraph;
import wooteco.subway.station.domain.Station;

public abstract class ShortestPath {
    private final GraphPath<Station, SectionEdge> path;

    public ShortestPath(List<Line> lines, Station source, Station target) {
        SubwayGraph subwayGraph = new SubwayGraph(SectionEdge.class, lines);
        this.path = getShortestPathAlgorithm(subwayGraph).getPath(source, target);
        validatePath(path);
    }

    private void validatePath(GraphPath<Station, SectionEdge> path) {
        if(path == null) {
            throw new InvalidPathException();
        }
    }

    public List<SectionEdge> getSections() {
        return path.getEdgeList();
    }

    public List<Station> getStations() {
        return path.getVertexList();
    }

    protected abstract ShortestPathAlgorithm<Station, SectionEdge> getShortestPathAlgorithm(SubwayGraph subwayGraph);
}
