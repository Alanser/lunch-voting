package com.alputov.lunchvoting.util;

import com.alputov.lunchvoting.model.Restaurant;
import com.alputov.lunchvoting.to.RestaurantTo;

public class RestaurantUtil {
    public static RestaurantTo asTo(Restaurant r){
        return new RestaurantTo(r.getId(), r.getName());
    }
}
