package com.alputov.lunchvoting.web.json;

import com.alputov.lunchvoting.TestData;
import com.alputov.lunchvoting.model.User;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonUtilTest {

    @Test
    void writeOnlyAccess() throws Exception {
        String json = JsonUtil.writeValue(TestData.USER);
        System.out.println(json);
        assertThat(json, not(containsString("userPassword")));
        String jsonWithPass = JsonUtil.writeAdditionProps(TestData.USER, "password", "newPass");
        System.out.println(jsonWithPass);
        User user = JsonUtil.readValue(jsonWithPass, User.class);
        assertEquals(user.getPassword(), "newPass");
    }
}