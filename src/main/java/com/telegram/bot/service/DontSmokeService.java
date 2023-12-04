package com.telegram.bot.service;

import com.telegram.bot.data.entity.DontSmokeInfo;
import com.telegram.bot.repository.DontSmokeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class DontSmokeService {
    private final DontSmokeRepository DontSmokeInfoRepository;

    public void saveDontSmokeInfo(DontSmokeInfo dontSmokeInfo) {
        DontSmokeInfoRepository.save(dontSmokeInfo);
    }


    public DontSmokeInfo getSingleRandomLineByType(int typeNumber) {
        int length = DontSmokeInfoRepository.findAllByTYPE(typeNumber).size();

        if (length > 0) {
            Random rn = new Random();
            int randomNum = rn.nextInt(length);

            return DontSmokeInfoRepository.findAllByTYPE(typeNumber).get(randomNum);
        } else
            return null;
    }

    public List<DontSmokeInfo> getAllLineByType(int typeNumber) {
        List<DontSmokeInfo> dontSmokeInfoList =  DontSmokeInfoRepository.findAllByTYPE(typeNumber);
        if (!dontSmokeInfoList.isEmpty()) {
            return dontSmokeInfoList;
        } else
            return null ;
    }

    
}
