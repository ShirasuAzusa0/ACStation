package ben.back_end.controller;

import ben.back_end.entity.*;
import ben.back_end.entity.dto.GetListDto;
import ben.back_end.entity.vo.response.*;
import ben.back_end.repository.*;
import ben.back_end.service.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/source")
public class SourceController {
    @Resource
    VideoRepository videoRepository;

    @Resource
    SkinRepository skinRepository;

    @Resource
    CarRepository carRepository;

    @Resource
    TrackRepository trackRepository;

    @Resource
    PluginRepository pluginRepository;

    @Autowired
    private VideoService videoService;

    @Autowired
    private SkinService skinService;

    @Autowired
    private CarService carService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private PluginService pluginService;

    // 获取视频列表
    @GetMapping("/get_videos")
    public ResponseEntity<?> GetVideos(@RequestBody GetListDto dto) {
        List<Videos> videosList = videoService.getVideosByTag(dto.getTag(), dto.getChoice());

        List<VideoListElementVO> vos = videosList.stream()
                .map(v -> new VideoListElementVO(
                        v.getVideoId(),
                        v.getVideoTitle(),
                        v.getVideoAvatar(),
                        v.getLinkURL(),
                        v.getViews(),
                        v.getCreatedAt()
                )).toList();

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取视频列表成功",
                        "videos", vos
                )
        );
    }

    // 获取涂装列表
    @GetMapping("/get_skins")
    public ResponseEntity<?> GetSkins(@RequestBody GetListDto dto) {
        List<Skins> skinsList = skinService.getSkinByTag(dto.getTag(), dto.getChoice());

        List<SkinListElementVO> vos = skinsList.stream()
                .map(s -> new SkinListElementVO(
                        s.getSkinId(),
                        s.getSkinName(),
                        s.getSkinAvatar(),
                        s.getLinkURL(),
                        s.getViews(),
                        s.getLikes(),
                        s.getDownloads(),
                        s.getCreatedAt()
                )).toList();

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取涂装列表成功",
                        "Skins", vos
                )
        );
    }

    // 获取车辆MOD列表
    @GetMapping("/get_cars")
    public ResponseEntity<?> GetCars(@RequestBody GetListDto dto) {
        List<Cars> carsList = carService.getCarByTag(dto.getTag(), dto.getChoice());

        List<CarListElementVO> vos = carsList.stream()
                .map(c -> new CarListElementVO(
                        c.getCarId(),
                        c.getCarName(),
                        c.getCarAvatar(),
                        c.getLinkURL(),
                        c.getViews(),
                        c.getLikes(),
                        c.getDownloads(),
                        c.getCreatedAt()
                )).toList();

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取车辆MOD列表成功",
                        "Cars", vos
                )
        );
    }

    // 获取赛道MOD列表
    @GetMapping("/get_tracks")
    public ResponseEntity<?> GetTracks(@RequestBody GetListDto dto) {
        List<Tracks> tracksList = trackService.getTrackByTag(dto.getTag(), dto.getChoice());

        List<TrackListElementVO> vos = tracksList.stream()
                .map(t -> new TrackListElementVO(
                        t.getTrackId(),
                        t.getTrackModName(),
                        t.getTrackAvatar(),
                        t.getLinkURL(),
                        t.getViews(),
                        t.getLikes(),
                        t.getDownloads(),
                        t.getCreatedAt()
                )).toList();

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取赛道MOD列表成功",
                        "Tracks", vos
                )
        );
    }

    // 获取插件列表
    @GetMapping("/get_plugins")
    public ResponseEntity<?> GetPlugin(@RequestBody GetListDto dto) {
        List<Plugins> pluginsList = pluginService.getPluginByTag(dto.getTag(), dto.getChoice());

        List<PluginListElementVO> vos = pluginsList.stream()
                .map(p -> new PluginListElementVO(
                        p.getPluginId(),
                        p.getPluginName(),
                        p.getPluginAvatar(),
                        p.getLinkURL(),
                        p.getViews(),
                        p.getLikes(),
                        p.getDownloads(),
                        p.getCreatedAt()
                )).toList();

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取插件列表成功",
                        "Plugins", vos
                )
        );
    }
}
