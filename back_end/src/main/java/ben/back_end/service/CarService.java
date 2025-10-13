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

    // 根据搜索和筛选tag查找相关车辆模组信息实体
    public List<Cars> getCarBySearch(String tag, int choice, String search) {
        if (search == null || search.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid search");
        }
        String[] parts = search.split(" ");
        String regex = String.join("|", parts);
        return carRepository.SearchCars(tag, regex, choice);
    }

    // 通过tag获取车辆模组列表
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

    // 通过名称更新数据
    public void updateByName(String method, String type, String carModName) {
        switch (type) {
            case "views": carRepository.updateViewsByName(carModName); break;
            case "downloads": carRepository.addDownloadsByName(carModName); break;
            case "likes": {
                if (method.equals("add")) carRepository.addLikesByName(carModName);
                else if(method.equals("remove")) carRepository.removeLikesByName(carModName);
                else throw new IllegalArgumentException("Invalid method");
            }
        }
    }

    // 通过名称获取更新字段数据
    public int getUpdatedByName(String type, String carModName) {
        return switch (type) {
            case "views" -> carRepository.getViewsByName(carModName);
            case "likes" -> carRepository.getLikesByName(carModName);
            default -> -1;
        };
    }
}
