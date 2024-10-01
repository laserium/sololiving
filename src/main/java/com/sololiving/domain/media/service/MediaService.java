package com.sololiving.domain.media.service;

import org.springframework.stereotype.Service;

import com.sololiving.domain.media.mapper.MediaMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaMapper mediaMapper;

}
