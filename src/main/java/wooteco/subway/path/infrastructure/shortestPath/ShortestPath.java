package wooteco.subway.path.infrastructure.shortestPath;

import static wooteco.subway.exception.SubwayExceptions.*;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;

import wooteco.subway.line.domain.Line;
import wooteco.subway.path.infrastructure.SectionEdge;
import wooteco.subway.path.infrastructure.SubwayGraph;
import wooteco.subway.station.domain.Station;

public abstract class ShortestPath {
    private final GraphPath<Station, SectionEdge> path;

    public ShortestPath(List<Line> lines, Station source, Station target) {
        SubwayGraph subwayGraph = new SubwayGraph(SectionEdge.class, lines);
        this.path = shortestPathAlgorithm(subwayGraph).getPath(source, target);
        validatePath(path);
    }

    private void validatePath(GraphPath<Station, SectionEdge> path) {
        if (path == null) {
            throw INVALID_PATH.makeException();
        }
    }

    public List<SectionEdge> getSections() {
        return path.getEdgeList();
    }

    public List<Station> getStations() {
        return path.getVertexList();
    }

    protected abstract ShortestPathAlgorithm<Station, SectionEdge> shortestPathAlgorithm(SubwayGraph subwayGraph);
}
