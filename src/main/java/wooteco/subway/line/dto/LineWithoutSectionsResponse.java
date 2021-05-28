package wooteco.subway.line.dto;

public class LineWithoutSectionsResponse {
    private Long id;
    private String name;
    private String color;
    private int extraFare;

    public LineWithoutSectionsResponse() {
    }

    public LineWithoutSectionsResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LineWithoutSectionsResponse(Long id, String name, String color, int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
