package wooteco.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;

public class SectionEdge extends DefaultWeightedEdge {
    private Section section;
    private Line line;

    public SectionEdge(Section section, Line line) {
        this.section = section;
        this.line = line;
    }

    public Section getSection() {
        return section;
    }

    public int getDistance() {
        return section.getDistance();
    }

    public Line getLine() {
        return line;
    }

    public int getExtraFare() {
        return line.getExtraFare();
    }

    @Override
    protected Object getSource() {
        return this.section.getUpStation();
    }

    @Override
    protected Object getTarget() {
        return this.section.getDownStation();
    }

    @Override
    protected double getWeight() {
        return this.section.getDistance();
    }
}
