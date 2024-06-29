package com.yclub.oss.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-11  17:22
 */
@Component
@Data
public class FileInfo {
    private String fileName;
    private String etag;
    private Boolean directoryFlag;
}
