package com.dzlin.oauth2.payload;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;

public class GrantAuthorityPayload {

    @JsonAlias(value = "user_id")
    @Min(value = 1)
    private Long userId;

    @JsonAlias(value = "authority_ids")
    @NotEmpty
    private ArrayList<Long> authorityIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ArrayList<Long> getAuthorityIds() {
        return authorityIds;
    }

    public void setAuthorityIds(ArrayList<Long> authorityIds) {
        this.authorityIds = authorityIds;
    }

    @Override
    public String toString() {
        return "GrantAuthorityPayload{" +
                "userId=" + userId +
                ", authorityIds=" + authorityIds +
                '}';
    }
}
