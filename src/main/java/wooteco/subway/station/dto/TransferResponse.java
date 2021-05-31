package wooteco.subway.station.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransferResponse {
    private Long id;
    private String name;
    private List<String> transfer;

    public TransferResponse() {
    }

    public TransferResponse(Long id, String name, List<String> transfer) {
        this.id = id;
        this.name = name;
        this.transfer = transfer;
    }

    public static List<TransferResponse> of(List<Station> stations, List<Line> lines) {
        List<TransferResponse> transfers = new ArrayList<>();
        for (Station station : stations) {
            List<String> lineNames = lines.stream()
                    .filter(line -> line.getStations().contains(station))
                    .map(Line::getName)
                    .collect(Collectors.toList());
            transfers.add(new TransferResponse(station.getId(), station.getName(), lineNames));
        }
        return transfers;
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
