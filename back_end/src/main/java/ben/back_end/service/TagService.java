package ben.back_end.service;

import ben.back_end.entity.Tags;
import ben.back_end.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public List<Tags> getAllTags() {
        return tagRepository.findAll();
    }
}
