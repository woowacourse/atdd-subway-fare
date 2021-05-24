package wooteco.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;
    private final Line line;

    public SectionEdge(Section section, Line line) {
        this.section = section;
        this.line = line;
    }

    public Section getSection() {
        return section;
    }

    public Line getLine() {
        return line;
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
