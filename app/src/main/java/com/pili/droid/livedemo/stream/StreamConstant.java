package com.pili.droid.livedemo.stream;

import static com.qiniu.pili.droid.streaming.StreamingProfile.*;


/**
 * Created by kevin on 2017/8/18.
 */

public class StreamConstant {
    public static final String[] ENCODING_SIZE_PRESETS = {
            "240p(320x240 (4:3), 424x240 (16:9))",
            "480p(640x480 (4:3), 848x480 (16:9))",
            "544p(720x544 (4:3), 960x544 (16:9))",
            "720p(960x720 (4:3), 1280x720 (16:9))",
            "1080p(1440x1080 (4:3), 1920x1080 (16:9))"
    };

    public static final int[] ENCODING_SIZE_PRESETS_MAP = {
            VIDEO_ENCODING_HEIGHT_240,
            VIDEO_ENCODING_HEIGHT_480,
            VIDEO_ENCODING_HEIGHT_544,
            VIDEO_ENCODING_HEIGHT_720,
            VIDEO_ENCODING_HEIGHT_1088
    };


    public static final String[] VIDEO_QUALITY_PRESETS = {
            "LOW1(FPS:12, Bitrate:150kbps)",
            "LOW2(FPS:15, Bitrate:264kbps)",
            "LOW3(FPS:15, Bitrate:350kbps)",
            "MEDIUM1(FPS:30, Bitrate:512kbps)",
            "MEDIUM2(FPS:30, Bitrate:800kbps)",
            "MEDIUM3(FPS:30, Bitrate:1000kbps)",
            "HIGH1(FPS:30, Bitrate:1200kbps)",
            "HIGH2(FPS:30, Bitrate:1500kbps)",
            "HIGH3(FPS:30, Bitrate:2000kbps)"
    };

    public static final int[] VIDEO_QUALITY_PRESETS_MAPPING = {
            VIDEO_QUALITY_LOW1,
            VIDEO_QUALITY_LOW2,
            VIDEO_QUALITY_LOW3,
            VIDEO_QUALITY_MEDIUM1,
            VIDEO_QUALITY_MEDIUM2,
            VIDEO_QUALITY_MEDIUM3,
            VIDEO_QUALITY_HIGH1,
            VIDEO_QUALITY_HIGH2,
            VIDEO_QUALITY_HIGH3
    };
}
