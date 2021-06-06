package wooteco.subway.station.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StationWithTransferResponse {
    @JsonProperty
    private Long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private List<String> transfer;

    public StationWithTransferResponse() {
    }

    public StationWithTransferResponse(Long id, String name, List<String> transfer) {
        this.id = id;
        this.name = name;
        this.transfer = transfer;
    }

    public static List<StationWithTransferResponse> listOf(Map<Station, List<Line>> stationWithTransferInfo) {
        return stationWithTransferInfo.keySet()
                .stream()
                .filter(station -> !stationWithTransferInfo.get(station).isEmpty())
                .map(station -> of(station, stationWithTransferInfo.get(station)))
                .collect(Collectors.toList());
    }

    public static StationWithTransferResponse of(Station station, List<Line> stationIncludedLines) {
        final List<String> includedLineName = stationIncludedLines.stream()
                .map(Line::getLineName)
                .collect(Collectors.toList());
        return new StationWithTransferResponse(station.getId(), station.getName(), includedLineName);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getTransfer() {
        return transfer;
    }
}
