package com.alputov.lunchvoting.service;

import com.alputov.lunchvoting.model.Restaurant;
import com.alputov.lunchvoting.model.User;
import com.alputov.lunchvoting.repository.RestaurantRepository;
import com.alputov.lunchvoting.repository.UserRepository;
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

    public Restaurant create(Restaurant r, User user) {
        return save(r, user);
    }

    public Restaurant update(Restaurant r, User user) {
        return checkNotFoundWithId(save(r, user), r.getId());
    }

    public Restaurant get(int id, User user) {
        Restaurant r = user.isAdmin() ? restaurantRepository.findById(id).orElse(null) : null;
        return checkNotFoundWithId(r, id);
    }

    public void delete(int id, User user) {
        checkNotFoundWithId(user.isAdmin() && restaurantRepository.delete(id) != 0, id);
    }

    public List<Restaurant> getAll(User user) {
        if(!user.isAdmin()){
            return null;
        }
        return restaurantRepository.findAll();
    }

    @Transactional
    public Restaurant save(Restaurant r, User user) {
        Assert.notNull(r, "restaurant must not be null");
        if (!r.isNew() && get(r.getId(), user) == null) {
            return null;
        }
        return restaurantRepository.save(r);
    }
}
