package com.telegram.bot.repository;

import com.telegram.bot.data.entity.DontSmokeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DontSmokeRepository extends JpaRepository<DontSmokeInfo, Long> {

    List<DontSmokeInfo> findAllByTYPE(int typeNumber);

}
