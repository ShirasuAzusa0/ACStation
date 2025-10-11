package ben.back_end.service;

import ben.back_end.entity.Cars;
import ben.back_end.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    // 根据搜索和筛选tag查找相关涂装信息实体
    public List<Cars> getCarBySearch(String tag, int choice, String search) {
        if (search == null || search.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid search");
        }
        String[] parts = search.split(" ");
        String regex = String.join("|", parts);
        return carRepository.SearchCars(tag, regex, choice);
    }

    // 通过tag获取视频列表
    public List<Cars> getCarByTag(String tag, int choice) {
        return switch (choice) {
            case 1 -> carRepository.getCarsById(tag);
            case 2 -> carRepository.getCarsByNewest(tag);
            case 3 -> carRepository.getCarsByOldest(tag);
            case 4 -> carRepository.getCarsByPopular(tag);
            default -> null;
        };
    }

    // 获取所有的车辆MOD
    public Cars getNewestCars() {
        return carRepository.findNewestCars();
    }

    // 通过名称获取车辆MOD
    public Cars getCarByName(String carModName) {
        return carRepository.findCarByModName(carModName);
    }
}
