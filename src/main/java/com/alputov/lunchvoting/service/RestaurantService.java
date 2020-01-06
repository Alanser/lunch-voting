package com.alputov.lunchvoting.service;

import com.alputov.lunchvoting.model.Restaurant;
import com.alputov.lunchvoting.repository.RestaurantRepository;
import com.alputov.lunchvoting.repository.UserRepository;
import com.alputov.lunchvoting.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.alputov.lunchvoting.util.ValidationUtil.checkNotFoundWithId;

@Service("restaurantService")
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    UserRepository userRepository;

    public Restaurant create(Restaurant r) {
        return save(r);
    }

    public Restaurant update(Restaurant r) {
        Assert.notNull(r, "restaurant must not be null");
        get(r.id());
        return save(r);
    }

    public Restaurant get(int id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void delete(int id) {
        checkNotFoundWithId(restaurantRepository.delete(id) != 0, id);
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    @Transactional
    public Restaurant save(Restaurant r) {
        Assert.notNull(r, "restaurant must not be null");
        return restaurantRepository.save(r);
    }
}
