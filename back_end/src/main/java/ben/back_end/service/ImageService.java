package ben.back_end.service;

import ben.back_end.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    // 通过 id 获取概览图列表
    public List<String> getImageSetById(String type, int id) {
        return switch (type) {
            case "skin" -> imageRepository.findImagesBySkinId(id);
            case "car" -> imageRepository.findImagesByCarId(id);
            case "track" -> imageRepository.findImagesByTrackId(id);
            default -> null;
        };
    }
}
