package ben.back_end.controller;

import ben.back_end.entity.*;
import ben.back_end.entity.vo.response.NewestListElementVO;
import ben.back_end.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class MainPageController {

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

    private NewestListElementVO createNewestListElementVO(Object entity, String type) {
        NewestListElementVO itemVO = new NewestListElementVO();
        if (entity != null) {
            // 根据不同类型的实体来设置name和description
            if (entity instanceof Videos video) {
                itemVO.setName(video.getVideoTitle());
                itemVO.setDescription(video.getShortInfo());
            } else if (entity instanceof Skins skin) {
                itemVO.setName(skin.getSkinName());
                itemVO.setDescription(skin.getDescription());
            } else if (entity instanceof Cars car) {
                itemVO.setName(car.getCarModName());
                itemVO.setDescription(car.getDescription());
            } else if (entity instanceof Tracks track) {
                itemVO.setName(track.getTrackModName());
                itemVO.setDescription(track.getDescription());
            } else if (entity instanceof Plugins plugin) {
                itemVO.setName(plugin.getPluginName());
                itemVO.setDescription(plugin.getDescription());
            }
            itemVO.setType(type); // 设置类型
        }
        return itemVO;
    }

    @GetMapping("/newest")
    public ResponseEntity<?> GetNewest() {

        List<NewestListElementVO> vos = new ArrayList<>();

        vos.add(createNewestListElementVO(videoService.getNewestVideos(), "video"));
        vos.add(createNewestListElementVO(skinService.getNewestSkins(), "skin"));
        vos.add(createNewestListElementVO(carService.getNewestCars(), "car"));
        vos.add(createNewestListElementVO(trackService.getNewestTracks(), "track"));
        vos.add(createNewestListElementVO(pluginService.getNewestPlugins(), "plugin"));

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取最新内容成功",
                        "data", vos
                )
        );
    }
}
