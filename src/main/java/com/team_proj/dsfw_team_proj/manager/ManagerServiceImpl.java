package com.team_proj.dsfw_team_proj.manager;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Override
    public List<FakeOverviewDTO> getFakeManagerDataForOverview() {
        List<FakeOverviewDTO> fakeData = new ArrayList<>();
        String[] fakeNames = {"Fares", "Harry", "Kawa", "Jacob", "Ghasan"};
        Random random = new Random();

        for (int i = 0; i < fakeNames.length; i++) {
            String name = fakeNames[i];

            LocalDateTime submittedAt = null;
            if (random.nextDouble() > 0.3) {
                submittedAt = LocalDateTime.now().minusHours(random.nextInt(120));
            }
            
            fakeData.add(new FakeOverviewDTO((long) (1001 + i), name, submittedAt));
        }
        return fakeData;
    }
}