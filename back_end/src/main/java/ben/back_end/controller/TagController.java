package ben.back_end.controller;

import ben.back_end.entity.Tags;
import ben.back_end.entity.vo.response.TagListElementVO;
import ben.back_end.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/categories")
    public ResponseEntity<?> getTagList(@RequestParam String category) {
        List<Tags> tagList = tagService.getAllTags(category);

        List<TagListElementVO> vos = tagList.stream()
                .map(t -> new TagListElementVO(
                        t.getTagId(),
                        t.getTagName()
                )).toList();

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "msg", "获取标签列表成功",
                        "tags", vos
                )
        );
    }
}
