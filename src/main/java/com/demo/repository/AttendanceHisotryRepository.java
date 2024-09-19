package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.entity.AttendanceHistory;

@Repository
public interface AttendanceHisotryRepository extends JpaRepository<AttendanceHistory, Long> {

}
