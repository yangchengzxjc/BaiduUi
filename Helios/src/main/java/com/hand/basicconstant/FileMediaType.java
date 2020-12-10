package com.hand.basicConstant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Administrator
 */

@Getter
@AllArgsConstructor
public enum FileMediaType {
    /**
     * 上传文件的文件类型
     */
    JPEG_MediaType(1, "image/jpeg", "jpeg文件类型");

    private int id;
    private String identity;
    private String description;
    @Override
    public String toString() {
        return this.getIdentity();
    }
}
