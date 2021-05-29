package wooteco.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

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
