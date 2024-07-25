package javatechy.codegen.service.impl;

import javatechy.codegen.Repository.AdminStatsRepository;
import javatechy.codegen.dto.AdminStats;
import javatechy.codegen.dto.ProjectCountPerDay;
import javatechy.codegen.service.AdminStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminStatsServiceImpl implements AdminStatsService {
    @Autowired
    private AdminStatsRepository adminStatsRepository;



    @Override
    public AdminStats getAdminStats() {
        int userCount = adminStatsRepository.getUserCount();
        int projectCount = adminStatsRepository.getProjectCount();
        List<ProjectCountPerDay> projectsPerDay = adminStatsRepository.getProjectsPerDay();


        AdminStats adminStats = new AdminStats(userCount, projectCount, projectsPerDay);
        return adminStats;
    }
}
