package com.example.Capstone.repository;

import com.example.Capstone.entity.GroupSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupScheduleRepository extends JpaRepository<GroupSchedule, Long> {
    List<GroupSchedule> findAllByMyGroup_Id(Long groupId);
}