package wooteco.subway.line.domain;

import wooteco.subway.exception.DuplicateException;
import wooteco.subway.line.dto.LineRequest;

import java.util.ArrayList;
import java.util.List;

public class Lines {
    private List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public Line update(Line line, LineRequest lineUpdateRequest) {
        validateDuplicate(line, lineUpdateRequest);
        Line updatedLine = new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        line.update(updatedLine);
        return updatedLine;
    }

    private void validateDuplicate(Line originalLine, LineRequest lineUpdateRequest) {
        if (isUpdatedNameDuplicate(originalLine, lineUpdateRequest)) {
            throw new DuplicateException("이미 존재하는 노선입니다.");
        }

        if (isUpdatedColorDuplicate(originalLine, lineUpdateRequest)) {
            throw new DuplicateException("이미 존재하는 노선 색깔입니다.");
        }
    }

    private boolean isUpdatedNameDuplicate(Line originalLine, LineRequest lineUpdateRequest) {
        return originalLine.hasDifferentName(lineUpdateRequest) && existsByName(lineUpdateRequest.getName());
    }

    private boolean existsByName(String name) {
        return lines.stream()
                .anyMatch(line -> line.hasSameName(name));
    }

    private boolean isUpdatedColorDuplicate(Line originalLine, LineRequest lineUpdateRequest) {
        return originalLine.hasDifferentColor(lineUpdateRequest) && existsByColor(lineUpdateRequest.getColor());
    }

    private boolean existsByColor(String color) {
        return lines.stream()
                .anyMatch(line -> line.hasSameColor(color));
    }
}
