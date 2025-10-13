package ben.back_end.controller;

import ben.back_end.entity.*;
import ben.back_end.entity.dto.SearchDto;
import ben.back_end.entity.vo.response.*;
import ben.back_end.service.*;
import ben.back_end.util.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Autowired
    private ImageService imageService;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private LikeService likeService;

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
        if(carModName == null || carModName.isEmpty()) return ResponseEntity.badRequest().body(RestBean.failure("carModName不能为空"));
        Cars car = carService.getCarByName(carModName);

        List<String> imageList = imageService.getImageSetById("car", car.getCarId());

        CarDetailsVO vo = new CarDetailsVO(
                car.getCarId(),
                car.getCarModName(),
                car.getCarAvatar(),
                car.getViews(),
                car.getLikes(),
                car.getDownloads(),
                car.getCreatedAt(),
                car.getDescription(),
                imageList
        );

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取具体车辆模组信息成功",
                        "data", vo
                )
        );
    }

    // 获取具体的涂装页面信息
    @GetMapping("/skins/{skinName}")
    public ResponseEntity<?> GetSkinDetails(@PathVariable String skinName) {
        if(skinName == null || skinName.isEmpty()) return ResponseEntity.badRequest().body(RestBean.failure("skinName不能为空"));
        Skins skin = skinService.getSkinByName(skinName);

        List<String> imageList = imageService.getImageSetById("skin", skin.getSkinId());

        SkinDetailsVO vo = new SkinDetailsVO(
                skin.getSkinId(),
                skin.getSkinName(),
                skin.getSkinAvatar(),
                skin.getViews(),
                skin.getLikes(),
                skin.getDownloads(),
                skin.getCreatedAt(),
                skin.getDescription(),
                imageList
        );

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取具体涂装信息成功",
                        "data", vo
                )
        );
    }

    // 获取具体的赛道模组页面信息
    @GetMapping("/tracks/{trackModName}")
    public ResponseEntity<?> GetTrackDetails(@PathVariable String trackModName) {
        if(trackModName == null || trackModName.isEmpty()) return ResponseEntity.badRequest().body(RestBean.failure("trackModName不能为空"));
        Tracks track = trackService.getTrackByName(trackModName);

        List<String> imageList = imageService.getImageSetById("track", track.getTrackId());

        TrackDetailsVO vo = new TrackDetailsVO(
                track.getTrackId(),
                track.getTrackModName(),
                track.getTrackAvatar(),
                track.getViews(),
                track.getLikes(),
                track.getDownloads(),
                track.getCreatedAt(),
                track.getDescription(),
                imageList
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

    // 点击量 view 更新
    @PostMapping("/{type}/{name}/views")
    public ResponseEntity<?> PostSourceViews(@PathVariable String type, @PathVariable String name) {
        int viewCount;
        switch (type.toLowerCase()) {
            case "skins" -> {
                skinService.updateByName("add", "views", name);
                viewCount = skinService.getUpdatedByName("views", name);
            }
            case "cars" -> {
                carService.updateByName("add", "views", name);
                viewCount = carService.getUpdatedByName("views", name);
            }
            case "tracks" -> {
                trackService.updateByName("add", "views", name);
                viewCount = trackService.getUpdatedByName("views", name);
            }
            case "plugins" -> {
                pluginService.updateByName("add", "views", name);
                viewCount = pluginService.getUpdatedByName("views", name);
            }
            default -> {
                return ResponseEntity.badRequest().body(RestBean.failure("未知的资源类型：" + type));
            }
        }
        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "更新点击量成功",
                        "viewCount", viewCount
                )
        );
    }

    // 点赞/取消点赞
    @PutMapping("/{type}/{name}/like")
    public ResponseEntity<?> PutSourceLikes(@PathVariable String type, @PathVariable String name, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 检查用户是否登录
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(RestBean.failure("用户未登录"));
        }

        DecodedJWT decodedJWT = jwtUtils.verifyJWT(authHeader);
        if (decodedJWT == null) {
            return ResponseEntity.badRequest().body(RestBean.failure("Token已过期"));
        }

        // 解析 JWT 获取当前用户的 id
        Integer userIdObj = jwtUtils.toId(decodedJWT);
        if (userIdObj == null) {
            return ResponseEntity.status(401).body(RestBean.failure("Token 中未包含 userId"));
        }
        int userId = userIdObj;

        int likeCount;

        boolean likeStatus;

        switch (type.toLowerCase()) {
            case "skins" -> {
                Skins skin = skinService.getSkinByName(name);
                if (skin == null) return ResponseEntity.status(404).body(RestBean.failure("未找到对应涂装"));
                Likes like = likeService.CheckLike(userId, "skin", skin.getSkinId());
                if (like == null) {
                    likeService.AddLike(userId, "skin", skin.getSkinId());
                    skinService.updateByName("add", "likes", name);
                    likeStatus = true;
                } else {
                    likeService.RemoveLike(userId, "skin", skin.getSkinId());
                    skinService.updateByName("remove", "likes", name);
                    likeStatus = false;
                }
                likeCount = skinService.getUpdatedByName("likes", name);
            }
            case "cars" -> {
                Cars car = carService.getCarByName(name);
                if (car == null) return ResponseEntity.status(404).body(RestBean.failure("未找到对应车辆模组"));
                Likes like = likeService.CheckLike(userId, "car", car.getCarId());
                if (like == null) {
                    carService.updateByName("add", "likes", name);
                    likeService.AddLike(userId, "car", car.getCarId());
                    likeStatus = true;
                } else {
                    carService.updateByName("remove", "likes", name);
                    likeService.RemoveLike(userId, "car", car.getCarId());
                    likeStatus = false;
                }
                likeCount = carService.getUpdatedByName("likes", name);
            }
            case "tracks" -> {
                Tracks track = trackService.getTrackByName(name);
                if (track == null) return ResponseEntity.status(404).body(RestBean.failure("未找到对应赛道模组"));
                Likes like = likeService.CheckLike(userId, "track", track.getTrackId());
                if (like == null) {
                    likeService.AddLike(userId, "track", track.getTrackId());
                    trackService.updateByName("add", "likes", name);
                    likeStatus = true;
                } else {
                    likeService.RemoveLike(userId, "track", track.getTrackId());
                    trackService.updateByName("remove", "likes", name);
                    likeStatus = false;
                }
                likeCount = trackService.getUpdatedByName("likes", name);
            }
            case "plugins" -> {
                Plugins plugins = pluginService.getPluginByName(name);
                if (plugins == null) return ResponseEntity.status(404).body(RestBean.failure("未找到对应插件"));
                Likes like = likeService.CheckLike(userId, "plugin", plugins.getPluginId());
                if (like == null) {
                    likeService.AddLike(userId, "plugin", plugins.getPluginId());
                    pluginService.updateByName("add", "likes", name);
                    likeStatus = true;
                } else {
                    likeService.RemoveLike(userId, "plugin", plugins.getPluginId());
                    pluginService.updateByName("remove", "likes", name);
                    likeStatus = false;
                }
                likeCount = pluginService.getUpdatedByName("likes", name);
            }
            default -> {
                return ResponseEntity.badRequest().body(RestBean.failure("未知的资源类型：" + type));
            }
        }

        if (likeCount < 0) return ResponseEntity.status(500).body(RestBean.failure("获取更改数据出错：" + type));

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "点赞操作成功",
                        "likeCount", likeCount,
                        "likeStatus", likeStatus
                )
        );
    }

    // 下载（车辆/赛道/图装）.zip 压缩文件
    @GetMapping("/{type}/{name}/download")
    public ResponseEntity<?> GetFileDownload(@PathVariable String type, @PathVariable String name, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 检查用户是否登录
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(RestBean.failure("用户未登录"));
        }

        DecodedJWT decodedJWT = jwtUtils.verifyJWT(authHeader);
        if (decodedJWT == null) {
            return ResponseEntity.badRequest().body(RestBean.failure("Token已过期"));
        }

        // 构建文件路径
        Path filePath = Paths.get("src/main/resources/static/" + type, name + ".zip");
        if (!Files.exists(filePath)) {
            return ResponseEntity.badRequest().body(RestBean.failure("未找到对应的下载文件: " + name));
        }

        // 返回文件流
        try {
            byte[] fileContent = Files.readAllBytes(filePath);
            HttpHeaders headers = new HttpHeaders();
            // 用 UTF-8 编码处理非英文文件名
            String filename = filePath.getFileName().toString();
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename);

            headers.add(HttpHeaders.CONTENT_TYPE, "application/zip");
            headers.setContentLength(Files.size(filePath));
            switch (type) {
                case "skins": skinService.updateByName("add", "downloads", name); break;
                case "cars": carService.updateByName("add", "downloads", name); break;
                case "tracks": trackService.updateByName("add", "downloads", name); break;
            }
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(Files.size(filePath))
                    .body(fileContent);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(RestBean.failure("文件读取失败：" + e.getMessage()));
        }
    }
}
