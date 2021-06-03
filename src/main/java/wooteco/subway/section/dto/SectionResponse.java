package wooteco.subway.section.dto;

import java.util.List;
import wooteco.subway.line.dto.TransferLineResponse;
import wooteco.subway.section.domain.Section;

public class SectionResponse {

    private Long id;
    private String name;
    private int distanceToNextStation;
    private List<TransferLineResponse> transferLines;

    public SectionResponse() {
    }

    public SectionResponse(Long id, String name, int distanceToNextStation, List<TransferLineResponse> transferLines) {
        this.id = id;
        this.name = name;
        this.distanceToNextStation = distanceToNextStation;
        this.transferLines = transferLines;
    }

    public static SectionResponse of(Section section, List<TransferLineResponse> transferLines) {
        return new SectionResponse(section.getUpStationId(), section.getUpStationName(), section.getDistanceValue(), transferLines);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDistanceToNextStation() {
        return distanceToNextStation;
    }

    public List<TransferLineResponse> getTransferLines() {
        return transferLines;
    }
}
