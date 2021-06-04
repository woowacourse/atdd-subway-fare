package wooteco.subway;

import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.section.dao.SectionDao;
import wooteco.subway.section.domain.Distance;
import wooteco.subway.section.domain.Section;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@Component
@Profile("local")
public class DataLoader implements CommandLineRunner {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final MemberDao memberDao;

    public DataLoader(StationDao stationDao, LineDao lineDao, SectionDao sectionDao, MemberDao memberDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.memberDao = memberDao;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Station> stations = stationDao.findAll();

        if (stations.isEmpty()) {
            Distance 기본거리 = new Distance(10);

            Station 강남역 = stationDao.insert(new Station("강남역"));
            Station 판교역 = stationDao.insert(new Station("판교역"));
            Station 정자역 = stationDao.insert(new Station("정자역"));
            Station 역삼역 = stationDao.insert(new Station("역삼역"));
            Station 잠실역 = stationDao.insert(new Station("잠실역"));

            Line 신분당선 = lineDao.insert(new Line("신분당선", "red lighten-1", 0));
            sectionDao.insert(new Section(신분당선, 강남역, 판교역, 기본거리));
            sectionDao.insert(new Section(신분당선, 판교역, 정자역, 기본거리));

            Line 이호선 = lineDao.insert(new Line("2호선", "green lighten-1", 0));
            sectionDao.insert(new Section(이호선, 강남역, 역삼역, 기본거리));
            sectionDao.insert(new Section(이호선, 역삼역, 잠실역, 기본거리));

            Member member = new Member("email@email.com", "password", 10);
            memberDao.insert(member);
        }
    }
}