package wooteco.subway.path.dto;

import org.springframework.lang.NonNull;

public class PathRequest {

    @NonNull
    private Long source;
    @NonNull
    private Long target;

    public PathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public long sourceId() {
        return this.source;
    }

    public long targetId() {
        return this.target;
    }
}
