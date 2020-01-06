package com.alputov.lunchvoting.to;

import com.alputov.lunchvoting.View;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class DishTo {
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 2, max = 100)
    @SafeHtml(groups = {View.Web.class})
    private String name;

    @NotNull
    @JsonProperty("price")
    private Integer price;

    public DishTo() {
    }

    public DishTo(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishTo dishTo = (DishTo) o;
        return name.equals(dishTo.name) &&
                price.equals(dishTo.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
