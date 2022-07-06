package com.tcbs.automation.coco.mongodb.manager;

import com.tcbs.automation.coco.mongodb.model.UserProfile;

import java.util.Optional;

public interface UserProfileManager {
  Optional<UserProfile> findRatingUserById(String copierRatingTcbsId);
}
