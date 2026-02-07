package com.ricky.chronicle.Repository;

import org.junit.jupiter.api.Test;

import com.ricky.chronicle.AbstractIntegrationTest;
import com.ricky.chronicle.entity.User;

public class FeedsRepositoryIntegrationTest extends AbstractIntegrationTest{
    @Test
    public void givenUserUsername_whenGetAllFeeds_returnAllFeedsByUser(){
        User user = this.seededUsers.getFirst();

    }
}
