package ben.back_end.controller;

import ben.back_end.entity.*;
import ben.back_end.entity.dto.SearchDto;
import ben.back_end.entity.vo.response.*;
import ben.back_end.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/source")
public class SourceController {

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
    public ResponseEntity<?> GetVideos(@RequestParam String tag, @RequestParam int choice) {
        List<Videos> videosList = videoService.getVideosByTag(tag, choice);

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
                        "data", vos
                )
        );
    }

    // 获取涂装列表
    @GetMapping("/get_skins")
    public ResponseEntity<?> GetSkins(@RequestParam String tag, @RequestParam int choice) {
        List<Skins> skinsList = skinService.getSkinByTag(tag, choice);

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
                        "data", vos
                )
        );
    }

    // 获取车辆MOD列表
    @GetMapping("/get_cars")
    public ResponseEntity<?> GetCars(@RequestParam String tag, @RequestParam int choice) {
        List<Cars> carsList = carService.getCarByTag(tag, choice);

        List<CarListElementVO> vos = carsList.stream()
                .map(c -> new CarListElementVO(
                        c.getCarId(),
                        c.getCarModName(),
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
                        "data", vos
                )
        );
    }

    // 获取赛道MOD列表
    @GetMapping("/get_tracks")
    public ResponseEntity<?> GetTracks(@RequestParam String tag, @RequestParam int choice) {
        List<Tracks> tracksList = trackService.getTrackByTag(tag, choice);

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
                        "data", vos
                )
        );
    }

    // 获取插件列表
    @GetMapping("/get_plugins")
    public ResponseEntity<?> GetPlugin(@RequestParam String tag, @RequestParam int choice) {
        List<Plugins> pluginsList = pluginService.getPluginByTag(tag, choice);

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
                        "data", vos
                )
        );
    }

    // 获取具体的车辆模组页面信息
    @GetMapping("/cars/{carModName}")
    public ResponseEntity<?> GetCarDetails(@PathVariable String carModName) {
        Cars car = carService.getCarByName(carModName);

        CarDetailsVO vo = new CarDetailsVO(
                car.getCarId(),
                car.getCarModName(),
                car.getCarAvatar(),
                car.getViews(),
                car.getLikes(),
                car.getDownloads(),
                car.getCreatedAt(),
                car.getDescription()
        );

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取具体车辆模组信息成功",
                        "data", vo
                )
        );
    }

    // 获取具体的车辆模组页面信息
    @GetMapping("/skins/{skinName}")
    public ResponseEntity<?> GetSkinDetails(@PathVariable String skinName) {
        Skins skin = skinService.getSkinByName(skinName);

        SkinDetailsVO vo = new SkinDetailsVO(
                skin.getSkinId(),
                skin.getSkinName(),
                skin.getSkinAvatar(),
                skin.getViews(),
                skin.getLikes(),
                skin.getDownloads(),
                skin.getCreatedAt(),
                skin.getDescription()
        );

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取具体涂装信息成功",
                        "data", vo
                )
        );
    }

    // 获取具体的车辆模组页面信息
    @GetMapping("/tracks/{trackModName}")
    public ResponseEntity<?> GetTrackDetails(@PathVariable String trackModName) {
        Tracks track = trackService.getTrackByName(trackModName);

        TrackDetailsVO vo = new TrackDetailsVO(
                track.getTrackId(),
                track.getTrackModName(),
                track.getTrackAvatar(),
                track.getViews(),
                track.getLikes(),
                track.getDownloads(),
                track.getCreatedAt(),
                track.getDescription()
        );

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取具体赛道模组信息成功",
                        "data", vo
                )
        );
    }

    // 通过搜索获取对应的数据
    @PostMapping("/search/{pageType}")
    public ResponseEntity<?> Search(@PathVariable String pageType, @RequestBody SearchDto searchDto) {
        String tag = searchDto.getTag();
        int choice = searchDto.getChoice();
        String search = searchDto.getSearch();

        List<?> vos;
        switch (pageType) {
            case "video" -> {
                List<Videos> videosList = videoService.getVideoBySearch(tag, choice, search);
                vos = videosList.stream()
                        .map(v -> new VideoListElementVO(
                                v.getVideoId(),
                                v.getVideoTitle(),
                                v.getVideoAvatar(),
                                v.getLinkURL(),
                                v.getViews(),
                                v.getCreatedAt()
                        )).toList();
            }
            case "skin" -> {
                List<Skins> skinsList = skinService.getSkinBySearch(tag, choice, search);
                vos = skinsList.stream()
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
            }
            case "car" -> {
                List<Cars> carsList = carService.getCarBySearch(tag, choice, search);
                vos = carsList.stream()
                        .map(c -> new CarListElementVO(
                                c.getCarId(),
                                c.getCarModName(),
                                c.getCarAvatar(),
                                c.getLinkURL(),
                                c.getViews(),
                                c.getLikes(),
                                c.getDownloads(),
                                c.getCreatedAt()
                        )).toList();
            }
            case "track" -> {
                List<Tracks> tracksList = trackService.getTrackBySearch(tag, choice, search);
                vos = tracksList.stream()
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
            }
            case "plugin" -> {
                List<Plugins> pluginsList = pluginService.getPluginBySearch(tag, choice, search);
                vos = pluginsList.stream()
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
            }
            default -> {
                return ResponseEntity.badRequest().body(RestBean.failure("错误页面分类"));
            }
        }

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取搜索所得列表成功",
                        "data", vos
                )
        );
    }
}
